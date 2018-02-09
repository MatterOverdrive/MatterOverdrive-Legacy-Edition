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

package matteroverdrive.proxy;

import com.astro.clib.proxy.impl.ProxyClass;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.client.RenderHandler;
import matteroverdrive.client.render.HoloIcons;
import matteroverdrive.client.resources.data.WeaponMetadataSection;
import matteroverdrive.client.resources.data.WeaponMetadataSectionSerializer;
import matteroverdrive.compat.MatterOverdriveCompat;
import matteroverdrive.gui.GuiAndroidHud;
import matteroverdrive.gui.GuiQuestHud;
import matteroverdrive.handler.GoogleAnalyticsClient;
import matteroverdrive.handler.KeyHandler;
import matteroverdrive.handler.MouseHandler;
import matteroverdrive.handler.TooltipHandler;
import matteroverdrive.handler.weapon.ClientWeaponHandler;
import matteroverdrive.handler.weapon.CommonWeaponHandler;
import matteroverdrive.init.MatterOverdriveGuides;
import matteroverdrive.init.MatterOverdriveIcons;
import matteroverdrive.starmap.GalaxyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@ProxyClass(modid = Reference.MOD_ID,side=Side.CLIENT)
public class ClientProxy extends CommonProxy {
    public static RenderHandler renderHandler;
    public static KeyHandler keyHandler;
    public static MouseHandler mouseHandler;
    public static GuiAndroidHud androidHud;
    public static HoloIcons holoIcons;
    public static GuiQuestHud questHud;
    public static FontRenderer moFontRender;
    private static ClientProxy clientProxy;
    private ClientWeaponHandler weaponHandler;

    public ClientProxy() {
        weaponHandler = new ClientWeaponHandler();
        googleAnalyticsCommon = new GoogleAnalyticsClient();

    }

    public static ClientProxy instance() {
        if (clientProxy == null) {
            clientProxy = (ClientProxy) MatterOverdrive.PROXY;
        }
        return clientProxy;
    }

    private void registerSubscribtions() {
        MinecraftForge.EVENT_BUS.register(keyHandler);
        MinecraftForge.EVENT_BUS.register(mouseHandler);
        MinecraftForge.EVENT_BUS.register(GalaxyClient.getInstance());
        MinecraftForge.EVENT_BUS.register(new MatterOverdriveIcons());
        MinecraftForge.EVENT_BUS.register(new TooltipHandler());
        MinecraftForge.EVENT_BUS.register(androidHud);
        MinecraftForge.EVENT_BUS.register(questHud);
        MinecraftForge.EVENT_BUS.register(weaponHandler);
        MinecraftForge.EVENT_BUS.register(holoIcons);
    }

    @Override
    public void registerCompatModules() {
        super.registerCompatModules();
        MatterOverdriveCompat.registerClientModules();
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        OBJLoader.INSTANCE.addDomain(Reference.MOD_ID);

        Minecraft.getMinecraft().getResourcePackRepository().rprMetadataSerializer.registerMetadataSectionType(new WeaponMetadataSectionSerializer(), WeaponMetadataSection.class);

        renderHandler = new RenderHandler();
        renderHandler.registerEntityRenderers();
        renderHandler.createItemRenderers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        renderHandler.init(Minecraft.getMinecraft().world, Minecraft.getMinecraft().getTextureManager());
        renderHandler.createEntityRenderers(Minecraft.getMinecraft().getRenderManager());

        androidHud = new GuiAndroidHud(Minecraft.getMinecraft());
        keyHandler = new KeyHandler();
        mouseHandler = new MouseHandler();
        holoIcons = new HoloIcons();
        weaponHandler = new ClientWeaponHandler();
        questHud = new GuiQuestHud();

        registerSubscribtions();

        //region Render Handler Functions
        //region Create
        //renderHandler.createBlockRenderers();
        renderHandler.createTileEntityRenderers(MatterOverdrive.configHandler);
        renderHandler.createBioticStatRenderers();
        renderHandler.createStarmapRenderers();
        renderHandler.createModels();
        //endregion
        //region Register
        renderHandler.registerWeaponModuleRenders();
        renderHandler.registerWeaponLayers();
        renderHandler.registerTileEntitySpecialRenderers();
        renderHandler.registerBlockColors();

        renderHandler.registerItemColors();
        renderHandler.registerBioticStatRenderers();
        renderHandler.registerBionicPartRenderers();
        renderHandler.registerStarmapRenderers();
        renderHandler.registerWeaponModuleRenders();
        //endregion
        //endregion

        MatterOverdrive.configHandler.subscribe(androidHud);

        weaponHandler.registerWeapon(MatterOverdrive.ITEMS.phaserRifle);
        weaponHandler.registerWeapon(MatterOverdrive.ITEMS.phaser);
        weaponHandler.registerWeapon(MatterOverdrive.ITEMS.omniTool);
        weaponHandler.registerWeapon(MatterOverdrive.ITEMS.plasmaShotgun);
        weaponHandler.registerWeapon(MatterOverdrive.ITEMS.ionSniper);

        MatterOverdriveGuides.registerGuideElements(event);
        moFontRender = new FontRenderer(Minecraft.getMinecraft().gameSettings, new ResourceLocation(Reference.MOD_ID, "textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(moFontRender);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MatterOverdriveGuides.registerGuides(event);
    }

    public ClientWeaponHandler getClientWeaponHandler() {
        return weaponHandler;
    }

    @Override
    public CommonWeaponHandler getWeaponHandler() {
        return weaponHandler;
    }

    @Override
    public boolean hasTranslation(String key) {
        return I18n.hasKey(key);
    }

    @Override
    public String translateToLocal(String key, Object... params) {
        return I18n.format(key, params);
    }
}