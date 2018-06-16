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
package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityRendererNetworkPipe extends TileEntityRendererPipe {

    public TileEntityRendererNetworkPipe() {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "network_pipe.png");
    }

    @Override
    protected void drawCore(TileEntityPipe tile, double x,
                            double y, double z, float f, int sides) {
        super.drawCore(tile, x, y, z, f, sides);
    }

    @Override
    protected void drawSide(TileEntityPipe tile, EnumFacing dir) {
        super.drawSide(tile, dir);
    }
}
