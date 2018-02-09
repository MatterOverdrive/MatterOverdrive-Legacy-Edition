package matteroverdrive.data.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.w3c.dom.Element;

import java.util.List;

/**
 * @author shadowfacts
 */
public abstract class Recipe<M> {

    public static String getAttr(Element e, String attr) {
        return e.getAttribute(attr);
    }

    public static int getIntAttr(Element e, String attr) {
        return Integer.parseInt(getAttr(e, attr));
    }

    public static ItemStack getStack(Element e) {
        String modid = getAttr(e, "modid");
        String name = getAttr(e, "name");
        int meta = getIntAttr(e, "meta");
        int count = getIntAttr(e, "count");
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid, name));
        return new ItemStack(item, count, meta);
    }

    public abstract boolean matches(M machine);

    public abstract List<ItemStack> getInputs();

    public abstract ItemStack getOutput(M machine);

    public abstract void fromXML(Element e);

}
