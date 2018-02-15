/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
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

package matteroverdrive.world;

import com.astro.clib.util.Platform;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.data.world.GenPositionWorldData;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.GoogleAnalyticsCommon;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.world.buildings.*;
import matteroverdrive.world.dimensions.alien.BiomeGeneratorAlien;
import matteroverdrive.world.dimensions.space.BiomeGeneratorSpace;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

/**
 * Created by Simeon on 3/23/2015.
 */
public class MOWorldGen implements IWorldGenerator, IConfigSubscriber {
    public static final int TRITANIUM_VEINS_PER_CHUNK = 10;
    public static final int TRITANIUM_VEIN_SIZE = 6;
    public static final int DILITHIUM_VEINS_PER_CHUNK = 6;
    public static final int DILITHIUM_VEIN_SIZE = 5;
    public static int SPACE_DIM_ID = 3;
    public static int ALIEN_DIM_ID = 4;
    public static WorldGenMinable dilithiumGen;
    public static WorldGenMinable tritaniumGen;
    public static WorldGenGravitationalAnomaly anomalyGen;
    public static BiomeGeneratorSpace biomeSpace;
    public static BiomeGeneratorAlien biomeAlien;
    public static float BUILDING_SPAWN_CHANCE = 10000000.0f;
    final Random oreRandom;
    final Random anomaliesRandom;
    final Random buildingsRandom;
    public DimensionType SpaceDimension;
    public DimensionType AlienDimension;
    public List<WeightedRandomMOWorldGenBuilding> buildings;
    public Queue<MOImageGen.ImageGenWorker> worldGenBuildingQueue;
    HashSet<Integer> oreDimentionsBlacklist;

    boolean generateTritanium;
    boolean generateDilithium;
    boolean generateAnomalies;
    boolean generateBuildings = true;

    public MOWorldGen(ConfigurationHandler configurationHandler) {
        oreRandom = new Random();
        anomaliesRandom = new Random();
        buildingsRandom = new Random();
        buildings = new ArrayList<>();
        worldGenBuildingQueue = new ArrayDeque<>();

        //biomeSpace = new BiomeGeneratorSpace(new BiomeGenBase.BiomeProperties("Space").setRainDisabled().setTemperature(-273.15f));
        //BiomeGenBase.registerBiome(100,"Space",biomeSpace);
        //biomeAlien = new BiomeGeneratorAlien(new BiomeGenBase.BiomeProperties("Alien").setWaterColor(new Color(250,90,90).getColor()));
        //BiomeGenBase.registerBiome(101,"Alien",biomeAlien);

        //SpaceDimension = DimensionType.register("Space","_space",SPACE_DIM_ID,WorldProviderSpace.class,true);
        //AlienDimension = DimensionType.register("Aline","_alien",ALIEN_DIM_ID,WorldProviderAlien.class,true);

        //DimensionManager.registerDimension(SPACE_DIM_ID,SpaceDimension);
        //DimensionManager.registerDimension(ALIEN_DIM_ID,AlienDimension);

        tritaniumGen = new WorldGenMinable(MatterOverdrive.BLOCKS.tritaniumOre.getDefaultState(), TRITANIUM_VEIN_SIZE);
        dilithiumGen = new WorldGenMinable(MatterOverdrive.BLOCKS.dilithium_ore.getDefaultState(), DILITHIUM_VEIN_SIZE);
        buildings.add(new WeightedRandomMOWorldGenBuilding(new MOAndroidHouseBuilding("android_house"), 40));
        buildings.add(new WeightedRandomMOWorldGenBuilding(new MOSandPit("sand_pit_house", 3), 100));
        buildings.add(new WeightedRandomMOWorldGenBuilding(new MOWorldGenCrashedSpaceShip("crashed_ship"), 75));
        buildings.add(new WeightedRandomMOWorldGenBuilding(new MOWorldGenUnderwaterBase("underwater_base"), 30));
        buildings.add(new WeightedRandomMOWorldGenBuilding(new MOWorldGenCargoShip("cargo_ship"), 5));
        anomalyGen = new WorldGenGravitationalAnomaly("gravitational_anomaly", 5f, 2048, 2048 + 8192);
        oreDimentionsBlacklist = new HashSet<>();

        configurationHandler.subscribe(anomalyGen);
    }

