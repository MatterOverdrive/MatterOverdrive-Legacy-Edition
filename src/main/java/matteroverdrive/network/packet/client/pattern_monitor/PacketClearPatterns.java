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
package matteroverdrive.network.packet.client.pattern_monitor;

import io.netty.buffer.ByteBuf;
import matteroverdrive.container.matter_network.ContainerPatternMonitor;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.AbstractClientPacketHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/30/2016.
 */
public class PacketClearPatterns extends PacketAbstract {
    private int windowID;
    private BlockPos database;
    private int patternStorageId;

    public PacketClearPatterns() {
    }

    public PacketClearPatterns(int windowID) {
        this.windowID = windowID;
    }

    public PacketClearPatterns(int windowID, BlockPos database) {
        this(windowID);
        this.database = database;
    }

    public PacketClearPatterns(int windowID, BlockPos database, int patternStorageId) {
        this(windowID, database);
        this.patternStorageId = patternStorageId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        windowID = buf.readByte();
        if (buf.readBoolean()) {
            database = BlockPos.fromLong(buf.readLong());
        }
        patternStorageId = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(windowID);
        buf.writeBoolean(database != null);
        if (database != null) {
            buf.writeLong(database.toLong());
        }
        buf.writeByte(patternStorageId);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketClearPatterns> {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketClearPatterns message, MessageContext ctx) {
            if (player.openContainer instanceof ContainerPatternMonitor) {
                if (message.database != null) {
                    if (message.patternStorageId >= 0) {
                        ((ContainerPatternMonitor) player.openContainer).clearPatternStoragePatterns(message.database, message.patternStorageId);
                    } else {
                        ((ContainerPatternMonitor) player.openContainer).clearDatabasePatterns(message.database);
                    }
                } else {
                    ((ContainerPatternMonitor) player.openContainer).clearAllPatterns();
                }
            }
        }
    }
}
