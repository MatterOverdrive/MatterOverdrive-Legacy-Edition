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
package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOBlockMachine;
import matteroverdrive.tile.TileEntityMachineStarMap;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 6/13/2015.
 */
public class BlockStarMap extends MOBlockMachine<TileEntityMachineStarMap> {
    public BlockStarMap(Material material, String name) {
        super(material, name);
        setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 9 / 16d, 1));
        setHardness(20.0F);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        lightValue = 10;
        setHasGui(true);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            TileEntityMachineStarMap starMap = (TileEntityMachineStarMap) worldIn.getTileEntity(pos);
            starMap.zoom();
            return true;
        } else {
            return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        }
    }

    @Override
    public Class<TileEntityMachineStarMap> getTileEntityClass() {
        return TileEntityMachineStarMap.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityMachineStarMap();
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
}
