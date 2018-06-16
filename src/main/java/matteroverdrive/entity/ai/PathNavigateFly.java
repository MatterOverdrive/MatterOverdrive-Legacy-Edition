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
package matteroverdrive.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Simeon on 1/22/2016.
 */
public class PathNavigateFly extends PathNavigate {
    public PathNavigateFly(EntityLiving entity, World world) {
        super(entity, world);
    }

    @Override
    protected PathFinder getPathFinder() {
        return new PathFinder(new FlyNodeProcessor());
    }

    /**
     * If on ground or swimming and can swim
     */
    @Override
    protected boolean canNavigate() {
        return true;
    }

    @Override
    protected Vec3d getEntityPosition() {
        return new Vec3d(this.entity.posX, this.entity.posY, this.entity.posZ);
    }

    @Override
    protected void pathFollow() {
        Vec3d vec3 = this.getEntityPosition();
        float f = this.entity.width * this.entity.width;
        int i = 6;

        if (vec3.squareDistanceTo(this.currentPath.getVectorFromIndex(this.entity, this.currentPath.getCurrentPathIndex())) < (double) f) {
            this.currentPath.incrementPathIndex();
        }

        for (int j = Math.min(this.currentPath.getCurrentPathIndex() + i, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j) {
            Vec3d vec31 = this.currentPath.getVectorFromIndex(this.entity, j);

            if (vec31.squareDistanceTo(vec3) <= 36.0D && this.isDirectPathBetweenPoints(vec3, vec31, 0, 0, 0)) {
                this.currentPath.setCurrentPathIndex(j);
                break;
            }
        }

        this.checkForStuck(vec3);
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();
    }

    /**
     * Returns true when an entity of specified size could safely walk in a straight line between the two points. Args:
     * pos1, pos2, entityXSize, entityYSize, entityZSize
     */
    @Override
    protected boolean isDirectPathBetweenPoints(Vec3d pos1, Vec3d pos2, int sizeX, int sizeY, int sizeZ) {
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(pos1, new Vec3d(pos2.x, pos2.y + (double) this.entity.height * 0.5D, pos2.z), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit == RayTraceResult.Type.MISS;
    }
}
