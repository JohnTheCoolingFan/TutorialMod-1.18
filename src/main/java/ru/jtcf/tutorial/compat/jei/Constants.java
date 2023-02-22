package ru.jtcf.tutorial.compat.jei;

import mezz.jei.api.constants.ModIds;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.resources.ResourceLocation;
import ru.jtcf.tutorial.TutorialMod;

public final class Constants {
    public static final String TEXTURE_GUI_PATH = "textures/gui/";
    public static final String TEXTURE_GUI_VANILLA = Constants.TEXTURE_GUI_PATH + "gui_vanilla.png";
    public static final String TEXTURE_GUI_PRESSING = Constants.TEXTURE_GUI_PATH + "metal_press_jei.png";

    public static final ResourceLocation RECIPE_GUI_VANILLA = new ResourceLocation(ModIds.JEI_ID, TEXTURE_GUI_VANILLA);

    public static final ResourceLocation RECIPE_GUI_PRESSING = new ResourceLocation(TutorialMod.MODID, TEXTURE_GUI_PRESSING);

    public static final RecipeType<?> UNIVERSAL_RECIPE_TRANSFER_TYPE = RecipeType.create(ModIds.JEI_ID, "universal_recipe_transfer_handler", Object.class);
    public static final ResourceLocation LOCATION_JEI_GUI_TEXTURE_ATLAS = new ResourceLocation(ModIds.JEI_ID, "textures/atlas/gui.png");

    private Constants() {

    }
}
