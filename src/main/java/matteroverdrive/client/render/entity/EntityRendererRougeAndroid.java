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
package matteroverdrive.client.render.entity;

import matteroverdrive.Reference;
import matteroverdrive.entity.monster.EntityRougeAndroidMob;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Simeon on 5/26/2015.
 */
@SideOnly(Side.CLIENT)
public class EntityRendererRougeAndroid<T extends EntityRougeAndroidMob> extends RenderBiped<T> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.PATH_ENTITIES + "android.png");
    public static final ResourceLocation TEXTURE_HOLOGRAM = new ResourceLocation(Reference.PATH_ENTITIES + "android_holo.png");
    public static final ResourceLocation TEXTURE_COLORLESS = new ResourceLocation(Reference.PATH_ENTITIES + "android_colorless.png");
    public static final List<String> NAMES = Arrays.asList("the_codedone","chairosto", "buuz135");

    private final boolean hologram;

    public EntityRendererRougeAndroid(RenderManager renderManager, ModelBiped modelBiped, float f, boolean hologram) {
        super(renderManager, modelBiped, f);
        this.hologram = hologram;
    }

    public EntityRendererRougeAndroid(RenderManager renderManager, boolean hologram) {
        this(renderManager, new ModelBiped(), 0, hologram);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        if (entity.hasCustomName() && NAMES.contains(TextFormatting.getTextWithoutFormattingCodes(entity.getCustomNameTag().toLowerCase()))){
            return TEXTURE_COLORLESS;
        } else if (hologram) {
            return TEXTURE_HOLOGRAM;
        } else {
            return TEXTURE;
        }
    }

    @Override
    protected boolean canRenderName(T entity) {
        return entity.getTeam() != null || Minecraft.getMinecraft().player.getDistance(entity) < 18;
    }

    @Override
    protected void preRenderCallback(T entity, float partialTicks) {
        if (entity.getIsLegendary()) {
            GlStateManager.scale(1.5, 1.5, 1.5);
        }
        if (entity.hasCustomName() && NAMES.contains(TextFormatting.getTextWithoutFormattingCodes(entity.getCustomNameTag().toLowerCase()))){
            float speed = 360 * 0.2f;
            int hsb = (int) (entity.world.getTotalWorldTime() % speed);
            Color color = Color.getHSBColor(hsb / speed, 0.5f, 1f);
            GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1.0f);
        }
        super.preRenderCallback(entity, partialTicks);
    }


}
