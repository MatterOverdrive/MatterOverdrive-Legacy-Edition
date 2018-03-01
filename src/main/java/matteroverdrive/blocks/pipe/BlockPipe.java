package matteroverdrive.blocks.pipe;

import matteroverdrive.blocks.includes.MOBlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public abstract class BlockPipe<TE extends TileEntityPipe> extends MOBlockContainer<TE> {

    public static final PropertyBool[] CONNECTED_PROPERTIES = new PropertyBool[EnumFacing.VALUES.length];

    static {
        for (EnumFacing facing : EnumFacing.VALUES)
            CONNECTED_PROPERTIES[facing.getIndex()] = PropertyBool.create(facing.getName());
    }

    public BlockPipe(Material material, String name) {
        super(material, name);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES)
            state=state.withProperty(CONNECTED_PROPERTIES[facing.getIndex()],canConnectTo(worldIn,pos.offset(facing)));
        return state;
    }

    public boolean canConnectTo(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer.Builder(this)
                .add(CONNECTED_PROPERTIES)
                .build();
    }
}