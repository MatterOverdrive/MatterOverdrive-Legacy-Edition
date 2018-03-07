/*
 * This file is part of Matter Overdrive
 * Copyright (c) 2015., Simeon Radivoev, All rights reserved.
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

package matteroverdrive.blocks.includes;

import com.astro.clib.api.render.ItemModelProvider;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.api.IMOTileEntity;
import matteroverdrive.tile.MOTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

import static matteroverdrive.util.MOBlockHelper.RotationType;
import static matteroverdrive.util.MOBlockHelper.SIDE_LEFT;

/**
 * Created by Simeon on 3/24/2015.
 */
public class MOBlock extends Block implements ItemModelProvider {
    public static final PropertyDirection PROPERTY_DIRECTION = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    protected AxisAlignedBB boundingBox = FULL_BLOCK_AABB;
    private BlockStateContainer blockState;
    private boolean hasRotation;
    private int rotationType;

    public MOBlock(Material material, String name) {
        super(material);
        setRegistryName(name);
        blockState = createBlockState();
        setDefaultState(getBlockState().getBaseState());
        fullBlock = getDefaultState().isOpaqueCube();
        lightOpacity = fullBlock ? 255 : 0;
        setUnlocalizedName(name);
        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE);
        rotationType = RotationType.FOUR_WAY;
    }

    public void setBoundingBox(AxisAlignedBB boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public void initItemModel() {
        NonNullList<ItemStack> sub = NonNullList.create();
        getSubBlocks(CreativeTabs.SEARCH, sub);
        for (ItemStack stack : sub) {
            ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(), new ModelResourceLocation(getRegistryName(), "inventory"));
        }
    }

    @Nonnull
    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (hasRotation) {
            return new BlockStateContainer(this, PROPERTY_DIRECTION);
        }
        return super.createBlockState();
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        if (hasRotation) {
            return getDefaultState().withProperty(PROPERTY_DIRECTION, EnumFacing.getHorizontal(meta));
        } else {
            return getDefaultState();
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (hasRotation) {
            EnumFacing facing = state.getValue(PROPERTY_DIRECTION);
            return facing.getHorizontalIndex();
        } else {
            return 0;
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);

        IMOTileEntity tileEntity = (IMOTileEntity) worldIn.getTileEntity(pos);
        if (tileEntity != null) {
            tileEntity.onAdded(worldIn, pos, state);
        }
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
        IMOTileEntity tileEntity = (IMOTileEntity) world.getTileEntity(pos);
        if (tileEntity != null) {
            tileEntity.onNeighborBlockChange(world, pos, world.getBlockState(pos), world.getBlockState(neighbor).getBlock());
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (hasRotation)
            return getDefaultState().withProperty(PROPERTY_DIRECTION, EnumFacing.fromAngle(placer.rotationYaw).getOpposite());
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    }

    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        if (rotationType >= 0) {
            IBlockState state = world.getBlockState(pos);
            for (IProperty prop : state.getProperties().keySet()) {
                if (prop.getName().equals(PROPERTY_DIRECTION)) {
                    EnumFacing facing = state.getValue(PROPERTY_DIRECTION);

                    if (rotationType == RotationType.FOUR_WAY) {
                        facing = EnumFacing.VALUES[SIDE_LEFT[facing.ordinal() % SIDE_LEFT.length]];
                    } else if (rotationType == RotationType.SIX_WAY) {
                        if (facing.ordinal() < 6) {
                            facing = EnumFacing.VALUES[(facing.ordinal() + 1) % 6];
                        }
                    }

                    world.setBlockState(pos, world.getBlockState(pos).withProperty(PROPERTY_DIRECTION, facing), 3);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (hasTileEntity(state) && worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof MOTileEntity) {
            ((MOTileEntity) worldIn.getTileEntity(pos)).onDestroyed(worldIn, pos, state);
        }
        super.breakBlock(worldIn, pos, state);
    }

    public void setRotationType(int type) {
        rotationType = type;
    }

    public void setHasRotation() {
        this.hasRotation = true;
        this.blockState = createBlockState();
        this.setDefaultState(this.blockState.getBaseState());
    }

    @Override
    public BlockStateContainer getBlockState() {
        return this.blockState;
    }
}
