package net.trique.gemforged.item.custom;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.trique.gemforged.effect.GemforgedEffects;

public class ShimmerBottleItem extends Item {
    private static final int DRINK_DURATION = 40;
    private static final int SHIMMER_DURATION_TICKS = 20 * 30; // 30s
    private static final int SHIMMER_AMP = 0;

    public ShimmerBottleItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // vanilla pattern
        super.finishUsingItem(stack, level, entity);

        if (entity instanceof ServerPlayer sp) {
            CriteriaTriggers.CONSUME_ITEM.trigger(sp, stack);
            sp.awardStat(Stats.ITEM_USED.get(this));
        }

        if (!level.isClientSide) {
            // 1) Sadece Shimmer Rage uygulansın
            Holder<MobEffect> shimmer = level.registryAccess()
                    .lookupOrThrow(Registries.MOB_EFFECT)
                    .getOrThrow(GemforgedEffects.SHIMMER_RAGE_KEY);
            entity.addEffect(new MobEffectInstance(shimmer, SHIMMER_DURATION_TICKS, SHIMMER_AMP, true, true));

            // 2) Şişe sanki kırılmış gibi ses
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 0.9f, 1.0f);
        }

        // 3) Eşya tükensin, şişe DÖNMEYECEK
        if (!(entity instanceof Player player && player.hasInfiniteMaterials())) {
            stack.shrink(1);
        }

        // 4) Eğer tamamen bittiyse boş dön (şişe yok)
        return stack.isEmpty() ? ItemStack.EMPTY : stack;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    // İçerken gluk-gluk için (istersen kaldırabiliriz); bitişte cam kırılma ayrı çalıyor.
    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
