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
package matteroverdrive.world;

import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Simeon on 2/10/2016.
 */
public class DimensionalRifts {
    private double noiseScale;

    public DimensionalRifts(double noiseScale) {
        this.noiseScale = noiseScale;
    }

    public float getValueAt(BlockPos pos) {
        return this.getValueAt(new Vec3d(pos));
    }

    public float getValueAt(Vec3d pos) {
        if (Minecraft.getMinecraft().world != null) {
            float yPos = (float) MOMathHelper.noise(pos.x * noiseScale, Minecraft.getMinecraft().world.provider.getSeed(), pos.z * noiseScale);
            yPos = MathHelper.clamp((float) Math.pow((yPos - 0.45f), 5) * 180, 0, 1);
            return yPos;
        }
        return 0;
    }
}
