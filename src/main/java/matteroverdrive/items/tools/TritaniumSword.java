package matteroverdrive.items.tools;

import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import com.astro.clib.api.render.ItemModelProvider;

/**
 * @author shadowfacts
 */
public class TritaniumSword extends ItemSword implements ItemModelProvider {

    public TritaniumSword(String name) {
        super(MatterOverdriveItems.TOOL_MATERIAL_TRITANIUM);
        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
    }

    @Override
    public void initItemModel() {
        MatterOverdrive.PROXY.registerItemModel(this, 0, getRegistryName().getResourcePath());
    }

}
