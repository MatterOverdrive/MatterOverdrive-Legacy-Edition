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
import matteroverdrive.blocks.BlockHoloSign;
import matteroverdrive.blocks.includes.MOBlock;
import matteroverdrive.tile.TileEntityHoloSign;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;

import static matteroverdrive.util.MOBlockHelper.getLeftSide;
import static matteroverdrive.util.MOBlockHelper.getRightSide;

/**
 * Created by Simeon on 8/15/2015.
 */
public class TileEntityRendererHoloSign extends TileEntitySpecialRenderer<TileEntityHoloSign> {
    @Override
    public void render(TileEntityHoloSign tile, double x, double y, double z, float ticks, int destoryStage, float a) {
        if (!tile.shouldRender())
            return;

        EnumFacing side = tile.getWorld().getBlockState(tile.getPos()).getValue(MOBlock.PROPERTY_DIRECTION);

        RenderUtils.beginDrawinngBlockScreen(x, y, z, side, Reference.COLOR_HOLO, tile, -0.8375, 0.2f);

        String text = tile.getText();
        if (text != null) {
            for (TextFormatting formatting : TextFormatting.values()) {
                text = text.replaceAll(formatting.toString().replace('§', '&'), formatting.toString());
            }
            String[] infos = text.split("\n");
            int leftMargin = 10;
            int rightMargin = 10;
            float maxSize = 4f;
            EnumFacing leftSide = getLeftSide(side);
            if (tile.getWorld().getBlockState(tile.getPos().offset(leftSide)).getBlock() instanceof BlockHoloSign) {
                leftMargin = 0;
                maxSize = 8;
            }
            EnumFacing rightSide = getRightSide(side);
            if (tile.getWorld().getBlockState(tile.getPos().offset(rightSide)).getBlock() instanceof BlockHoloSign) {
                rightMargin = 0;
                maxSize = 8;
            }

            if (tile.getConfigs().getBoolean("AutoLineSize", false)) {
                RenderUtils.drawScreenInfoWithLocalAutoSize(infos, Reference.COLOR_HOLO, side, leftMargin, rightMargin, maxSize);
            } else {
                RenderUtils.drawScreenInfoWithGlobalAutoSize(infos, Reference.COLOR_HOLO, side, leftMargin, rightMargin, maxSize);
            }
        }

        RenderUtils.endDrawinngBlockScreen();
    }
}