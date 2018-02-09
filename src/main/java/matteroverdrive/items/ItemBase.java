package matteroverdrive.items;

import com.astro.clib.api.render.ItemModelProvider;
import matteroverdrive.MatterOverdrive;
import net.minecraft.item.Item;

public class ItemBase extends Item implements ItemModelProvider {
    protected final String name;

    public ItemBase(String name) {
        this.name = name;
        this.setRegistryName(name);
        this.setUnlocalizedName(getRegistryName().toString().replace(':', '.'));
    }

    @Override
    public void initItemModel() {
        MatterOverdrive.PROXY.registerItemModel(this, 0, this.getRegistryName());
    }
}
