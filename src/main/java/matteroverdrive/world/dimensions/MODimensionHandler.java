package matteroverdrive.world.dimensions;

import com.astro.clib.command.CustomTeleporter;
import matteroverdrive.client.data.Color;
import matteroverdrive.entity.EntityVillagerMadScientist;
import matteroverdrive.world.dimensions.alien.BiomeAlien;
import matteroverdrive.world.dimensions.alien.WorldProviderAlien;
import matteroverdrive.world.dimensions.space.BiomeSpace;
import matteroverdrive.world.dimensions.space.WorldProviderSpace;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MODimensionHandler {
    public DimensionType ALIEN_TYPE;
    public DimensionType SPACE_TYPE;
    public BiomeAlien ALIEN_BIOME;
    public BiomeSpace SPACE_BIOME;

    public void init() {
        int alienID = DimensionManager.getNextFreeDimId();
        ALIEN_TYPE = DimensionType.register("alien", "_mo_alien", alienID, WorldProviderAlien.class, false);
        DimensionManager.registerDimension(alienID, ALIEN_TYPE);
        int spaceID = DimensionManager.getNextFreeDimId();
        SPACE_TYPE = DimensionType.register("space", "_mo_space", spaceID, WorldProviderSpace.class, false);
        DimensionManager.registerDimension(spaceID, SPACE_TYPE);
        ALIEN_BIOME = new BiomeAlien(new Biome.BiomeProperties("Alien").setWaterColor(new Color(250, 90, 90).getColor()));
        ALIEN_BIOME.setRegistryName("alien");
        SPACE_BIOME = new BiomeSpace(new Biome.BiomeProperties("Space").setWaterColor(new Color(250, 90, 90).getColor()));
        SPACE_BIOME.setRegistryName("space");
    }

    @SubscribeEvent
    public void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().registerAll(
                ALIEN_BIOME, SPACE_BIOME
        );
    }

    @SubscribeEvent
    public void gravitySimulator(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (!living.world.isRemote && living.dimension == SPACE_TYPE.getId()) {
            if (!(living instanceof EntityPlayer) || !(((EntityPlayer) living).capabilities.isFlying)) {
                living.motionY += 0.0784000015258789;
                living.motionY -= 0.0784000015258789 / 4;
            }
            if (living instanceof EntityPlayer && living.posY <= -3)
                CustomTeleporter.teleportToDimension((EntityPlayer) living, 0, ((EntityPlayer) living).posX, 800, ((EntityPlayer) living).posZ);
        }
        living.world.getEntitiesWithinAABB(EntityVillagerMadScientist.class, TileEntity.INFINITE_EXTENT_AABB);
    }
}