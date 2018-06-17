package matteroverdrive.util;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StackUtils {
    public static boolean isNullOrEmpty(@Nullable ItemStack stack) {
        return stack == null || stack.isEmpty();
    }

    @Nonnull
    public static ItemStack nullToEmpty(@Nullable ItemStack stack) {
        return stack == null ? ItemStack.EMPTY : stack;
    }

    @Nullable
    public static ItemStack emptyToNull(@Nullable ItemStack stack) {
        return isNullOrEmpty(stack) ? null : stack;
    }
}
