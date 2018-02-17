package matteroverdrive.client.render.tileentity;

import matteroverdrive.Reference;
import matteroverdrive.tile.pipes.TileEntityPipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Simeon on 3/7/2015.
 */
public class TileEntityRendererNetworkPipe extends TileEntityRendererPipe {

    public TileEntityRendererNetworkPipe() {
        texture = new ResourceLocation(Reference.PATH_BLOCKS + "network_pipe.png");
    }

    @Override
    protected void drawCore(TileEntityPipe tileEntity, double x,
                            double y, double z, float f, int sides) {
        super.drawCore(tileEntity, x, y, z, f, sides);
    }

    @Override
    protected void drawSide(TileEntityPipe tileEntity, EnumFacing dir) {
        super.drawSide(tileEntity, dir);
    }
}
