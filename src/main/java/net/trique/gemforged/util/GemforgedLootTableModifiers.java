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

    private static final ResourceLocation ANCIENT_CITY_CHEST = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/ancient_city");
    private static final ResourceLocation BASTION_BRIDGE = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/bastion_bridge");
    private static final ResourceLocation BASTION_HOGLIN = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/bastion_hoglin_stable");
    private static final ResourceLocation BASTION_OTHER = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/bastion_other");
    private static final ResourceLocation BASTION_TREASURE = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/bastion_treasure");
    private static final ResourceLocation DESERT_PYRAMID = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/desert_pyramid");
    private static final ResourceLocation JUNGLE_TEMPLE = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/jungle_temple");
    private static final ResourceLocation END_CITY_TREASURE = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/end_city_treasure");
    private static final ResourceLocation OCEAN_RUIN_COLD_ARCHAEOLOGY = ResourceLocation.fromNamespaceAndPath("minecraft", "archaeology/ocean_ruin_cold");
    private static final ResourceLocation PILLAGER_OUTPOST = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/pillager_outpost");
    private static final ResourceLocation WOODLAND_MANSION = ResourceLocation.fromNamespaceAndPath("minecraft", "chests/woodland_mansion");

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

        if (id.equals(ANCIENT_CITY_CHEST)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.DAGGER_TEMPLATE.get()), 0.25f)
            );
        } else if (id.equals(BASTION_BRIDGE) || id.equals(BASTION_HOGLIN) || id.equals(BASTION_OTHER)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.CHARM_TEMPLATE.get()), 0.25f)
            );
        } else if (id.equals(BASTION_TREASURE)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.CHARM_TEMPLATE.get()), 0.50f)
            );
        } else if (id.equals(DESERT_PYRAMID)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.STAFF_TEMPLATE.get()), 0.10f)
            );
        } else if (id.equals(JUNGLE_TEMPLE)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.BLADE_TEMPLATE.get()), 0.25f)
            );
        } else if (id.equals(OCEAN_RUIN_COLD_ARCHAEOLOGY)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.PRISM_TEMPLATE.get()), 0.25f)
            );
        } else if (id.equals(END_CITY_TREASURE)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.HORN_TEMPLATE.get()), 0.25f)
            );
        } else if (id.equals(PILLAGER_OUTPOST)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.TOTEM_TEMPLATE.get()), 0.25f)
            );
        } else if (id.equals(WOODLAND_MANSION)) {
            event.getTable().addPool(
                    makePoolWithChance(LootItem.lootTableItem(GemforgedItems.BOW_TEMPLATE.get()), 0.50f)
            );
        }
    }
}