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
package matteroverdrive.api.android;

import net.minecraft.util.EnumFacing;

/**
 * Created by Simeon on 2/1/2016.
 */
public class BionicStatGuiInfo {
    EnumFacing direction;
    private int posX;
    private int posY;
    private int posZ;
    private float parallaxMultiply;
    private boolean strongConnection;

    public BionicStatGuiInfo(int posX, int posY) {
        this(posX, posY, 0, 1, null, false);
    }

    public BionicStatGuiInfo(int posX, int posY, EnumFacing direction) {
        this(posX, posY, 0, 1, direction, false);
    }

    public BionicStatGuiInfo(int posX, int posY, EnumFacing direction, boolean strongConnection) {
        this(posX, posY, 0, 1, direction, strongConnection);
    }

    public BionicStatGuiInfo(int posX, int posY, int posZ, float parallaxMultiply, EnumFacing direction, boolean strongConnection) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.parallaxMultiply = parallaxMultiply;
        this.direction = direction;
        this.strongConnection = strongConnection;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosZ() {
        return posZ;
    }

    public float getParallaxMultiply() {
        return parallaxMultiply;
    }

    public EnumFacing getDirection() {
        return direction;
    }

    public boolean isStrongConnection() {
        return strongConnection;
    }
}
