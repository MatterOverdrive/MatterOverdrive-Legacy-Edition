package matteroverdrive.tile.pipes;

import matteroverdrive.machines.MachineNBTCategory;
import matteroverdrive.tile.MOTileEntity;
import matteroverdrive.util.math.MOMathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public abstract class TileEntityPipe extends MOTileEntity implements ITickable {
    protected boolean needsUpdate = true;
    protected boolean awoken;
    private int connections = 0;

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories, boolean toDisk) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            nbt.setInteger("connections", (byte) getConnectionsMask());
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, EnumSet<MachineNBTCategory> categories) {
        if (categories.contains(MachineNBTCategory.DATA)) {
            setConnections(nbt.getInteger("connections"), false);
            needsUpdate = false;
            if(world!=null)
                world.markBlockRangeForRenderUpdate(pos,pos);
        }
    }

    @Override
    public void update() {
        if (needsUpdate) {
            updateSides(true);
            needsUpdate = false;
        }

        UPDATING_POS.clear();

        if (!awoken) {
            onAwake(world.isRemote ? Side.CLIENT : Side.SERVER);
            awoken = true;
        }
    }

    public void updateSides(boolean notify) {
        int connections = 0;

        for (EnumFacing direction : EnumFacing.VALUES) {
            TileEntity t = this.world.getTileEntity(getPos().offset(direction));

            if (canConnectToPipe(t, direction)) {
                connections |= 1 << direction.ordinal();
            }
        }

        this.setConnections(connections, notify);
    }

    public int getConnectionsMask() {
        return connections;
    }

    public int getConnectionsCount() {
        int tot = 0;
        int con = connections;
        while (con > 0) {
            ++tot;
            con &= con - 1;
        }

        return tot;
    }

    public static List<BlockPos> UPDATING_POS = new ArrayList<>();

    public void setConnections(int connections, boolean notify) {
        this.connections = connections;
        if (notify) {
            UPDATING_POS.add(getPos());
            world.markBlockRangeForRenderUpdate(pos, pos);
            for (EnumFacing facing : EnumFacing.VALUES) {
                if (isConnectedFromSide(facing)) {
                    if (!UPDATING_POS.contains(getPos().offset(facing)))
                        world.neighborChanged(getPos().offset(facing), getBlockType(), getPos());
                }
            }
            markDirty();
        }
    }

    public void setConnection(EnumFacing connection, boolean value) {
        this.connections = MOMathHelper.setBoolean(connections, connection.ordinal(), value);
    }

    public boolean isConnectedFromSide(EnumFacing enumFacing) {
        return MOMathHelper.getBoolean(connections, enumFacing.ordinal());
    }

    public abstract boolean canConnectToPipe(TileEntity entity, EnumFacing direction);

    public void queueUpdate() {
        needsUpdate = true;
    }

    public boolean isConnectableSide(EnumFacing dir) {
        return MOMathHelper.getBoolean(connections, dir.ordinal());
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1));
    }
}
