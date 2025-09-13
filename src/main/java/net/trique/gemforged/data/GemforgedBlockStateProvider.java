package net.trique.gemforged.data;

import net.trique.gemforged.Gemforged;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class GemforgedBlockStateProvider extends BlockStateProvider {

    public GemforgedBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Gemforged.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}