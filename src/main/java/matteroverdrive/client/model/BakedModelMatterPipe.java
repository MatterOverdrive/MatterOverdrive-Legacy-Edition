package matteroverdrive.client.model;

import matteroverdrive.client.model.baked.BakedModelBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BakedModelMatterPipe extends BakedModelBase {
    public BakedModelMatterPipe(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ModelBase model) {
        super(state, format, bakedTextureGetter, model);
    }

    @Override
    public List<BakedQuad> buildQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        List<BakedQuad> quads = new ArrayList<>();
        ModelUtil.makeCuboid(format, 0, 0, 0, 1, 1, 1, ModelUtil.FULL_FACES, textureArray, -1).addToList(quads, side);
        return quads;
    }
}