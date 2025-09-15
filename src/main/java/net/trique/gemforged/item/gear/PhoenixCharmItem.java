package net.trique.gemforged.item.gear;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import net.trique.gemforged.effect.GemforgedEffects;

import java.util.List;

public class PhoenixCharmItem extends Item {

    private static final int USE_DURATION_TICKS = 20; // 1s şarj
    private static final int EFFECT_DURATION = 20 * 30; // 30s

    // Turuncu / Sarı toz partikülleri
    private static final DustParticleOptions ORANGE =
            new DustParticleOptions(new Vector3f(1.0f, 0.5f, 0.0f), 2.0f);
    private static final DustParticleOptions YELLOW =
            new DustParticleOptions(new Vector3f(1.0f, 0.9f, 0.2f), 2.0f);

    public PhoenixCharmItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION_TICKS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {
            applyPhoenixEffect((ServerLevel) level, player);
            stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
        }
        return stack;
    }

    private void applyPhoenixEffect(ServerLevel level, Player user) {
        Vec3 c = user.position();

        // Sesler
        level.playSound(null, c.x, c.y, c.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.2f, 1.0f);
        level.playSound(null, c.x, c.y, c.z,
                SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 0.8f, 1.2f);

        // Dost oyuncu + mob seçimi
        AABB area = AABB.ofSize(c, 8, 8, 8);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e.isAlive() && (e == user || e.isAlliedTo(user)));

        // Efekti uygula
        for (LivingEntity e : targets) {
            e.addEffect(new MobEffectInstance(GemforgedEffects.PHOENIX,
                    EFFECT_DURATION, 0, true, true));

            // Spiral partikül efekti
            Vec3 base = e.position();
            for (int i = 0; i < 80; i++) {
                double angle = (Math.PI * 2 * i) / 20.0;
                double radius = 0.5 + 0.1 * Math.sin(i * 0.5);
                double px = base.x + radius * Math.cos(angle);
                double pz = base.z + radius * Math.sin(angle);
                double py = base.y + 0.2 + (i % 20) * 0.1; // 2 blok yükseklik

                DustParticleOptions dust = (i % 2 == 0) ? ORANGE : YELLOW;
                level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0);

                // Ateş + alev karışımı
                if (i % 5 == 0) {
                    level.sendParticles(ParticleTypes.FLAME, px, py, pz, 1, 0, 0, 0, 0.01);
                    level.sendParticles(ParticleTypes.SMALL_FLAME, px, py, pz, 1, 0, 0, 0, 0.02);
                }
            }
        }
    }
}
