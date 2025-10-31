package net.trique.gemforged.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class GlacialFistEffect extends MobEffect {
    private static final String MODID = "gemforged";

    private static final ResourceLocation KNOCKBACK_ID =
            ResourceLocation.fromNamespaceAndPath(MODID, "glacial_fist_knockback");

    private static final double BASE_KNOCKBACK_ADD = 16.00D;

    public GlacialFistEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x7ED6FF);
        addAttributeModifier(
                Attributes.ATTACK_KNOCKBACK, KNOCKBACK_ID, BASE_KNOCKBACK_ADD, AttributeModifier.Operation.ADD_VALUE
        );
    }
}