    public static GenPositionWorldData getWorldPositionData(World world) {
        GenPositionWorldData data = (GenPositionWorldData) world.loadData(GenPositionWorldData.class, Reference.WORLD_DATA_MO_GEN_POSITIONS);
        if (data == null) {
            data = new GenPositionWorldData(Reference.WORLD_DATA_MO_GEN_POSITIONS);
            world.setData(Reference.WORLD_DATA_MO_GEN_POSITIONS, data);
        }
        return data;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            case -1:
                generateNether(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 0:
                generateOverworld(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 2:
                generateEnd(world, random, chunkX * 16, chunkZ * 16);
                break;
            default:
                generateOther(world, random, chunkX * 16, chunkZ * 16);
        }

        long worldSeed = world.getSeed();
        Random moRandom = new Random(worldSeed);
        long xSeed = moRandom.nextLong() >> 2 + 1L;
        long zSeed = moRandom.nextLong() >> 2 + 1L;
        long chunkSeed = (xSeed * chunkX + zSeed * chunkZ) ^ worldSeed;
        oreRandom.setSeed(chunkSeed);
        anomaliesRandom.setSeed(chunkSeed);
        buildingsRandom.setSeed(chunkSeed);
        generateGravitationalAnomalies(world, anomaliesRandom, chunkX * 16, chunkZ * 16, world.provider.getDimension());
        generateOres(world, oreRandom, chunkX * 16, chunkZ * 16, world.provider.getDimension());
        //startGenerateBuildings(world, buildingsRandom, chunkX, chunkZ, chunkGenerator, chunkProvider);
    }

    public void generateOverworld(World world, Random random, int chunkX, int chunkZ) {

    }

    public void generateNether(World world, Random random, int chunkX, int chunkZ) {

    }

    public void generateEnd(World world, Random random, int chunkX, int chunkZ) {

    }

    public void generateOther(World world, Random random, int chunkX, int chunkZ) {

    }

    public void generateOres(World world, Random random, int chunkX, int chunkZ, int dimentionID) {
        if (!oreDimentionsBlacklist.contains(dimentionID)) {
            if (generateDilithium) {
                for (int i = 0; i < DILITHIUM_VEINS_PER_CHUNK; i++) {
                    int x = chunkX + random.nextInt(16);
                    int z = chunkZ + random.nextInt(16);
                    int y = random.nextInt(28) + 4;

                    if (dilithiumGen.generate(world, random, new BlockPos(x, y, z))) {

                    }
                }
            }

            if (generateTritanium) {
                for (int i = 0; i < TRITANIUM_VEINS_PER_CHUNK; i++) {
                    int x = chunkX + random.nextInt(16);
                    int z = chunkZ + random.nextInt(16);
                    int y = random.nextInt(60) + 4;

                    if (tritaniumGen.generate(world, random, new BlockPos(x, y, z))) {

                    }
                }
            }
        }
    }

    private void generateGravitationalAnomalies(World world, Random random, int chunkX, int chunkZ, int dimension) {
        if (generateAnomalies) {
            BlockPos pos = new BlockPos(chunkX + random.nextInt(16), chunkZ + random.nextInt(16), random.nextInt(60) + 4);

            if (anomalyGen.generate(world, random, pos)) {
                if (Platform.isDevEnv())
                    System.out.println("Generated Anomaly at " + pos);
            }
        }
    }

    private boolean shouldGenerate(Block block, ConfigurationHandler config) {
        Property p = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN, ConfigurationHandler.CATEGORY_WORLD_SPAWN + "." + block.getUnlocalizedName(), true);
        p.setLanguageKey(block.getUnlocalizedName() + ".name");
        return p.getBoolean(true);
    }

    private void startGenerateBuildings(World world, Random random, int chunkX, int chunkZ, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (generateBuildings && random.nextDouble() <= BUILDING_SPAWN_CHANCE) {
            BlockPos pos = world.getHeight(new BlockPos(chunkX * 16 + random.nextInt(16), 0, chunkZ * 16 + random.nextInt(16)));

            WeightedRandomMOWorldGenBuilding building = getRandomBuilding(world, pos.add(0, -2, 0), random);
            if (building != null) {
                startBuildingGeneration(building.worldGenBuilding, pos.add(0, -2, 0), random, world, chunkGenerator, chunkProvider, false);
            }
        }
    }

