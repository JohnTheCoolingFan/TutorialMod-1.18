package ru.jtcf.tutorial.crafting.recipe;

import com.google.gson.JsonObject;
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
import ru.jtcf.tutorial.block.metalpress.MetalPressBlockEntity;
import ru.jtcf.tutorial.setup.ModRecipes;

// Usually custom recipes should just implement Recipe<?> interface, but extending SingleItemRecipe allows using
// some tools thatt are already there to generate a recipe json (see data package) as well as some boilerplate.
// Although, with this recipe type I had to add my own property (ingredient count), so I had to make my own recipe
// data builder as well, which mostly copies SingleItemRecipeBuilder.
public class PressingRecipe extends SingleItemRecipe {
	private final int ingredient_count;
	private final int energyRequired;

	public PressingRecipe(ResourceLocation recipeId,
						  Ingredient ingredient,
						  int ingredient_count,
						  ItemStack result,
						  int energyRequired) {
		super(ModRecipes.Types.PRESSING.get(), ModRecipes.Serializers.PRESSING.get(), recipeId, "", ingredient, result);
		this.ingredient_count = ingredient_count;
		this.energyRequired = energyRequired;
	}

	@Override
	public boolean matches(Container inv, Level world) {
		if (inv instanceof MetalPressBlockEntity metal_press) {
			return this.ingredient.test(metal_press.getItem(0)) &&
					metal_press.getItem(0).getCount() >= ingredient_count &&
					metal_press.getEnergyStored() >= energyRequired;
		}
		return false;
	}

	public int getIngredientCount() {
		return ingredient_count;
	}

	public int getEnergyRequired() {
		return energyRequired;
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PressingRecipe> {

		@Override
		public PressingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
			int ingredient_count = json.get("ingredient").getAsJsonObject().get("count").getAsInt();
			ResourceLocation itemId = new ResourceLocation(GsonHelper.getAsString(json, "result"));
			int count = GsonHelper.getAsInt(json, "count", 1);
			int energyRequired = GsonHelper.getAsInt(json, "energy_required", 100);

			ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);
			return new PressingRecipe(recipeId, ingredient, ingredient_count, result, energyRequired);
		}

		@Override
		public PressingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			int ingredient_count = buffer.readInt();
			ItemStack result = buffer.readItem();
			int energyRequired = buffer.readInt();
			return new PressingRecipe(recipeId, ingredient, ingredient_count, result, energyRequired);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, PressingRecipe recipe) {
			recipe.ingredient.toNetwork(buffer);
			buffer.writeInt(recipe.ingredient_count);
			buffer.writeItem(recipe.result);
			buffer.writeInt(recipe.energyRequired);
		}

	}
}
