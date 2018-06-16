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
import matteroverdrive.api.weapon.IWeaponModule;
import matteroverdrive.items.includes.MOBaseItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 2/16/2016.
 */
public class WeaponModuleRicochet extends MOBaseItem implements IWeaponModule {
    public WeaponModuleRicochet(String name) {
        super(name);
    }

    @Override
    public int getSlot(ItemStack module) {
        return Reference.MODULE_OTHER;
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
        if (statID == Reference.WS_RICOCHET) {
            return 1;
        }
        return originalStat;
    }
}
