package matteroverdrive.client.model;

import com.google.common.collect.Maps;
import matteroverdrive.client.TextureArray;
import matteroverdrive.client.model.part.BakedCuboid;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.util.vector.Vector4f;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "CanBeFinal", "unused"})
public final class ModelUtil {
    public static Vector4f FULL_FACE_UV = new Vector4f(0.0F, 0.0F, 16.0F, 16.0F);
    public static Vector4f[] FULL_FACES;
    public static Map<ItemCameraTransforms.TransformType, TRSRTransformation> ITEM_TRANSFORMS = Maps.newHashMap();
    public static Map<ItemCameraTransforms.TransformType, TRSRTransformation> BLOCK_TRANSFORMS = Maps.newHashMap();
    public static Map<ItemCameraTransforms.TransformType, TRSRTransformation> HANDHELD_TRANSFORMS = Maps.newHashMap();
    public static TRSRTransformation ITEM_GROUND = createTESRTransformation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F);
    public static TRSRTransformation ITEM_HEAD = createTESRTransformation(0.0F, 13.0F, 7.0F, 0.0F, 180.0F, 0.0F, 1.0F);
    public static TRSRTransformation ITEM_THIRD_PERSON_RIGHT = createTESRTransformation(0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.55F);
    public static TRSRTransformation ITEM_THIRD_PERSON_LEFT = createTESRTransformation(0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.55F);
    public static TRSRTransformation ITEM_FIRST_PERSON_RIGHT = createTESRTransformation(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F);
    public static TRSRTransformation ITEM_FIRST_PERSON_LEFT = createTESRTransformation(1.13F, 3.2F, 1.13F, 0.0F, 90.0F, -25.0F, 0.68F);
    public static TRSRTransformation HANDHELD_THIRD_PERSON_RIGHT = createTESRTransformation(0.0F, 4.0F, 0.5F, 0.0F, -90.0F, 55.0F, 0.85F);
    public static TRSRTransformation HANDHELD_THIRD_PERSON_LEFT = createTESRTransformation(0.0F, 4.0F, 0.5F, 0.0F, 90.0F, -55.0F, 0.85F);
    public static TRSRTransformation BLOCK_GUI = createTESRTransformation(0.0F, 0.0F, 0.0F, 30.0F, 225.0F, 0.0F, 0.625F);
    public static TRSRTransformation BLOCK_GROUND = createTESRTransformation(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.25F);
    public static TRSRTransformation BLOCK_FIXED = createTESRTransformation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F);
    public static TRSRTransformation BLOCK_HEAD = createTESRTransformation(0.0F, 13.0F, 7.0F, 0.0F, 180.0F, 0.0F, 1.0F);
    public static TRSRTransformation BLOCK_THIRD_PERSON_RIGHT = createTESRTransformation(0.0F, 2.5F, 0.0F, 75.0F, 45.0F, 0.0F, 0.375F);
    public static TRSRTransformation BLOCK_THIRD_PERSON_LEFT = createTESRTransformation(0.0F, 2.5F, 0.0F, 75.0F, 45.0F, 0.0F, 0.375F);
    public static TRSRTransformation BLOCK_FIRST_PERSON_RIGHT = createTESRTransformation(0.0F, 0.0F, 0.0F, 0.0F, 45.0F, 0.0F, 0.4F);
    public static TRSRTransformation BLOCK_FIRST_PERSON_LEFT = createTESRTransformation(0.0F, 0.0F, 0.0F, 0.0F, 225.0F, 0.0F, 0.4F);

    static {
        ITEM_TRANSFORMS.put(ItemCameraTransforms.TransformType.GROUND, ITEM_GROUND);
        ITEM_TRANSFORMS.put(ItemCameraTransforms.TransformType.HEAD, ITEM_HEAD);
        ITEM_TRANSFORMS.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, ITEM_THIRD_PERSON_RIGHT);
        ITEM_TRANSFORMS.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, ITEM_THIRD_PERSON_LEFT);
        ITEM_TRANSFORMS.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, ITEM_FIRST_PERSON_RIGHT);
        ITEM_TRANSFORMS.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, ITEM_FIRST_PERSON_LEFT);
        HANDHELD_TRANSFORMS.put(ItemCameraTransforms.TransformType.GROUND, ITEM_GROUND);
        HANDHELD_TRANSFORMS.put(ItemCameraTransforms.TransformType.HEAD, ITEM_HEAD);
        HANDHELD_TRANSFORMS.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HANDHELD_THIRD_PERSON_RIGHT);
        HANDHELD_TRANSFORMS.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HANDHELD_THIRD_PERSON_LEFT);
        HANDHELD_TRANSFORMS.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, ITEM_FIRST_PERSON_RIGHT);
        HANDHELD_TRANSFORMS.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, ITEM_FIRST_PERSON_LEFT);
        BLOCK_TRANSFORMS.put(ItemCameraTransforms.TransformType.GUI, BLOCK_GUI);
        BLOCK_TRANSFORMS.put(ItemCameraTransforms.TransformType.GROUND, BLOCK_GROUND);
        BLOCK_TRANSFORMS.put(ItemCameraTransforms.TransformType.FIXED, BLOCK_FIXED);
        BLOCK_TRANSFORMS.put(ItemCameraTransforms.TransformType.HEAD, BLOCK_HEAD);
        BLOCK_TRANSFORMS.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, BLOCK_THIRD_PERSON_RIGHT);
        BLOCK_TRANSFORMS.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, BLOCK_THIRD_PERSON_LEFT);
        BLOCK_TRANSFORMS.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, BLOCK_FIRST_PERSON_RIGHT);
        BLOCK_TRANSFORMS.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, BLOCK_FIRST_PERSON_LEFT);
        FULL_FACES = new Vector4f[]{FULL_FACE_UV, FULL_FACE_UV, FULL_FACE_UV, FULL_FACE_UV, FULL_FACE_UV, FULL_FACE_UV};
    }

    public static TRSRTransformation createTESRTransformation(float translateX, float translateY, float translateZ, float rotateX, float rotateY, float rotateZ, float scale) {
        return createTESRTransformation(new Vector3f(translateX / 16.0F, translateY / 16.0F, translateZ / 16.0F), TRSRTransformation.quatFromXYZDegrees(new Vector3f(rotateX, rotateY, rotateZ)), new Vector3f(scale, scale, scale));
    }

    public static TRSRTransformation createTESRTransformation(Vector3f translation, Quat4f rotation, Vector3f scale) {
        return new TRSRTransformation(translation, rotation, scale, null);
    }

    public static BakedCuboid makeCuboid(VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, Vector4f[] uv, TextureArray textureArray, int tintIndex) {
        if (uv == null)
            uv = FULL_FACES;
        return new BakedCuboid(
                makeCubeFace(format, EnumFacing.NORTH, x1, y1, x1, x2, y2, z2, uv[0].x, uv[0].y, uv[0].z, uv[0].w, textureArray.getNorthTexture(), tintIndex),
                makeCubeFace(format, EnumFacing.EAST, x1, y1, x1, x2, y2, z2, uv[1].x, uv[1].y, uv[1].z, uv[1].w, textureArray.getEastTexture(), tintIndex),
                makeCubeFace(format, EnumFacing.SOUTH, x1, y1, x1, x2, y2, z2, uv[2].x, uv[2].y, uv[2].z, uv[2].w, textureArray.getSouthTexture(), tintIndex),
                makeCubeFace(format, EnumFacing.WEST, x1, y1, x1, x2, y2, z2, uv[3].x, uv[3].y, uv[3].z, uv[3].w, textureArray.getWestTexture(), tintIndex),
                makeCubeFace(format, EnumFacing.UP, x1, y1, x1, x2, y2, z2, uv[4].x, uv[4].y, uv[4].z, uv[4].w, textureArray.getUpTexture(), tintIndex),
                makeCubeFace(format, EnumFacing.DOWN, x1, y1, x1, x2, y2, z2, uv[5].x, uv[5].y, uv[5].z, uv[5].w, textureArray.getDownTexture(), tintIndex)
        );
    }

    public static BakedQuad makeCubeFace(VertexFormat format, EnumFacing side, double x1, double y1, double z1, double x2, double y2, double z2, float u, float v, float uw, float vh, TextureAtlasSprite sprite, int tintIndex) {
        switch (side) {
            case NORTH:
                return createQuad(format, x1 + x2, y1 + y2, z1, x1 + x2, y1, z1, x1, y1, z1, x1, y1 + y2, z1, u, v, uw, vh, new Vec3d(0.0D, 0.0D, -1.0D), sprite, tintIndex);
            case EAST:
                return createQuad(format, x1 + x2, y1 + y2, z1 + z2, x1 + x2, y1, z1 + z2, x1 + x2, y1, z1, x1 + x2, y1 + y2, z1, u, v, uw, vh, new Vec3d(1.0D, 0.0D, 0.0D), sprite, tintIndex);
            case SOUTH:
                return createQuad(format, x1, y1 + y2, z1 + z2, x1, y1, z1 + z2, x1 + x2, y1, z1 + z2, x1 + x2, y1 + y2, z1 + z2, u, v, uw, vh, new Vec3d(0.0D, 0.0D, 1.0D), sprite, tintIndex);
            case WEST:
                return createQuad(format, x1, y1 + y2, z1, x1, y1, z1, x1, y1, z1 + z2, x1, y1 + y2, z1 + z2, u, v, uw, vh, new Vec3d(-1.0D, 0.0D, 0.0D), sprite, tintIndex);
            case UP:
                return createQuad(format, x1, y1 + y2, z1, x1, y1 + y2, z1 + z2, x1 + x2, y1 + y2, z1 + z2, x1 + x2, y1 + y2, z1, u, v, uw, vh, new Vec3d(0.0D, 1.0D, 0.0D), sprite, tintIndex);
            case DOWN:
                return createQuad(format, x1 + x2, y1, z1, x1 + x2, y1, z1 + z2, x1, y1, z1 + z2, x1, y1, z1, u, v, uw, vh, new Vec3d(0.0D, -1.0D, 0.0D), sprite, tintIndex);
            default:
                return null;
        }
    }

    public static BakedQuad createQuad(VertexFormat format, double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4, float u, float v, float uw, float vh, Vec3d normal, TextureAtlasSprite sprite, int tintIndex) {
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        if (tintIndex > -1) {
            builder.setQuadTint(tintIndex);
        }
        putVertex(builder, normal, x1, y1, z1, u, v, sprite);
        putVertex(builder, normal, x2, y2, z2, u, v + vh, sprite);
        putVertex(builder, normal, x3, y3, z3, u + uw, v + vh, sprite);
        putVertex(builder, normal, x4, y4, z4, u + uw, v, sprite);
        return builder.build();
    }

    public static void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v, TextureAtlasSprite sprite) {
        VertexFormat format = builder.getVertexFormat();
        for (int e = 0; e < format.getElementCount(); ++e) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float) x, (float) y, (float) z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0) {
                        u = sprite.getInterpolatedU((double) u);
                        v = sprite.getInterpolatedV((double) v);
                        builder.put(e, u, v, 0.0F, 1.0F);
                    }
                    break;
                case NORMAL:
                    builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0.0F);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }
}