    public MOImageGen.ImageGenWorker startBuildingGeneration(MOWorldGenBuilding building, BlockPos pos, Random random, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider, boolean forceGeneration) {
        if (building == null)
            return null;
        pos = pos.add(8, 0, 8);
        if (building != null && (forceGeneration || (building.shouldGenerate(random, world, pos) && building.isLocationValid(world, pos)))) {
            MOImageGen.ImageGenWorker worker = building.createWorker(random, pos, world, chunkGenerator, chunkProvider);
            worldGenBuildingQueue.add(worker);
            return worker;
        }
        return null;
    }

    public void manageBuildingGeneration(TickEvent.WorldTickEvent worldTickEvent) {
        MOImageGen.ImageGenWorker worker = worldGenBuildingQueue.peek();
        if (worker != null) {
            if (worker.generate()) {
                worldGenBuildingQueue.remove();
                return;
            }
        }
    }

    public WeightedRandomMOWorldGenBuilding getRandomBuilding(World world, BlockPos pos, Random random) {
        return getBuilding(random, world, pos, buildings, random.nextInt(getTotalBuildingsWeight(random, world, pos, buildings)));
    }

    public int getTotalBuildingsWeight(Random random, World world, BlockPos pos, Collection p_76272_0_) {
        int i = 0;
        WeightedRandomMOWorldGenBuilding building;

        for (Iterator iterator = p_76272_0_.iterator(); iterator.hasNext(); i += building.getWeight(random, world, pos)) {
            building = (WeightedRandomMOWorldGenBuilding) iterator.next();
        }

        return i;
    }

    public WeightedRandomMOWorldGenBuilding getBuilding(Random random, World world, BlockPos pos, Collection par1Collection, int weight) {
        int j = weight;
        Iterator iterator = par1Collection.iterator();
        WeightedRandomMOWorldGenBuilding building;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            building = (WeightedRandomMOWorldGenBuilding) iterator.next();
            j -= building.getWeight(random, world, pos);
        }
        while (j >= 0);

        return building;
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        Property shouldGenerateOres = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN, ConfigurationHandler.CATEGORY_WORLD_SPAWN_ORES, true);
        shouldGenerateOres.setComment("Should Matter Overdrive Ore Blocks be Generated ?");
        generateTritanium = shouldGenerate(MatterOverdrive.BLOCKS.tritaniumOre, config) && shouldGenerateOres.getBoolean(true);
        generateDilithium = shouldGenerate(MatterOverdrive.BLOCKS.dilithium_ore, config) && shouldGenerateOres.getBoolean(true);
        Property shouldGenerateOthers = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN, ConfigurationHandler.CATEGORY_WORLD_SPAWN_OTHER, true);
        shouldGenerateOthers.setComment("Should other Matter Overdrive World Blocks be Generated?");
        generateAnomalies = shouldGenerate(MatterOverdrive.BLOCKS.gravitational_anomaly, config) && shouldGenerateOthers.getBoolean(true);
        this.oreDimentionsBlacklist.clear();
        Property oreDimentionBlacklistProp = config.config.get(ConfigurationHandler.CATEGORY_WORLD_GEN, "ore_gen_blacklist", new int[]{-1, 2});
        oreDimentionBlacklistProp.setComment("A blacklist of all the Dimensions ores shouldn't spawn in");
        oreDimentionBlacklistProp.setLanguageKey("config.ore_gen_blacklist.name");
        int[] oreDimentionBlacklist = oreDimentionBlacklistProp.getIntList();
        for (int anOreDimentionBlacklist : oreDimentionBlacklist) {
            this.oreDimentionsBlacklist.add(anOreDimentionBlacklist);
        }
        generateBuildings = config.getBool("generate buildings", ConfigurationHandler.CATEGORY_WORLD_GEN, true, "Should Matter Overdrive Buildings Generate aka ImageGen");
        MatterOverdrive.PROXY.getGoogleAnalytics().sendEventHit(GoogleAnalyticsCommon.EVENT_CATEGORY_CONFIG, ConfigurationHandler.CATEGORY_WORLD_GEN, String.format("generate buildings(%s)", generateBuildings ? "true" : "false"), null);
    }
}
