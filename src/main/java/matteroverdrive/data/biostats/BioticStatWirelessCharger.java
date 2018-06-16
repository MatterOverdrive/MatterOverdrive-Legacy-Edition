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
import matteroverdrive.entity.android_player.AndroidPlayer;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.EnumSet;

/**
 * Created by Simeon on 2/2/2016.
 */
public class BioticStatWirelessCharger extends AbstractBioticStat {
    public static final int CHARGE_SPEED = 32;

    public BioticStatWirelessCharger(String name, int xp) {
        super(name, xp);
    }

    public String getDetails(int level) {
        return String.format(super.getDetails(level), TextFormatting.YELLOW.toString() + CHARGE_SPEED + TextFormatting.GRAY);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {
        if (!android.getPlayer().getEntityWorld().isRemote && isActive(android, level)) {
            for (int i = 0; i < 9; i++) {
                ItemStack itemStack = android.getPlayer().inventory.getStackInSlot(i);
                if (!itemStack.isEmpty() && itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
                    IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
                    if (storage != null && android.getPlayer().getHeldItemMainhand() != itemStack) {
                        android.extractEnergy(storage.receiveEnergy(android.extractEnergy(CHARGE_SPEED, true), false), false);
                    }
                }
            }
        }
    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server) {
        if (server && this.equals(androidPlayer.getActiveStat())) {
            androidPlayer.getAndroidEffects().updateEffect(AndroidPlayer.EFFECT_WIRELESS_CHARGING, !androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_WIRELESS_CHARGING));
            androidPlayer.sync(EnumSet.of(AndroidPlayer.DataType.EFFECTS));
        }
    }

    @Override
    public boolean showOnHud(AndroidPlayer android, int level) {
        return isActive(android, level);
    }

    @Override
    public boolean showOnWheel(AndroidPlayer androidPlayer, int level) {
        return androidPlayer.getPlayer().isSneaking();
    }

    @Override
    public boolean isEnabled(AndroidPlayer android, int level) {
        return super.isEnabled(android, level) && android.getEnergyStored() > CHARGE_SPEED;
    }

    @Override
    public void onKeyPress(AndroidPlayer androidPlayer, int level, int keycode, boolean down) {

    }

    @Override
    public void onLivingEvent(AndroidPlayer androidPlayer, int level, LivingEvent event) {

    }

    @Override
    public void changeAndroidStats(AndroidPlayer androidPlayer, int level, boolean enabled) {

    }

    @Override
    public Multimap<String, AttributeModifier> attributes(AndroidPlayer androidPlayer, int level) {
        return null;
    }

    @Override
    public boolean isActive(AndroidPlayer androidPlayer, int level) {
        return androidPlayer.getAndroidEffects().getEffectBool(AndroidPlayer.EFFECT_WIRELESS_CHARGING);
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level) {
        return 0;
    }
}
