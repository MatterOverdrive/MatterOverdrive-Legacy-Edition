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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Simeon on 3/18/2015.
 */
public class MachineSound extends PositionedSound implements ITickableSound {
    private boolean donePlaying;

    public MachineSound(SoundEvent sound, SoundCategory category, BlockPos pos, float volume, float pitch) {
        super(sound, category);
        setPosition(pos);
        this.volume = volume;
        this.pitch = pitch;
        this.repeat = true;
        this.repeatDelay = 0;
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

    public void setPosition(BlockPos position) {
        this.xPosF = position.getX();
        this.yPosF = position.getY();
        this.zPosF = position.getZ();
    }

    @Override
    public void update() {
    }
}
