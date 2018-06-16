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
package matteroverdrive.compat.modules.waila.provider;

import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.tile.TileEntityWeaponStation;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author shadowfacts
 */
public class WeaponStation implements IWailaBodyProvider {

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();

        if (te instanceof TileEntityWeaponStation) {
            TileEntityWeaponStation weaponStation = (TileEntityWeaponStation) te;

            if (!weaponStation.getStackInSlot(weaponStation.INPUT_SLOT).isEmpty()) {
                String name = weaponStation.getStackInSlot(weaponStation.INPUT_SLOT).getDisplayName();
                currenttip.add(TextFormatting.YELLOW + "Current Weapon: " + TextFormatting.WHITE + name);
            }

        } else {
            throw new RuntimeException("Weapon Station WAILA provider is being used for something that is not a Weapon Station: " + te.getClass());
        }

        return currenttip;
    }

}
