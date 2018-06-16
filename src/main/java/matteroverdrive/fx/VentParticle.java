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
package matteroverdrive.fx;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Simeon on 4/17/2015.
 */
@SideOnly(Side.CLIENT)
public class VentParticle extends Particle {
    public VentParticle(World world, double x, double y, double z, double dirX, double dirY, double dirZ) {
        this(world, x, y, z, dirX, dirY, dirZ, 1.0F);
    }

    public VentParticle(World world, double x, double y, double z, double dirX, double dirY, double dirZ, float scale) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = dirX;
        this.motionY = dirY;
        this.motionZ = dirZ;
        this.particleRed = this.particleGreen = this.particleBlue = 1 - rand.nextFloat() * 0.3f;
        this.particleScale = scale;
        this.particleMaxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
        //this.noClip = false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.move(this.motionX, this.motionY, this.motionZ);

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;
    }
}
