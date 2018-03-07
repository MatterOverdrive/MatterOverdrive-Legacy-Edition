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

package matteroverdrive.client;

import com.astro.clib.api.render.ItemModelProvider;
import com.astro.clib.client.ClientUtil;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.android.IAndroidStatRenderRegistry;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.api.inventory.IBionicPart;
import matteroverdrive.api.renderer.IBionicPartRenderer;
import matteroverdrive.api.renderer.IBioticStatRenderer;
import matteroverdrive.blocks.BlockDecorativeColored;
import matteroverdrive.client.model.ModelTritaniumArmor;
import matteroverdrive.client.render.*;
import matteroverdrive.client.render.biostat.BioticStatRendererShield;
import matteroverdrive.client.render.biostat.BioticStatRendererTeleporter;
import matteroverdrive.client.render.entity.*;
import matteroverdrive.client.render.tileentity.*;
import matteroverdrive.client.render.weapons.*;
import matteroverdrive.client.render.weapons.layers.WeaponLayerAmmoRender;
import matteroverdrive.client.render.weapons.modules.ModuleHoloSightsRender;
import matteroverdrive.client.render.weapons.modules.ModuleSniperScopeRender;
import matteroverdrive.entity.*;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.monster.EntityMeleeRougeAndroidMob;
import matteroverdrive.entity.monster.EntityMutantScientist;
import matteroverdrive.entity.monster.EntityRangedRogueAndroidMob;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.entity.weapon.PlasmaBolt;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveBlocks;
import matteroverdrive.init.MatterOverdriveItems;
import matteroverdrive.init.OverdriveBioticStats;
import matteroverdrive.items.weapon.module.WeaponModuleColor;
import matteroverdrive.items.weapon.module.WeaponModuleHoloSights;
import matteroverdrive.items.weapon.module.WeaponModuleSniperScope;
import matteroverdrive.machines.fusionReactorController.TileEntityMachineFusionReactorController;
import matteroverdrive.machines.pattern_monitor.TileEntityMachinePatternMonitor;
import matteroverdrive.machines.pattern_storage.TileEntityMachinePatternStorage;
import matteroverdrive.machines.replicator.TileEntityMachineReplicator;
import matteroverdrive.tile.*;
import matteroverdrive.util.MOLog;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 4/17/2015.
 */
