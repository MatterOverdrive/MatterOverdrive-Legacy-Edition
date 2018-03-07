package matteroverdrive.blocks;

import matteroverdrive.blocks.includes.MOMatterEnergyStorageBlock;
import matteroverdrive.tile.TileEntityMachineSolarPanel;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Simeon on 4/9/2015.
 */
public class BlockSolarPanel extends MOMatterEnergyStorageBlock<TileEntityMachineSolarPanel> {
    public BlockSolarPanel(Material material, String name) {
        super(material, name, true, false);

        setBoundingBox(new AxisAlignedBB(0, 0, 0, 1, 8 / 16d, 1));
        setHardness(20.0F);
        this.setResistance(5.0f);
        this.setHarvestLevel("pickaxe", 2);
        setHasGui(true);
    }

    @Override
    public Class<TileEntityMachineSolarPanel> getTileEntityClass() {
        return TileEntityMachineSolarPanel.class;
    }

    @Nonnull
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState meta) {
        return new TileEntityMachineSolarPanel();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

}
