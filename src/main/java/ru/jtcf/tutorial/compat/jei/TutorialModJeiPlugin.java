package ru.jtcf.tutorial.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.jtcf.tutorial.TutorialMod;
import ru.jtcf.tutorial.setup.ModBlocks;

@JeiPlugin
public class TutorialModJeiPlugin implements IModPlugin {

    public TutorialModJeiPlugin() {}

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(TutorialMod.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(new PressingCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ModRecipesList mod_recipes = new ModRecipesList();
        registration.addRecipes(PressingCategory.TYPE, mod_recipes.getPressingRecipes());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.METAL_PRESS.get()), PressingCategory.TYPE);
    }
}
