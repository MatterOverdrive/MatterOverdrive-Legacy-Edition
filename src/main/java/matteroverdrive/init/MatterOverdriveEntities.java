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

package matteroverdrive.init;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.entity.*;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import matteroverdrive.entity.monster.EntityRogueAndroid;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.handler.village.VillageCreatationMadScientist;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

/**
 * Created by Simeon on 5/26/2015.
 */
@Mod.EventBusSubscriber
public class MatterOverdriveEntities {
    public static final int ENTITY_STARTING_ID = 171;
    public static EntityRogueAndroid rogueandroid;

    public static VillagerRegistry.VillagerProfession MAD_SCIENTIST_PROFESSION;
    public static VillagerRegistry.VillagerCareer MAD_SCIENTIST_CAREER;
    public static boolean enableVillager = false;

    public static void init(FMLPreInitializationEvent event, ConfigurationHandler configurationHandler) {
        rogueandroid = new EntityRogueAndroid();
        configurationHandler.subscribe(rogueandroid);
    }

    public static void register(FMLPostInitializationEvent event) {
        MatterOverdrive.CONFIG_HANDLER.config.load();
        int id = 0;
        addEntity(EntityFailedPig.class, "failed_pig", 15771042, 0x33CC33, id++);
        addEntity(EntityFailedCow.class, "failed_cow", 4470310, 0x33CC33, id++);
        addEntity(EntityFailedChicken.class, "failed_chicken", 10592673, 0x33CC33, id++);
        addEntity(EntityFailedSheep.class, "failed_sheep", 15198183, 0x33CC33, id++);
        if (addEntity(EntityVillagerMadScientist.class, "mad_scientist", 0xFFFFFF, 0, id++)) {
            VillageCreatationMadScientist creatationMadScientist = new VillageCreatationMadScientist();
            VillagerRegistry.instance().registerVillageCreationHandler(creatationMadScientist);
        }
        addEntity(EntityMutantScientist.class, "mutant_scientist", 0xFFFFFF, 0x00FF00, id++);
        if (addEntity(EntityMeleeRougeAndroidMob.class, "rogue_android", 0xFFFFF, 0, id++))
            EntityRogueAndroid.addAsBiomeGen(EntityMeleeRougeAndroidMob.class);
        if (addEntity(EntityRangedRogueAndroidMob.class, "ranged_rogue_android", 0xFFFFF, 0, id++))
            EntityRogueAndroid.addAsBiomeGen(EntityRangedRogueAndroidMob.class);
        addEntity(EntityDrone.class, "drone", 0x3e5154, 0xbaa1c4, id++);
        MatterOverdrive.CONFIG_HANDLER.save();
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<VillagerRegistry.VillagerProfession> event) {
        MAD_SCIENTIST_PROFESSION = new VillagerRegistry.VillagerProfession("matteroverdrive:mad_scientist", Reference.PATH_ENTITIES + "mad_scientist.png", Reference.PATH_ENTITIES + "hulking_scinetist.png") {
            @Override
            public VillagerRegistry.VillagerCareer getCareer(int id) {
                return MAD_SCIENTIST_CAREER;
            }
        };
        MAD_SCIENTIST_CAREER = new VillagerRegistry.VillagerCareer(MAD_SCIENTIST_PROFESSION, "matteroverdrive.mad_scientist");
        event.getRegistry().register(MAD_SCIENTIST_PROFESSION);
    }

    public static boolean addEntity(Class<? extends Entity> enityClass, String name, int mainColor, int spotsColor, int id) {
        boolean enabled = MatterOverdrive.CONFIG_HANDLER.config.getBoolean("enable", String.format("%s.%s", ConfigurationHandler.CATEGORY_ENTITIES, name), true, "");
        if (enabled)
            EntityRegistry.registerModEntity(new ResourceLocation(Reference.MOD_ID, name), enityClass, name, id, MatterOverdrive.INSTANCE, 64, 1, true, mainColor, spotsColor);
        return enabled;
    }
}