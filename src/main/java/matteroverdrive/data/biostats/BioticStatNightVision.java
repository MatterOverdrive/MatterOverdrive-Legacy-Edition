/*
 * This file is part of Matter Overdrive
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
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
package matteroverdrive.data.biostats;

import com.google.common.collect.Multimap;
import matteroverdrive.api.events.bionicStats.MOEventBionicStat;
import matteroverdrive.client.sound.MOPositionedSound;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.handler.ConfigurationHandler;
import matteroverdrive.init.MatterOverdriveSounds;
import matteroverdrive.util.IConfigSubscriber;
import matteroverdrive.util.MOEnergyHelper;
import matteroverdrive.util.MOStringHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.EnumSet;

/**
 * Created by Simeon on 7/11/2015.
 */
public class BioticStatNightVision extends AbstractBioticStat implements IConfigSubscriber {
    private static int ENERGY_PER_TICK = 16;

    public BioticStatNightVision(String name, int xp) {
        super(name, xp);
        setShowOnWheel(true);
        setShowOnHud(true);
    }

    @Override
    public String getDetails(int level) {
        return MOStringHelper.translateToLocal(getUnlocalizedDetails(), TextFormatting.YELLOW.toString() + ENERGY_PER_TICK + MOEnergyHelper.ENERGY_UNIT);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {
        if (!android.getPlayer().world.isRemote) {
            if (isActive(android, level)) {

            }
        } else {
            manageNightvision(android, level);
        }
    }

    @SideOnly(Side.CLIENT)
    private void manageNightvision(AndroidPlayer android, int level) {
        if (isActive(android, level)) {
            android.getPlayer().addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 500));
        }
    }

    private void setActive(AndroidPlayer androidPlayer, int level, boolean active) {
        androidPlayer.getPlayer().addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 500));
        androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_NIGHTVISION, active);
        androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS), true);
    }

    @Override
    public void onActionKeyPress(AndroidPlayer android, int level, boolean server) {
        if (this.equals(android.getActiveStat())) {
            if (server) {
                if (!MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this, level, android))) {
                    setActive(android, level, !android.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_NIGHTVISION));
                }
            } else {
                if (!MinecraftForge.EVENT_BUS.post(new MOEventBionicStat(this, level, android))) {
                    playSound(android);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void playSound(AndroidPlayer android) {
        if (!android.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_NIGHTVISION)) {
            MOPositionedSound sound = new MOPositionedSound(MatterOverdriveSounds.androidNightVision, SoundCategory.PLAYERS, 0.05f + android.getPlayer().getRNG().nextFloat() * 0.1f, 0.95f + android.getPlayer().getRNG().nextFloat() * 0.1f);
            sound.setAttenuationType(ISound.AttenuationType.NONE);
            Minecraft.getMinecraft().getSoundHandler().playSound(sound);
        } else {
            Minecraft.getMinecraft().getSoundHandler().playSound(new MOPositionedSound(MatterOverdriveSounds.androidPowerDown, SoundCategory.PLAYERS, 0.05f + android.getPlayer().getRNG().nextFloat() * 0.1f, 0.95f + android.getPlayer().getRNG().nextFloat() * 0.1f).setAttenuationType(ISound.AttenuationType.NONE));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {
        if (!isEnabled(androidPlayer, level) && isActive(androidPlayer, level)) {
            setActive(androidPlayer, level, false);
        }
    }

    @Override
    public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
        return null;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_NIGHTVISION);
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level) {
        return 0;
    }

    @Override
    public boolean isEnabled(AndroidPlayer androidPlayer, int level) {
        return super.isEnabled(androidPlayer, level) && androidPlayer.hasEnoughEnergyScaled(ENERGY_PER_TICK);
    }

    @Override
    public boolean showOnHud(AndroidPlayer android, int level) {
        return isActive(android, level);
    }

    @Override
    public void onConfigChanged(ConfigurationHandler config) {
        ENERGY_PER_TICK = config.getInt("nighvision_energy_per_tick", ConfigurationHandler.CATEGORY_ABILITIES, 16, "The energy cost of the Nightvision");
    }
}
