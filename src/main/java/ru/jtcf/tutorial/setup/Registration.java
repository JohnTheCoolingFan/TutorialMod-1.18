package ru.jtcf.tutorial.setup;

import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import ru.jtcf.tutorial.TutorialMod;

// Some otehr tutorials show registries being members of separate general registry class that gets passed modEventBus
// to register, eliminating the need to call a dummy register method to load the classes and avoid a runtime error.
public class Registration {
    public final static DeferredRegister<Block> BLOCKS = createDeferredRegister(ForgeRegistries.BLOCKS);
    public final static DeferredRegister<MenuType<?>> CONTAINERS = createDeferredRegister(ForgeRegistries.CONTAINERS);
    public final static DeferredRegister<Item> ITEMS = createDeferredRegister(ForgeRegistries.ITEMS);
    public final static DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = createDeferredRegister(ForgeRegistries.RECIPE_SERIALIZERS);
    public final static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = createDeferredRegister(ForgeRegistries.BLOCK_ENTITIES);
    public final static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, TutorialMod.MODID);

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        CONTAINERS.register(modEventBus);
        ITEMS.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        RECIPE_TYPES.register(modEventBus);

        ModBlocks.register();
        ModContainerTypes.register();
        ModItems.register();
        ModRecipes.register();
        ModRecipes.Types.register();
        ModRecipes.Serializers.register();
        ModBlockEntityTypes.register();
    }

    private static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> createDeferredRegister(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, TutorialMod.MODID);
    }

    @Mod.EventBusSubscriber(modid = TutorialMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Client {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ModContainerTypes.registerScreens(event);
        }
    }
}
