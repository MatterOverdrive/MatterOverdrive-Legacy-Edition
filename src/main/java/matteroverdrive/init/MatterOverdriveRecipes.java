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

package matteroverdrive.init;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.data.recipes.InscriberRecipeManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simeon on 8/29/2015.
 */
public class MatterOverdriveRecipes {
    public static final List<IRecipe> recipes = new ArrayList<>();

    public static final InscriberRecipeManager INSCRIBER = new InscriberRecipeManager();

    public static void registerBlockRecipes(FMLInitializationEvent event) {
    }

    public static void registerItemRecipes(FMLInitializationEvent event) {

    }

    public static void registerInscriberRecipes(FMLInitializationEvent event) {
        File file = new File(MatterOverdrive.configHandler.configDir, "MatterOverdrive/recipes/inscriber.xml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                IOUtils.copy(MatterOverdriveRecipes.class.getResourceAsStream("/assets/matteroverdrive/recipes/inscriber.xml"), new FileOutputStream(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //TO: Recipes
        INSCRIBER.load(file);
    }
}
