package matteroverdrive.blocks;

import com.astro.clib.api.wrench.IWrenchable;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.blocks.includes.MOBlockContainer;
import matteroverdrive.machines.dimensional_pylon.TileEntityMachineDimensionalPylon;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 2/4/2016.
 */
public class BlockPylon extends MOBlockContainer<TileEntityMachineDimensionalPylon> implements IWrenchable {
    public static final PropertyEnum<MultiblockType> TYPE = PropertyEnum.create("type", MultiblockType.class);
    public static final PropertyBool CTM = PropertyBool.create("ctm");

    public BlockPylon(Material material, String name) {
        super(material, name);
        setHardness(8f);
        setLightOpacity(0);
        this.setResistance(9.0f);
        this.setHarvestLevel("pickaxe", 2);
        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return super.getActualState(state, worldIn, pos).withProperty(CTM, Loader.isModLoaded("ctm") && state.getValue(TYPE) == MultiblockType.MAIN);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, CTM);
    }

    @Nonnull
    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, MultiblockType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        MultiblockType type = state.getValue(TYPE);
        return type.ordinal();
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public Class<TileEntityMachineDimensionalPylon> getTileEntityClass() {
        return TileEntityMachineDimensionalPylon.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileEntityMachineDimensionalPylon();
    }

    @Override
    public boolean onWrenchHit(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity pylon = world.getTileEntity(pos);
        if (pylon != null && pylon instanceof TileEntityMachineDimensionalPylon) {
            return ((TileEntityMachineDimensionalPylon) pylon).onWrenchHit(stack, player, world, pos, side, hitX, hitY, hitZ);
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMachineDimensionalPylon) {
            return ((TileEntityMachineDimensionalPylon) tileEntity).openMultiBlockGui(worldIn, playerIn);
        }

        return false;
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecated")
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState originalBlockState = world.getBlockState(pos.offset(side.getOpposite()));
        if (originalBlockState.getBlock() == this) {
            if (originalBlockState.getValue(TYPE) == MultiblockType.DUMMY) {
                return false;
            }

        }
        return super.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return state.getValue(TYPE) == MultiblockType.DUMMY ? EnumBlockRenderType.INVISIBLE : super.getRenderType(state);
    }

    public enum MultiblockType implements IStringSerializable {
        NORMAL("normal"), DUMMY("dummy"), MAIN("main");

        private final String name;

        MultiblockType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}