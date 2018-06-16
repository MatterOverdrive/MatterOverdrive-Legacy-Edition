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
package matteroverdrive.client.sound;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.FMLClientHandler;

/**
 * Created by Simeon on 5/12/2015.
 */
public class GravitationalAnomalySound extends PositionedSound implements ITickableSound {

    boolean donePlaying = false;
    double range;

    public GravitationalAnomalySound(SoundEvent sound, SoundCategory category, BlockPos pos, float volume, double range) {
        super(sound, category);
        setPosition(pos.getX(), pos.getY(), pos.getZ());
        this.volume = volume;
        this.range = range;
        this.attenuationType = AttenuationType.NONE;
        this.repeat = true;
    }

    @Override
    public boolean isDonePlaying() {
        return donePlaying;
    }

    public void stopPlaying() {
        donePlaying = true;
    }

    public void startPlaying() {
        donePlaying = false;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void setPosition(float x, float y, float z) {
        this.xPosF = x;
        this.yPosF = y;
        this.zPosF = z;
    }

    @Override
    public void update() {
        EntityPlayerSP mp = FMLClientHandler.instance().getClient().player;
        double distance = new Vec3d(xPosF, yPosF, zPosF).distanceTo(mp.getPositionVector());
        volume = 1 - (float) (distance / range);
    }
}
