/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
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

package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.client.render.RenderParticlesHandler;
import matteroverdrive.data.biostats.BioticStatTeleport;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.init.OverdriveBioticStats;
import matteroverdrive.network.packet.PacketAbstract;
import matteroverdrive.network.packet.client.PacketSpawnParticle;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.EnumSet;

/**
 * Created by Simeon on 6/1/2015.
 */
public class PacketTeleportPlayer extends PacketAbstract {

    double x, y, z;

    public PacketTeleportPlayer() {

    }

    public PacketTeleportPlayer(Vec3d vec3) {
        x = vec3.x;
        y = vec3.y;
        z = vec3.z;
    }

    public PacketTeleportPlayer(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketTeleportPlayer> {

        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketTeleportPlayer message, MessageContext ctx) {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
            if (androidPlayer != null && androidPlayer.isAndroid()) {
                int unlockedLevel = androidPlayer.getUnlockedLevel(OverdriveBioticStats.teleport);
                if (!MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(OverdriveBioticStats.teleport, unlockedLevel, androidPlayer))) {
                    if (OverdriveBioticStats.teleport.isEnabled(androidPlayer, unlockedLevel)) {
                        MatterOverdrive.NETWORK.sendToAllAround(new PacketSpawnParticle("teleport", player.posX, player.posY + 1, player.posZ, 1, RenderParticlesHandler.Blending.Additive), player, 64);
                        player.world.playSound(player, player.posX, player.posY, player.posZ, MatterOverdriveSounds.androidTeleport, SoundCategory.BLOCKS, 0.2f, 0.8f + 0.4f * player.world.rand.nextFloat());
                        player.setPositionAndUpdate(message.x, message.y, message.z);
                        player.world.playSound(null, message.x, message.y, message.z, MatterOverdriveSounds.androidTeleport, SoundCategory.BLOCKS, 0.2f, 0.8f + 0.4f * player.world.rand.nextFloat());
                        androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_LAST_TELEPORT, player.world.getTotalWorldTime() + BioticStatTeleport.TELEPORT_DELAY);
                        androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_GLITCH_TIME, 5);
                        androidPlayer.extractEnergyScaled(BioticStatTeleport.ENERGY_PER_TELEPORT);
                        androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS));
                        androidPlayer.getPlayer().fallDistance = 0;
                        for (int i = 0; i < 9; i++) {
                            ItemStack stack = androidPlayer.getPlayer().inventory.getStackInSlot(i);
                            CooldownTracker tracker = androidPlayer.getPlayer().getCooldownTracker();
                            if (!tracker.hasCooldown(stack.getItem()) || tracker.getCooldown(stack.getItem(), 0) < 40)
                                tracker.setCooldown(stack.getItem(), 40);
                        }
                    }
                }
            }
        }
    }
}
