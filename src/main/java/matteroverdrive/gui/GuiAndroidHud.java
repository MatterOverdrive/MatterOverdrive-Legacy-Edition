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

package matteroverdrive.gui;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.animation.AnimationSegmentText;
import matteroverdrive.animation.AnimationTextTyping;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.api.weapon.IWeapon;
import matteroverdrive.client.data.Color;
import matteroverdrive.client.render.HoloIcon;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.gui.android.*;
import matteroverdrive.gui.config.EnumConfigProperty;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.OverdriveBioticStats;
import matteroverdrive.proxy.ClientProxy;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOStringHelper;
import matteroverdrive.util.RenderUtils;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Simeon on 5/26/2015.
 */
@SideOnly(Side.CLIENT)
public class GuiAndroidHud extends Gui implements IConfigSubscriber {
    public static final ResourceLocation glitch_tex = new ResourceLocation(Reference.PATH_GUI + "glitch.png");
    public static final ResourceLocation spinner_tex = new ResourceLocation(Reference.PATH_ELEMENTS + "spinner.png");
    public static final ResourceLocation top_element_bg = new ResourceLocation(Reference.PATH_ELEMENTS + "android_bg_element.png");
    public static final ResourceLocation cloak_overlay = new ResourceLocation(Reference.PATH_ELEMENTS + "cloak_overlay.png");
    public static boolean showRadial = false;
    public static double radialDeltaX, radialDeltaY, radialAngle;
    public static float hudRotationYawSmooth;
    public static float hudRotationPitchSmooth;
    private static double radialAnimationTime;
    public final AndroidHudMinimap hudMinimap;
    public final AndroidHudStats hudStats;
    public final AndroidHudBionicStats bionicStats;
    private final Minecraft mc;
    private final Random random;
    private final List<IBioticStat> stats = new ArrayList<>();
    private final List<IAndroidHudElement> hudElements;
    public Color baseGuiColor;
    public float opacity;
    public float opacityBackground;
    public boolean hideVanillaHudElements;
    public boolean hudMovement;
    private AnimationTextTyping textTyping;
    private ShaderGroup hurtShader;
    private HoloIcon crosshairIcon;

