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
package matteroverdrive.network.packet.client;

import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/28/2015.
 */
public class PacketReplicationComplete extends TileEntityUpdatePacket {
    public PacketReplicationComplete() {
        super();
    }

    public PacketReplicationComplete(TileEntity entity) {
        super(entity);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketReplicationComplete> {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketReplicationComplete message, MessageContext ctx) {
            TileEntity entity = message.getTileEntity(player.world);
            if (entity instanceof TileEntityMachineReplicator) {
                ((TileEntityMachineReplicator) entity).beginSpawnParticles();
            }
        }
    }
}
