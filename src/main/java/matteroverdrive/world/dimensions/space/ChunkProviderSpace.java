package matteroverdrive.world.dimensions.space;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 2/5/2016.
 */
public class ChunkProviderSpace implements IChunkGenerator {
    private World spaceWorld;
    private Biome[] biomesForGeneration;
    private Random spaceRandom;

    public ChunkProviderSpace(World world, long seed) {
        spaceWorld = world;
        spaceRandom = new Random(seed);
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
        return null;
    }

    @Override
    public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
        return false;
    }

    @Override
    public Chunk generateChunk(int x, int z) {
        ChunkPrimer primer = new ChunkPrimer();
        Chunk chunk = new Chunk(this.spaceWorld, primer, x, z);
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return this.spaceWorld.getBiome(pos).getSpawnableList(creatureType);
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {

    }

    @Override
    public void populate(int x, int z) {

    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }
}