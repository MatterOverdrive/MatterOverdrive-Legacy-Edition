package matteroverdrive.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Optional;

public final class TileUtils {
    public static <T> Optional<T> getTileEntity(IBlockAccess world, BlockPos blockPos, Class<T> tClass) {
        return Optional.ofNullable(getNullableTileEntity(world, blockPos, tClass));
    }

    public static <T> T getNullableTileEntity(IBlockAccess world, BlockPos blockPos, Class<T> tClass) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        return tClass.isInstance(tileEntity) ? tClass.cast(tileEntity) : null;
    }

}