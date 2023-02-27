package ru.jtcf.tutorial.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.jtcf.tutorial.TutorialMod;
import ru.jtcf.tutorial.block.metalpress.MetalPressScreen;
import ru.jtcf.tutorial.setup.ModBlocks;

// This is messy since I just wanted jsut to make it work. In the end, I'm quite satisfied with the result, although
// many things were just copied from vanilla plugin
@JeiPlugin
public class TutorialModJeiPlugin implements IModPlugin {

    public TutorialModJeiPlugin() {
    }

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

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // The area is set to a bounding box of an arrow. I don't know for sure what size around the arrow it
        // should be, but definitely bigger, judging by the vanilla crafting table click area size
        registration.addRecipeClickArea(MetalPressScreen.class, 80, 35, 22, 15, PressingCategory.TYPE);
    }
}
