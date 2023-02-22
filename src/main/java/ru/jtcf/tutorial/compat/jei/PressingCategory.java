package ru.jtcf.tutorial.compat.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ru.jtcf.tutorial.TutorialMod;
import ru.jtcf.tutorial.crafting.recipe.PressingRecipe;
import ru.jtcf.tutorial.setup.ModBlocks;

public class PressingCategory implements IRecipeCategory<PressingRecipe> {
    private final IDrawable background;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final TranslatableComponent localizedName;
    private final IDrawable icon;
    public static final RecipeType<PressingRecipe> TYPE = RecipeType.create(TutorialMod.MODID, "pressing", PressingRecipe.class);

    public PressingCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(Constants.RECIPE_GUI_PRESSING, 0, 0, 82, 34);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.METAL_PRESS.get()));
        this.localizedName = new TranslatableComponent("gui.tutorial.category.pressing");
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(Constants.RECIPE_GUI_PRESSING, 82, 0, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return getRecipeType().getUid();
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends PressingRecipe> getRecipeClass() {
        return PressingRecipe.class;
    }

    @Override
    public RecipeType<PressingRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PressingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addItemStack(recipe.getResultItem());
    }

    protected IDrawableAnimated getArrow(PressingRecipe recipe) {
        int cookTime = 200;
        return cachedArrows.getUnchecked(cookTime);
    }

    @Override
    public void draw(PressingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack poseStack, double mouseX, double mouseY) {
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(poseStack, 24, 9);
    }
}
