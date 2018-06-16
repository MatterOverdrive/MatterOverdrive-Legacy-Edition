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

import matteroverdrive.api.internal.ItemModelProvider;
import matteroverdrive.client.ClientUtil;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

public class ItemBase extends Item implements ItemModelProvider {
    protected final String name;

    public ItemBase(String name) {
        this.name = name;
        this.setRegistryName(name);
        this.setUnlocalizedName(getRegistryName().toString().replace(':', '.'));
    }

    @Override
    public void initItemModel() {
        if (this instanceof IAdvancedModelProvider) {
            String[] subNames = ((IAdvancedModelProvider) this).getSubNames();
            for (int i = 0; i < subNames.length; i++) {
                String sub = subNames[i];
                ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(getRegistryName() + "_" + sub, "inventory"));
            }
            return;
        }
        if (!getHasSubtypes())
            ClientUtil.registerModel(this, getRegistryName().toString());
        else {
            NonNullList<ItemStack> sub = NonNullList.create();
            getSubItems(CreativeTabs.SEARCH, sub);
            for (ItemStack stack : sub) {
                ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(), new ModelResourceLocation(getRegistryName(), "inventory"));
            }
        }
    }
}