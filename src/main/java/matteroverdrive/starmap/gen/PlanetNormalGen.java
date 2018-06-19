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

import java.util.Random;

/**
 * Created by Simeon on 6/21/2015.
 */
public class PlanetNormalGen extends PlanetAbstractGen {
    public PlanetNormalGen() {
        super((byte) 0, 6, 6);
    }

    @Override
    protected void setSize(Planet planet, Random random) {
        planet.setSize(0.7f + random.nextFloat() * 0.6f);
    }

    @Override
    public double getWeight(Planet body) {
        if (body.getOrbit() < 0.6f && body.getOrbit() > 0.4f) {
            return 0.3f;
        }
        return 0.1f;
    }
}
