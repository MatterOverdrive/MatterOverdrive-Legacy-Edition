package matteroverdrive.client.model.baked;

import matteroverdrive.client.model.ModelBase;
import matteroverdrive.client.model.ModelUtil;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EmptyBakedModel implements IBakedModel {

    Function<ResourceLocation, TextureAtlasSprite> getter;
    VertexFormat format;

    public EmptyBakedModel(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ModelBase model) {
        this.getter = bakedTextureGetter;
        this.format = format;
    }

    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        return Collections.emptyList();
    }

    public boolean isAmbientOcclusion() {
        return true;
    }

    public boolean isGui3d() {
        return true;
    }

    public boolean isBuiltInRenderer() {
        return false;
    }

    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Collections.emptyList());
    }

    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
        if (ModelUtil.BLOCK_TRANSFORMS.containsKey(type)) {
            return Pair.of(this, ModelUtil.BLOCK_TRANSFORMS.get(type).getMatrix());
        } else {
            return ForgeHooksClient.handlePerspective(this, type);
        }
    }
}