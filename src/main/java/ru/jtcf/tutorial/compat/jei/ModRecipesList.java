package ru.jtcf.tutorial.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.RecipeManager;
import ru.jtcf.tutorial.crafting.recipe.PressingRecipe;
import ru.jtcf.tutorial.setup.ModRecipes;

import java.util.List;

public class ModRecipesList {
    private final RecipeManager recipe_manager;

    public ModRecipesList() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel world = minecraft.level;
        this.recipe_manager = world.getRecipeManager();
    }

    public List<PressingRecipe> getPressingRecipes() {
        List<PressingRecipe> recipes = recipe_manager.getAllRecipesFor(ModRecipes.Types.PRESSING.get());
        return recipes;
    }

}
