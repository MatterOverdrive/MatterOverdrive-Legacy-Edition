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
package matteroverdrive.raytrace;

import net.minecraft.util.math.AxisAlignedBB;

public class Cuboid {
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;
    public final int identifier;
    public final boolean collidable;

    public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int identifier) {
        this(minX, minY, minZ, maxX, maxY, maxZ, identifier, true);
    }

    public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this(minX, minY, minZ, maxX, maxY, maxZ, 0, true);
    }

    public Cuboid(AxisAlignedBB bb) {
        this(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, 0, true);
    }

    public Cuboid(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int identifier, boolean collidable) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.identifier = identifier;
        this.collidable = collidable;
    }

    public AxisAlignedBB aabb() {
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }
}