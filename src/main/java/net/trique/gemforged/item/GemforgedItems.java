package net.trique.gemforged.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.item.custom.ShimmerBottleItem;
import net.trique.gemforged.item.gear.*;

public class GemforgedItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gemforged.MODID);

    public static final DeferredItem<Item> ONYX = ITEMS.register("onyx",
             () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GARNET = ITEMS.register("garnet",
             () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TOPAZ = ITEMS.register("topaz",
             () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> MALACHITE = ITEMS.register("malachite",
             () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> SAPPHIRE = ITEMS.register("sapphire",
             () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BATTLE_CHARM = ITEMS.register("battle_charm",
             () -> new BattleCharmItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SANDBURST_STAFF = ITEMS.register("sandburst_staff",
             () -> new SandburstStaffItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SHADOWSTEP_DAGGER = ITEMS.register("shadowstep_dagger", () -> {
        ItemAttributeModifiers.Builder attrs = ItemAttributeModifiers.builder();
        attrs.add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(
                        ResourceLocation.parse(Gemforged.MODID + ":onyx_dagger_damage"),
                        5.0, AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );
        attrs.add(
                Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        ResourceLocation.parse(Gemforged.MODID + ":onyx_dagger_speed"),
                        -2.0, AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );

        return new ShadowstepDaggerItem(
                Tiers.DIAMOND,
                new Item.Properties()
                        .stacksTo(1)
                        .rarity(Rarity.EPIC)
                        .attributes(attrs.build())
        );
    });

    public static final DeferredItem<Item> VENOMFANG_BLADE = ITEMS.register("venomfang_blade",
            () -> new VenomfangBladeItem(new Item.Properties()));

    public static final DeferredItem<Item> FROSTBINDER_WAND = ITEMS.register("frostbinder_wand",
            () -> new FrostbinderWandItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SHIMMER_BOTTLE =
            ITEMS.register("shimmer_bottle",
                    () -> new ShimmerBottleItem(new Item.Properties().stacksTo(16).food(Foods.GOLDEN_APPLE)));

    public static final DeferredItem<Item> SHIMMER_POWDER = ITEMS.register("shimmer_powder",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}