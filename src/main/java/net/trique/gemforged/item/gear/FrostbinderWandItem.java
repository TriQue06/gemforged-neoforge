package net.trique.gemforged.item.gear;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
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
import net.minecraft.world.entity.Entity;
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

public class FrostbinderWandItem extends Item {

    private static final float MAX_RADIUS = 10.0f;    // 12 → 10 (bir-iki blok daha yakın)
    private static final int COOLDOWN_TICKS = 120;
    private static final int USE_DURATION_TICKS = 20;

    // Daha uzun ve daha yavaş yayılım (32*8=256t → 40*10=400t)
    private static final int WAVE_FRAMES = 40;
    private static final int WAVE_FRAME_STEP = 10;

    private static final Vector3f ICE_LIGHT = new Vector3f(0.75f, 0.95f, 1.00f);
    private static final Vector3f ICE_TURQ  = new Vector3f(0.30f, 0.95f, 0.92f);
    private static final Vector3f ICE_MID   = new Vector3f(0.40f, 0.80f, 1.00f);
    private static final Vector3f ICE_DEEP  = new Vector3f(0.12f, 0.38f, 0.88f);
    private static final float PARTICLE_SIZE = 1.20f;

    private static final int FREEZE_DURATION_TICKS = 200;
    private static final int FREEZE_AMP = 0;

    public FrostbinderWandItem(Properties properties) { super(properties); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.8f, 0.7f);
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BOW; }
    @Override public int getUseDuration(ItemStack stack, LivingEntity entity) { return USE_DURATION_TICKS; }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (level.isClientSide) return;
        int elapsed = getUseDuration(stack, user) - remainingUseTicks;
        if (elapsed == 1) {
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.7F, 0.9F);
        }
        if (elapsed % 5 == 0) {
            float pitch = 0.7f + (elapsed / (float) USE_DURATION_TICKS) * 0.3f;
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 0.4F, pitch);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {
            triggerBurst((ServerLevel) level, player);
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
        }
        return super.finishUsingItem(stack, level, user);
    }

    private void triggerBurst(ServerLevel server, Player player) {
        Vec3 origin = player.position().add(0, 0.15, 0);
        server.playSound(null, origin.x, origin.y, origin.z, SoundEvents.GLASS_BREAK,        SoundSource.PLAYERS, 1.1f, 0.6f);
        server.playSound(null, origin.x, origin.y, origin.z, SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1.0f, 1.0f);
        scheduleWave(server, origin);
        applyFreezeEffect(server, player);
    }

    private void scheduleWave(ServerLevel server, Vec3 center) {
        final int startTick = server.getServer().getTickCount();
        for (int f = 0; f <= WAVE_FRAMES; f++) {
            final int when = startTick + f * WAVE_FRAME_STEP;
            final float t = f / (float) WAVE_FRAMES;
            final float radius = t * MAX_RADIUS;
            final float fade = 1.0f - t;
            final int frameIndex = f;
            server.getServer().tell(new TickTask(when, () ->
                    spawnSnowflakeFrame(server, center, radius, 2.0, fade, frameIndex)));
        }
    }

    private void spawnSnowflakeFrame(ServerLevel server, Vec3 center, float radius, double height, float fade, int frameIndex) {
        if (radius <= 0.05f) return;

        final double cx = center.x, cy = center.y, cz = center.z;
        final int layers = 8;

        int ringPoints = Math.max(24, (int)(radius * 16));
        for (int i = 0; i < ringPoints; i++) {
            double a = (Math.PI * 2 * i) / ringPoints;
            double px = cx + radius * Math.cos(a);
            double pz = cz + radius * Math.sin(a);
            for (int h = 0; h <= layers; h++) {
                double py = cy + (h / (double) layers) * height;
                DustParticleOptions dust = chooseDustForLayer(h, layers, i, fade);
                server.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0.0);
            }
        }

        final int arms = 6;
        final double armStep = (Math.PI * 2.0) / arms;
        int armSamples = Math.max(10, (int)(radius * 6));
        for (int k = 0; k < arms; k++) {
            double base = armStep * k;
            for (int s = 1; s <= armSamples; s++) {
                double u = s / (double) armSamples;
                double r = u * radius;
                double px = cx + r * Math.cos(base);
                double pz = cz + r * Math.sin(base);
                int h = (int)Math.round((layers * 0.5) + Math.sin(frameIndex * 0.1 + s * 0.15) * (layers * 0.25));
                h = Math.max(0, Math.min(layers, h));
                DustParticleOptions dust = chooseDustForLayer(h, layers, s, fade);
                double py = cy + (h / (double) layers) * height;
                server.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0.0);

                if (s % 5 == 0 && u > 0.15) {
                    double branchLen = 0.18 + 0.22 * (u * fade);
                    double angL = base - Math.toRadians(30);
                    double angR = base + Math.toRadians(30);
                    double blx = px + branchLen * Math.cos(angL);
                    double blz = pz + branchLen * Math.sin(angL);
                    double brx = px + branchLen * Math.cos(angR);
                    double brz = pz + branchLen * Math.sin(angR);
                    DustParticleOptions lightDust = new DustParticleOptions(ICE_LIGHT, PARTICLE_SIZE * (0.7f + 0.3f * fade));
                    server.sendParticles(lightDust, blx, py + 0.02, blz, 1, 0, 0, 0, 0.0);
                    server.sendParticles(lightDust, brx, py + 0.02, brz, 1, 0, 0, 0, 0.0);
                }
            }
        }

        double tipRadius = radius * 0.98;
        for (int k = 0; k < arms; k++) {
            double a = armStep * k;
            double tx = cx + tipRadius * Math.cos(a);
            double tz = cz + tipRadius * Math.sin(a);
            double ty = cy + height * 0.9;
            server.sendParticles(ParticleTypes.SNOWFLAKE, tx, ty, tz, 1, 0, 0, 0, 0.0);
        }
    }

    private DustParticleOptions chooseDustForLayer(int h, int layers, int indexSeed, float fade) {
        float t = h / (float) layers;
        float scale = PARTICLE_SIZE * (0.85f + 0.45f * fade);
        if (t >= 0.6f) {
            return (indexSeed % 2 == 0)
                    ? new DustParticleOptions(ICE_TURQ, scale)
                    : new DustParticleOptions(ICE_LIGHT, scale);
        } else {
            return (indexSeed % 2 == 0)
                    ? new DustParticleOptions(ICE_DEEP, scale * 0.95f)
                    : new DustParticleOptions(ICE_MID,  scale);
        }
    }

    private void applyFreezeEffect(ServerLevel server, Player source) {
        Vec3 center = source.position();
        AABB box = new AABB(center.x - MAX_RADIUS, center.y - 1.0, center.z - MAX_RADIUS,
                center.x + MAX_RADIUS, center.y + 2.5, center.z + MAX_RADIUS);

        Holder<MobEffect> freezeEffect = server.registryAccess()
                .lookupOrThrow(Registries.MOB_EFFECT)
                .getOrThrow(GemforgedEffects.FREEZE_KEY);

        for (Entity e : server.getEntities(source, box)) {
            if (e == source || !(e instanceof LivingEntity le)) continue;
            le.addEffect(new MobEffectInstance(freezeEffect, FREEZE_DURATION_TICKS, FREEZE_AMP, true, true));
            server.playSound(null, le.blockPosition(), SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 0.6f, 1.2f);
        }
    }
}
