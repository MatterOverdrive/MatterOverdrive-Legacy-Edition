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
package matteroverdrive.client.model;

import matteroverdrive.client.model.baked.BakedModelBase;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class ModelBlock extends ModelBase {
    Constructor<? extends BakedModelBase> constructor;

    public ModelBlock(Class<? extends BakedModelBase> block, ResourceLocation particle, ResourceLocation all) {
        this(block, particle, all, all, all, all, all, all);
    }

    public ModelBlock(Class<? extends BakedModelBase> block, ResourceLocation particle, ResourceLocation top, ResourceLocation side, ResourceLocation bottom) {
        this(block, particle, side, side, side, side, top, bottom);
    }

    public ModelBlock(Class<? extends BakedModelBase> block, ResourceLocation particle, ResourceLocation north, ResourceLocation east, ResourceLocation south, ResourceLocation west, ResourceLocation up, ResourceLocation down) {
        try {
            this.constructor = block.getConstructor(IModelState.class, VertexFormat.class, Function.class, ModelBase.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        this.addTexture("particle", particle);
        this.addTexture("north", north);
        this.addTexture("south", south);
        this.addTexture("east", east);
        this.addTexture("west", west);
        this.addTexture("up", up);
        this.addTexture("down", down);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        try {
            IBakedModel model = constructor.newInstance(state, format, bakedTextureGetter, this);
            return model;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return super.bake(state, format, bakedTextureGetter);
    }

    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }
}