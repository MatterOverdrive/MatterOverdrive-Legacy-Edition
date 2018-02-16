package matteroverdrive.data.recipes;

import com.astro.clib.recipe.XMLRecipe;
import com.google.common.collect.ImmutableList;
import matteroverdrive.tile.TileEntityInscriber;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;

public class InscriberRecipe extends Recipe<TileEntityInscriber> implements XMLRecipe<InscriberRecipe,InscriberRecipe> {
    public static final ResourceLocation TYPE = new ResourceLocation("matteroverdrive:inscriber");

    private Ingredient main;
    private Ingredient sec;
    private ItemStack output;
    private int energy;
    private int time;

    public InscriberRecipe() {
    }

    public Ingredient getMain() {
        return main;
    }

    public Ingredient getSec() {
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
    public InscriberRecipe build() {
        return this;
    }

    @Override
    public ResourceLocation getType() {
        return TYPE;
    }

    @Override
    public boolean matches(TileEntityInscriber machine) {
        ItemStack primary = machine.getStackInSlot(TileEntityInscriber.MAIN_INPUT_SLOT_ID);
        ItemStack secondary = machine.getStackInSlot(TileEntityInscriber.SEC_INPUT_SLOT_ID);
        return this.main.apply(primary) && this.sec.apply(secondary);
    }

    public ItemStack getOutput(TileEntityInscriber machine) {
        return output.copy();
    }

    @Override
    public List<Ingredient> getInputs() {
        return ImmutableList.of(main, sec);
    }

    @Override
    public InscriberRecipe fromXML(Element element) {
        energy = Integer.parseInt(XMLRecipe.getElement(element, "energy").getFirstChild().getTextContent());
        time = Integer.parseInt(XMLRecipe.getElement(element, "time").getFirstChild().getTextContent());

        NodeList nodes = XMLRecipe.getElement(element, "ingredients").getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                switch (XMLRecipe.getAttr((Element)node,"key")) {
                    case "primary":
                        main = XMLRecipe.getIngredient((Element) node);
                        break;
                    case "secondary":
                        sec = XMLRecipe.getIngredient((Element) node);
                        break;
                }
            }
        }
        this.output = XMLRecipe.getStack(XMLRecipe.getElement(element, "result"));

        return this;
    }
}
