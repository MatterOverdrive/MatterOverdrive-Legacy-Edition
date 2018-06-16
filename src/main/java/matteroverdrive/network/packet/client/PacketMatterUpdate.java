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

import io.netty.buffer.ByteBuf;
import matteroverdrive.init.MatterOverdriveCapabilities;
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/22/2015.
 */
public class PacketMatterUpdate extends TileEntityUpdatePacket {
    private int matter = 0;

    public PacketMatterUpdate() {
    }

    public PacketMatterUpdate(TileEntity tileentity) {
        super(tileentity.getPos());
        matter = tileentity.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null).getMatterStored();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        matter = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(matter);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketMatterUpdate> {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketMatterUpdate message, MessageContext ctx) {
            if (player != null && player.world != null) {
                TileEntity tileEntity = player.world.getTileEntity(message.pos);

                if (tileEntity != null && tileEntity.hasCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null)) {
                    tileEntity.getCapability(MatterOverdriveCapabilities.MATTER_HANDLER, null).setMatterStored(message.matter);
                }
            }
        }
    }
}
