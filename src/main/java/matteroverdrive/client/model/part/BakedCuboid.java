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
package matteroverdrive.client.model.part;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;

import java.util.List;
import java.util.Map;

public class BakedCuboid implements IBakedPart<BakedCuboid> {
    private final Map<EnumFacing, BakedQuad> quads = Maps.newHashMap();
    private final List<BakedQuad> unculledQuads = Lists.newArrayList();

    public BakedCuboid(BakedQuad north, BakedQuad east, BakedQuad south, BakedQuad west, BakedQuad up, BakedQuad down) {
        this.quads.put(EnumFacing.NORTH, north);
        this.quads.put(EnumFacing.EAST, east);
        this.quads.put(EnumFacing.SOUTH, south);
        this.quads.put(EnumFacing.WEST, west);
        this.quads.put(EnumFacing.UP, up);
        this.quads.put(EnumFacing.DOWN, down);
    }

    @Override
    public List<BakedQuad> addToList(List<BakedQuad> list, EnumFacing facing) {
        if (facing != null && this.quads.containsKey(facing))
            list.add(this.quads.get(facing));
        else if (facing == null)
            list.addAll(this.unculledQuads);
        return list;
    }

    @Override
    public BakedCuboid setNoCull() {
        this.unculledQuads.addAll(this.quads.values());
        this.quads.clear();
        return this;
    }

    @Override
    public BakedCuboid setNoCull(EnumFacing face) {
        this.unculledQuads.add(this.quads.get(face));
        this.quads.remove(face);
        return this;
    }
}