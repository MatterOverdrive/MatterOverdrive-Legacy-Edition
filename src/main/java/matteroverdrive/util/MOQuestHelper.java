package matteroverdrive.util;

import matteroverdrive.api.quest.QuestStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

/**
 * Created by Simeon on 2/13/2016.
 */
public class MOQuestHelper {
    public static BlockPos getPosition(QuestStack questStack) {
        if (questStack.getTagCompound() != null) {
            if (questStack.getTagCompound().hasKey("pos", Constants.NBT.TAG_LONG)) {
                return BlockPos.fromLong(questStack.getTagCompound().getLong("pos"));
            } else if (questStack.getTagCompound().hasKey("pos", Constants.NBT.TAG_INT_ARRAY) && questStack.getTagCompound().getIntArray("pos").length >= 3) {
                int[] s = questStack.getTagCompound().getIntArray("pos");
                return new BlockPos(s[0], s[1], s[2]);
            }
        }
        return null;
    }
}
