package matteroverdrive.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class AABBUtils {
    public static AxisAlignedBB rotateFace(AxisAlignedBB box, EnumFacing side) {
        switch (side) {
            case DOWN:
            default:
                return box;
            case UP:
                return new AxisAlignedBB(box.minX, 1 - box.maxY, box.minZ, box.maxX, 1 - box.minY, box.maxZ);
            case NORTH:
                return new AxisAlignedBB(box.minX, box.minZ, box.minY, box.maxX, box.maxZ, box.maxY);
            case SOUTH:
                return new AxisAlignedBB(box.minX, box.minZ, 1 - box.maxY, box.maxX, box.maxZ, 1 - box.minY);
            case WEST:
                return new AxisAlignedBB(box.minY, box.minZ, box.minX, box.maxY, box.maxZ, box.maxX);
            case EAST:
                return new AxisAlignedBB(1 - box.maxY, box.minZ, box.minX, 1 - box.minY, box.maxZ, box.maxX);
        }
    }
}