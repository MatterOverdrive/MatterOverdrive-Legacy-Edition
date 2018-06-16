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
package matteroverdrive.client.render;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.inventory.IBlockScanner;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/**
 * Created by Simeon on 2/3/2016.
 */
public class DimensionalRiftsRender implements IWorldLastRenderer {
    Random random = new Random();
    double lastY;
    float[][] points = new float[128][128];

    @Override
    public void onRenderWorldLast(RenderHandler handler, RenderWorldLastEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        boolean holdingPad = false;
        ItemStack heldItem = ItemStack.EMPTY;
        if (!player.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
            if (heldItem.getItem() instanceof IBlockScanner && ((IBlockScanner) heldItem.getItem()).showsGravitationalWaves(heldItem)) {
                holdingPad = true;
            } else {
                heldItem = ItemStack.EMPTY;
            }
        } else if (!player.getHeldItem(EnumHand.OFF_HAND).isEmpty()) {
            heldItem = player.getHeldItem(EnumHand.OFF_HAND);
            if (heldItem.getItem() instanceof IBlockScanner && ((IBlockScanner) heldItem.getItem()).showsGravitationalWaves(heldItem)) {
                holdingPad = true;
            } else {
                heldItem = ItemStack.EMPTY;
            }
        }
        if (holdingPad && !heldItem.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
            GlStateManager.depthMask(true);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glPointSize(6);
            GL11.glLineWidth(1);
            Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();
            Vec3d viewEntityPos = renderViewEntity.getPositionEyes(event.getPartialTicks()).subtract(0, renderViewEntity.getEyeHeight(), 0);
            if (lastY == 0) {
                lastY = 64;
            }
            if (renderViewEntity.onGround) {
                lastY = MOMathHelper.Lerp(lastY, viewEntityPos.y, 0.05);
            }
            Vec3d viewEntityPosRound = new Vec3d(Math.floor(viewEntityPos.x), lastY, Math.floor(viewEntityPos.z));

            GlStateManager.translate(-viewEntityPos.x, -viewEntityPos.y, -viewEntityPos.z);
            BufferBuilder worldRenderer = Tessellator.getInstance().getBuffer();

            int vewDistance = 128;
            double height = 5;

            random.setSeed(Minecraft.getMinecraft().world.getSeed());

            for (int x = 0; x < vewDistance; x++) {
                for (int z = 0; z < vewDistance; z++) {

                    float yPos = MatterOverdrive.MO_WORLD.getDimensionalRifts().getValueAt(new Vec3d(viewEntityPosRound.x + x - vewDistance / 2, 0, viewEntityPosRound.z + z - vewDistance / 2));
                    yPos *= Math.sin((x / (double) vewDistance) * Math.PI) * Math.sin((z / (double) vewDistance) * Math.PI);
                    points[x][z] = yPos;
                }
            }

            GlStateManager.translate(0, viewEntityPosRound.y, 0);

            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
            for (int z = 0; z < vewDistance; z++) {
                for (int x = 0; x < vewDistance; x++) {
                    if (points[x][z] > 0.01) {
                        double xPos = viewEntityPosRound.x + x - vewDistance / 2;
                        double zPos = viewEntityPosRound.z + z - vewDistance / 2;

                        float r = Reference.COLOR_HOLO.getFloatR() * points[x][z];
                        float g = Reference.COLOR_HOLO.getFloatG() * points[x][z];
                        float b = Reference.COLOR_HOLO.getFloatB() * points[x][z];
                        worldRenderer.pos(xPos, getPointSafe(x, z) * height, zPos).color(r, g, b, 1).endVertex();
                        worldRenderer.pos(xPos, getPointSafe(x, z + 1) * height, zPos + 1).color(r, g, b, 1).endVertex();
                        worldRenderer.pos(xPos + 1, getPointSafe(x + 1, z + 1) * height, zPos + 1).color(r, g, b, 1).endVertex();
                        worldRenderer.pos(xPos + 1, getPointSafe(x + 1, z) * height, zPos).color(r, g, b, 1).endVertex();
                    }
                }
            }
            Tessellator.getInstance().draw();

            worldRenderer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
            for (int z = 0; z < vewDistance; z++) {
                for (int x = 0; x < vewDistance; x++) {
                    if (points[x][z] > 0.01) {
                        double xPos = viewEntityPosRound.x + x - vewDistance / 2;
                        double zPos = viewEntityPosRound.z + z - vewDistance / 2;

                        float r = Reference.COLOR_HOLO.getFloatR() * points[x][z];
                        float g = Reference.COLOR_HOLO.getFloatG() * points[x][z];
                        float b = Reference.COLOR_HOLO.getFloatB() * points[x][z];
                        worldRenderer.pos(xPos, getPointSafe(x, z) * height, zPos).color(r, g, b, 1).endVertex();
                        worldRenderer.pos(xPos, getPointSafe(x, z + 1) * height, zPos + 1).color(r, g, b, 1).endVertex();
                        worldRenderer.pos(xPos + 1, getPointSafe(x + 1, z + 1) * height, zPos + 1).color(r, g, b, 1).endVertex();
                        worldRenderer.pos(xPos + 1, getPointSafe(x + 1, z) * height, zPos).color(r, g, b, 1).endVertex();
                    }
                }
            }
            Tessellator.getInstance().draw();

            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public double getPointSafe(int x, int y) {
        return points[MathHelper.clamp(x, 0, 127)][MathHelper.clamp(y, 0, 127)];
    }
}