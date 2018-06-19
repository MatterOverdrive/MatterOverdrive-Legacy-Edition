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
package matteroverdrive.starmap.gen;

import matteroverdrive.starmap.data.Planet;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

/**
 * Created by Simeon on 6/26/2015.
 */
public abstract class PlanetAbstractGen implements ISpaceBodyGen<Planet> {
    byte type;
    int buildingSpaces, fleetSpaces;

    public PlanetAbstractGen(byte type, int buildingSpaces, int fleetSpaces) {
        this.type = type;
        this.buildingSpaces = buildingSpaces;
        this.fleetSpaces = fleetSpaces;
    }

    @Override
    public void generateSpaceBody(Planet planet, Random random) {
        planet.setType((byte) 2);
        setSize(planet, random);
    }

    @Override
    public boolean generateMissing(NBTTagCompound tagCompound, Planet planet, Random random) {
        if (planet.getType() == type) {
            if (!tagCompound.hasKey("Type", 1)) {
                planet.setType(type);
            }
            if (!tagCompound.hasKey("Size", 5)) {
                setSize(planet, random);
            }
            return true;
        }
        return false;
    }

    protected abstract void setSize(Planet planet, Random random);
}
