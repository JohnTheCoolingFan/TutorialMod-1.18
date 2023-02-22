package ru.jtcf.tutorial.setup;

import ru.jtcf.tutorial.TutorialMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static final class Blocks {
        public static final TagKey<Block> ORES_SILVER = forge("ores/silver");
        public static final TagKey<Block> STORAGE_BLOCKS_SILVER = forge("storage_block/silver");

        private static TagKey<Block> forge(String path) {
            return BlockTags.create(new ResourceLocation("forge", path));
        }

        private static TagKey<Block> mod(String path) {
            return BlockTags.create(new ResourceLocation(TutorialMod.MODID, path));
        }
    }

    public static final class Items {
        public static final TagKey<Item> ORES_SILVER = forge("ores/silver");
        public static final TagKey<Item> STORAGE_BLOCKS_SILVER = forge("storage_block/silver");
        
        public static final TagKey<Item> INGOTS_SILVER = forge("ingots/silver");

        private static TagKey<Item> forge(String path) {
            return ItemTags.create(new ResourceLocation("forge", path));
        }

        private static TagKey<Item> mod(String path) {
            return ItemTags.create(new ResourceLocation(TutorialMod.MODID, path));
        }
    }
}
