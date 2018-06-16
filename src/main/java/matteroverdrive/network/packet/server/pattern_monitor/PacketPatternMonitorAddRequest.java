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
package matteroverdrive.network.packet.server.pattern_monitor;

import io.netty.buffer.ByteBuf;
import matteroverdrive.api.network.MatterNetworkTaskState;
import matteroverdrive.data.matter_network.ItemPattern;
import matteroverdrive.machines.pattern_monitor.ComponentTaskProcessingPatternMonitor;
import matteroverdrive.machines.pattern_monitor.TileEntityMachinePatternMonitor;
import matteroverdrive.matter_network.tasks.MatterNetworkTaskReplicatePattern;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.network.packet.server.AbstractServerPacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Simeon on 4/26/2015.
 */
public class PacketPatternMonitorAddRequest extends TileEntityUpdatePacket {
    private ItemPattern pattern;
    private int amount;

    public PacketPatternMonitorAddRequest() {
        super();
    }

    public PacketPatternMonitorAddRequest(TileEntityMachinePatternMonitor monitor, ItemPattern pattern, int amount) {
        super(monitor);
        this.pattern = pattern;
        this.amount = amount;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        pattern = ItemPattern.fromBuffer(buf);
        amount = buf.readShort();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ItemPattern.writeToBuffer(buf, pattern);
        buf.writeShort(amount);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketPatternMonitorAddRequest> {
        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketPatternMonitorAddRequest message, MessageContext ctx) {
            TileEntity entity = message.getTileEntity(player.world);
            if (entity != null && entity instanceof TileEntityMachinePatternMonitor) {
                TileEntityMachinePatternMonitor monitor = (TileEntityMachinePatternMonitor) entity;

                if (monitor != null) {
                    MatterNetworkTaskReplicatePattern task = new MatterNetworkTaskReplicatePattern(message.pattern, message.amount);
                    task.setState(MatterNetworkTaskState.WAITING);
                    monitor.getComponent(ComponentTaskProcessingPatternMonitor.class).addReplicateTask(task);
                }
            }
        }
    }
}
