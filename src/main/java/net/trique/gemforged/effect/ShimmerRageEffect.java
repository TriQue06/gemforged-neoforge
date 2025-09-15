package net.trique.gemforged.effect;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ShimmerRageEffect extends MobEffect {
    private static final String MODID = "gemforged";

    private static final ResourceLocation MOVE_ID  = ResourceLocation.fromNamespaceAndPath(MODID, "shimmer_rage_move");
    private static final ResourceLocation ATKSPD_ID= ResourceLocation.fromNamespaceAndPath(MODID, "shimmer_rage_attack_speed");
    private static final ResourceLocation ATKDAM_ID= ResourceLocation.fromNamespaceAndPath(MODID, "shimmer_rage_attack_damage");
    private static final ResourceLocation SCALE_ID = ResourceLocation.fromNamespaceAndPath(MODID, "shimmer_rage_scale");

    private static final double MOVE_MULT   = 0.80D; // Garnet'in 2x'i (0.40 x 2)
    private static final double ATKDAM_MULT = 0.80D; // 0.40 x 2
    private static final double ATKSPD_MULT = 1.40D; // 0.70 x 2
    private static final double SCALE_MULT  = 0.40D; // 0.20 x 2

    public ShimmerRageEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xB4E1FF); // parıltılı mavi-beyaz ton

        addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVE_ID,   MOVE_MULT,   AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.ATTACK_SPEED,   ATKSPD_ID, ATKSPD_MULT, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.ATTACK_DAMAGE,  ATKDAM_ID, ATKDAM_MULT, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.SCALE,          SCALE_ID,  SCALE_MULT,  AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
