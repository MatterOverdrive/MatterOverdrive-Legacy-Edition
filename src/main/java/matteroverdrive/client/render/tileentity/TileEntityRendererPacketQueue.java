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
package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.blocks.BlockNetworkSwitch;
import matteroverdrive.tile.TileEntityMachinePacketQueue;
import matteroverdrive.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Simeon on 8/22/2015.
 */
public class TileEntityRendererPacketQueue extends TileEntitySpecialRenderer<TileEntityMachinePacketQueue> {
    final Block fakeBlock = new BlockNetworkSwitch(Material.IRON, "fake_block");

    @Override
    public void render(TileEntityMachinePacketQueue tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.shouldRender())
            return;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (tileEntity.flashTime > 0) {
            renderBlock(fakeBlock);
        }

        GlStateManager.popMatrix();
    }

    private void renderBlock(Block block) {
        float distance = 0.1f;

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
        RenderUtils.disableLightmap();
        RenderUtils.drawCube(-0.01, -0.01, -0.01, 1.02, 1.02, 1.02, Reference.COLOR_HOLO);
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        RenderUtils.enableLightmap();
    }
}