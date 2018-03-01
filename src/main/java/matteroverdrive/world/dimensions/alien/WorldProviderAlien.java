package matteroverdrive.world.dimensions.alien;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.client.data.Color;
import matteroverdrive.world.MOWorldGen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/23/2016.
 */
public class WorldProviderAlien extends WorldProvider {
    @Override
    public DimensionType getDimensionType() {
        return MatterOverdrive.DIMENSION_HANDLER.ALIEN_TYPE;
    }

    @Override
    protected void init() {
        super.init();
        this.biomeProvider = new BiomeProviderSingle(MatterOverdrive.DIMENSION_HANDLER.ALIEN_BIOME);
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks) {
        float f = world.getCelestialAngle(partialTicks);
        float f1 = MathHelper.cos(f * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
        f1 = MathHelper.clamp(f1, 0.0F, 1.0F);
        Color color = new Color(188, 147, 221).multiplyWithoutAlpha(0.7f);
        Biome biome = getBiomeForCoords(cameraEntity.getPosition());
        if (biome != null) {
            double d0 = (double) MathHelper.clamp(biome.getTemperature(cameraEntity.getPosition()), 0.0F, 1.0F);
            double d1 = (double) MathHelper.clamp(biome.getRainfall(), 0.0F, 1.0F);
            color = new Color(ColorizerAlien.getSkyColor(d0, d1));
        }
        return new Vec3d(color.getFloatR() * f1, color.getFloatG() * f1, color.getFloatB() * f1);
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        float f = world.getCelestialAngle(p_76562_2_);
        float f1 = MathHelper.cos(f * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
        f1 = Math.max(0.3f, f1);
        Color color = new Color(207, 168, 238);
        Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();
        if (renderViewEntity != null) {
            Biome biomeGenBase = getBiomeForCoords(renderViewEntity.getPosition());
            if (biomeGenBase != null) {
                double d0 = (double) MathHelper.clamp(biomeGenBase.getTemperature(renderViewEntity.getPosition()), 0.0F, 1.0F);
                double d1 = (double) MathHelper.clamp(biomeGenBase.getRainfall(), 0.0F, 1.0F);
                color = new Color(ColorizerAlien.getFogColor(d0, d1));
            }
        }
        return new Vec3d(color.getFloatR() * f1, color.getFloatG() * f1, color.getFloatB() * f1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return -10;
    }

    @Override
    public String getSaveFolder() {
        return "ALIEN";
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorAlien(world, world.getSeed(), world.getWorldInfo().getGeneratorOptions());
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }


}