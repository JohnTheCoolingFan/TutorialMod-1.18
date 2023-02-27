package ru.jtcf.tutorial.setup;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.RegistryObject;
import ru.jtcf.tutorial.block.metalpress.MetalPressContainer;
import ru.jtcf.tutorial.block.metalpress.MetalPressScreen;

public class ModContainerTypes {
    public static final RegistryObject<MenuType<MetalPressContainer>> METAL_PRESS = register("metal_press",
            MetalPressContainer::new);

    private ModContainerTypes() {
    }

    static void register() {
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreens(FMLClientSetupEvent event) {
        MenuScreens.register(METAL_PRESS.get(), MetalPressScreen::new);
    }

    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(String name, IContainerFactory<T> factory) {
        return Registration.CONTAINERS.register(name, () -> IForgeMenuType.create(factory));
    }
}
