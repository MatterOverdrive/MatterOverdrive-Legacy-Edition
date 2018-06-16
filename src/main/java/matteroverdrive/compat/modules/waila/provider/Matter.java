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

import matteroverdrive.api.matter.IMatterHandler;
import matteroverdrive.compat.modules.waila.IWailaBodyProvider;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.util.MatterHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author shadowfacts
 */
public class Matter implements IWailaBodyProvider {
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();

        if (te.hasCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null)) {
            IMatterHandler storage = te.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null);
            currenttip.add(TextFormatting.AQUA + String.format("%s / %s %s", storage.getMatterStored(), storage.getCapacity(), MatterHelper.MATTER_UNIT));

        } else {
            throw new RuntimeException("MOTileEntityMachineMatter WAILA provider is being used for something that is not a MOTileEntityMachineMatter: " + te.getClass());
        }

        return currenttip;
    }

}
