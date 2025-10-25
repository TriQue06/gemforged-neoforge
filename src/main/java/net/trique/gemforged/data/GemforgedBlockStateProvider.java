package net.trique.gemforged.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.block.GemforgedBlocks;

public class GemforgedBlockStateProvider extends BlockStateProvider {

    public GemforgedBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Gemforged.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(GemforgedBlocks.NYXITE_BLOCK);
        blockWithItem(GemforgedBlocks.BLOODSTONE_BLOCK);
        blockWithItem(GemforgedBlocks.SOLARIUM_BLOCK);
        blockWithItem(GemforgedBlocks.VENOMYTE_BLOCK);
        blockWithItem(GemforgedBlocks.PHOENIXTONE_BLOCK);
        blockWithItem(GemforgedBlocks.PRISMYTE_BLOCK);
        blockWithItem(GemforgedBlocks.GRAVITIUM_BLOCK);
        blockWithItem(GemforgedBlocks.VERDANTITE_BLOCK);
        blockWithItem(GemforgedBlocks.RANDOM_GEM_VEIN);
        blockWithItem(GemforgedBlocks.DEEPSLATE_RANDOM_GEM_VEIN);
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}