package ru.jtcf.tutorial.data.client;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import ru.jtcf.tutorial.TutorialMod;
import ru.jtcf.tutorial.setup.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, TutorialMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.SILVER_BLOCK.get());
        simpleBlock(ModBlocks.SILVER_ORE.get());
        horizontalBlock(ModBlocks.METAL_PRESS.get(), modLoc("block/metal_press_side"), modLoc("block/metal_press_side"), modLoc("block/metal_press_top"));
    }

}
