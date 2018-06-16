package matteroverdrive.tile;

import matteroverdrive.util.TileUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

public class PacketDispatcher {
    public static void dispatchTEToNearbyPlayers(@Nonnull TileEntity tile) {
        if (tile.getWorld() instanceof WorldServer) {
            WorldServer ws = ((WorldServer) tile.getWorld());
            SPacketUpdateTileEntity packet = tile.getUpdatePacket();

            if (packet == null)
                return;

            for (EntityPlayer player : ws.playerEntities) {
                if (!(player instanceof EntityPlayerMP))
                    continue;
                EntityPlayerMP playerMP = ((EntityPlayerMP) player);

                if (playerMP.getDistanceSq(tile.getPos()) < 64 * 64 && ws.getPlayerChunkMap().isPlayerWatchingChunk(playerMP, tile.getPos().getX() >> 4, tile.getPos().getZ() >> 4)) {
                    playerMP.connection.sendPacket(packet);
                }
            }
        }
    }

    public static void dispatchTEToNearbyPlayers(@Nonnull World world,@Nonnull BlockPos pos) {
        TileUtils.getTileEntity(world,pos,TileEntity.class).ifPresent(PacketDispatcher::dispatchTEToNearbyPlayers);
    }
}