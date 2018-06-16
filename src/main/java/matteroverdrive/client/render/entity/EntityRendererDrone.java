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
import matteroverdrive.client.model.ModelDrone;
import matteroverdrive.entity.EntityDrone;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 1/22/2016.
 */
@SideOnly(Side.CLIENT)
public class EntityRendererDrone extends RenderLiving<EntityDrone> {
    private final ResourceLocation texture = new ResourceLocation(Reference.PATH_ENTITIES + "drone_default.png");

    public EntityRendererDrone(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelDrone(), 0.5f);
    }

    @Override
    public void doRender(EntityDrone entity, double x, double y, double z, float entityYaw, float partialTicks) {
        mainModel = new ModelDrone();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDrone entity) {
        return texture;
    }
}
