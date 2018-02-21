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

import com.astro.clib.api.OreDictItem;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, guiFactory = Reference.GUI_FACTORY_CLASS, dependencies = Reference.DEPENDENCIES)
public class MatterOverdrive {
    public static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(2);
    //	Content
    public static final MatterOverdriveItems ITEMS = new MatterOverdriveItems();
    public static final MatterOverdriveBlocks BLOCKS = new MatterOverdriveBlocks();
    public static final OverdriveTab TAB_OVERDRIVE = new OverdriveTab("tabMO", () -> new ItemStack(ITEMS.matter_scanner));
    public static final OverdriveTab TAB_OVERDRIVE_MODULES = new OverdriveTab("tabMO_modules", () -> new ItemStack(ITEMS.weapon_module_color));
    public static final OverdriveTab TAB_OVERDRIVE_UPGRADES = new OverdriveTab("tabMO_upgrades", () -> new ItemStack(ITEMS.item_upgrade));
    public static final OverdriveTab TAB_OVERDRIVE_FOOD = new OverdriveTab("tabMO_food", () -> new ItemStack(ITEMS.earl_gray_tea));
    public static final OverdriveTab TAB_OVERDRIVE_SHIPS = new OverdriveTab("tabMO_ships", () -> new ItemStack(ITEMS.colonizerShip));
    public static final OverdriveTab TAB_OVERDRIVE_BUILDINGS = new OverdriveTab("tabMO_buildings", () -> new ItemStack(ITEMS.buildingBase));
    public static final OverdriveTab TAB_OVERDRIVE_DECORATIVE = new OverdriveTab("tabMO_decorative", () -> new ItemStack(BLOCKS.decorative_stripes));
    public static final OverdriveTab TAB_OVERDRIVE_ANDROID_PARTS = new OverdriveTab("tabMO_androidParts", () -> new ItemStack(ITEMS.androidParts));
    @Instance(Reference.MOD_ID)
    public static MatterOverdrive INSTANCE;
    @SidedProxy(clientSide = "matteroverdrive.proxy.ClientProxy", serverSide = "matteroverdrive.proxy.CommonProxy")
    public static CommonProxy PROXY;
    public static TickHandler TICK_HANDLER;
    public static PlayerEventHandler PLAYER_EVENT_HANDLER;
    public static ConfigurationHandler CONFIG_HANDLER;
    public static GuiHandler GUI_HANDLER;
    public static PacketPipeline NETWORK;
    public static MatterOverdriveWorld MO_WORLD;
    public static EntityHandler ENTITY_HANDLER;
    public static MatterRegistry MATTER_REGISTRY;
    public static AndroidStatRegistry STAT_REGISTRY;
    public static DialogRegistry DIALOG_REGISTRY;
    public static MatterRegistrationHandler MATTER_REGISTRATION_HANDLER;
    public static WeaponFactory WEAPON_FACTORY;
    public static AndroidPartsFactory ANDROID_PARTS_FACTORY;
    public static Quests QUESTS;
    public static QuestFactory QUEST_FACTORY;
    public static DialogFactory DIALOG_FACTORY;
    public static BlockHandler BLOCK_HANDLER;
    public static QuestAssembler QUEST_ASSEMBLER;
    public static DialogAssembler DIALOG_ASSEMBLER;
    public static MatterNetworkHandler MATTER_NETWORK_HANDLER;
    public static FluidNetworkHandler FLUID_NETWORK_HANDLER;

    static {
        FluidRegistry.enableUniversalBucket();
        CLibTech.enable();
    }

