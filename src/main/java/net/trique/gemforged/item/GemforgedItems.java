package net.trique.gemforged.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.item.gear.OnyxDaggerItem;

public class GemforgedItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Gemforged.MODID);

    public static final DeferredItem<Item> ONYX_DAGGER = ITEMS.register("onyx_dagger", () -> {
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
                        -1.0, AttributeModifier.Operation.ADD_VALUE
                ),
                EquipmentSlotGroup.MAINHAND
        );

        return new OnyxDaggerItem(
                Tiers.DIAMOND,
                new Item.Properties()
                        .stacksTo(1)
                        .rarity(Rarity.EPIC)
                        .attributes(attrs.build()) // << burada attribute’ler eklendi
        );
    });



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}