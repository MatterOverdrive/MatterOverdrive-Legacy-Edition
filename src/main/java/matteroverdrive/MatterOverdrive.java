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

package matteroverdrive;

import com.astro.clib.proxy.impl.ProxyHolder;
import com.astro.clib.tech.CLibTech;
import matteroverdrive.commands.AndoidCommands;
import matteroverdrive.commands.MatterRegistryCommands;
import matteroverdrive.commands.QuestCommands;
import matteroverdrive.commands.WorldGenCommands;
import matteroverdrive.compat.MatterOverdriveCompat;
import matteroverdrive.entity.EntityVillagerMadScientist;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.OverdriveExtendedProperties;
import matteroverdrive.handler.*;
import matteroverdrive.handler.dialog.DialogAssembler;
import matteroverdrive.handler.dialog.DialogRegistry;
import matteroverdrive.handler.matter_network.FluidNetworkHandler;
import matteroverdrive.handler.matter_network.MatterNetworkHandler;
import matteroverdrive.handler.quest.QuestAssembler;
import matteroverdrive.handler.quest.Quests;
import matteroverdrive.imc.MOIMCHandler;
import matteroverdrive.init.*;
import matteroverdrive.matter_network.MatterNetworkRegistry;
import matteroverdrive.network.PacketPipeline;
import matteroverdrive.proxy.CommonProxy;
import matteroverdrive.util.AndroidPartsFactory;
import matteroverdrive.util.DialogFactory;
import matteroverdrive.util.QuestFactory;
import matteroverdrive.util.WeaponFactory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS, dependencies = Reference.DEPEDNENCIES)
public class MatterOverdrive {
    public static final OverdriveTab TAB_OVERDRIVE = new OverdriveTab("tabMO");
    public static final OverdriveTab TAB_OVERDRIVE_MODULES = new OverdriveTab("tabMO_modules");
    public static final OverdriveTab TAB_OVERDRIVE_UPGRADES = new OverdriveTab("tabMO_upgrades");
    public static final OverdriveTab TAB_OVERDRIVE_FOOD = new OverdriveTab("tabMO_food");
    public static final OverdriveTab TAB_OVERDRIVE_SHIPS = new OverdriveTab("tabMO_ships");
    public static final OverdriveTab TAB_OVERDRIVE_BUILDINGS = new OverdriveTab("tabMO_buildings");
    public static final OverdriveTab TAB_OVERDRIVE_DECORATIVE = new OverdriveTab("tabMO_decorative");
    public static final OverdriveTab TAB_OVERDRIVE_ANDROID_PARTS = new OverdriveTab("tabMO_androidParts");
    public static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(2);
    //	Content
    public static final MatterOverdriveItems ITEMS = new MatterOverdriveItems();
    public static final MatterOverdriveBlocks BLOCKS = new MatterOverdriveBlocks();

    @Mod.Instance(Reference.MOD_ID)
    public static MatterOverdrive INSTANCE;

    @ProxyHolder
    public static CommonProxy PROXY;
    public static TickHandler tickHandler;
    public static PlayerEventHandler playerEventHandler;
    public static ConfigurationHandler configHandler;
    public static GuiHandler guiHandler;
    public static PacketPipeline packetPipeline;
    public static MatterOverdriveWorld moWorld;
    public static EntityHandler entityHandler;
    public static MatterRegistry matterRegistry;
    public static AndroidStatRegistry statRegistry;
    public static DialogRegistry dialogRegistry;
    public static MatterRegistrationHandler matterRegistrationHandler;
    public static WeaponFactory weaponFactory;
    public static AndroidPartsFactory androidPartsFactory;
    public static Quests quests;
    public static QuestFactory questFactory;
    public static DialogFactory dialogFactory;
    public static BlockHandler blockHandler;
    public static QuestAssembler questAssembler;
    public static DialogAssembler dialogAssembler;
    public static MatterNetworkHandler matterNetworkHandler;
    public static FluidNetworkHandler fluidNetworkHandler;

