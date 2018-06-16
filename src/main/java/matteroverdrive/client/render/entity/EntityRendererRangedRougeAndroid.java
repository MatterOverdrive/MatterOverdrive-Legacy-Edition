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
import matteroverdrive.client.data.Color;
import matteroverdrive.client.model.MOModelRenderColored;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 11/15/2015.
 */
public class EntityRendererRangedRougeAndroid extends EntityRendererRougeAndroid<EntityRangedRogueAndroidMob> {
    public static final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTITIES + "android_ranged.png");
    final MOModelRenderColored visorModel;

    public EntityRendererRangedRougeAndroid(RenderManager renderManager) {
        super(renderManager, new ModelBiped(0, 0, 96, 64), 0, false);
        visorModel = new MOModelRenderColored(mainModel, 64, 0);
        visorModel.setDisableLighting(true);
        visorModel.addBox(-4, -8, -4, 8, 8, 8);
        ((ModelBiped) mainModel).bipedHead.addChild(visorModel);
    }

    /*@Override
	protected void func_82422_c()
    {
        GlStateManager.translate(0,0.2,-0.3);
        GL11.glRotatef(-97, 0, 0, 1.0F);
        GL11.glRotatef(-60, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.6,0.6,0.6);
    }*/

    @Override
    protected ResourceLocation getEntityTexture(EntityRangedRogueAndroidMob entity) {
        return texture;
    }

    @Override
    public void doRender(EntityRangedRogueAndroidMob entity, double x, double y, double z, float entityYaw, float partialTicks) {
        visorModel.setColor(new Color(entity.getVisorColor()));

        ((ModelBiped) mainModel).rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

}
