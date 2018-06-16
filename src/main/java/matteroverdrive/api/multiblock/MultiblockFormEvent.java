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
package matteroverdrive.api.multiblock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import java.util.Map;
import java.util.TreeMap;

public class MultiblockFormEvent extends BlockEvent {

    private final Multiblock multiblock;

    public MultiblockFormEvent(World world, BlockPos pos, IBlockState state, Multiblock multiblock) {
        super(world, pos, state);
        this.multiblock = multiblock;
    }

    public Multiblock getMultiblock() {
        return multiblock;
    }

    public enum Multiblock {
        PYLON("pylon");

        private static final Map<String, Multiblock> MAP = new TreeMap<>();

        static {
            for (Multiblock multiblock1 : values())
                MAP.put(multiblock1.name, multiblock1);
        }

        private String name;

        Multiblock(String name) {
            this.name = name;
        }

        public static Multiblock getMultiblock(String name) {
            return MAP.get(name);
        }

        public boolean equals(Multiblock multiblock) {
            return this == multiblock;
        }

        public boolean equals(String name) {
            return getName().equals(name);
        }

        public String getName() {
            return name;
        }
    }
}