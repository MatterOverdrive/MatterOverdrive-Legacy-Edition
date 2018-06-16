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