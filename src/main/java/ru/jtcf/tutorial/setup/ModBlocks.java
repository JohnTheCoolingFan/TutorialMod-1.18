package ru.jtcf.tutorial.setup;

import ru.jtcf.tutorial.block.metalpress.MetalPressBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final RegistryObject<Block> SILVER_ORE = register("silver_ore", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE)
                .strength(3, 10)
                .requiresCorrectToolForDrops()
                .sound(SoundType.STONE)));

    public static final RegistryObject<Block> SILVER_BLOCK = register("silver_block", () ->
            new Block(BlockBehaviour.Properties.of(Material.METAL)
                .strength(3, 10)
                .sound(SoundType.METAL)));

    public static final RegistryObject<MetalPressBlock> METAL_PRESS = register("metal_press", () ->
        new MetalPressBlock(BlockBehaviour.Properties.of(Material.METAL)
                .strength(4, 20)
                .sound(SoundType.METAL)));

    static void register() {}

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return Registration.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block) {
        RegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        return ret;
    }
}
