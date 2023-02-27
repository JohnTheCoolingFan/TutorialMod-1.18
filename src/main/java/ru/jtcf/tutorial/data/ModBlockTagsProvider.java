package ru.jtcf.tutorial.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import ru.jtcf.tutorial.TutorialMod;
import ru.jtcf.tutorial.setup.ModBlocks;
import ru.jtcf.tutorial.setup.ModTags;

public class ModBlockTagsProvider extends BlockTagsProvider {

	public ModBlockTagsProvider(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
		super(generatorIn, TutorialMod.MODID, existingFileHelper);
	}

    @Override
    protected void addTags() {
        tag(ModTags.Blocks.ORES_SILVER).add(ModBlocks.SILVER_ORE.get());
        // Yes, now in 1.18 and up which tools mines and what grade itt need to be is set via tags.
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.SILVER_ORE.get());
        tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.SILVER_ORE.get());
        tag(Tags.Blocks.ORES).addTag(ModTags.Blocks.ORES_SILVER);
        tag(ModTags.Blocks.STORAGE_BLOCKS_SILVER).add(ModBlocks.SILVER_BLOCK.get());
        tag(Tags.Blocks.STORAGE_BLOCKS).addTag(ModTags.Blocks.ORES_SILVER);
    }
}
