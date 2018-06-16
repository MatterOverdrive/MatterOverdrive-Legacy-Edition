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
import matteroverdrive.network.packet.TileEntityUpdatePacket;
import matteroverdrive.tile.MOTileEntityMachineEnergy;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/22/2015.
 */
public class PacketPowerUpdate extends TileEntityUpdatePacket {
    int energy;

    public PacketPowerUpdate() {
    }

    public PacketPowerUpdate(MOTileEntityMachineEnergy entityMachineEnergy) {
        super(entityMachineEnergy.getPos());
        energy = entityMachineEnergy.getEnergyStorage().getEnergyStored();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        energy = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(energy);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketPowerUpdate> {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketPowerUpdate message, MessageContext ctx) {
            TileEntity tileEntity = player.world.getTileEntity(message.pos);
            if (tileEntity instanceof MOTileEntityMachineEnergy) {
                ((MOTileEntityMachineEnergy) tileEntity).getEnergyStorage().setEnergy(message.energy);
            }
        }
    }
}
