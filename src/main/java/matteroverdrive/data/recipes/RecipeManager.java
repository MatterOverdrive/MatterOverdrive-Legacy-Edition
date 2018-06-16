/*
 * This file is part of Matter Overdrive
 * Copyright (C) 2018, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * Matter Overdrive is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Matter Overdrive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Matter Overdrive.  If not, see <http://www.gnu.org/licenses>.
 */
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

    public void load(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList nodes = document.getElementsByTagName("recipe");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node instanceof Element) {
                    Element e = (Element) node;
                    R recipe = recipeClass.newInstance();
                    recipe.fromXML(e);
                    register(recipe);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                .anyMatch(s -> s.getItem() == stack.getItem() && s.getItemDamage() == stack.getItemDamage());
    }

    public List<R> getRecipes() {
        return ImmutableList.copyOf(recipes);
    }

}
