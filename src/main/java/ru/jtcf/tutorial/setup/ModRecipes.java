package ru.jtcf.tutorial.setup;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;
import ru.jtcf.tutorial.crafting.recipe.PressingRecipe;

public class ModRecipes {
    public ModRecipes() {
    }

    static void register() {
    }

    public static class Types {
        public static final RegistryObject<RecipeType<PressingRecipe>> PRESSING = Registration.RECIPE_TYPES.register("pressing", () -> new RecipeType<>() {
        });

        public Types() {
        }

        static void register() {
        }
    }

    public static class Serializers {
        public static final RegistryObject<RecipeSerializer<?>> PRESSING = Registration.RECIPE_SERIALIZERS.register("pressing",
                PressingRecipe.Serializer::new);

        static void register() {
        }
    }
}
