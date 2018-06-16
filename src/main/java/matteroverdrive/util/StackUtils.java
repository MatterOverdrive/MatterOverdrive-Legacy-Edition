package matteroverdrive.util;

import net.minecraft.item.ItemStack;

public class StackUtils {
    public static boolean isNullOrEmpty(ItemStack stack) {
        return stack == null || stack.isEmpty();
    }
}