public class RenderHandler {
    public static final Function<ResourceLocation, TextureAtlasSprite> modelTextureBakeFunc = new Function<ResourceLocation, TextureAtlasSprite>() {
        @Nullable
        @Override
        public TextureAtlasSprite apply(@Nullable ResourceLocation input) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
        }
    };
    public static int stencilBuffer;
    //endregion
    //region Item Renderers
    private static ItemRendererPhaser rendererPhaser;
    private static ItemRendererPhaserRifle rendererPhaserRifle;
    private static ItemRendererOmniTool rendererOmniTool;
    private static ItemRenderPlasmaShotgun renderPlasmaShotgun;
    private static ItemRendererIonSniper rendererIonSniper;
    private final Random random = new Random();
    //endregion
    //region Weapon Layers
    private final WeaponLayerAmmoRender weaponLayerAmmoRender = new WeaponLayerAmmoRender();
    //endregion
    //region World
    public EntityRendererRougeAndroid rendererRougeAndroidHologram;
    //endregion
    //region Models
    public ModelTritaniumArmor modelTritaniumArmor;
    public ModelTritaniumArmor modelTritaniumArmorFeet;
    public ModelBiped modelMeleeRogueAndroidParts;
    public ModelBiped modelRangedRogueAndroidParts;
    public IBakedModel doubleHelixModel;
    private RenderMatterScannerInfoHandler matterScannerInfoHandler;
    private RenderParticlesHandler renderParticlesHandler;
    private RenderWeaponsBeam renderWeaponsBeam;
    private List<IWorldLastRenderer> customRenderers;
    private AndroidStatRenderRegistry statRenderRegistry;
    private RenderDialogSystem renderDialogSystem;
    private AndroidBionicPartRenderRegistry bionicPartRenderRegistry;
    private WeaponModuleModelRegistry weaponModuleModelRegistry;
    private PipeRenderManager pipeRenderManager;
    private DimensionalRiftsRender dimensionalRiftsRender;
    private WeaponRenderHandler weaponRenderHandler;
    //region Weapon Module Renderers
    private ModuleSniperScopeRender moduleSniperScopeRender;
    private ModuleHoloSightsRender moduleHoloSightsRender;
    //endregion
    //region Biostat Renderers
    private BioticStatRendererTeleporter rendererTeleporter;
    private BioticStatRendererShield biostatRendererShield;
    //endregion
    //region Tile Entity Renderers
    private TileEntityRendererReplicator tileEntityRendererReplicator;
    private TileEntityRendererPipe tileEntityRendererPipe;
    private TileEntityRendererMatterPipe tileEntityRendererMatterPipe;
    private TileEntityRendererNetworkPipe tileEntityRendererNetworkPipe;
    private TileEntityRendererPatterStorage tileEntityRendererPatterStorage;
    private TileEntityRendererWeaponStation tileEntityRendererWeaponStation;
    private TileEntityRendererPatternMonitor tileEntityRendererPatternMonitor;
    private TileEntityRendererGravitationalAnomaly tileEntityRendererGravitationalAnomaly;
    private TileEntityRendererGravitationalStabilizer tileEntityRendererGravitationalStabilizer;
    private TileEntityRendererFusionReactorController tileEntityRendererFusionReactorController;
    private TileEntityRendererAndroidStation tileEntityRendererAndroidStation;
    private TileEntityRendererChargingStation tileEntityRendererChargingStation;
    private TileEntityRendererHoloSign tileEntityRendererHoloSign;
    private TileEntityRendererPacketQueue tileEntityRendererPacketQueue;
    private TileEntityRendererInscriber tileEntityRendererInscriber;
    private TileEntityRendererContractMarket tileEntityRendererContractMarket;
    //endregion

    public RenderHandler() {
        customRenderers = new ArrayList<>();
        MinecraftForge.EVENT_BUS.register(this);
        weaponRenderHandler = new WeaponRenderHandler();
        moduleSniperScopeRender = new ModuleSniperScopeRender(weaponRenderHandler);
        moduleHoloSightsRender = new ModuleHoloSightsRender(weaponRenderHandler);
    }

    public void init(World world, TextureManager textureManager) {
        matterScannerInfoHandler = new RenderMatterScannerInfoHandler();
        renderParticlesHandler = new RenderParticlesHandler(world, textureManager);
        renderWeaponsBeam = new RenderWeaponsBeam();
        statRenderRegistry = new AndroidStatRenderRegistry();
        renderDialogSystem = new RenderDialogSystem();
        bionicPartRenderRegistry = new AndroidBionicPartRenderRegistry();
        weaponModuleModelRegistry = new WeaponModuleModelRegistry();
        pipeRenderManager = new PipeRenderManager();
        dimensionalRiftsRender = new DimensionalRiftsRender();


        addCustomRenderer(matterScannerInfoHandler);
        addCustomRenderer(renderParticlesHandler);
        addCustomRenderer(renderWeaponsBeam);
        addCustomRenderer(renderDialogSystem);
        addCustomRenderer(dimensionalRiftsRender);

        MinecraftForge.EVENT_BUS.register(pipeRenderManager);
        MinecraftForge.EVENT_BUS.register(weaponRenderHandler);
        if (Minecraft.getMinecraft().getFramebuffer().enableStencil()) {
            stencilBuffer = MinecraftForgeClient.reserveStencilBit();
        }
    }

    @SubscribeEvent
    public void modelLoadEvent(ModelRegistryEvent event) {
        for (Item item : MatterOverdriveItems.items) {
            if (item instanceof ItemModelProvider)
                ((ItemModelProvider) item).initItemModel();
        }
        for (Block block : MatterOverdriveBlocks.blocks) {
            if (block instanceof ItemModelProvider)
                ((ItemModelProvider) block).initItemModel();
            else
                ClientUtil.registerWithMapper(block);
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        for (IWorldLastRenderer renderer : customRenderers) {
            renderer.onRenderWorldLast(this, event);
        }
        for (IBioticStat stat : MatterOverdrive.STAT_REGISTRY.getStats()) {
            Collection<IBioticStatRenderer> statRendererCollection = statRenderRegistry.getRendererCollection(stat.getClass());
            if (statRendererCollection != null) {
                for (IBioticStatRenderer renderer : statRendererCollection) {
                    renderer.onWorldRender(stat, MOPlayerCapabilityProvider.GetAndroidCapability(Minecraft.getMinecraft().player).getUnlockedLevel(stat), event);
                }
            }
        }
    }

    //Called when the client ticks.
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        renderParticlesHandler.onClientTick(event);
    }

    public void createTileEntityRenderers(ConfigurationHandler configHandler) {
        tileEntityRendererReplicator = new TileEntityRendererReplicator();
        tileEntityRendererPipe = new TileEntityRendererPipe();
        tileEntityRendererMatterPipe = new TileEntityRendererMatterPipe();
        tileEntityRendererNetworkPipe = new TileEntityRendererNetworkPipe();
        tileEntityRendererPatterStorage = new TileEntityRendererPatterStorage();
        tileEntityRendererWeaponStation = new TileEntityRendererWeaponStation();
        tileEntityRendererPatternMonitor = new TileEntityRendererPatternMonitor();
        tileEntityRendererGravitationalAnomaly = new TileEntityRendererGravitationalAnomaly();
        tileEntityRendererGravitationalStabilizer = new TileEntityRendererGravitationalStabilizer();
        tileEntityRendererFusionReactorController = new TileEntityRendererFusionReactorController();
        tileEntityRendererAndroidStation = new TileEntityRendererAndroidStation();
        tileEntityRendererChargingStation = new TileEntityRendererChargingStation();
        tileEntityRendererHoloSign = new TileEntityRendererHoloSign();
        tileEntityRendererPacketQueue = new TileEntityRendererPacketQueue();
        tileEntityRendererInscriber = new TileEntityRendererInscriber();
        tileEntityRendererContractMarket = new TileEntityRendererContractMarket();

        configHandler.subscribe(tileEntityRendererAndroidStation);
        configHandler.subscribe(tileEntityRendererWeaponStation);
    }

    @SubscribeEvent
    public void onPlayerRenderPost(RenderPlayerEvent.Post event) {
        //GL11.glEnable(GL11.GL_LIGHTING);
        //GL11.glColor3f(1, 1, 1);

        AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntity());
        if (androidPlayer != null && androidPlayer.isAndroid() && !event.getEntity().isInvisible()) {
            for (int i = 0; i < 5; i++) {
                ItemStack part = androidPlayer.getStackInSlot(i);
                if (part != null && part.getItem() instanceof IBionicPart) {
                    IBionicPartRenderer renderer = bionicPartRenderRegistry.getRenderer(((IBionicPart) part.getItem()).getClass());
                    if (renderer != null) {
                        try {
                            GlStateManager.pushMatrix();
                            GlStateManager.enableBlend();
                            renderer.renderPart(part, androidPlayer, event.getRenderer(), event.getPartialRenderTick());
                            GlStateManager.popMatrix();
                        } catch (Exception e) {
                            MOLog.log(Level.ERROR, e, "An Error occurred while rendering bionic part");
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRenderPre(RenderPlayerEvent.Pre event) {
        //GL11.glEnable(GL11.GL_LIGHTING);
        //GL11.glColor3f(1, 1, 1);

        AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(event.getEntity());
        if (androidPlayer != null && androidPlayer.isAndroid() && !event.getEntity().isInvisible()) {
            for (int i = 0; i < 5; i++) {
                ItemStack part = androidPlayer.getStackInSlot(i);
                if (part != null && part.getItem() instanceof IBionicPart) {
                    IBionicPartRenderer renderer = bionicPartRenderRegistry.getRenderer(((IBionicPart) part.getItem()).getClass());
                    if (renderer != null) {
                        renderer.affectPlayerRenderer(part, androidPlayer, event.getRenderer(), event.getPartialRenderTick());
                    }
                }
            }
        }
    }

    public void registerWeaponModuleRenders() {
        weaponRenderHandler.addModuleRender(WeaponModuleSniperScope.class, moduleSniperScopeRender);
        weaponRenderHandler.addModuleRender(WeaponModuleHoloSights.class, moduleHoloSightsRender);
    }

    public void registerWeaponLayers() {
        weaponRenderHandler.addWeaponLayer(weaponLayerAmmoRender);
    }

    public void registerTileEntitySpecialRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineReplicator.class, tileEntityRendererReplicator);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePatternStorage.class, tileEntityRendererPatterStorage);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWeaponStation.class, tileEntityRendererWeaponStation);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePatternMonitor.class, tileEntityRendererPatternMonitor);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGravitationalAnomaly.class, tileEntityRendererGravitationalAnomaly);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineGravitationalStabilizer.class, tileEntityRendererGravitationalStabilizer);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineFusionReactorController.class, tileEntityRendererFusionReactorController);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAndroidStation.class, tileEntityRendererAndroidStation);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineChargingStation.class, tileEntityRendererChargingStation);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHoloSign.class, tileEntityRendererHoloSign);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachinePacketQueue.class, tileEntityRendererPacketQueue);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInscriber.class, tileEntityRendererInscriber);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMachineContractMarket.class, tileEntityRendererContractMarket);
    }

    public void registerBlockColors() {
        Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, p_186720_2_, pos, tintIndex) -> {
            EnumDyeColor color = state.getValue(BlockDecorativeColored.COLOR);
            return ItemDye.DYE_COLORS[MathHelper.clamp(color.getMetadata(), 0, ItemDye.DYE_COLORS.length - 1)];
        }, MatterOverdrive.BLOCKS.decorative_tritanium_plate_colored);
    }

    public void createItemRenderers() {
        rendererPhaser = new ItemRendererPhaser();
        rendererPhaserRifle = new ItemRendererPhaserRifle();
        rendererOmniTool = new ItemRendererOmniTool();
        renderPlasmaShotgun = new ItemRenderPlasmaShotgun();
        rendererIonSniper = new ItemRendererIonSniper();
    }

    public void activateItemRenderers() {
        rendererPhaser.init();
        rendererPhaserRifle.init();
        rendererOmniTool.init();
        renderPlasmaShotgun.init();
        rendererIonSniper.init();
    }

    public void bakeItemModels() {
        weaponRenderHandler.onModelBake(Minecraft.getMinecraft().getTextureMapBlocks(), this);
        rendererPhaser.bakeModel();
        rendererPhaserRifle.bakeModel();
        rendererOmniTool.bakeModel();
        rendererIonSniper.bakeModel();
        renderPlasmaShotgun.bakeModel();
    }

    public void registerModelTextures(TextureMap textureMap, OBJModel model) {
        model.getTextures().forEach(textureMap::registerSprite);
    }

    public OBJModel getObjModel(ResourceLocation location, ImmutableMap<String, String> customOptions) {
        try {
            OBJModel model = (OBJModel) OBJLoader.INSTANCE.loadModel(location);
            model = (OBJModel) model.process(customOptions);
            return model;
        } catch (Exception e) {
            MOLog.log(Level.ERROR, e, "There was a problem while baking %s model", location.getResourcePath());
        }
        return null;
    }

    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        event.getModelRegistry().putObject(new ModelResourceLocation(MatterOverdrive.ITEMS.phaser.getRegistryName(), "inventory"), rendererPhaser);
        event.getModelRegistry().putObject(new ModelResourceLocation(MatterOverdrive.ITEMS.phaserRifle.getRegistryName(), "inventory"), rendererPhaserRifle);
        event.getModelRegistry().putObject(new ModelResourceLocation(MatterOverdrive.ITEMS.omniTool.getRegistryName(), "inventory"), rendererOmniTool);
        event.getModelRegistry().putObject(new ModelResourceLocation(MatterOverdrive.ITEMS.ionSniper.getRegistryName(), "inventory"), rendererIonSniper);
        event.getModelRegistry().putObject(new ModelResourceLocation(MatterOverdrive.ITEMS.plasmaShotgun.getRegistryName(), "inventory"), renderPlasmaShotgun);

        activateItemRenderers();
        bakeItemModels();
    }

    @SubscribeEvent
    public void onTextureStich(TextureStitchEvent.Pre event) {
        if (event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
            weaponRenderHandler.onTextureStich(Minecraft.getMinecraft().getTextureMapBlocks(), this);
        }
    }

    public void registerItemColors() {
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex == 1 ? Reference.COLOR_HOLO_RED.getColor() : -1, MatterOverdrive.ITEMS.energyPack);
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex == 1 ? Reference.COLOR_MATTER.getColor() : -1, MatterOverdrive.ITEMS.battery);
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex == 1 ? Reference.COLOR_YELLOW_STRIPES.getColor() : -1, MatterOverdrive.ITEMS.hc_battery);
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex == 1 ? Reference.COLOR_HOLO_RED.getColor() : -1, MatterOverdrive.ITEMS.creative_battery);
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex == 1 ? Reference.COLOR_YELLOW_STRIPES.getColor() : -1, MatterOverdrive.ITEMS.networkFlashDrive);
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> tintIndex == 1 ? Reference.COLOR_HOLO_GREEN.getColor() : -1, MatterOverdrive.ITEMS.transportFlashDrive);
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            switch (tintIndex) {
                case 1:
                    return Reference.COLOR_YELLOW_STRIPES.getColor();
                case 2:
                case 3:
                    return Reference.COLOR_MATTER.getColor();
                default:
                    return -1;
            }
        }, MatterOverdrive.ITEMS.matterContainer);
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            if (tintIndex == 1 && !stack.isEmpty() && stack.getItem() != null) {
                return WeaponModuleColor.colors[stack.getItemDamage()].getColor();
            } else {
                return 16777215;
            }
        }, MatterOverdrive.ITEMS.weapon_module_color);
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            if (tintIndex == 0 && !stack.isEmpty() && stack.getItem() != null) {
                return ItemDye.DYE_COLORS[MathHelper.clamp(stack.getItemDamage(), 0, ItemDye.DYE_COLORS.length - 1)];
            } else {
                return -1;
            }
        }, Item.getItemFromBlock(MatterOverdrive.BLOCKS.decorative_tritanium_plate_colored));
        FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler((stack, tintIndex) -> {
            if (stack.getItemDamage() == 0) return 0xd00000;
            else if (stack.getItemDamage() == 1) return 0x019fea;
            else if (stack.getItemDamage() == 2) return 0xffe400;
            return 0xffffff;
        }, MatterOverdrive.ITEMS.androidPill);
    }

    public void createEntityRenderers(RenderManager renderManager) {
        rendererRougeAndroidHologram = new EntityRendererRougeAndroid(renderManager, true);
    }

    public void registerEntityRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityMeleeRougeAndroidMob.class, renderManager -> new EntityRendererRougeAndroid(renderManager, false));
        RenderingRegistry.registerEntityRenderingHandler(EntityVillagerMadScientist.class, EntityRendererMadScientist::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedPig.class, EntityRendererFailedPig::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedCow.class, EntityRendererFailedCow::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedChicken.class, EntityRendererFailedChicken::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFailedSheep.class, EntityRendererFailedSheep::new);
        RenderingRegistry.registerEntityRenderingHandler(PlasmaBolt.class, EntityRendererPhaserFire::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityRangedRogueAndroidMob.class, EntityRendererRangedRougeAndroid::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMutantScientist.class, EntityRendererMutantScientist::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityDrone.class, EntityRendererDrone::new);
    }

    public void createBioticStatRenderers() {
        rendererTeleporter = new BioticStatRendererTeleporter();
        biostatRendererShield = new BioticStatRendererShield();
    }

    public void registerBioticStatRenderers() {
        statRenderRegistry.registerRenderer(OverdriveBioticStats.shield.getClass(), biostatRendererShield);
        statRenderRegistry.registerRenderer(OverdriveBioticStats.teleport.getClass(), rendererTeleporter);
    }

    public void registerBionicPartRenderers() {

    }

    public void createModels() {
        modelTritaniumArmor = new ModelTritaniumArmor(0);
        modelTritaniumArmorFeet = new ModelTritaniumArmor(0.5f);
        modelMeleeRogueAndroidParts = new ModelBiped(0);
        modelRangedRogueAndroidParts = new ModelBiped(0, 0, 96, 64);
        try {
            IModel model = OBJLoader.INSTANCE.loadModel(new ResourceLocation(Reference.PATH_MODEL + "gui/double_helix.obj"));
            doubleHelixModel = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, new Function<ResourceLocation, TextureAtlasSprite>() {
                @Nullable
                @Override
                public TextureAtlasSprite apply(@Nullable ResourceLocation input) {
                    return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(input);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RenderParticlesHandler getRenderParticlesHandler() {
        return renderParticlesHandler;
    }

    public IAndroidStatRenderRegistry getStatRenderRegistry() {
        return statRenderRegistry;
    }

    public ItemRendererOmniTool getRendererOmniTool() {
        return rendererOmniTool;
    }

    public AndroidBionicPartRenderRegistry getBionicPartRenderRegistry() {
        return bionicPartRenderRegistry;
    }

    public WeaponModuleModelRegistry getWeaponModuleModelRegistry() {
        return weaponModuleModelRegistry;
    }

    public Random getRandom() {
        return random;
    }

    public void addCustomRenderer(IWorldLastRenderer renderer) {
        customRenderers.add(renderer);
    }

    public WeaponRenderHandler getWeaponRenderHandler() {
        return weaponRenderHandler;
    }
}
