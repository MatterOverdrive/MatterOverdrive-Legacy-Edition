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
import matteroverdrive.util.MOLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 2/10/2016.
 */
public class BioticStatAirDash extends AbstractBioticStat {
    @SideOnly(Side.CLIENT)
    private int lastClickTime;
    @SideOnly(Side.CLIENT)
    private int clickCount;
    @SideOnly(Side.CLIENT)
    private boolean hasNotReleased;
    @SideOnly(Side.CLIENT)
    private boolean hasDashed;

    public BioticStatAirDash(String name, int xp) {
        super(name, xp);
    }

    @Override
    public void onAndroidUpdate(AndroidPlayer android, int level) {
        if (android.getPlayer().world.isRemote) {
            manageDashing(android);
        }
    }

    @SideOnly(Side.CLIENT)
    private void manageDashing(AndroidPlayer android) {
        EntityPlayerSP playerSP = (EntityPlayerSP) android.getPlayer();

        if (!playerSP.onGround) {
            if (!hasDashed) {
                if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
                    if (!hasNotReleased) {
                        hasNotReleased = true;
                        if (lastClickTime > 0) {
                            clickCount++;
                            MOLog.info("clickCount: %s", lastClickTime);
                        }
                        lastClickTime = 5;
                    }
                } else {
                    hasNotReleased = false;
                }

                if (clickCount >= 1) {
                    clickCount = 0;
                    dash(playerSP);
                    hasDashed = true;
                }

                if (lastClickTime > 0) {
                    lastClickTime--;
                }
            }
        } else {
            hasNotReleased = false;
            hasDashed = false;
            clickCount = 0;
            lastClickTime = 0;
        }
    }

    @SideOnly(Side.CLIENT)
    private void dash(EntityPlayerSP playerSP) {
        Vec3d look = playerSP.getLookVec().addVector(0, 0.75, 0).normalize();
        playerSP.addVelocity(look.x, look.y, look.z);
        for (int i = 0; i < 30; i++) {
            playerSP.world.spawnParticle(EnumParticleTypes.CLOUD, playerSP.posX + playerSP.getRNG().nextGaussian() * 0.5, playerSP.posY + playerSP.getRNG().nextFloat() * playerSP.getEyeHeight(), playerSP.posZ + playerSP.getRNG().nextGaussian() * 0.5, -look.x, -look.z, -look.z);
        }
    }

    @Override
    public void onActionKeyPress(AndroidPlayer androidPlayer, int level, boolean server) {

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
        return false;
    }

    @Override
    public int getDelay(AndroidPlayer androidPlayer, int level) {
        return 0;
    }
}
