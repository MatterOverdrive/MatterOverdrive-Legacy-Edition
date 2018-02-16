package matteroverdrive;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;

public class OverdriveTab extends CreativeTabs {
    private ItemStack itemstack = ItemStack.EMPTY;
    private Callable<ItemStack> stackCallable;

    public OverdriveTab(String label, Callable<ItemStack> stackCallable) {
        super(label);
        this.stackCallable = stackCallable;
    }

    @Override
    @Nonnull
    public ItemStack getTabIconItem() {
        if (stackCallable != null && itemstack.isEmpty()) {
            try {
                itemstack = stackCallable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (itemstack == null || itemstack.isEmpty())
            return new ItemStack(MatterOverdrive.ITEMS.matter_scanner);
        return itemstack;
    }
}