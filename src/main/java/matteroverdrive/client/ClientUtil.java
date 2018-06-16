package matteroverdrive.client;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class ClientUtil {
    private static final float fluidOffset = 0.005F;

    public ClientUtil() {
    }

    public static void registerModel(Item item, String location) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(location), "inventory"));
    }

    public static void registerModel(Block block, String location) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(location), "inventory"));
    }

    public static void registerToNormal(Block block) {
        final String resourcePath = block.getRegistryName().toString();
        ModelLoader.setCustomStateMapper(block, new DefaultStateMapper() {
            protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                return new ModelResourceLocation(resourcePath, "normal");
            }
        });
        NonNullList<ItemStack> subBlocks = NonNullList.create();
        block.getSubBlocks(null, subBlocks);

        for (ItemStack stack : subBlocks) {
            IBlockState state = block.getStateFromMeta(stack.getMetadata());
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), stack.getMetadata(), new ModelResourceLocation(resourcePath, "inventory"));
        }
    }

    public static void registerWithMapper(Block block) {
        if (block != null) {
            final String resourcePath = block.getRegistryName().toString();
            ModelLoader.setCustomStateMapper(block, new DefaultStateMapper() {
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return new ModelResourceLocation(resourcePath, this.getPropertyString(state.getProperties()));
                }
            });
            NonNullList<ItemStack> subBlocks = NonNullList.create();
            block.getSubBlocks(null, subBlocks);

            for (ItemStack stack : subBlocks) {
                IBlockState state = block.getStateFromMeta(stack.getMetadata());
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), stack.getMetadata(), new ModelResourceLocation(resourcePath, getPropertyString(state.getProperties())));
            }
        }

    }
    public static String getPropertyString(Map<IProperty<?>, Comparable<?>> values, String... extrasArgs) {
        StringBuilder stringbuilder = new StringBuilder();

        for (Map.Entry<IProperty<?>, Comparable<?>> entry : values.entrySet()) {
            if (stringbuilder.length() != 0) {
                stringbuilder.append(",");
            }

            IProperty<?> iproperty = entry.getKey();
            stringbuilder.append(iproperty.getName());
            stringbuilder.append("=");
            stringbuilder.append(getPropertyName(iproperty, entry.getValue()));
        }


        if (stringbuilder.length() == 0) {
            stringbuilder.append("inventory");
        }

        for (String args : extrasArgs) {
            if (stringbuilder.length() != 0)
                stringbuilder.append(",");
            stringbuilder.append(args);
        }

        return stringbuilder.toString();
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String getPropertyName(IProperty<T> property, Comparable<?> comparable) {
        return property.getName((T) comparable);
    }

    public static void setCustomModelLocation(Item item) {
        ModelResourceLocation location = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, location);
    }

    public static void setCustomModelLocation(Block block) {
        ModelResourceLocation location = new ModelResourceLocation(block.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, location);
    }

    public static void renderTexturedQuad(TextureAtlasSprite texture, EnumFacing face, BlockPos pos, double x1, double y1, double z1, double x2, double y2, double z2, int brightness, int color) {
        BufferBuilder renderer = Tessellator.getInstance().getBuffer();
        renderer.begin(7, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        putTexturedQuad(renderer, texture, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, face, color, brightness, false);
        Tessellator.getInstance().draw();
    }

    public static void renderTexturedCuboid(TextureAtlasSprite[] textures, BlockPos pos, double x1, double y1, double z1, double x2, double y2, double z2, int brightness, int color) {
        BufferBuilder renderer = Tessellator.getInstance().getBuffer();
        renderer.begin(7, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        putTexturedQuad(renderer, textures[0], x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.DOWN, color, brightness, false);
        putTexturedQuad(renderer, textures[1], x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.NORTH, color, brightness, false);
        putTexturedQuad(renderer, textures[2], x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.EAST, color, brightness, false);
        putTexturedQuad(renderer, textures[3], x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.SOUTH, color, brightness, false);
        putTexturedQuad(renderer, textures[4], x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.WEST, color, brightness, false);
        putTexturedQuad(renderer, textures[5], x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.UP, color, brightness, false);
        Tessellator.getInstance().draw();
    }

    public static void renderTexturedCuboid(TextureAtlasSprite texture, BlockPos pos, double x1, double y1, double z1, double x2, double y2, double z2, int brightness, int color) {
        BufferBuilder renderer = Tessellator.getInstance().getBuffer();
        renderer.begin(7, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        putTexturedQuad(renderer, texture, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.DOWN, color, brightness, false);
        putTexturedQuad(renderer, texture, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.NORTH, color, brightness, false);
        putTexturedQuad(renderer, texture, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.EAST, color, brightness, false);
        putTexturedQuad(renderer, texture, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.SOUTH, color, brightness, false);
        putTexturedQuad(renderer, texture, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.WEST, color, brightness, false);
        putTexturedQuad(renderer, texture, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.UP, color, brightness, false);
        Tessellator.getInstance().draw();
    }

    public static void drawCube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, BufferBuilder buffer) {
        drawPlaneNegX(minX, minY, maxY, minZ, maxZ, buffer);
        drawPlanePosX(maxX, minY, maxY, minZ, maxZ, buffer);
        drawPlaneNegY(minY, minX, maxX, minZ, maxZ, buffer);
        drawPlanePosY(maxY, minX, maxX, minZ, maxZ, buffer);
        drawPlaneNegZ(minZ, minX, maxX, minY, maxY, buffer);
        drawPlanePosZ(maxZ, minX, maxX, minY, maxY, buffer);
    }

    public static void drawCube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float a, BufferBuilder buffer) {
        drawPlaneNegX(minX, minY, maxY, minZ, maxZ, r, g, b, a * 0.9F, buffer);
        drawPlanePosX(maxX, minY, maxY, minZ, maxZ, r, g, b, a * 0.9F, buffer);
        drawPlaneNegY(minY, minX, maxX, minZ, maxZ, r, g, b, a * 0.8F, buffer);
        drawPlanePosY(maxY, minX, maxX, minZ, maxZ, r, g, b, a * 1.1F, buffer);
        drawPlaneNegZ(minZ, minX, maxX, minY, maxY, r, g, b, a, buffer);
        drawPlanePosZ(maxZ, minX, maxX, minY, maxY, r, g, b, a, buffer);
    }

    public static void drawPlaneNegX(double x, double minY, double maxY, double minZ, double maxZ, BufferBuilder buffer) {
        buffer.pos(x, minY, minZ).endVertex();
        buffer.pos(x, minY, maxZ).endVertex();
        buffer.pos(x, maxY, maxZ).endVertex();
        buffer.pos(x, maxY, minZ).endVertex();
    }

    public static void drawPlaneNegX(double x, double minY, double maxY, double minZ, double maxZ, float r, float g, float b, float a, BufferBuilder buffer) {
        buffer.pos(x, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(x, minY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(x, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(x, maxY, minZ).color(r, g, b, a).endVertex();
    }

    public static void drawPlanePosX(double x, double minY, double maxY, double minZ, double maxZ, BufferBuilder buffer) {
        buffer.pos(x, minY, minZ).endVertex();
        buffer.pos(x, maxY, minZ).endVertex();
        buffer.pos(x, maxY, maxZ).endVertex();
        buffer.pos(x, minY, maxZ).endVertex();
    }

    public static void drawPlanePosX(double x, double minY, double maxY, double minZ, double maxZ, float r, float g, float b, float a, BufferBuilder buffer) {
        buffer.pos(x, minY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(x, maxY, minZ).color(r, g, b, a).endVertex();
        buffer.pos(x, maxY, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(x, minY, maxZ).color(r, g, b, a).endVertex();
    }

    public static void drawPlaneNegY(double y, double minX, double maxX, double minZ, double maxZ, BufferBuilder buffer) {
        buffer.pos(minX, y, minZ).endVertex();
        buffer.pos(maxX, y, minZ).endVertex();
        buffer.pos(maxX, y, maxZ).endVertex();
        buffer.pos(minX, y, maxZ).endVertex();
    }

    public static void drawPlaneNegY(double y, double minX, double maxX, double minZ, double maxZ, float r, float g, float b, float a, BufferBuilder buffer) {
        buffer.pos(minX, y, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, y, minZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, y, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, y, maxZ).color(r, g, b, a).endVertex();
    }

    public static void drawPlanePosY(double y, double minX, double maxX, double minZ, double maxZ, BufferBuilder buffer) {
        buffer.pos(minX, y, minZ).endVertex();
        buffer.pos(minX, y, maxZ).endVertex();
        buffer.pos(maxX, y, maxZ).endVertex();
        buffer.pos(maxX, y, minZ).endVertex();
    }

    public static void drawPlanePosY(double y, double minX, double maxX, double minZ, double maxZ, float r, float g, float b, float a, BufferBuilder buffer) {
        buffer.pos(minX, y, minZ).color(r, g, b, a).endVertex();
        buffer.pos(minX, y, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, y, maxZ).color(r, g, b, a).endVertex();
        buffer.pos(maxX, y, minZ).color(r, g, b, a).endVertex();
    }

    public static void drawPlaneNegZ(double z, double minX, double maxX, double minY, double maxY, BufferBuilder buffer) {
        buffer.pos(minX, minY, z).endVertex();
        buffer.pos(minX, maxY, z).endVertex();
        buffer.pos(maxX, maxY, z).endVertex();
        buffer.pos(maxX, minY, z).endVertex();
    }

    public static void drawPlaneNegZ(double z, double minX, double maxX, double minY, double maxY, float r, float g, float b, float a, BufferBuilder buffer) {
        buffer.pos(minX, minY, z).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, z).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, z).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, z).color(r, g, b, a).endVertex();
    }

    public static void drawPlanePosZ(double z, double minX, double maxX, double minY, double maxY, BufferBuilder buffer) {
        buffer.pos(minX, minY, z).endVertex();
        buffer.pos(maxX, minY, z).endVertex();
        buffer.pos(maxX, maxY, z).endVertex();
        buffer.pos(minX, maxY, z).endVertex();
    }

    public static void drawPlanePosZ(double z, double minX, double maxX, double minY, double maxY, float r, float g, float b, float a, BufferBuilder buffer) {
        buffer.pos(minX, minY, z).color(r, g, b, a).endVertex();
        buffer.pos(maxX, minY, z).color(r, g, b, a).endVertex();
        buffer.pos(maxX, maxY, z).color(r, g, b, a).endVertex();
        buffer.pos(minX, maxY, z).color(r, g, b, a).endVertex();
    }

    public static void renderFluidCuboid(FluidStack fluid, BlockPos pos, double x, double y, double z, double w, double h, double d) {
        double wd = (1.0D - w) / 2.0D;
        double hd = (1.0D - h) / 2.0D;
        double dd = (1.0D - d) / 2.0D;
        renderFluidCuboid(fluid, pos, x, y, z, wd, hd, dd, 1.0D - wd, 1.0D - hd, 1.0D - dd);
    }

    public static void renderFluidCuboid(FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2) {
        int color = fluid.getFluid().getColor(fluid);
        renderFluidCuboid(fluid, pos, x, y, z, x1, y1, z1, x2, y2, z2, color);
    }

    public static void renderFluidCuboid(FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        BufferBuilder renderer = Tessellator.getInstance().getBuffer();
        renderer.begin(7, DefaultVertexFormats.BLOCK);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        int brightness = Minecraft.getMinecraft().world.getCombinedLight(pos, fluid.getFluid().getLuminosity());
        TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
        TextureAtlasSprite flowing = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getFlowing(fluid).toString());
        putTexturedQuad(renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.DOWN, color, brightness, false);
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.NORTH, color, brightness, true);
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.EAST, color, brightness, true);
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.SOUTH, color, brightness, true);
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.WEST, color, brightness, true);
        putTexturedQuad(renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.UP, color, brightness, false);
        Tessellator.getInstance().draw();
    }

    public static void renderStackedFluidCuboid(FluidStack fluid, double px, double py, double pz, BlockPos pos, BlockPos from, BlockPos to, double ymin, double ymax) {
        if (ymin < ymax) {
            BufferBuilder renderer = Tessellator.getInstance().getBuffer();
            renderer.begin(7, DefaultVertexFormats.BLOCK);
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            int color = fluid.getFluid().getColor(fluid);
            int brightness = Minecraft.getMinecraft().world.getCombinedLight(pos, fluid.getFluid().getLuminosity());
            GlStateManager.translate((float) from.getX(), (float) from.getY(), (float) from.getZ());
            TextureAtlasSprite still = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
            TextureAtlasSprite flowing = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getFlowing(fluid).toString());
            if (still == null) {
                still = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
            }

            if (flowing == null) {
                flowing = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
            }

            int xd = to.getX() - from.getX();
            int yd = (int) (ymax - ymin);
            int zd = to.getZ() - from.getZ();
            double xmin = 0.004999999888241291D;
            double xmax = (double) xd + 1.0D - 0.004999999888241291D;
            double zmin = 0.004999999888241291D;
            double zmax = (double) zd + 1.0D - 0.004999999888241291D;
            double[] xs = new double[2 + xd];
            double[] ys = new double[2 + yd];
            double[] zs = new double[2 + zd];
            xs[0] = xmin;

            int y;
            for (y = 1; y <= xd; ++y) {
                xs[y] = (double) y;
            }

            xs[xd + 1] = xmax;
            ys[0] = ymin;

            for (y = 1; y <= yd; ++y) {
                ys[y] = (double) y;
            }

            ys[yd + 1] = ymax;
            zs[0] = zmin;

            for (y = 1; y <= zd; ++y) {
                zs[y] = (double) y;
            }

            zs[zd + 1] = zmax;

            for (y = 0; y <= yd; ++y) {
                for (int z = 0; z <= zd; ++z) {
                    for (int x = 0; x <= xd; ++x) {
                        double x1 = xs[x];
                        double x2 = xs[x + 1] - x1;
                        double y1 = ys[y];
                        double y2 = ys[y + 1] - y1;
                        double z1 = zs[z];
                        double z2 = zs[z + 1] - z1;
                        if (x == 0) {
                            putTexturedQuad(renderer, flowing, x1, y1, z1, x2, y2, z2, EnumFacing.WEST, color, brightness, true);
                        }

                        if (x == xd) {
                            putTexturedQuad(renderer, flowing, x1, y1, z1, x2, y2, z2, EnumFacing.EAST, color, brightness, true);
                        }

                        if (y == 0) {
                            putTexturedQuad(renderer, still, x1, y1, z1, x2, y2, z2, EnumFacing.DOWN, color, brightness, false);
                        }

                        if (y == yd) {
                            putTexturedQuad(renderer, still, x1, y1, z1, x2, y2, z2, EnumFacing.UP, color, brightness, false);
                        }

                        if (z == 0) {
                            putTexturedQuad(renderer, flowing, x1, y1, z1, x2, y2, z2, EnumFacing.NORTH, color, brightness, true);
                        }

                        if (z == zd) {
                            putTexturedQuad(renderer, flowing, x1, y1, z1, x2, y2, z2, EnumFacing.SOUTH, color, brightness, true);
                        }
                    }
                }
            }

            Tessellator.getInstance().draw();
        }
    }

    public static void putTexturedQuad(BufferBuilder renderer, TextureAtlasSprite sprite, double x, double y, double z, double w, double h, double d, EnumFacing face, int color, int brightness, boolean flowing) {
        int l1 = brightness >> 16 & '\uffff';
        int l2 = brightness & '\uffff';
        int a = color >> 24 & 255;
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;
        putTexturedQuad(renderer, sprite, x, y, z, w, h, d, face, r, g, b, a, l1, l2, flowing);
    }

    public static void putTexturedQuad(BufferBuilder renderer, TextureAtlasSprite sprite, double x, double y, double z, double w, double h, double d, EnumFacing face, int r, int g, int b, int a, int light1, int light2, boolean flowing) {
        if (sprite != null) {
            double size = 16.0D;
            if (flowing) {
                size = 8.0D;
            }

            double x2 = x + w;
            double y2 = y + h;
            double z2 = z + d;
            double xt1 = x % 1.0D;

            double xt2;
            for (xt2 = xt1 + w; xt2 > 1.0D; --xt2) {
            }

            double yt1 = y % 1.0D;

            double yt2;
            for (yt2 = yt1 + h; yt2 > 1.0D; --yt2) {
            }

            double zt1 = z % 1.0D;

            double zt2;
            for (zt2 = zt1 + d; zt2 > 1.0D; --zt2) {
            }

            if (flowing) {
                double tmp = 1.0D - yt1;
                yt1 = 1.0D - yt2;
                yt2 = tmp;
            }

            double minU;
            double maxU;
            double minV;
            double maxV;
            switch (face) {
                case DOWN:
                case UP:
                    minU = (double) sprite.getInterpolatedU(xt1 * size);
                    maxU = (double) sprite.getInterpolatedU(xt2 * size);
                    minV = (double) sprite.getInterpolatedV(zt1 * size);
                    maxV = (double) sprite.getInterpolatedV(zt2 * size);
                    break;
                case NORTH:
                case SOUTH:
                    minU = (double) sprite.getInterpolatedU(xt2 * size);
                    maxU = (double) sprite.getInterpolatedU(xt1 * size);
                    minV = (double) sprite.getInterpolatedV(yt1 * size);
                    maxV = (double) sprite.getInterpolatedV(yt2 * size);
                    break;
                case WEST:
                case EAST:
                    minU = (double) sprite.getInterpolatedU(zt2 * size);
                    maxU = (double) sprite.getInterpolatedU(zt1 * size);
                    minV = (double) sprite.getInterpolatedV(yt1 * size);
                    maxV = (double) sprite.getInterpolatedV(yt2 * size);
                    break;
                default:
                    minU = (double) sprite.getMinU();
                    maxU = (double) sprite.getMaxU();
                    minV = (double) sprite.getMinV();
                    maxV = (double) sprite.getMaxV();
            }

            switch (face) {
                case DOWN:
                    renderer.pos(x, y, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                    renderer.pos(x, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                    break;
                case UP:
                    renderer.pos(x, y2, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                    renderer.pos(x, y2, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y2, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                    break;
                case NORTH:
                    renderer.pos(x, y, z).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                    renderer.pos(x, y2, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y2, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y, z).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                    break;
                case SOUTH:
                    renderer.pos(x, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                    renderer.pos(x, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                    break;
                case WEST:
                    renderer.pos(x, y, z).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                    renderer.pos(x, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                    renderer.pos(x, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                    renderer.pos(x, y2, z).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                    break;
                case EAST:
                    renderer.pos(x2, y, z).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y2, z).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                    renderer.pos(x2, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
            }

        }
    }
}
