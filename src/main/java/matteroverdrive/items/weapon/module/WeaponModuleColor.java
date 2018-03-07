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

package matteroverdrive.items.weapon.module;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponColor;
import matteroverdrive.client.data.Color;
import matteroverdrive.items.includes.MOBaseItem;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/14/2015.
 */
public class WeaponModuleColor extends MOBaseItem implements IWeaponColor {
    public static final Color defaultColor = new Color(255, 255, 255);
    public static final Color colors[] = {
            new Color(204, 0, 0),      //red
            new Color(0, 153, 51),     //green
            new Color(0, 102, 255),    //blue
            new Color(102, 51, 51),    //brown
            new Color(255, 153, 255),  //pink
            new Color(153, 204, 255),  //sky blue
            new Color(212, 175, 55),   //gold
            new Color(102, 255, 102),  //lime green
            new Color(30, 30, 30),     //black
            new Color(128, 128, 128)   //grey
    };
    public static final String names[] = {"red", "green", "blue", "brown", "pink", "sky_blue", "gold", "lime_green", "black", "grey"};

    public WeaponModuleColor(String name) {
        super(name);
        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE_MODULES);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
    }

    public int getMetadata(int damage) {
        return 0;
    }

    public void addToDunguns() {
        for (int i = 0; i < colors.length; i++) {
            // TODO: Add to dungeon loot
			/*ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,1));
			ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,1));
            ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,1));
            ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,1));
            ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,1));
            ChestGenHooks.getInfo(Reference.CHEST_GEN_ANDROID_HOUSE).addItem(new WeightedRandomChestContent(new ItemStack(this,1,i),1,1,10));*/
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        return ("" + MOStringHelper.translateToLocal(this.getUnlocalizedNameInefficiently(itemStack) + ".name")).trim() + " (" + MOStringHelper.translateToLocal("module.color." + names[itemStack.getItemDamage()]) + ")";
    }

    @Override
    public int getSlot(ItemStack module) {
        return Reference.MODULE_COLOR;
    }

    @Override
    public String getModelPath() {
        return null;
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack module) {
        return null;
    }

    @Override
    public String getModelName(ItemStack module) {
        return null;
    }

    @Override
    public float modifyWeaponStat(int statID, ItemStack module, ItemStack weapon, float originalStat) {
        return originalStat;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTabs))
            for (int i = 0; i < colors.length; i++) {
                list.add(new ItemStack(this, 1, i));
            }
    }

    @Override
    public int getColor(ItemStack module, ItemStack weapon) {
        int damage = module.getItemDamage();
        if (damage >= 0 && damage < colors.length) {
            return colors[damage].getColor();
        }
        return defaultColor.getColor();
    }
}