    public GuiAndroidHud(Minecraft mc) {
        super();
        this.mc = mc;
        random = new Random();
        textTyping = new AnimationTextTyping(false, AndroidPlayer.TRANSFORM_TIME);
        String info;
        for (int i = 0; i < 5; i++) {
            info = MOStringHelper.translateToLocal("gui.android_hud.transforming.line." + i);
            textTyping.addSegmentSequential(new AnimationSegmentText(info, 0, 1).setLengthPerCharacter(2));
            textTyping.addSegmentSequential(new AnimationSegmentText(info, 0, 0).setLengthPerCharacter(2));
        }

        info = MOStringHelper.translateToLocal("gui.android_hud.transforming.line.final");
        textTyping.addSegmentSequential(new AnimationSegmentText(info, 0, 1).setLengthPerCharacter(2));
        textTyping.addSegmentSequential(new AnimationSegmentText(info, AndroidPlayer.TRANSFORM_TIME, 0));

        hudElements = new ArrayList<>();
        hudMinimap = new AndroidHudMinimap(AndroidHudPosition.BOTTOM_LEFT, "android_minimap");
        hudStats = new AndroidHudStats(AndroidHudPosition.TOP_LEFT, "android_stats");
        bionicStats = new AndroidHudBionicStats(AndroidHudPosition.TOP_RIGHT, "android_biotic_stats");
        hudElements.add(hudMinimap);
        hudElements.add(hudStats);
        hudElements.add(bionicStats);

        baseGuiColor = Reference.COLOR_HOLO.multiplyWithoutAlpha(0.5f);
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (showRadial) {
            Mouse.getDX();
            Mouse.getDY();
            mc.mouseHelper.deltaX = mc.mouseHelper.deltaY = 0;
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onRenderExperienceBar(RenderGameOverlayEvent event) {
        if (this.mc.player.isSpectator()) {
            return;
        }

        AndroidPlayer android = MOPlayerCapabilityProvider.GetAndroidCapability(mc.player);

        if ((mc.currentScreen instanceof GuiDialog || mc.currentScreen instanceof GuiStarMap) && !event.getType().equals(RenderGameOverlayEvent.ElementType.ALL) && event.isCancelable()) {
            event.setCanceled(true);
            return;
        }

        if ((android.isAndroid() && event.isCancelable())) {
            if (hideVanillaHudElements) {
                if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
                    event.setCanceled(true);
                    return;
                } else if (event.getType() == RenderGameOverlayEvent.ElementType.AIR && android.isUnlocked(OverdriveBioticStats.oxygen, 1) && OverdriveBioticStats.oxygen.isEnabled(android, 1)) {
                    event.setCanceled(true);
                    return;
                } else if (event.getType() == RenderGameOverlayEvent.ElementType.FOOD && android.isUnlocked(OverdriveBioticStats.zeroCalories, 1) && OverdriveBioticStats.zeroCalories.isEnabled(android, 1)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        if ((android.isAndroid() || (!mc.player.getHeldItem(EnumHand.MAIN_HAND).isEmpty() && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof IWeapon)) && event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            event.setCanceled(true);

            if (!showRadial) {
                renderCrosshair(event);
            }

            mc.getTextureManager().bindTexture(Gui.ICONS);
        } else if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            //glPushAttrib(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            GlStateManager.enableBlend();
            renderHud(event);

            if (android.isAndroid()) {
                if (showRadial) {
                    GuiAndroidHud.radialAnimationTime = Math.min(1, GuiAndroidHud.radialAnimationTime + event.getPartialTicks() * 0.2);
                } else {
                    GuiAndroidHud.radialAnimationTime = Math.max(0, GuiAndroidHud.radialAnimationTime - event.getPartialTicks() * 0.2);
                }

                if (GuiAndroidHud.radialAnimationTime > 0) {
                    renderRadialMenu(event);
                }
            }
        }
    }

    public void renderCrosshair(RenderGameOverlayEvent event) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        float scale = 6 + ClientProxy.instance().getClientWeaponHandler().getEquippedWeaponAccuracyPercent(Minecraft.getMinecraft().player) * 256;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);
        GlStateManager.enableAlpha();
        //RenderUtils.applyColorWithMultipy(Reference.COLOR_HOLO,0.5f);
        GlStateManager.color(1, 1, 1);
        crosshairIcon = ClientProxy.holoIcons.getIcon("crosshair");
        GlStateManager.translate(event.getResolution().getScaledWidth() / 2, event.getResolution().getScaledHeight() / 2, 0);
        GlStateManager.pushMatrix();
        ClientProxy.holoIcons.bindSheet();
        GlStateManager.rotate(90, 0, 0, 1);
        ClientProxy.holoIcons.renderIcon(crosshairIcon, -1, -scale);
        GlStateManager.rotate(90, 0, 0, 1);
        ClientProxy.holoIcons.renderIcon(crosshairIcon, -2, -scale);
        GlStateManager.rotate(90, 0, 0, 1);
        ClientProxy.holoIcons.renderIcon(crosshairIcon, -1.8, -scale + 1);
        GlStateManager.rotate(90, 0, 0, 1);
        ClientProxy.holoIcons.renderIcon(crosshairIcon, -1, -scale + 1);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public void renderRadialMenu(RenderGameOverlayEvent event) {
        if (this.mc.player.isSpectator()) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(event.getResolution().getScaledWidth() / 2, event.getResolution().getScaledHeight() / 2, 0);
        double scale = MOMathHelper.easeIn(GuiAndroidHud.radialAnimationTime, 0, 1, 1);
        GlStateManager.scale(scale, scale, scale);
        ClientProxy.holoIcons.bindSheet();
        AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(Minecraft.getMinecraft().player);

        stats.clear();
        for (IBioticStat stat : MatterOverdrive.statRegistry.getStats()) {
            if (stat.showOnWheel(androidPlayer, androidPlayer.getUnlockedLevel(stat)) && androidPlayer.isUnlocked(stat, 0)) {
                stats.add(stat);
            }
        }

        GlStateManager.color(1, 1, 1);
        GlStateManager.depthMask(false);
        //glDisable(GL_DEPTH_TEST);
        //GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_ONE, GL_ONE);
        GlStateManager.pushMatrix();
        GlStateManager.rotate((float) radialAngle, 0, 0, -1);
        RenderUtils.applyColorWithMultipy(baseGuiColor, 0.4f);
        ClientProxy.holoIcons.renderIcon("up_arrow_large", -9, -50);
        GlStateManager.popMatrix();

        int i = 0;
        for (IBioticStat stat : stats) {
            double angleSeg = (Math.PI * 2 / stats.size());
            double angle, x, y, radiusMin, radiusMax, angleAb, angleCircle;
            double radius = 80;
            angle = angleSeg * i;
            angle += Math.toRadians(180D);

            radiusMin = radius - 16;
            radiusMax = radius + 16;

            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            if (stat.equals(androidPlayer.getActiveStat())) {
                radiusMax = radius + 20;
                radiusMin = radius - 16;
                GlStateManager.color(0, 0, 0, 0.6f);
            } else {
                GlStateManager.color(0, 0, 0, 0.4f);
            }


            BufferBuilder wr = Tessellator.getInstance().getBuffer();
            wr.begin(7, DefaultVertexFormats.POSITION);
            for (int c = 0; c < 32; c++) {
                angleAb = ((angleSeg) / 32d);
                angleCircle = c * angleAb + angle - angleSeg / 2;
                wr.pos(Math.sin(angleCircle) * radiusMax, Math.cos(angleCircle) * radiusMax, -1).endVertex();
                wr.pos(Math.sin(angleCircle + angleAb) * radiusMax, Math.cos(angleCircle + angleAb) * radiusMax, -1).endVertex();
                wr.pos(Math.sin(angleCircle + angleAb) * radiusMin, Math.cos(angleCircle + angleAb) * radiusMin, -1).endVertex();
                wr.pos(Math.sin(angleCircle) * radiusMin, Math.cos(angleCircle) * radiusMin, -1).endVertex();
            }
            Tessellator.getInstance().draw();

            radiusMax = radius - 20;
            radiusMin = radius - 25;
            wr.begin(7, DefaultVertexFormats.POSITION);
            GlStateManager.color(0, 0, 0, 0.2f);
            for (int c = 0; c < 32; c++) {
                angleAb = ((Math.PI * 2) / 32d);
                angleCircle = c * angleAb;
                wr.pos(Math.sin(angleCircle) * radiusMax, Math.cos(angleCircle) * radiusMax, -1).endVertex();
                wr.pos(Math.sin(angleCircle + angleAb) * radiusMax, Math.cos(angleCircle + angleAb) * radiusMax, -1).endVertex();
                wr.pos(Math.sin(angleCircle + angleAb) * radiusMin, Math.cos(angleCircle + angleAb) * radiusMin, -1).endVertex();
                wr.pos(Math.sin(angleCircle) * radiusMin, Math.cos(angleCircle) * radiusMin, -1).endVertex();
            }
            Tessellator.getInstance().draw();
            GlStateManager.enableTexture2D();

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_ONE, GL_ONE);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            GlStateManager.depthMask(false);

            ClientProxy.holoIcons.bindSheet();
            if (androidPlayer.getActiveStat() != null) {
                if (stat.equals(androidPlayer.getActiveStat())) {
                    RenderUtils.applyColorWithMultipy(baseGuiColor, 1);
                    x = Math.sin(angle) * radius;
                    y = Math.cos(angle) * radius;
                    ClientProxy.holoIcons.renderIcon(stat.getIcon(0), -12 + x, -12 + y);
                    String statName = stat.getDisplayName(androidPlayer, androidPlayer.getUnlockedLevel(stat));
                    int statNameWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(statName);
                    GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                    Minecraft.getMinecraft().fontRenderer.drawString(statName, -statNameWidth / 2, -5, Reference.COLOR_HOLO.getColor());
                } else {
                    x = Math.sin(angle) * radius;
                    y = Math.cos(angle) * radius;
                    RenderUtils.applyColorWithMultipy(baseGuiColor, 0.2f);
                    ClientProxy.holoIcons.renderIcon(stat.getIcon(0), -12 + x, -12 + y);
                }
            }

            i++;
        }
        GlStateManager.popMatrix();
    }

    public void renderHud(RenderGameOverlayEvent event) {
        if (this.mc.player.isSpectator()) {
            return;
        }

        AndroidPlayer android = MOPlayerCapabilityProvider.GetAndroidCapability(mc.player);

        if (android != null) {

            if (android.isAndroid()) {

                GlStateManager.pushMatrix();

                if (OverdriveBioticStats.cloak.isActive(android, 0)) {
                    GlStateManager.blendFunc(GL_DST_COLOR, GL_ZERO);
                    GlStateManager.color(1, 1, 1);
                    mc.renderEngine.bindTexture(cloak_overlay);
                    RenderUtils.drawPlane(0, 0, -100, event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight());
                }

                if (hudMovement) {
                    hudRotationYawSmooth = mc.player.prevRenderArmYaw + (mc.player.renderArmYaw - mc.player.prevRenderArmYaw) * event.getPartialTicks();
                    hudRotationPitchSmooth = mc.player.prevRenderArmPitch + (mc.player.renderArmPitch - mc.player.prevRenderArmPitch) * event.getPartialTicks();
                    GlStateManager.translate((hudRotationYawSmooth - mc.player.rotationYaw) * 0.2f, (hudRotationPitchSmooth - mc.player.rotationPitch) * 0.2f, 0);
                }

                for (IAndroidHudElement element : hudElements) {
                    if (element.isVisible(android)) {
                        GlStateManager.pushMatrix();
                        int elementWidth = (int) (element.getWidth(event.getResolution(), android) * element.getPosition().x);
                        GlStateManager.translate(element.getPosition().x * event.getResolution().getScaledWidth_double() - elementWidth, element.getPosition().y * event.getResolution().getScaledHeight_double() - element.getHeight(event.getResolution(), android) * element.getPosition().y, 0);
                        element.setBaseColor(baseGuiColor);
                        element.setBackgroundAlpha(opacityBackground);
                        element.drawElement(android, event.getResolution(), event.getPartialTicks());
                        GlStateManager.popMatrix();
                    }
                }
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.popMatrix();

                renderHurt(android, event);
            } else {
                if (android.isTurning()) {
                    renderTransformAnimation(android, event);
                }
            }
        }
    }

    private void renderTransformAnimation(AndroidPlayer player, RenderGameOverlayEvent event) {
        int centerX = event.getResolution().getScaledWidth() / 2;
        int centerY = event.getResolution().getScaledHeight() / 2;
        int maxTime = AndroidPlayer.TRANSFORM_TIME;
        int time = maxTime - player.getAndroidEffects().getEffectShort(AndroidPlayer.EFFECT_TURNNING);
        textTyping.setTime(time);

        if (time % 40 > 0 && time % 40 < 3) {
            renderGlitch(player, event);
        }

        String info = textTyping.getString();
        int width = mc.fontRenderer.getStringWidth(info);
        mc.fontRenderer.drawString(info, centerX - width / 2, centerY - 28, Reference.COLOR_HOLO.getColor());

        mc.renderEngine.bindTexture(spinner_tex);
        GlStateManager.pushMatrix();
        GlStateManager.translate(centerX, centerY, 0);
        GlStateManager.rotate(mc.world.getWorldTime() * 10, 0, 0, -1);
        GlStateManager.translate(-16, -16, 0);
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 32, 32, 32, 32);
        GlStateManager.popMatrix();

        mc.fontRenderer.drawString(Math.round(textTyping.getPercent() * 100) + "%", centerX - 6, centerY - 3, Reference.COLOR_HOLO.getColor());
    }

