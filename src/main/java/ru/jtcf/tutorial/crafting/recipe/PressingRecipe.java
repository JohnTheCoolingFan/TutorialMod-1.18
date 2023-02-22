package ru.jtcf.tutorial.crafting.recipe;

import com.google.gson.JsonObject;
import ru.jtcf.tutorial.setup.ModRecipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PressingRecipe extends SingleItemRecipe {
	public PressingRecipe(ResourceLocation recipeId,
						  Ingredient ingredient,
						  ItemStack result) {
		super(ModRecipes.Types.PRESSING.get(), ModRecipes.Serializers.PRESSING.get(), recipeId, "", ingredient, result);
	}

	@Override
	public boolean matches(Container inv, Level world) {
		return this.ingredient.test(inv.getItem(0));
	}

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PressingRecipe> {

		@Override
        public PressingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            ResourceLocation itemId = new ResourceLocation(GsonHelper.getAsString(json, "result"));
            int count = GsonHelper.getAsInt(json, "count", 1);

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);
            return new PressingRecipe(recipeId, ingredient, result);
		}

		@Override
		public PressingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new PressingRecipe(recipeId, ingredient, result);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, PressingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
		}

    }
}
