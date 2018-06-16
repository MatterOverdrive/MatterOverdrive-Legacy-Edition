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
package matteroverdrive.blocks.world;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.includes.MOBlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Simeon on 3/23/2015.
 */
public class DilithiumOre extends MOBlockOre {

    private final Random rand = new Random();

    public DilithiumOre(Material material, String name, String oreDict) {
        super(material, name, oreDict);
        this.setHardness(4.0f);
        this.setResistance(5.0f);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return MatterOverdrive.ITEMS.dilithium_crystal;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        if (fortune > 0 && Item.getItemFromBlock(this) != this.getItemDropped(state, random, fortune)) {
            int j = random.nextInt(fortune) - 1;

            if (j < 0) {
                j = 0;
            }

            return this.quantityDropped(random) * (j + 1);
        } else {
            return this.quantityDropped(random);
        }
    }

    @Override
    public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
        if (this.getItemDropped(world.getBlockState(pos), rand, fortune) != Item.getItemFromBlock(this)) {
            return MathHelper.getInt(rand, 2, 5);
        }
        return 0;
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }
}