    public void renderHurt(AndroidPlayer player, RenderGameOverlayEvent event) {
        if (player.getAndroidEffects().getEffectInt(AndroidPlayer.EFFECT_GLITCH_TIME) > 0) {
            renderGlitch(player, event);
        }
    }

    public void renderGlitch(AndroidPlayer player, RenderGameOverlayEvent event) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_ONE, GL_ONE);
        GlStateManager.disableDepth();
        GlStateManager.color(1, 1, 1);
        mc.renderEngine.bindTexture(glitch_tex);
        RenderUtils.drawPlaneWithUV(0, 0, -100, event.getResolution().getScaledWidth(), event.getResolution().getScaledHeight(), random.nextGaussian(), random.nextGaussian(), 1, 1);
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        Property prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD, hudMinimap.getName() + ".position", hudMinimap.getDefaultPosition().ordinal());
        prop.setConfigEntryClass(EnumConfigProperty.class);
        prop.setValidValues(AndroidHudPosition.getNames());
        prop.setLanguageKey("config.android_hud.minimap.position");
        hudMinimap.setHudPosition(AndroidHudPosition.values()[prop.getInt()]);

        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD, hudStats.getName() + ".position", hudStats.getDefaultPosition().ordinal());
        prop.setConfigEntryClass(EnumConfigProperty.class);
        prop.setValidValues(AndroidHudPosition.getNames());
        prop.setLanguageKey("config.android_hud.stats.position");
        hudStats.setHudPosition(AndroidHudPosition.values()[prop.getInt()]);

        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD, bionicStats.getName() + ".position", bionicStats.getDefaultPosition().ordinal());
        prop.setConfigEntryClass(EnumConfigProperty.class);
        prop.setValidValues(AndroidHudPosition.getNames());
        prop.setLanguageKey("config.android_hud.bionicStats.position");
        bionicStats.setHudPosition(AndroidHudPosition.values()[prop.getInt()]);

        Color color = Reference.COLOR_HOLO;
        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD, "hud_color", Integer.toHexString(color.getColor()));
        prop.setLanguageKey("config.android_hud.color");
        try {
            baseGuiColor = new Color(Integer.parseInt(prop.getString(), 16));
        } catch (Exception e) {
            baseGuiColor = Reference.COLOR_HOLO;
        }

        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD, "hud_opacity", 0.5f, "The Opacity of the HUD in %", 0, 1);
        prop.setLanguageKey("config.android_hud.opacity");
        baseGuiColor = new Color(baseGuiColor.getIntR(), baseGuiColor.getIntG(), baseGuiColor.getIntB(), (int) (255 * prop.getDouble()));

        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD, "hud_background_opacity", 0F, "The opacity of the black background for each HUD element");
        prop.setLanguageKey("config.android_hud.opacity_background");
        opacityBackground = (float) prop.getDouble();

        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD, "hide_vanilla_hud_elements", true, "Should the health bar and food bar be hidden");
        prop.setLanguageKey("config.android_hud.hide_vanilla");
        hideVanillaHudElements = prop.getBoolean();

        prop = config.config.get(ConfigurationHandler.CATEGORY_ANDROID_HUD, "hud_movement", true, "Should the Android HUD move when the player turns his head.");
        prop.setLanguageKey("config.android_hud.hud_movement");
        hudMovement = prop.getBoolean();
    }
}
