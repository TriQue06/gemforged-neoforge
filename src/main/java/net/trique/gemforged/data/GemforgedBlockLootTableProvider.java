package net.trique.gemforged.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.item.GemforgedItems;

import java.util.Set;

public class GemforgedBlockLootTableProvider extends BlockLootSubProvider {

    public GemforgedBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(GemforgedBlocks.NYXITE_BLOCK.get());
        dropSelf(GemforgedBlocks.BLOODSTONE_BLOCK.get());
        dropSelf(GemforgedBlocks.SOLARIUM_BLOCK.get());
        dropSelf(GemforgedBlocks.VENOMYTE_BLOCK.get());
        dropSelf(GemforgedBlocks.PHOENIXTONE_BLOCK.get());
        dropSelf(GemforgedBlocks.PRISMYTE_BLOCK.get());
        dropSelf(GemforgedBlocks.GRAVITIUM_BLOCK.get());
        dropSelf(GemforgedBlocks.VERDANTITE_BLOCK.get());

        add(GemforgedBlocks.RANDOM_GEM_VEIN.get(),
                block -> createRandomOreDrop(
                        GemforgedItems.NYXITE.get(),
                        GemforgedItems.BLOODSTONE.get(),
                        GemforgedItems.SOLARIUM.get(),
                        GemforgedItems.VENOMYTE.get(),
                        GemforgedItems.PHOENIXTONE.get(),
                        GemforgedItems.PRISMYTE.get(),
                        GemforgedItems.GRAVITIUM.get(),
                        GemforgedItems.VERDANTITE.get()
                )
        );

        add(GemforgedBlocks.DEEPSLATE_RANDOM_GEM_VEIN.get(),
                block -> createRandomOreDrop(
                        GemforgedItems.NYXITE.get(),
                        GemforgedItems.BLOODSTONE.get(),
                        GemforgedItems.SOLARIUM.get(),
                        GemforgedItems.VENOMYTE.get(),
                        GemforgedItems.PHOENIXTONE.get(),
                        GemforgedItems.PRISMYTE.get(),
                        GemforgedItems.GRAVITIUM.get(),
                        GemforgedItems.VERDANTITE.get()
                )
        );
    }

    private LootTable.Builder createRandomOreDrop(Item... possibleDrops) {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1));

        for (Item item : possibleDrops) {
            pool.add(
                    LootItem.lootTableItem(item)
                            .setWeight(1)
                            .apply(ApplyBonusCount.addOreBonusCount(
                                    registries
                                            .lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                                            .getOrThrow(Enchantments.FORTUNE)
                            ))
                            .apply(ApplyExplosionDecay.explosionDecay())
            );
        }

        return LootTable.lootTable().withPool(pool);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return new Iterable<>() {
            @Override
            public java.util.Iterator<Block> iterator() {
                return GemforgedBlocks.BLOCKS.getEntries()
                        .stream()
                        .map(e -> (Block) e.get())
                        .iterator();
            }
        };
    }
}