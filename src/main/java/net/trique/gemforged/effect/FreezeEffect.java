package net.trique.gemforged.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FreezeEffect extends MobEffect {

    public FreezeEffect() {
        super(MobEffectCategory.HARMFUL, 0x7FD9FF); // buz mavisi renk
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        int current = entity.getTicksFrozen();
        int max = entity.getTicksRequiredToFreeze();

        // her tik sabit bir miktar ekle (ör. 2 + amplifier)
        int increment = 2 + amplifier;
        int next = Math.min(current + increment, max);

        entity.setTicksFrozen(next);
        entity.setIsInPowderSnow(true); // çözülme mekanizmasını bastır

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true; // her tik uygula
    }
}
