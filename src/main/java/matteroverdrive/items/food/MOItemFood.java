package matteroverdrive.items.food;

import com.astro.clib.api.render.ItemModelProvider;
import com.astro.clib.client.ClientUtil;
import matteroverdrive.MatterOverdrive;
import matteroverdrive.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;

/**
 * @author shadowfacts
 */
public class MOItemFood extends ItemFood implements ItemModelProvider {

    private String name;

    public MOItemFood(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        this.name = name;

        setUnlocalizedName(Reference.MOD_ID+"."+name);
        setRegistryName(name);

        setCreativeTab(MatterOverdrive.TAB_OVERDRIVE_FOOD);
    }

    @Override
    public void initItemModel() {
        if (!getHasSubtypes())
            ClientUtil.registerModel(this, getRegistryName().toString());
        else {
            NonNullList<ItemStack> sub = NonNullList.create();
            getSubItems(CreativeTabs.SEARCH, sub);
            for (ItemStack stack : sub) {
                ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getMetadata(), new ModelResourceLocation(getRegistryName(), "inventory"));
            }
        }
    }

}
