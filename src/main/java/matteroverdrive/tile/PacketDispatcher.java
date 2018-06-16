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
package matteroverdrive.tile;

import matteroverdrive.util.TileUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public class PacketDispatcher {
    public static void dispatchTEToNearbyPlayers(@Nonnull TileEntity tile) {
        if (tile.getWorld() instanceof WorldServer) {
            WorldServer ws = ((WorldServer) tile.getWorld());
            SPacketUpdateTileEntity packet = tile.getUpdatePacket();

            if (packet == null)
                return;

            for (EntityPlayer player : ws.playerEntities) {
                if (!(player instanceof EntityPlayerMP))
                    continue;
                EntityPlayerMP playerMP = ((EntityPlayerMP) player);

                if (playerMP.getDistanceSq(tile.getPos()) < 64 * 64 && ws.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4)) {
                    playerMP.connection.sendPacket(packet);
                }
            }
        }
    }

    public static void dispatchTEToNearbyPlayers(@Nonnull World world, @Nonnull BlockPos pos) {
        TileUtils.getTileEntity(world, pos, TileEntity.class).ifPresent(PacketDispatcher::dispatchTEToNearbyPlayers);
    }
}