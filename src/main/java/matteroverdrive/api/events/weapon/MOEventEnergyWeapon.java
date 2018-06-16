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
package matteroverdrive.api.events.weapon;

import matteroverdrive.items.weapon.EnergyWeapon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

/**
 * Created by Simeon on 1/1/2016.
 */
public class MOEventEnergyWeapon extends LivingEvent {
    public final ItemStack weaponStack;
    public final EnergyWeapon energyWeapon;

    public MOEventEnergyWeapon(ItemStack weaponStack) {
        this(weaponStack, null);
    }

    public MOEventEnergyWeapon(ItemStack weaponStack, EntityLivingBase shooter) {
        super(shooter);
        this.weaponStack = weaponStack;
        if (weaponStack.getItem() instanceof EnergyWeapon) {
            this.energyWeapon = (EnergyWeapon) weaponStack.getItem();
        } else {
            throw new RuntimeException("Weapon Stack's Item must be of type Energy Weapon");
        }
    }

    public static class Overheat extends MOEventEnergyWeapon {

        public Overheat(ItemStack weaponStack) {
            super(weaponStack);
        }

        public Overheat(ItemStack weaponStack, EntityLivingBase shooter) {
            super(weaponStack, shooter);
        }

        public boolean isCancelable() {
            return true;
        }
    }
}
