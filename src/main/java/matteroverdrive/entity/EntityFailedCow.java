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
package matteroverdrive.entity;

import matteroverdrive.init.MatterOverdriveSounds;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * Created by Simeon on 5/28/2015.
 */
public class EntityFailedCow extends EntityCow {
    public EntityFailedCow(World world) {
        super(world);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return MatterOverdriveSounds.failedAnimalIdleCow;
    }

    protected SoundEvent getHurtSound() {
        return MatterOverdriveSounds.failedAnimalIdleCow;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return MatterOverdriveSounds.failedAnimalDie;
    }

    public EntityCow createChild(EntityAgeable entity) {
        return new EntityFailedCow(this.world);
    }
}
