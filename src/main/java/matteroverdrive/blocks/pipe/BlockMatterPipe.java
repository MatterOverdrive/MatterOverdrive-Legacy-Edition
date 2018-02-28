package matteroverdrive.blocks.pipe;

import com.astro.clib.client.ClientUtil;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMatterPipe extends BlockPipe<TileEntityPipe> {
    public BlockMatterPipe(Material material, String name) {
        super(material, name);
    }

    @Override
    public Class<TileEntityPipe> getTileEntityClass() {
        return TileEntityPipe.class;
    }

    @Nullable
    @Override
    public TileEntityPipe createNewTileEntity(World worldIn, int meta) {
        return new TileEntityPipe();
    }

    @Override
    public void initItemModel() {
        ClientUtil.registerToNormal(this);
    }
}
