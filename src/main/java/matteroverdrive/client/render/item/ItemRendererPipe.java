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
package matteroverdrive.client.render.item;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Simeon on 3/8/2015.
 */
public class ItemRendererPipe {
    private TileEntitySpecialRenderer renderer;
    private TileEntity pipe;
    private float size;

    public ItemRendererPipe(TileEntitySpecialRenderer renderer, TileEntity pipe, float size) {
        this.renderer = renderer;
        this.pipe = pipe;
        this.size = size;
    }

    /*@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
    {
    	 if(type != ItemRenderType.ENTITY)
    	 {
    		 GL11.glTranslatef(0.0F, size * -0.1f, 0);
    	 }

        if(type == ItemRenderType.EQUIPPED)
        {
            GL11.glTranslatef(size * -0.1f, 0, size * -0.1f);
        }

        GlStateManager.scale(size,size,size);
    }*/
}
