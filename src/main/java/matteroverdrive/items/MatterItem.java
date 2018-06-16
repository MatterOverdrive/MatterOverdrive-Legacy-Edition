/*
 * This file is part of Matter Overdrive
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
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
package matteroverdrive.items;

import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;

public class MatterItem extends MOBaseItem implements IAdvancedModelProvider {
    public static final String[] subItemNames = {"antimatter", "darkmatter", "redmatter"};

    public MatterItem(String name) {
        super(name);
        this.setHasSubtypes(true);
    }

    public static MatterType getType(ItemStack stack) {
        return MatterType.values()[MathHelper.clamp(stack.getMetadata(), 0, subItemNames.length)];
    }

    @Override
    public String[] getSubNames() {
        return subItemNames;
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTabs)) {
            for (int i = 0; i < subItemNames.length; i++) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    public String getUnlocalizedName(ItemStack stack) {
        int i = MathHelper.clamp(stack.getMetadata(), 0, subItemNames.length);
        return super.getUnlocalizedName() + "." + subItemNames[i];
    }

    public enum MatterType {
        ANTIMATTER,
        DARKMATTER,
        REDMATTER;
    }
}