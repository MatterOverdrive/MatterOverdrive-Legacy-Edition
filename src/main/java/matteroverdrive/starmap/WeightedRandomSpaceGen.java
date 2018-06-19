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
package matteroverdrive.starmap;

import matteroverdrive.starmap.data.SpaceBody;
import matteroverdrive.starmap.gen.ISpaceBodyGen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by Simeon on 6/21/2015.
 */
public class WeightedRandomSpaceGen<T extends SpaceBody> {
    final Random random;
    final List<ISpaceBodyGen<T>> collection;

    public WeightedRandomSpaceGen(Random random) {
        collection = new ArrayList<>();
        this.random = random;
    }

    public ISpaceBodyGen<T> getRandomGen(T spaceBody) {
        return getRandomGen(collection, spaceBody, random);
    }

    public ISpaceBodyGen<T> getRandomGen(T spaceBody, Random random) {
        return getRandomGen(collection, spaceBody, random);
    }

    public ISpaceBodyGen<T> getRandomGen(Collection<ISpaceBodyGen<T>> collection, T spaceBody, Random rangomGen) {
        // Compute the total weight of all items together
        double totalWeight = 0.0d;
        for (ISpaceBodyGen<T> i : collection) {
            totalWeight += i.getWeight(spaceBody);
        }
        // Now choose a random item
        ISpaceBodyGen gen = null;
        double random = rangomGen.nextDouble() * totalWeight;
        for (ISpaceBodyGen<T> i : collection) {
            random -= i.getWeight(spaceBody);
            if (random <= 0.0d) {
                gen = i;
                break;
            }
        }
        return gen;
    }

    public ISpaceBodyGen<T> getGenAt(int index) {
        return collection.get(index);
    }

    public void addGen(ISpaceBodyGen<T> gen) {
        collection.add(gen);
    }

    public <K extends ISpaceBodyGen<T>> void addGens(Collection<K> gens) {
        collection.addAll(gens);
    }

    public List<ISpaceBodyGen<T>> getGens() {
        return collection;
    }
}
