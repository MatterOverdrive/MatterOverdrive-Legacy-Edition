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
package matteroverdrive.handler;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.Random;

public class SoundHandler {
    protected static final Random soundRand = new Random();

    public static void PlaySoundAt(World world, SoundEvent event, SoundCategory category, Entity entity) {
        PlaySoundAt(world, event, category, entity, 1.0f, 1.0f, 0.1f, 0.1f);
    }

    public static void PlaySoundAt(World world, SoundEvent event, SoundCategory category, Entity entity, float maxVolume) {
        PlaySoundAt(world, event, category, entity, maxVolume, 1f, 0.1f, 0.1f);
    }

    public static void PlaySoundAt(World world, SoundEvent event, SoundCategory category, Entity entity, float maxVolume, float maxPitch) {
        PlaySoundAt(world, event, category, entity, maxVolume, maxPitch, 0.1f, 0.1f);
    }

    public static void PlaySoundAt(World world, SoundEvent event, SoundCategory category, Entity entity, float maxVolume, float maxPitch, float maxVolumeDeviation) {
        PlaySoundAt(world, event, category, entity, maxVolume, maxPitch, maxVolumeDeviation, 0.1f);
    }

    public static void PlaySoundAt(World world, SoundEvent even, SoundCategory category, Entity entity, float maxVolume, float maxPitch, float maxVolumeDeviation, float maxPitchDeviation) {
        float PitchDeviation = maxPitch * maxPitchDeviation * soundRand.nextFloat();
        float VolumeDeviation = maxVolume * maxVolumeDeviation * soundRand.nextFloat();
        float volume = (maxVolume - maxVolumeDeviation) + VolumeDeviation;
        float pitch = (maxPitch - maxPitchDeviation) + PitchDeviation;
        world.playSound(null, entity.posX, entity.posY, entity.posZ, even, category, volume, pitch);
    }

    public static void PlaySoundAt(World world, SoundEvent even, SoundCategory category, float x, float y, float z) {
        PlaySoundAt(world, even, category, x, y, z, 1.0f, 1.0f, 0.1f, 0.1f);
    }

    public static void PlaySoundAt(World world, SoundEvent even, SoundCategory category, float x, float y, float z, float maxVolume) {
        PlaySoundAt(world, even, category, x, y, z, maxVolume, 1.0f, 0.1f, 0.1f);
    }

    public static void PlaySoundAt(World world, SoundEvent even, SoundCategory category, float x, float y, float z, float maxVolume, float maxPitch) {
        PlaySoundAt(world, even, category, x, y, z, maxVolume, maxPitch, 0.1f, 0.1f);
    }

    public static void PlaySoundAt(World world, SoundEvent even, SoundCategory category, float x, float y, float z, float maxVolume, float maxPitch, float maxVolumeDeviation) {
        PlaySoundAt(world, even, category, x, y, z, maxVolume, maxPitch, maxVolumeDeviation, 0.1f);
    }

    public static void PlaySoundAt(World world, SoundEvent even, SoundCategory category, float x, float y, float z, float maxVolume, float maxPitch, float maxVolumeDeviation, float maxPitchDeviation) {
        float PitchDeviation = maxPitch * maxPitchDeviation * soundRand.nextFloat();
        float VolumeDeviation = maxVolume * maxVolumeDeviation * soundRand.nextFloat();
        float volume = (maxVolume - VolumeDeviation) + VolumeDeviation;
        float pitch = (maxPitch - PitchDeviation) + PitchDeviation;
        world.playSound(null, x, y, z, even, category, volume, pitch);
    }
}
