package ru.jtcf.tutorial.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import ru.jtcf.tutorial.setup.ModRecipes;

import java.util.function.Consumer;

public class PressingRecipeBuilder implements RecipeBuilder {
    private final Ingredient ingredient;
    private final int ingredient_count;
    private final Item result;
    private final int result_count;

    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private String group;

    private static final RecipeSerializer<?> TYPE = ModRecipes.Serializers.PRESSING.get();

    public PressingRecipeBuilder(Ingredient ingredient, int ingredient_count, ItemLike result, int result_count) {
        this.ingredient = ingredient;
        this.ingredient_count = ingredient_count;
        this.result = result.asItem();
        this.result_count = result_count;
    }
    @Override
    public RecipeBuilder unlockedBy(String crit_name, CriterionTriggerInstance crit) {
        this.advancement.addCriterion(crit_name, crit);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }
    private void ensureValid(ResourceLocation p_126330_) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + p_126330_);
        }
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipe_id) {
        ensureValid(recipe_id);
        this.advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipe_id)).rewards(AdvancementRewards.Builder.recipe(recipe_id)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(recipe_id, this.group == null ? "" : this.group, this.ingredient, this.ingredient_count, this.result, this.result_count, this.advancement, new ResourceLocation(recipe_id.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + recipe_id.getPath())));
    }

    private static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final int ingredient_count;
        private final Item result;
        private final int result_count;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation id, String group, Ingredient ingredient, int ingredient_count, Item result, int result_count, Advancement.Builder advancement, ResourceLocation advancement_id) {
            this.id =  id;
            this.group = group;
            this.ingredient = ingredient;
            this.ingredient_count = ingredient_count;
            this.result = result;
            this.result_count = result_count;
            this.advancement = advancement;
            this.advancementId = advancement_id;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            JsonObject ingredient_json = this.ingredient.toJson().getAsJsonObject();
            ingredient_json.addProperty("count", this.ingredient_count);
            jsonObject.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
            jsonObject.addProperty("count", this.result_count);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return TYPE;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}
