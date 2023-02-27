package ru.jtcf.tutorial.compat.jei;

import net.minecraft.resources.ResourceLocation;
import ru.jtcf.tutorial.TutorialMod;

// Just some constants, pulled from vanilla plugin, adjusted to the mod and removed unused stuff
public final class Constants {
    public static final String TEXTURE_GUI_PATH = "textures/gui/";
    public static final String TEXTURE_GUI_PRESSING = Constants.TEXTURE_GUI_PATH + "metal_press_jei.png";

    public static final ResourceLocation RECIPE_GUI_PRESSING = new ResourceLocation(TutorialMod.MODID, TEXTURE_GUI_PRESSING);

    private Constants() {

    }
}
