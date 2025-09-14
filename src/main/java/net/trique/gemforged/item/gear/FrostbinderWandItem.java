package net.trique.gemforged.item.gear;

import net.minecraft.core.particles.DustParticleOptions;
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

public class FrostbinderWandItem extends Item {

    private static final float MAX_RADIUS = 14.0f;
    private static final int COOLDOWN_TICKS = 120;
    private static final int USE_DURATION_TICKS = 20;

    private static final Vector3f FROST_LIGHT = new Vector3f(0.65f, 0.90f, 1.00f);
    private static final Vector3f FROST_TURQUOISE = new Vector3f(0.25f, 0.95f, 0.90f);
    private static final Vector3f FROST_DARK = new Vector3f(0.10f, 0.25f, 0.60f);
    private static final float PARTICLE_SIZE = 1.35f;

    private static final int FREEZE_TICKS = 200; // 10s

    public FrostbinderWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.7f, 0.8f);
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
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (level.isClientSide) return;
        int elapsed = getUseDuration(stack, user) - remainingUseTicks;
        if (elapsed == 1) {
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.6F, 0.9F);
        }
        if (elapsed % 5 == 0) {
            float pitch = 0.7f + (elapsed / (float) USE_DURATION_TICKS) * 0.4f;
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.GLASS_PLACE, SoundSource.PLAYERS, 0.35F, pitch);
        }
        if (user instanceof Player p) {
            ServerLevel server = (ServerLevel) level;
            Vec3 c = p.getEyePosition(1.0f).add(p.getViewVector(1.0f).scale(0.4));
            server.sendParticles(new DustParticleOptions(FROST_LIGHT, PARTICLE_SIZE),
                    c.x, c.y - 0.2, c.z, 4, 0.05, 0.05, 0.05, 0.0);
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
        server.playSound(null, origin.x, origin.y, origin.z, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.2f, 0.7f);
        server.playSound(null, origin.x, origin.y, origin.z, SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.6f, 1.6f);
        scheduleWave(server, origin);
        affectEntities(server, player);
    }

    private void scheduleWave(ServerLevel server, Vec3 center) {
        final int startTick = server.getServer().getTickCount();
        final int waves = 5;
        final int waveLifetime = 22;
        for (int w = 0; w < waves; w++) {
            final int waveStart = startTick + w * 7;
            for (int f = 0; f <= waveLifetime; f++) {
                final int when = waveStart + f;
                final float t = f / (float) waveLifetime;
                final float radius = t * MAX_RADIUS;
                final float alpha = 1.0f - t;
                server.getServer().tell(new TickTask(when, () -> {
                    spawnIcyRing(server, center, radius, 2.5, alpha);
                }));
            }
        }
    }

    private void spawnIcyRing(ServerLevel server, Vec3 center, float radius, double height, float fade) {
        double cx = center.x, cy = center.y, cz = center.z;
        int points = Math.max(40, (int) (radius * 20));
        for (int i = 0; i < points; i++) {
            double angle = (2 * Math.PI * i) / points;
            double px = cx + radius * Math.cos(angle);
            double pz = cz + radius * Math.sin(angle);
            for (int h = 0; h <= 10; h++) {
                double py = cy + (h / 10.0) * height;
                Vector3f col;
                float size;
                if (i % 3 == 0) {
                    col = FROST_LIGHT; size = PARTICLE_SIZE * (0.9f + fade);
                } else if (i % 3 == 1) {
                    col = FROST_TURQUOISE; size = PARTICLE_SIZE * (0.8f + fade);
                } else {
                    col = FROST_DARK; size = PARTICLE_SIZE * (0.7f + fade);
                }
                server.sendParticles(new DustParticleOptions(col, size),
                        px, py, pz, 1, 0.01, 0.01, 0.01, 0.0);
            }
        }
    }

    private void affectEntities(ServerLevel server, Player source) {
        Vec3 center = source.position();
        AABB box = new AABB(
                center.x - MAX_RADIUS, center.y - 1.0, center.z - MAX_RADIUS,
                center.x + MAX_RADIUS, center.y + 3.0, center.z + MAX_RADIUS
        );
        for (Entity e : server.getEntities(source, box)) {
            if (e == source || !e.isPushable()) continue;
            if (e instanceof LivingEntity le) {
                le.setTicksFrozen(FREEZE_TICKS);
            }
        }
    }
}
