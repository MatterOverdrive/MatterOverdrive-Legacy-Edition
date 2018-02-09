package matteroverdrive;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class OverdriveTab extends CreativeTabs {
    ItemStack itemstack = ItemStack.EMPTY;

    public OverdriveTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem() {
        if (itemstack == null || itemstack.isEmpty())
            return new ItemStack(MatterOverdrive.ITEMS.matter_scanner);
        return itemstack;
    }
}
