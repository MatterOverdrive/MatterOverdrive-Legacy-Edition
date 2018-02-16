package matteroverdrive.data.recipes;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author shadowfacts
 */
public class RecipeManager<M, R extends Recipe<M>> {

    protected final Class<R> recipeClass;
    protected final List<R> recipes = new ArrayList<>();

    public RecipeManager(Class<R> recipeClass) {
        this.recipeClass = recipeClass;
    }

    public void register(R recipe) {
        recipes.add(recipe);
    }

    public Optional<R> get(M machine) {
        return recipes.stream()
                .filter(r -> r.matches(machine))
                .findFirst();
    }

    public boolean isInput(ItemStack stack) {
        return recipes.stream()
                .flatMap(r -> r.getInputs().stream())
                .anyMatch(s -> s.apply(stack));
    }

    public List<R> getRecipes() {
        return ImmutableList.copyOf(recipes);
    }

}