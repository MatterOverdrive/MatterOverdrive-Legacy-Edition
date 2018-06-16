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
import matteroverdrive.MatterOverdrive;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.handler.weapon.ClientWeaponHandler;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/16/2016.
 */
public class PacketUpdatePlasmaBolt extends PacketAbstract {
    int boltID;
    double posX;
    float posY;
    double posZ;

    public PacketUpdatePlasmaBolt() {
    }

    public PacketUpdatePlasmaBolt(int boltID, double posX, double posY, double posZ) {
        this.boltID = boltID;
        this.posX = posX;
        this.posY = (float) posY;
        this.posZ = posZ;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        boltID = buf.readInt();
        posX = buf.readDouble();
        posY = buf.readFloat();
        posZ = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(boltID);
        buf.writeDouble(posX);
        buf.writeFloat(posY);
        buf.writeDouble(posZ);
    }

    public static class ClientHandler extends AbstractClientPacketHandler<PacketUpdatePlasmaBolt> {
        @SideOnly(Side.CLIENT)
        @Override
        public void handleClientMessage(EntityPlayerSP player, PacketUpdatePlasmaBolt message, MessageContext ctx) {
            Entity bolt = ((ClientWeaponHandler) MatterOverdrive.PROXY.getWeaponHandler()).getPlasmaBolt(message.boltID);
            if (bolt instanceof PlasmaBolt) {
                bolt.setPosition(message.posX, message.posY, message.posZ);
            }
        }
    }
}
