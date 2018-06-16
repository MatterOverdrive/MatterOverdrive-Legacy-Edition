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

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nullable;

public class SoundMuffled implements ISound {
    private final ISound original;
    private final float amount;

    public SoundMuffled(ISound original, float amount) {
        this.original = original;
        this.amount = amount;
    }

    @Override
    public ResourceLocation getSoundLocation() {
        return original.getSoundLocation();
    }

    @Nullable
    @Override
    public SoundEventAccessor createAccessor(SoundHandler handler) {
        return original.createAccessor(handler);
    }

    @Override
    public Sound getSound() {
        return original.getSound();
    }

    @Override
    public SoundCategory getCategory() {
        return original.getCategory();
    }

    @Override
    public boolean canRepeat() {
        return original.canRepeat();
    }

    @Override
    public int getRepeatDelay() {
        return original.getRepeatDelay();
    }

    @Override
    public float getVolume() {
        return original.getVolume() / amount;
    }

    @Override
    public float getPitch() {
        return original.getPitch();
    }

    @Override
    public float getXPosF() {
        return original.getXPosF();
    }

    @Override
    public float getYPosF() {
        return original.getYPosF();
    }

    @Override
    public float getZPosF() {
        return original.getZPosF();
    }

    @Override
    public AttenuationType getAttenuationType() {
        return original.getAttenuationType();
    }
}
