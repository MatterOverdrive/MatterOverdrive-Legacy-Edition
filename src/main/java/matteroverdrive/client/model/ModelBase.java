package matteroverdrive.client.model;

import matteroverdrive.client.model.baked.EmptyBakedModel;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import matteroverdrive.client.model.baked.EmptyBakedModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class ModelBase implements IModel {
    public Map<String, ResourceLocation> textures = Maps.newHashMap();

    public ModelBase addTexture(String name, ResourceLocation texture) {
        this.textures.put(name, texture);
        return this;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new EmptyBakedModel(state, DefaultVertexFormats.BLOCK, bakedTextureGetter, this);
    }

    @Override
    public IModelState getDefaultState() {
        return TRSRTransformation.identity();
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        return this.textures.values();
    }
}