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
package matteroverdrive.client.render.weapons.layers;

import matteroverdrive.Reference;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.resources.data.WeaponMetadataSection;
import matteroverdrive.items.weapon.EnergyWeapon;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

/**
 * Created by Simeon on 2/17/2016.
 */
public class WeaponLayerAmmoRender implements IWeaponLayer {
    public static final ResourceLocation ammoBackground = new ResourceLocation(Reference.PATH_ELEMENTS + "ammo_background.png");

    @Override
    public void renderLayer(WeaponMetadataSection weaponMeta, ItemStack weapon, float ticks) {
        Vec3d modulePosition = weaponMeta.getModulePosition("ammo_holo", new Vec3d(0.17, 0.13, 0.2));

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GlStateManager.disableLighting();
        RenderUtils.disableLightmap();
        GlStateManager.translate(modulePosition.x, modulePosition.y, modulePosition.z);
        GlStateManager.rotate(180, 0, 0, 1);

        GlStateManager.pushMatrix();
        GlStateManager.scale(0.002, 0.002, 0.002);
        GlStateManager.translate(0, 0, -1);
        EnergyWeapon energyWeapon = (EnergyWeapon) weapon.getItem();
        float heatPerc = energyWeapon.getHeat(weapon) / energyWeapon.getMaxHeat(weapon);
        Color color = RenderUtils.lerp(Reference.COLOR_HOLO, Reference.COLOR_HOLO_RED, heatPerc);
        RenderUtils.applyColor(color);
        color = color.multiplyWithoutAlpha(0.7f);

        String ammoString = DecimalFormat.getPercentInstance().format((float) energyWeapon.getAmmo(weapon) / (float) energyWeapon.getMaxAmmo(weapon));
        int ammoStringWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(ammoString);
        ClientProxy.moFontRender.drawString(ammoString, 16 - ammoStringWidth / 2, 9, color.getColor(), false);
        GlStateManager.scale(0.7, 0.7, 0.7);

        ClientProxy.moFontRender.drawString(DecimalFormat.getPercentInstance().format(heatPerc), 54, 18, color.getColor(), false);
        GlStateManager.translate(46, 28 - 18 * heatPerc, 0);
        GlStateManager.disableTexture2D();
        RenderUtils.drawPlane(4, 18 * heatPerc);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();

        RenderUtils.bindTexture(ammoBackground);
        RenderUtils.drawPlane(0.1, 0.05);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        RenderUtils.enableLightmap();
        RenderUtils.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    }
}
