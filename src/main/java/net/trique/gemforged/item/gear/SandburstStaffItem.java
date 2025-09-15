package net.trique.gemforged.item.gear;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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

public class SandburstStaffItem extends Item {

    private static final float MAX_RADIUS = 10.0f;
    private static final float BASE_KNOCKBACK = 8.0f; // x2
    private static final double VERTICAL_BOOST = 0.28;
    private static final int COOLDOWN_TICKS = 100;
    private static final float MAGIC_DAMAGE = 5.0f;
    private static final int USE_DURATION_TICKS = 20;

    private static final Vector3f TOPAZ_SAND = new Vector3f(0.94f, 0.82f, 0.52f);
    private static final Vector3f TOPAZ_GLOW = new Vector3f(1.00f, 0.95f, 0.70f);
    private static final Vector3f TOPAZ_DEEP = new Vector3f(0.78f, 0.62f, 0.32f);
    private static final float PARTICLE_SIZE = 1.15f;

    public SandburstStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.7f, 1.25f);
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
                    SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.7F, 1.05F);
        }
        if (elapsed % 5 == 0) {
            float pitch = 0.9f + (elapsed / (float) USE_DURATION_TICKS) * 0.5f;
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 0.35F, pitch);
        }
        if (user instanceof Player p) {
            ServerLevel server = (ServerLevel) level;
            Vec3 c = p.getEyePosition(1.0f).add(p.getViewVector(1.0f).scale(0.4));
            server.sendParticles(new DustParticleOptions(TOPAZ_GLOW, PARTICLE_SIZE),
                    c.x, c.y - 0.2, c.z, 3, 0.03, 0.03, 0.03, 0.0);
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
        Vec3 origin = player.position().add(0, 0.2, 0);
        server.playSound(null, origin.x, origin.y, origin.z, SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.6f, 1.55f);
        server.playSound(null, origin.x, origin.y, origin.z, SoundEvents.SAND_BREAK, SoundSource.PLAYERS, 1.2f, 0.85f);
        scheduleWave(server, origin);
        affectEntities(server, player);
    }

    private void scheduleWave(ServerLevel server, Vec3 center) {
        final int startTick = server.getServer().getTickCount();
        final int waves = 5;
        final int waveLifetime = 20;
        for (int w = 0; w < waves; w++) {
            final int waveStart = startTick + w * 6;
            for (int f = 0; f <= waveLifetime; f++) {
                final int when = waveStart + f;
                final float t = f / (float) waveLifetime;
                final float radius = t * MAX_RADIUS;
                final float alpha = 1.0f - t;
                server.getServer().tell(new TickTask(when, () -> {
                    spawn3DRing(server, center, radius, 2.0, alpha);
                }));
            }
        }
    }

    private void spawn3DRing(ServerLevel server, Vec3 center, float radius, double height, float fade) {
        double cx = center.x, cy = center.y, cz = center.z;

        // yoğunluk faktörü: merkez 0.5x → kenar 1.5x
        float factor = 0.5f + (radius / MAX_RADIUS); // 0.5 .. 1.5
        int points = Math.max(24, (int) (radius * 18 * factor));

        for (int i = 0; i < points; i++) {
            double angle = (2 * Math.PI * i) / points;
            double px = cx + radius * Math.cos(angle);
            double pz = cz + radius * Math.sin(angle);
            for (int h = 0; h <= 8; h++) {
                double py = cy + (h / 8.0) * height;

                DustParticleOptions dust;
                float rnd = server.random.nextFloat();
                if (rnd < 0.3f) {
                    dust = new DustParticleOptions(TOPAZ_GLOW, PARTICLE_SIZE * (1.0f + fade));
                } else if (rnd < 0.6f) {
                    dust = new DustParticleOptions(TOPAZ_SAND, PARTICLE_SIZE * (0.8f + fade));
                } else {
                    dust = new DustParticleOptions(TOPAZ_DEEP, PARTICLE_SIZE * (0.6f + fade));
                }

                double sx = (server.random.nextDouble() - 0.5) * 0.04;
                double sy = (server.random.nextDouble() - 0.5) * 0.04;
                double sz = (server.random.nextDouble() - 0.5) * 0.04;
                server.sendParticles(dust, px, py, pz, 1, sx, sy, sz, 0.0);

                if (server.random.nextFloat() < 0.02f) {
                    server.sendParticles(ParticleTypes.END_ROD, px, py, pz, 1, 0, 0, 0, 0);
                }
                if (server.random.nextFloat() < 0.015f) {
                    server.sendParticles(ParticleTypes.CRIT, px, py, pz, 1, 0.02, 0.02, 0.02, 0.0);
                }
            }
        }
    }

    private void affectEntities(ServerLevel server, Player source) {
        Vec3 center = source.position();
        AABB box = new AABB(
                center.x - MAX_RADIUS, center.y - 1.0, center.z - MAX_RADIUS,
                center.x + MAX_RADIUS, center.y + 2.5, center.z + MAX_RADIUS
        );
        for (Entity e : server.getEntities(source, box)) {
            if (e == source || !e.isPushable()) continue;
            Vec3 diff = e.position().subtract(center);
            Vec3 horizontal = new Vec3(diff.x, 0.0, diff.z);
            double dist = horizontal.length();
            if (dist <= 0.0001 || dist > MAX_RADIUS) continue;
            double falloff = Math.max(0.0, 1.0 - (dist / MAX_RADIUS));
            double strength = BASE_KNOCKBACK * falloff;
            Vec3 push = horizontal.normalize().scale(strength);
            e.push(push.x, VERTICAL_BOOST * falloff, push.z);
            e.hurtMarked = true;
            server.playSound(null, e.blockPosition(), SoundEvents.SAND_HIT, SoundSource.PLAYERS, 0.45f, 1.15f);
            if (e instanceof LivingEntity le) {
                le.hurt(server.damageSources().indirectMagic(source, source), MAGIC_DAMAGE);
            }
        }
    }
}
