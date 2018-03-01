package matteroverdrive.world.dimensions;

import matteroverdrive.client.data.Color;
import matteroverdrive.world.dimensions.alien.BiomeGeneratorAlien;
import matteroverdrive.world.dimensions.alien.WorldProviderAlien;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MODimensionHandler {
    public DimensionType ALIEN_TYPE;
    public BiomeGeneratorAlien ALIEN_BIOME;

    public void init() {
        int alienID = DimensionManager.getNextFreeDimId();
        ALIEN_TYPE = DimensionType.register("alien", "_mo_alien", alienID, WorldProviderAlien.class, false);
        DimensionManager.registerDimension(alienID, ALIEN_TYPE);
        ALIEN_BIOME = new BiomeGeneratorAlien(new Biome.BiomeProperties("Alien").setWaterColor(new Color(250, 90, 90).getColor()));
        ALIEN_BIOME.setRegistryName("alien");
    }

    @SubscribeEvent
    public void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().registerAll(
                ALIEN_BIOME
        );
    }
}
