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

import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.tile.TileEntityInscriber;
import matteroverdrive.util.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.Random;

/**
 * Created by Simeon on 11/9/2015.
 */
public class TileEntityRendererInscriber extends TileEntitySpecialRenderer<TileEntityInscriber> {
    private final Random random;
    private float nextHeadX, nextHeadY;
    private float lastHeadX, lastHeadY;
    private EntityItem item;

    public TileEntityRendererInscriber() {
        random = new Random();
    }

    @Override
    public void render(TileEntityInscriber tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!tileEntity.shouldRender())
            return;
        if (item == null) {
            item = new EntityItem(tileEntity.getWorld());
            item.setItem(new ItemStack(MatterOverdrive.ITEMS.isolinear_circuit, 1, 2));
        }

        GlStateManager.color(0, 0, 1, 0.5f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderUtils.rotateFromBlock(tileEntity.getWorld(), tileEntity.getPos());
        IBlockState blockState = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        EnumFacing rotation = blockState.getValue(MOBlock.PROPERTY_DIRECTION);
        if (rotation == EnumFacing.EAST) {
            GlStateManager.translate(-0.75, 0, 0.5);
        } else if (rotation == EnumFacing.WEST) {
            GlStateManager.translate(0.25, 0, -0.5);
        } else if (rotation == EnumFacing.NORTH) {
            GlStateManager.translate(-0.75, 0, -0.5);
        } else {
            GlStateManager.translate(0.25, 0, 0.5);
        }

        ItemStack newStack = tileEntity.getStackInSlot(TileEntityInscriber.MAIN_INPUT_SLOT_ID);
        if (newStack.isEmpty()) {
            newStack = tileEntity.getStackInSlot(TileEntityInscriber.OUTPUT_SLOT_ID);
        }
        if (!newStack.isEmpty()) {
            item.setItem(newStack);
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.23, 0.69, 0);
            GlStateManager.rotate(90, 0, 1, 0);
            GlStateManager.rotate(90, 1, 0, 0);
            item.hoverStart = 0f;
            Minecraft.getMinecraft().getRenderManager().renderEntity(item, 0, 0, 0, 0, 0, true);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }
}