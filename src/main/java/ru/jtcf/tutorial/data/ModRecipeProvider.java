package ru.jtcf.tutorial.data;

import net.minecraft.world.item.ItemStack;
import ru.jtcf.tutorial.TutorialMod;
import ru.jtcf.tutorial.crafting.recipe.PressingRecipe;
import ru.jtcf.tutorial.crafting.recipe.PressingRecipeBuilder;
import ru.jtcf.tutorial.setup.ModBlocks;
import ru.jtcf.tutorial.setup.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import ru.jtcf.tutorial.setup.ModRecipes;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

	public ModRecipeProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(ModItems.SILVER_INGOT.get(), 9)
            .requires(ModBlocks.SILVER_BLOCK.get())
            .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
            .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.SILVER_BLOCK.get())
            .define('#', ModItems.SILVER_INGOT.get())
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
            .save(consumer);

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.SILVER_ORE.get()), ModItems.SILVER_INGOT.get(), 0.7F, 200)
            .unlockedBy("has_item", has(ModBlocks.SILVER_ORE.get()))
            .save(consumer, modId("silver_ingot_smelting"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModBlocks.SILVER_ORE.get()), ModItems.SILVER_INGOT.get(), 0.7F, 100)
            .unlockedBy("has_item", has(ModBlocks.SILVER_ORE.get()))
            .save(consumer, modId("silver_ingot_blasting"));

        new PressingRecipeBuilder(Ingredient.of(new ItemStack(ModItems.SILVER_INGOT.get())), 9, ModBlocks.SILVER_BLOCK.get(), 1)
                .unlockedBy("has_item", has(ModItems.SILVER_INGOT.get()))
                .save(consumer, modId("silver_block_pressing"));
    }

	private ResourceLocation modId(String path) {
		return new ResourceLocation(TutorialMod.MODID, path);
	}
}
