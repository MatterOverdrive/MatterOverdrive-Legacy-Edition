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
package matteroverdrive.network.packet.client.task_queue;

import io.netty.buffer.ByteBuf;
import matteroverdrive.api.network.IMatterNetworkDispatcher;
import matteroverdrive.matter_network.MatterNetworkTaskQueue;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 12/30/2015.
 */
public class PacketSyncTaskQueue extends TileEntityUpdatePacket {
    int queueID;
    ByteBuf byteBuf;
    MatterNetworkTaskQueue taskQueue;

    public PacketSyncTaskQueue() {
    }

    public PacketSyncTaskQueue(IMatterNetworkDispatcher dispatcher, int taskQueue) {
        super(dispatcher.getPosition());
        this.taskQueue = dispatcher.getTaskQueue(taskQueue);
        this.queueID = taskQueue;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        queueID = buf.readByte();
        byteBuf = buf;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeByte(queueID);
        taskQueue.writeToBuffer(buf);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketSyncTaskQueue> {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketSyncTaskQueue message, MessageContext ctx) {
            TileEntity tileEntity = message.getTileEntity(player.world);
            if (tileEntity != null && tileEntity instanceof IMatterNetworkDispatcher) {
                ((IMatterNetworkDispatcher) tileEntity).getTaskQueue(message.queueID).readFromBuffer(message.byteBuf);
            }
        }
    }
}
