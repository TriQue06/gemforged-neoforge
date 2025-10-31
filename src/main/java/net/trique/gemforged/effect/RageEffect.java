package net.trique.gemforged.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RageEffect extends MobEffect {
    private static final String MODID = "gemforged";

    private static final ResourceLocation MOVE_ID  =
            ResourceLocation.fromNamespaceAndPath(MODID, "rage_move");
    private static final ResourceLocation ATKSPD_ID =
            ResourceLocation.fromNamespaceAndPath(MODID, "rage_attack_speed");
    private static final ResourceLocation ATKDAM_ID =
            ResourceLocation.fromNamespaceAndPath(MODID, "rage_attack_damage");

    private static final double BASE_MOVE_MULT = 1.00D;
    private static final double BASE_ATKDAM_MULT = 1.00D;
    private static final double BASE_ATKSPD_MULT = 1.00D;

    public RageEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xB80E2A);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVE_ID, BASE_MOVE_MULT, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.ATTACK_SPEED, ATKSPD_ID, BASE_ATKSPD_MULT, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, ATKDAM_ID, BASE_ATKDAM_MULT, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}