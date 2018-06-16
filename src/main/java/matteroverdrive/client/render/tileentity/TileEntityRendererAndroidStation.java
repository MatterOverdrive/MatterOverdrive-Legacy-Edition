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
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.tile.TileEntityAndroidStation;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by Simeon on 5/27/2015.
 */
public class TileEntityRendererAndroidStation extends TileEntityRendererStation<TileEntityAndroidStation> {
    EntityMeleeRougeAndroidMob mob;

    public TileEntityRendererAndroidStation() {
        super();
    }

    @Override
    protected void renderHologram(TileEntityAndroidStation tile, double x, double y, double z, float partialTicks, double noise) {
        if ((tile).isUsableByPlayer(Minecraft.getMinecraft().player)) {
            if (mob == null) {
                mob = new EntityMeleeRougeAndroidMob(Minecraft.getMinecraft().world);
                mob.getEntityData().setBoolean("Hologram", true);
            }

            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5, y + 0.8, z + 0.5);
            rotate(tile, noise);

            RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO, 0.3f);

            if (tile.isUsableByPlayer(Minecraft.getMinecraft().player)) {
                ClientProxy.renderHandler.rendererRougeAndroidHologram.doRender(mob, 0, 0, 0, 0, 0);
            }

            GlStateManager.popMatrix();
        } else {
            super.renderHologram(tile, x, y, z, partialTicks, noise);
        }
    }
}