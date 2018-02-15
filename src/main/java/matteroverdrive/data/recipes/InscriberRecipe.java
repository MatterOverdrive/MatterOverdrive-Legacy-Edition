package matteroverdrive.data.recipes;

import com.google.common.collect.ImmutableList;
import matteroverdrive.tile.TileEntityInscriber;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * @author shadowfacts
 */
public class InscriberRecipe extends Recipe<TileEntityInscriber> {

    private ItemStack main;
    private ItemStack sec;
    private ItemStack output;
    private int energy;
    private int time;

    public InscriberRecipe() {
    }

    public ItemStack getMain() {
        return main;
    }

    public ItemStack getSec() {
        return sec;
    }

    public ItemStack getOutput() {
        return output;
    }

    public int getEnergy() {
        return energy;
    }

    public int getTime() {
        return time;
    }

    @Override
    public boolean matches(TileEntityInscriber machine) {
        ItemStack primary = machine.getStackInSlot(TileEntityInscriber.MAIN_INPUT_SLOT_ID);
        ItemStack secondary = machine.getStackInSlot(TileEntityInscriber.SEC_INPUT_SLOT_ID);
        return ItemStack.areItemsEqual(primary, this.main) &&
                ItemStack.areItemsEqual(secondary, this.sec);
    }

    public ItemStack getOutput(TileEntityInscriber machine) {
        return output.copy();
    }

    @Override
    public List<ItemStack> getInputs() {
        return ImmutableList.of(main, sec);
    }

    @Override
    public void fromXML(Element element) {
        energy = getIntAttr(element, "energy");
        time = getIntAttr(element, "time");

        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                switch (node.getNodeName()) {
                    case "primary":
                        main = getStack((Element) node);
                        break;
                    case "secondary":
                        sec = getStack((Element) node);
                        break;
                    case "output":
                        output = getStack((Element) node);
                        break;
                }
            }
        }
    }

}
