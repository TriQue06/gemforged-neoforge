package net.trique.gemforged.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.trique.gemforged.item.GemforgedItems;

public final class GemforgedLootTableModifiers {
    private GemforgedLootTableModifiers() {}

    private static final ResourceLocation WOODLAND_MANSION = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/woodland_mansion");
    private static final ResourceLocation PILLAGER_OUTPOST = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/pillager_outpost");
    private static final ResourceLocation DESERT_PYRAMID   = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/desert_pyramid");
    private static final ResourceLocation JUNGLE_TEMPLE    = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/jungle_temple");
    private static final ResourceLocation NETHER_BRIDGE    = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/nether_bridge");
    private static final ResourceLocation ANCIENT_CITY     = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/ancient_city");

    public static void register() {
        NeoForge.EVENT_BUS.addListener(GemforgedLootTableModifiers::onLootTableLoad);
    }

    private static LootPool makePoolWithChance(LootPoolEntryContainer.Builder<?> entryBuilder, float chance) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(entryBuilder.when(LootItemRandomChanceCondition.randomChance(chance)))
                .build();
    }

    private static void onLootTableLoad(final LootTableLoadEvent event) {
        final ResourceLocation id = event.getName();
        if (id == null) return;

        if (id.equals(WOODLAND_MANSION)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.ONYX.get()), 0.25f)
            );
        } else if (id.equals(PILLAGER_OUTPOST)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.GARNET.get()), 0.20f)
            );
        } else if (id.equals(DESERT_PYRAMID)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.CITRINE.get()), 0.10f)
            );
        } else if (id.equals(JUNGLE_TEMPLE)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.MALACHITE.get()), 0.20f)
            );
        } else if (id.equals(NETHER_BRIDGE)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.CARNELIAN.get()), 0.10f)
            );
        } else if (id.equals(ANCIENT_CITY)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.ZIRCON.get()), 0.10f)
            );
        }
    }
}