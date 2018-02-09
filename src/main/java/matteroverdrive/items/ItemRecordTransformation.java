package matteroverdrive.items;

import com.astro.clib.api.render.ItemModelProvider;
import com.astro.clib.client.ClientUtil;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import matteroverdrive.init.MatterOverdriveSounds;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * @author shadowfacts
 */
public class ItemRecordTransformation extends ItemRecord implements ItemModelProvider {

    private static final ResourceLocation SOUND = new ResourceLocation(Reference.MOD_ID, "transformation_music");

    public ItemRecordTransformation() {
        super("mo.transformation", MatterOverdriveSounds.musicTransformation);
        setUnlocalizedName("record");
        setRegistryName("record_transformation");
        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE);
    }

    @Override
    public SoundEvent getSound() {
        return super.getSound();
    }

    @Override
    public void initItemModel() {
        ClientUtil.registerModel(this, this.getRegistryName().toString());
    }

}