    static {
        FluidRegistry.enableUniversalBucket();
        CLibTech.enable();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        AndroidPlayer.register();
        OverdriveExtendedProperties.register();
        matterRegistry = new MatterRegistry();
        statRegistry = new AndroidStatRegistry();
        dialogRegistry = new DialogRegistry();
        guiHandler = new GuiHandler();
        packetPipeline = new PacketPipeline();
        entityHandler = new EntityHandler();
        configHandler = new ConfigurationHandler(event.getModConfigurationDirectory());
        playerEventHandler = new PlayerEventHandler(configHandler);
        matterRegistrationHandler = new MatterRegistrationHandler();
        weaponFactory = new WeaponFactory();
        androidPartsFactory = new AndroidPartsFactory();
        quests = new Quests();
        questFactory = new QuestFactory();
        dialogFactory = new DialogFactory(dialogRegistry);
        blockHandler = new BlockHandler();
        questAssembler = new QuestAssembler();
        dialogAssembler = new DialogAssembler();
        matterNetworkHandler = new MatterNetworkHandler();
        fluidNetworkHandler = new FluidNetworkHandler();


        ITEMS.init();
        OverdriveFluids.init(event);
        BLOCKS.init();
        OverdriveBioticStats.init();
        MatterOverdriveDialogs.init(configHandler, dialogRegistry);
        MatterOverdriveQuests.init();
        MatterOverdriveQuests.register(quests);
        MatterOverdriveSounds.register();
        EntityVillagerMadScientist.registerDialogMessages(dialogRegistry, event.getSide());
        MatterOverdriveCapabilities.init();

        MinecraftForge.EVENT_BUS.register(matterRegistrationHandler);
        MinecraftForge.EVENT_BUS.register(configHandler);
        tickHandler = new TickHandler(configHandler, playerEventHandler);
        MinecraftForge.EVENT_BUS.register(tickHandler);
        MinecraftForge.EVENT_BUS.register(playerEventHandler);
        MinecraftForge.EVENT_BUS.register(playerEventHandler);
        MinecraftForge.EVENT_BUS.register(blockHandler);
        moWorld = new MatterOverdriveWorld(configHandler);
        MatterOverdriveEntities.init(event, configHandler);
        MatterOverdriveEnchantments.init(event, configHandler);
        moWorld.register();
        MatterNetworkRegistry.register();
        packetPipeline.registerPackets();
        OverdriveBioticStats.registerAll(configHandler, statRegistry);
        matterRegistry.preInit(event, configHandler);
        MinecraftForge.EVENT_BUS.register(matterNetworkHandler);
        MinecraftForge.EVENT_BUS.register(fluidNetworkHandler);
        updateTabs();

        PROXY.preInit(event);

        MatterOverdriveCompat.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        guiHandler.register(event.getSide());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
        MinecraftForge.EVENT_BUS.register(entityHandler);
        configHandler.init();
        MatterOverdriveCompat.init(event);

        PROXY.init(event);

        MatterOverdriveRecipes.registerBlockRecipes(event);
        MatterOverdriveRecipes.registerItemRecipes(event);
        MatterOverdriveRecipes.registerInscriberRecipes(event);

        weaponFactory.initModules();
        weaponFactory.initWeapons();
        androidPartsFactory.initParts();

        AndroidPlayer.loadConfigs(configHandler);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit(event);
        MatterOverdriveCompat.postInit(event);
        MatterOverdriveEntities.register(event);
        ITEMS.addToDungons();

        questAssembler.loadQuests(quests);
        questAssembler.loadCustomQuests(quests);
        dialogAssembler.loadDialogs(dialogRegistry);
        dialogAssembler.loadCustomDialogs(dialogRegistry);

        MatterOverdriveMatter.registerBlacklistFromConfig(configHandler);
        MatterOverdriveMatter.registerBasic(configHandler);

        configHandler.postInit();
    }


    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new AndoidCommands());
        event.registerServerCommand(new MatterRegistryCommands());
        event.registerServerCommand(new QuestCommands());

        event.registerServerCommand(new WorldGenCommands());

        PROXY.getGoogleAnalytics().load();
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        PROXY.getGoogleAnalytics().unload();
    }

    @EventHandler
    public void serverStart(FMLServerStartedEvent event) {
        //matterRegistrationHandler.serverStart(event);
        tickHandler.onServerStart(event);
    }

    @EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
        MOIMCHandler.imcCallback(event);
    }

    private void updateTabs() {
        TAB_OVERDRIVE.itemstack = new ItemStack(ITEMS.matter_scanner);
        TAB_OVERDRIVE_MODULES.itemstack = new ItemStack(ITEMS.weapon_module_color);
        TAB_OVERDRIVE_UPGRADES.itemstack = new ItemStack(ITEMS.item_upgrade);
        TAB_OVERDRIVE_FOOD.itemstack = new ItemStack(ITEMS.earl_gray_tea);
        TAB_OVERDRIVE_SHIPS.itemstack = new ItemStack(ITEMS.colonizerShip);
        TAB_OVERDRIVE_BUILDINGS.itemstack = new ItemStack(ITEMS.buildingBase);
        TAB_OVERDRIVE_DECORATIVE.itemstack = new ItemStack(BLOCKS.decorative_stripes);
        TAB_OVERDRIVE_ANDROID_PARTS.itemstack = new ItemStack(ITEMS.androidParts);
    }
}