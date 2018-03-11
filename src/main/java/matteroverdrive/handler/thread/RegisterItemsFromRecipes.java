/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */

package matteroverdrive.handler.thread;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.data.matter.ItemStackHandlerCachable;
import matteroverdrive.util.MOLog;
import matteroverdrive.util.MatterHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by Simeon on 5/7/2015.
 */
public class RegisterItemsFromRecipes implements Runnable {

    private final File file;

    public RegisterItemsFromRecipes(File file) {
        this.file = file;
    }

    @Override
    public void run() {

        long startTime = System.nanoTime();
        int startEntriesCount = MatterOverdrive.MATTER_REGISTRY.getItemEntires().size();

        if (MatterOverdrive.MATTER_REGISTRY.CALCULATE_RECIPES) {
            int passesCount = 8;
            MOLog.info("Starting Matter Recipe Calculation !");

            for (int pass = 0; pass < passesCount; pass++) {
                long passStartTime = System.nanoTime();
                int passStartRecipeCount = MatterOverdrive.MATTER_REGISTRY.getItemEntires().size();

                List<IRecipe> recipes = new CopyOnWriteArrayList<>(ForgeRegistries.RECIPES.getValues());

                MOLog.info("Matter Recipe Calculation Started for %s recipes at pass %s, with %s matter entries", recipes.size(), pass + 1, passStartRecipeCount);
                for (IRecipe recipe : recipes) {
                    if (recipe == null || recipe.getRecipeOutput().isEmpty()) {
                        continue;
                    }

                    if (Thread.interrupted()) {
                        return;
                    }

                    try {
                        ItemStack itemStack = recipe.getRecipeOutput();
                        if (!itemStack.isEmpty() && !MatterOverdrive.MATTER_REGISTRY.blacklistedFromMod(itemStack)) {
                            debug("Calculating Recipe for: %s", recipe.getRecipeOutput());
                            int matter = MatterOverdrive.MATTER_REGISTRY.getMatter(itemStack);
                            if (matter <= 0) {
                                matter = MatterOverdrive.MATTER_REGISTRY.getMatterFromRecipe(itemStack);

                                if (matter > 0) {
                                    MatterOverdrive.MATTER_REGISTRY.register(itemStack.getItem(), new ItemStackHandlerCachable(matter, itemStack.getItemDamage()));
                                } else {
                                    debug("Could not calculate recipe for: %s. Matter from recipe is 0.", recipe.getRecipeOutput());
                                }
                            }
                        } else {
                            debug("% was blacklisted. Skipping matter calculation", recipe.getRecipeOutput());
                        }
                    } catch (Exception e) {
                        if (!recipe.getRecipeOutput().isEmpty()) {
                            debug("Recipe missing output", e);
                        } else {
                            debug("There was a problem calculating matter from recipe", e);
                        }
                    }
                }

                MOLog.info("Matter Recipe Calculation for pass %s complete. Took %s milliseconds. Registered %s recipes", pass + 1, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - passStartTime), MatterOverdrive.MATTER_REGISTRY.getItemEntires().size() - passStartRecipeCount);
                if (MatterOverdrive.MATTER_REGISTRY.getItemEntires().size() - passStartRecipeCount <= 0) {
                    break;
                }
            }

            MOLog.info("Matter Recipe Calculation, Complete ! Took %s Milliseconds. Registered total of %s items", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime), MatterOverdrive.MATTER_REGISTRY.getItemEntires().size() - startEntriesCount);
        }

        if (MatterOverdrive.MATTER_REGISTRY.CALCULATE_FURNACE) {
            startTime = System.nanoTime();
            startEntriesCount = MatterOverdrive.MATTER_REGISTRY.getItemEntires().size();

            MOLog.info("Matter Furnace Calculation Started");
            registerFromFurnace();
            MOLog.info("Matter Furnace Calculation Complete. Took %s Milliseconds. Registered %s entries", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime), MatterOverdrive.MATTER_REGISTRY.getItemEntires().size() - startEntriesCount);
        }

        if (MatterOverdrive.MATTER_REGISTRY.CALCULATE_FURNACE || MatterOverdrive.MATTER_REGISTRY.CALCULATE_RECIPES) {
            startTime = System.nanoTime();

            MOLog.info("Saving Registry to Disk");
            try {
                MatterOverdrive.MATTER_REGISTRY.saveToFile(file);
                MOLog.info("Registry saved at: %s. Took %s Milliseconds.", file.getPath(), TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
            } catch (Exception e) {
                MOLog.log(Level.ERROR, e, "Could not save registry to: %s", file.getPath());
            }
        }
        MatterOverdrive.MATTER_REGISTRY.hasComplitedRegistration = true;
        MatterOverdrive.MATTER_REGISTRATION_HANDLER.onRegistrationComplete();
    }

    private void registerFromFurnace() {
        Map<ItemStack, ItemStack> smeltingMap = new ConcurrentHashMap<>(FurnaceRecipes.instance().getSmeltingList());
        for (Map.Entry<ItemStack, ItemStack> entry : smeltingMap.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                int keyMatter = (MatterHelper.getMatterAmountFromItem(entry.getKey()) * entry.getKey().getCount()) / entry.getValue().getCount();
                int valueMatter = MatterHelper.getMatterAmountFromItem(entry.getValue());
                if (keyMatter > 0 && valueMatter <= 0) {
                    MatterOverdrive.MATTER_REGISTRY.register(entry.getValue().getItem(), new ItemStackHandlerCachable(keyMatter, entry.getValue().getItemDamage()));
                }
            }
        }
    }

    private void debug(String debug, Exception ex, Object... params) {
        if (MatterOverdrive.MATTER_REGISTRY.CALCULATION_DEBUG) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof ItemStack) {
                    try {
                        params[i] = ((ItemStack) params[i]).getUnlocalizedName();
                    } catch (Exception e) {
                        MOLog.log(Level.ERROR, e, "There was a problem getting the name of item %s", ((ItemStack) params[i]).getItem());
                    }
                }
            }
            MOLog.log(Level.DEBUG, ex, debug, params);
        }
    }

    private void debug(String debug, Object... params) {
        if (MatterOverdrive.MATTER_REGISTRY.CALCULATION_DEBUG) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof ItemStack) {
                    try {
                        params[i] = ((ItemStack) params[i]).getUnlocalizedName();
                    } catch (Exception e) {
                        MOLog.log(Level.ERROR, e, "There was a problem getting the name of item %s", ((ItemStack) params[i]).getItem());
                    }
                }
            }
            MOLog.debug(debug, params);
        }
    }
}
