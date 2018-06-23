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
import matteroverdrive.api.weapon.WeaponStats;
import matteroverdrive.items.IAdvancedModelProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/15/2015.
 */
public class WeaponModuleBarrel extends WeaponModuleBase implements IAdvancedModelProvider {
    public static final int DAMAGE_BARREL_ID = 0;
    public static final int FIRE_BARREL_ID = 1;
    public static final int EXPLOSION_BARREL_ID = 2;
    public static final int HEAL_BARREL_ID = 3;
    public static final int DOOMSDAY_BARREL_ID = 4;
    public static final int BLOCK_BARREL_ID = 5;
    public static final String[] names = {"damage", "fire", "explosion", "heal", "doomsday", "block"};

    public WeaponModuleBarrel(String name) {
        super(name);
        applySlot(Reference.MODULE_BARREL);
        applyWeaponStat(DAMAGE_BARREL_ID, WeaponStats.DAMAGE, 1.5f);
        applyWeaponStat(DAMAGE_BARREL_ID, WeaponStats.AMMO, 0.5f);
        applyWeaponStat(DAMAGE_BARREL_ID, WeaponStats.EFFECT, 0.5f);

        applyWeaponStat(FIRE_BARREL_ID, WeaponStats.DAMAGE, 0.75f);
        applyWeaponStat(FIRE_BARREL_ID, WeaponStats.FIRE_DAMAGE, 1f);
        applyWeaponStat(FIRE_BARREL_ID, WeaponStats.AMMO, 0.5f);

        applyWeaponStat(EXPLOSION_BARREL_ID, WeaponStats.EXPLOSION_DAMAGE, 1f);
        applyWeaponStat(EXPLOSION_BARREL_ID, WeaponStats.AMMO, 0.2f);
        applyWeaponStat(EXPLOSION_BARREL_ID, WeaponStats.EFFECT, 0.5f);
        applyWeaponStat(EXPLOSION_BARREL_ID, WeaponStats.FIRE_RATE, 0.15f);

        applyWeaponStat(HEAL_BARREL_ID, WeaponStats.DAMAGE, 0f);
        applyWeaponStat(HEAL_BARREL_ID, WeaponStats.AMMO, 0.5f);
        applyWeaponStat(HEAL_BARREL_ID, WeaponStats.HEAL, 0.2f);

        applyWeaponStat(DOOMSDAY_BARREL_ID, WeaponStats.EXPLOSION_DAMAGE, 3f);
        applyWeaponStat(DOOMSDAY_BARREL_ID, WeaponStats.AMMO, 0.2f);
        applyWeaponStat(DOOMSDAY_BARREL_ID, WeaponStats.EFFECT, 0.3f);
        applyWeaponStat(DOOMSDAY_BARREL_ID, WeaponStats.FIRE_RATE, 0.1f);

        applyWeaponStat(BLOCK_BARREL_ID, WeaponStats.DAMAGE, 0f);
        applyWeaponStat(BLOCK_BARREL_ID, WeaponStats.AMMO, 0.5f);
        applyWeaponStat(BLOCK_BARREL_ID, WeaponStats.BLOCK_DAMAGE, 3f);
    }

    @Override
    public String[] getSubNames() {
        return names;
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
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (isInCreativeTab(creativeTabs))
            for (int i = 0; i < names.length; i++) {
                list.add(new ItemStack(this, 1, i));
            }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        int damage = itemStack.getItemDamage();
        return this.getUnlocalizedName() + "." + names[damage];
    }
}
