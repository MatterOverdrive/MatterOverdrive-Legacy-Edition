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
package matteroverdrive.machines.events;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Simeon on 1/28/2016.
 */
public class MachineEvent {
    public static class Destroyed extends MachineEvent {
        public final World world;
        public final BlockPos pos;
        public final IBlockState state;

        public Destroyed(World world, BlockPos pos, IBlockState state) {
            this.world = world;
            this.pos = pos;
            this.state = state;
        }
    }

    public static class NeighborChange extends MachineEvent {
        public final IBlockAccess world;
        public final BlockPos pos;
        public final IBlockState state;
        public final Block neighborBlock;

        public NeighborChange(IBlockAccess world, BlockPos pos, IBlockState state, Block neighborBlock) {
            this.world = world;
            this.pos = pos;
            this.state = state;
            this.neighborBlock = neighborBlock;
        }
    }

    public static class Placed extends MachineEvent {
        public final World world;
        public final EntityLivingBase entityLiving;

        public Placed(World world, EntityLivingBase entityLiving) {
            this.world = world;
            this.entityLiving = entityLiving;
        }
    }

    public static class Added extends MachineEvent {
        public final World world;
        public final BlockPos pos;
        public final IBlockState state;

        public Added(World world, BlockPos pos, IBlockState state) {
            this.world = world;
            this.pos = pos;
            this.state = state;
        }
    }

    public static class ActiveChange extends MachineEvent {

    }

    public static class Awake extends MachineEvent {
        public final Side side;

        public Awake(Side side) {
            this.side = side;
        }
    }

    public static class OpenContainer extends MachineEvent {
        public final Side side;

        public OpenContainer(Side side) {
            this.side = side;
        }
    }

    public static class Unload extends MachineEvent {

    }
}
