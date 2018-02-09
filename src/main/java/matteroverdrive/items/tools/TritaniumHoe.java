package matteroverdrive.items.tools;

import com.astro.clib.client.ClientUtil;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.item.ItemHoe;
import net.minecraft.util.ResourceLocation;
import com.astro.clib.api.render.ItemModelProvider;

/**
 * @author shadowfacts
 */
public class TritaniumHoe extends ItemHoe implements ItemModelProvider {

    public TritaniumHoe(String name) {
        super(MatterOverdriveItems.TOOL_MATERIAL_TRITANIUM);
        setUnlocalizedName(name);
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
    }

    @Override
    public void initItemModel() {
        ClientUtil.registerModel(this, this.getRegistryName().toString());
    }

}
