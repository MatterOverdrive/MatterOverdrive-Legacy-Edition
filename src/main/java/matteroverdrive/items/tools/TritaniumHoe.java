package matteroverdrive.items.tools;

import matteroverdrive.api.internal.ItemModelProvider;
import matteroverdrive.client.ClientUtil;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveItems;
import net.minecraft.item.ItemHoe;
import net.minecraft.util.ResourceLocation;

/**
 * @author shadowfacts
 */
public class TritaniumHoe extends ItemHoe implements ItemModelProvider {

    public TritaniumHoe(String name) {
        super(MatterOverdriveItems.TOOL_MATERIAL_TRITANIUM);
        setUnlocalizedName(Reference.MOD_ID + "." + name);
        setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
    }

    @Override
    public void initItemModel() {
        ClientUtil.registerModel(this, this.getRegistryName().toString());
    }

}
