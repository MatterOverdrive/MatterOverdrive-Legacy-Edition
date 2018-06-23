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
package matteroverdrive.items.weapon.module;

import matteroverdrive.Reference;
import matteroverdrive.api.weapon.IWeaponScope;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Simeon on 2/18/2016.
 */
public class WeaponModuleHoloSights extends WeaponModuleBase implements IWeaponScope {
    public WeaponModuleHoloSights(String name) {
        super(name);
        applySlot(Reference.MODULE_SIGHTS);
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i = 0; i < 3; i++) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }

    @Override
    public float getZoomAmount(ItemStack scopeStack, ItemStack weaponStack) {
        return 0.3f;
    }

    @Override
    public float getAccuracyModify(ItemStack scopeStack, ItemStack weaponStack, boolean zoomed, float originalAccuracy) {
        if (zoomed) {
            return originalAccuracy * 0.6f;
        }
        return originalAccuracy * 0.8f;
    }

    @Override
    public String getModelPath() {
        return null;
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack module) {
        return new ResourceLocation(Reference.PATH_ELEMENTS + String.format("holo_sight_%s.png", module.getItemDamage()));
    }

    @Override
    public String getModelName(ItemStack module) {
        return null;
    }
}