    public MatterOverdrive() {
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        AndroidPlayer.register();
        OverdriveExtendedProperties.register();
        MATTER_REGISTRY = new MatterRegistry();
        STAT_REGISTRY = new AndroidStatRegistry();
        DIALOG_REGISTRY = new DialogRegistry();
        GUI_HANDLER = new GuiHandler();
        NETWORK = new PacketPipeline();
        ENTITY_HANDLER = new EntityHandler();
        CONFIG_HANDLER = new ConfigurationHandler(event.getModConfigurationDirectory());
        PLAYER_EVENT_HANDLER = new PlayerEventHandler(CONFIG_HANDLER);
        MATTER_REGISTRATION_HANDLER = new MatterRegistrationHandler();
        WEAPON_FACTORY = new WeaponFactory();
        ANDROID_PARTS_FACTORY = new AndroidPartsFactory();
        QUESTS = new Quests();
        QUEST_FACTORY = new QuestFactory();
        DIALOG_FACTORY = new DialogFactory(DIALOG_REGISTRY);
        BLOCK_HANDLER = new BlockHandler();
        QUEST_ASSEMBLER = new QuestAssembler();
        DIALOG_ASSEMBLER = new DialogAssembler();
        MATTER_NETWORK_HANDLER = new MatterNetworkHandler();
        FLUID_NETWORK_HANDLER = new FluidNetworkHandler();


        ITEMS.init();
        OverdriveFluids.init(event);
        BLOCKS.init();
        OverdriveBioticStats.init();
        MatterOverdriveDialogs.init(CONFIG_HANDLER, DIALOG_REGISTRY);
        MatterOverdriveQuests.init();
        MatterOverdriveQuests.register(QUESTS);
        MatterOverdriveSounds.register();
        EntityVillagerMadScientist.registerDialogMessages(DIALOG_REGISTRY, event.getSide());
        MatterOverdriveCapabilities.init();

        MinecraftForge.EVENT_BUS.register(MATTER_REGISTRATION_HANDLER);
        MinecraftForge.EVENT_BUS.register(CONFIG_HANDLER);
        TICK_HANDLER = new TickHandler(CONFIG_HANDLER, PLAYER_EVENT_HANDLER);
        MinecraftForge.EVENT_BUS.register(TICK_HANDLER);
        MinecraftForge.EVENT_BUS.register(PLAYER_EVENT_HANDLER);
        MinecraftForge.EVENT_BUS.register(BLOCK_HANDLER);
        MO_WORLD = new MatterOverdriveWorld(CONFIG_HANDLER);
        MatterOverdriveEntities.init(event, CONFIG_HANDLER);
        MatterOverdriveEnchantments.init(event, CONFIG_HANDLER);
        MO_WORLD.register();
        MatterNetworkRegistry.register();
        NETWORK.registerPackets();
        OverdriveBioticStats.registerAll(CONFIG_HANDLER, STAT_REGISTRY);
        MATTER_REGISTRY.preInit(event, CONFIG_HANDLER);
        MinecraftForge.EVENT_BUS.register(MATTER_NETWORK_HANDLER);
        MinecraftForge.EVENT_BUS.register(FLUID_NETWORK_HANDLER);

        PROXY.preInit(event);

        MatterOverdriveCompat.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        GUI_HANDLER.register(event.getSide());
        NetworkRegistry.INSTANCE.registerGuiHandler(this, GUI_HANDLER);
        MinecraftForge.EVENT_BUS.register(ENTITY_HANDLER);
        CONFIG_HANDLER.init();
        MatterOverdriveCompat.init(event);

        PROXY.init(event);

        MatterOverdriveItems.items.stream().filter(item -> item instanceof OreDictItem).forEach(item -> ((OreDictItem) item).registerOreDict());
        MatterOverdriveBlocks.blocks.stream().filter(block -> block instanceof OreDictItem).forEach(block -> ((OreDictItem) block).registerOreDict());
        MatterOverdriveRecipes.registerMachineRecipes(event);

        WEAPON_FACTORY.initModules();
        WEAPON_FACTORY.initWeapons();
        ANDROID_PARTS_FACTORY.initParts();

        AndroidPlayer.loadConfigs(CONFIG_HANDLER);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit(event);
        MatterOverdriveCompat.postInit(event);
        MatterOverdriveEntities.register(event);
        ITEMS.addToDungons();

        QUEST_ASSEMBLER.loadQuests(QUESTS);
        QUEST_ASSEMBLER.loadCustomQuests(QUESTS);
        DIALOG_ASSEMBLER.loadDialogs(DIALOG_REGISTRY);
        DIALOG_ASSEMBLER.loadCustomDialogs(DIALOG_REGISTRY);

        MatterOverdriveMatter.registerBlacklistFromConfig(CONFIG_HANDLER);
        MatterOverdriveMatter.registerBasic(CONFIG_HANDLER);

        CONFIG_HANDLER.postInit();
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
        //MATTER_REGISTRATION_HANDLER.serverStart(event);
        TICK_HANDLER.onServerStart(event);
    }

    @EventHandler
    public void imcCallback(FMLInterModComms.IMCEvent event) {
        MOIMCHandler.imcCallback(event);
    }
}