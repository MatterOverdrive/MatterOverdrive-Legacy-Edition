package matteroverdrive.network.packet.server;

import io.netty.buffer.ByteBuf;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.api.android.IBioticStat;
import matteroverdrive.entity.android_player.AndroidPlayer;
import matteroverdrive.entity.player.MOPlayerCapabilityProvider;
import matteroverdrive.network.packet.PacketAbstract;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by Simeon on 1/1/2016.
 */
public class PacketBioticActionKey extends PacketAbstract {
    public PacketBioticActionKey() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class ServerHandler extends AbstractServerPacketHandler<PacketBioticActionKey> {
        @Override
        public void handleServerMessage(EntityPlayerMP player, PacketBioticActionKey message, MessageContext ctx) {
            AndroidPlayer androidPlayer = MOPlayerCapabilityProvider.GetAndroidCapability(player);
            if (androidPlayer.isAndroid()) {
                for (IBioticStat stat : MatterOverdrive.STAT_REGISTRY.getStats()) {
                    int unlockedLevel = androidPlayer.getUnlockedLevel(stat);
                    if (unlockedLevel > 0 && stat.isEnabled(androidPlayer, unlockedLevel)) {
                        stat.onActionKeyPress(androidPlayer, unlockedLevel, true);
                    }
                }
            }
        }
    }
}
