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

package matteroverdrive.client.render.biostat;

import matteroverdrive.Reference;
import matteroverdrive.api.renderer.IBioticStatRenderer;
import matteroverdrive.data.biostats.BioticStatTeleport;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.init.OverdriveBioticStats;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import static org.lwjgl.opengl.GL11.GL_ONE;

/**
 * Created by Simeon on 6/13/2015.
 */
public class BioticStatRendererTeleporter implements IBioticStatRenderer<BioticStatTeleport> {
    public static final ResourceLocation glow = new ResourceLocation(Reference.PATH_FX + "teleport_glow.png");
    @Override
    public void onWorldRender(BioticStatTeleport stat, int level, RenderWorldLastEvent event) {
        AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(Minecraft.getMinecraft().player);

        if (androidPlayer != null && androidPlayer.isAndroid() && androidPlayer.isUnlocked(OverdriveBioticStats.teleport, OverdriveBioticStats.teleport.maxLevel()) && OverdriveBioticStats.teleport.isEnabled(androidPlayer, 0) && OverdriveBioticStats.teleport.getHasPressedKey()) {
            Vec3d playerPos = androidPlayer.getPlayer().getPositionEyes(event.getPartialTicks());
            if (ClientProxy.keyHandler.getBinding(KeyHandler.ABILITY_USE_KEY).isKeyDown()) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(GL_ONE, GL_ONE, 0, 1);
                RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.5f);
                GlStateManager.translate(-playerPos.x, -playerPos.y + Minecraft.getMinecraft().player.getEyeHeight(), -playerPos.z);

                //mob.rotationYawHead = androidPlayer.getPlayer().rotationYawHead;

                Vec3d pos = OverdriveBioticStats.teleport.getPos(androidPlayer);
                if (pos != null) {
                    Minecraft.getMinecraft().renderEngine.bindTexture(glow);
                    GlStateManager.translate(pos.x, pos.y, pos.z);
                    GlStateManager.rotate(androidPlayer.getPlayer().rotationYaw, 0, -1, 0);
                    GlStateManager.rotate(androidPlayer.getPlayer().rotationPitch, 1, 0, 0);
                    GlStateManager.rotate(Minecraft.getMinecraft().world.getWorldTime() * 10, 0, 0, 1);
                    GlStateManager.translate(-0.5, -0.5, 0);
                    RenderUtils.drawPlane(1);
                }

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }
}
