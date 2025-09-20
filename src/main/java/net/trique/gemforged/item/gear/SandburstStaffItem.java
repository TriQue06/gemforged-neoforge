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

public class SandburstStaffItem extends Item {

    private static final float MAX_RADIUS = 10.0f;
    private static final float BASE_KNOCKBACK = 7.5f;
    private static final int COOLDOWN_TICKS = 300;
    private static final float MAGIC_DAMAGE = 5.0f;
    private static final int USE_DURATION_TICKS = 20;

    private static final Vector3f SAND_COLOR = new Vector3f(0.9f, 0.8f, 0.5f);
    private static final float PARTICLE_SIZE = 1.0f;

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
        server.playSound(null, origin.x, origin.y, origin.z,
                SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 0.8f, 1.1f);
        server.playSound(null, origin.x, origin.y, origin.z,
                SoundEvents.SAND_BREAK, SoundSource.PLAYERS, 1.0f, 0.9f);
        scheduleWave(server, origin);
        affectEntities(server, player);
    }

    private void scheduleWave(ServerLevel server, Vec3 center) {
        final int startTick = server.getServer().getTickCount();
        final int waves = 3;
        final int waveLifetime = 15;
        for (int w = 0; w < waves; w++) {
            final int waveStart = startTick + w * 6;
            for (int f = 0; f <= waveLifetime; f++) {
                final int when = waveStart + f;
                final float t = f / (float) waveLifetime;
                final float radius = t * MAX_RADIUS;
                server.getServer().tell(new TickTask(when, () ->
                        spawnRing(server, center, radius, 2.0)));
            }
        }
    }

    private void spawnRing(ServerLevel server, Vec3 center, float radius, double height) {
        if (radius < 1.0f) return; // merkezde partikül yok
        double cx = center.x, cy = center.y, cz = center.z;
        int points = Math.max(8, (int) (radius * 10));

        for (int i = 0; i < points; i++) {
            double angle = (2 * Math.PI * i) / points;
            double px = cx + radius * Math.cos(angle);
            double pz = cz + radius * Math.sin(angle);
            for (int h = 0; h <= 8; h++) {
                double py = cy + (h / 8.0) * height;
                server.sendParticles(new DustParticleOptions(SAND_COLOR, PARTICLE_SIZE),
                        px, py, pz, 1, 0.01, 0.01, 0.01, 0.0);
            }
        }
    }

    private void affectEntities(ServerLevel server, Player source) {
        Vec3 center = source.position();
        AABB box = new AABB(
                center.x - MAX_RADIUS, center.y - 1.0, center.z - MAX_RADIUS,
                center.x + MAX_RADIUS, center.y + 2.0, center.z + MAX_RADIUS
        );
        for (Entity e : server.getEntities(source, box)) {
            if (e == source || !e.isPushable()) continue;
            Vec3 diff = e.position().subtract(center);
            Vec3 horizontal = new Vec3(diff.x, 0.0, diff.z);
            double dist = horizontal.length();
            if (dist <= 0.0001 || dist > MAX_RADIUS) continue;
            double falloff = Math.max(0.0, 1.0 - (dist / MAX_RADIUS));
            double hScale = BASE_KNOCKBACK * falloff;
            Vec3 push = horizontal.normalize().scale(hScale);
            e.push(push.x, 0.0, push.z); // dikey yok
            e.hurtMarked = true;
            server.playSound(null, e.getX(), e.getY(), e.getZ(),
                    SoundEvents.WIND_CHARGE_BURST, SoundSource.PLAYERS, 0.4f, 1.0f);
            if (e instanceof LivingEntity le) {
                le.hurt(server.damageSources().indirectMagic(source, source), MAGIC_DAMAGE);
            }
        }
    }
}
