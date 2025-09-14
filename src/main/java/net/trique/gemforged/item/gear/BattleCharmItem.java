package net.trique.gemforged.item.gear;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
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

public class BattleCharmItem extends Item {

    // Görsel yoğunluğu (hafif düşük)
    private static final float PARTICLE_DENSITY_SCALE = 0.60f;
    private static final float DUST_SIZE_SCALE        = 1.75f;

    // Oynanış
    private static final float RADIUS = 5.5f;
    private static final int   COOLDOWN_TICKS = 20 * 1;

    // FireWand tarzı şarj
    private static final int USE_DURATION_TICKS = 20; // 1s

    // Buff
    private static final int DURATION_GARNET_TICKS = 20 * 20; // 20s
    private static final int AMP_GARNET = 0;

    // Dalga (ring wave) + dikey sütun animasyonu
    private static final int FRAME_STEP = 2;         // her 2 tick'te bir kare
    private static final int FRAMES     = 18;        // toplam ~36 tick ≈ 1.8s
    private static final float WAVE_START = RADIUS * 0.8f;
    private static final float WAVE_END   = RADIUS * 2.8f;

    private static final int VERTICAL_COLUMNS = 10;  // çevreye yayılmış dikey çizgiler
    private static final float COLUMN_HEIGHT  = 3.4f;

    // Basit partikül stilleri
    private static final DustParticleOptions RED_MAIN =
            new DustParticleOptions(new Vector3f(0.98f, 0.07f, 0.10f), 2.0f * DUST_SIZE_SCALE);
    private static final DustParticleOptions RED_GLOW =
            new DustParticleOptions(new Vector3f(1.00f, 0.22f, 0.26f), 2.4f * DUST_SIZE_SCALE);
    private static final DustParticleOptions RED_DEEP =
            new DustParticleOptions(new Vector3f(0.75f, 0.02f, 0.03f), 1.6f * DUST_SIZE_SCALE);

    public BattleCharmItem(Properties props) { super(props); }

    // --- FireWand benzeri kullanım akışı ---

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand); // şarja başla
        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.6f, 1.35f);
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BOW; }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) { return USE_DURATION_TICKS; }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (level.isClientSide) return;
        int elapsed = getUseDuration(stack, user) - remainingUseTicks;
        if (elapsed == 1) {
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 1.6F, 1.0F);
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.2F, 1.0F);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        // 1 saniye tamamlandığında tetiklenir; erken bırakılırsa çağrılmaz
        if (!level.isClientSide && user instanceof Player player) {
            triggerBurst((ServerLevel) level, player);
            stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }
        return super.finishUsingItem(stack, level, user);
    }

    private void triggerBurst(ServerLevel level, Player player) {
        Vec3 c = player.position();

        level.playSound(null, c.x, c.y, c.z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 2.0f, 0.1f);
        level.playSound(null, c.x, c.y, c.z, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 2.0f, 0.1f);

        spawnRing(level, c.add(0, 0.2, 0), RADIUS, scaledCount(90), RED_MAIN);
        spawnRing(level, c.add(0, 0.5, 0), RADIUS * 0.75f, scaledCount(70), RED_GLOW);

        spawnLingeringCloud(level, c, 2.6f, RADIUS * 1.0f);

        scheduleWave(level, c);

        // Müttefik buff
        AABB box = AABB.ofSize(c, 10.0, 8.0, 10.0);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box,
                e -> e.isAlive() && (e == player || e.isAlliedTo(player)));

        Holder<MobEffect> garnetRage = level.registryAccess()
                .lookupOrThrow(net.minecraft.core.registries.Registries.MOB_EFFECT)
                .getOrThrow(GemforgedEffects.GARNET_RAGE_KEY);

        for (LivingEntity e : targets) {
            e.addEffect(new MobEffectInstance(garnetRage, DURATION_GARNET_TICKS, AMP_GARNET, true, true));
        }
    }

    // --- Dalga zamanlayıcı: genişleyen halkalar + dikey sütunlar ---
    private void scheduleWave(ServerLevel level, Vec3 center) {
        final int start = level.getServer().getTickCount();

        for (int f = 0; f <= FRAMES; f++) {
            final int when = start + f * FRAME_STEP;
            final float t = f / (float) FRAMES;              // 0→1
            final float tt = easeOutCubic(t);
            final float radius = lerp(WAVE_START, WAVE_END, tt);
            final double yLift = 0.04 * f;                   // halka yavaşça yükselsin

            level.getServer().tell(new TickTask(when, () -> {
                spawnRing(level, center.add(0, yLift, 0), radius, scaledCount(75), RED_MAIN);
                spawnRing(level, center.add(0, yLift + 0.12, 0), radius * 0.92f, scaledCount(60), RED_GLOW);

                // Dikey sütunlar (çevreye eşit açıyla, aşağıdan yukarı noktalı)
                spawnVerticalColumnsFrame(level, center, radius, COLUMN_HEIGHT, VERTICAL_COLUMNS);
            }));
        }
    }

    private void spawnVerticalColumnsFrame(ServerLevel level, Vec3 center, float radius, float height, int columns) {
        double cx = center.x, cy = center.y, cz = center.z;
        double stepAngle = (Math.PI * 2.0) / columns;

        for (int i = 0; i < columns; i++) {
            double a = i * stepAngle;
            double x = cx + radius * Math.cos(a);
            double z = cz + radius * Math.sin(a);

            int samples = 7;
            for (int j = 0; j < samples; j++) {
                double u = j / (double) (samples - 1);           // 0..1
                double y = cy + 0.2 + u * height;                 // zeminden yukarı
                // derin + glow karışık
                if ((j & 1) == 0) {
                    level.sendParticles(RED_DEEP, x, y, z, 1, 0.02, 0.06, 0.02, 0.0);
                } else {
                    level.sendParticles(RED_GLOW, x, y, z, 1, 0.02, 0.06, 0.02, 0.0);
                }
                if (j == samples - 1) {
                    level.sendParticles(ParticleTypes.ENCHANT, x, y + 0.02, z, 1, 0, 0, 0, 0);
                }
            }
        }
    }

    private void spawnRing(ServerLevel level, Vec3 center, float radius, int points, DustParticleOptions dust) {
        double cx = center.x, cy = center.y, cz = center.z;
        for (int i = 0; i < points; i++) {
            double a = (Math.PI * 2 * i) / points;
            double px = cx + radius * Math.cos(a);
            double pz = cz + radius * Math.sin(a);
            level.sendParticles(dust, px, cy, pz, 1, 0.03, 0.03, 0.03, 0.0);
        }
    }

    private void spawnLingeringCloud(ServerLevel level, Vec3 center, float seconds, float startRadius) {
        int duration = (int) (seconds * 20f);
        AreaEffectCloud cloud = new AreaEffectCloud(level, center.x, center.y + 0.1, center.z);
        cloud.setParticle(RED_DEEP);
        cloud.setDuration(duration);
        cloud.setRadius(startRadius);
        cloud.setRadiusPerTick(-(startRadius * 0.6f) / duration);
        cloud.setWaitTime(0);
        cloud.setNoGravity(true);
        level.addFreshEntity(cloud);
    }

    // easing / lerp
    private static float lerp(float a, float b, float t) { return a + (b - a) * t; }
    private static float easeOutCubic(float x) {
        float inv = 1.0f - x;
        return 1.0f - inv * inv * inv;
    }
    private static int scaledCount(int base) {
        return Math.max(1, Math.round(base * PARTICLE_DENSITY_SCALE));
    }
}
