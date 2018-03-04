package matteroverdrive.world.dimensions.space;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.proxy.ClientProxy;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by Simeon on 2/5/2016.
 */
public class WorldProviderSpace extends WorldProvider {

    @Override
    protected void init() {
        this.biomeProvider = new BiomeProviderSingle(MatterOverdrive.DIMENSION_HANDLER.SPACE_BIOME);
        this.hasSkyLight = true;
    }

    @Nullable
    @Override
    public IRenderHandler getSkyRenderer() {
        return ClientProxy.renderHandler.getSpaceSkyRenderer();
    }

    @Override
    public DimensionType getDimensionType() {
        return MatterOverdrive.DIMENSION_HANDLER.SPACE_TYPE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vec3d getFogColor(float x, float z) {
        return new Vec3d(0, 0, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public double getVoidFogYFactor() {
        return 0;
    }

    @Override
    public String getSaveFolder() {
        return "SPACE";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }

    @Nullable
    @Override
    public BlockPos getSpawnCoordinate() {
        return new BlockPos(0, 50, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        return -10000;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isSkyColored() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks) {
        return new Vec3d(0, 0, 0);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getSunBrightness(float par1) {
        return world.getSunBrightnessBody(par1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getStarBrightness(float par1) {
        return 1;
    }

    @Override
    public double getHorizon() {
        return Double.MIN_VALUE;
    }

    @Override
    public float getSunBrightnessFactor(float par1) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
        return new float[0];
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderSpace(this.world, this.world.getSeed());
    }
}