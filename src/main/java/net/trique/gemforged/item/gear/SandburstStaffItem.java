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
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SandburstStaffItem extends Item {
    private static final float MAX_RADIUS = 10.0f;
    private static final float BASE_KNOCKBACK = 16.0f;
    private static final double VERTICAL_BOOST = 1.0;
    private static final int COOLDOWN_TICKS = 20 * 60 * 1;
    private static final float MAGIC_DAMAGE = 5.0f;
    private static final int USE_DURATION_TICKS = 20;
    private static final Vector3f CITRINE_MIX  = new Vector3f(0.95f, 0.90f, 0.60f);
    private static final Vector3f CITRINE_DEEP = new Vector3f(1.00f, 0.75f, 0.25f);
    private static final float CITRINE_SCALE_SOFT = 1.6f;
    private static final float CITRINE_SCALE_DEEP = 2.0f;
    private static final int WAVE_COUNT = 3;
    private static final int WAVE_FRAMES = 16;
    private static final int WAVE_FRAME_STEP = 2;
    private static final int WAVE_GAP_TICKS = 4;
    private static final float MIN_RENDER_RADIUS = 0.8f;
    private static final float RINGS_HEIGHT = 2.0f;

    public SandburstStaffItem(Item.Properties props) {
        super(props.durability(250));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.75f, 1.25f);
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
        server.playSound(null, origin.x, origin.y, origin.z, SoundEvents.SAND_BREAK,   SoundSource.PLAYERS, 1.2f, 0.85f);
        scheduleWaves(server, origin);
        affectEntities(server, player);
    }

    private void scheduleWaves(ServerLevel server, Vec3 center) {
        final int start = server.getServer().getTickCount();
        for (int w = 0; w < WAVE_COUNT; w++) {
            final int waveStart = start + w * WAVE_GAP_TICKS;
            for (int f = 0; f <= WAVE_FRAMES; f += WAVE_FRAME_STEP) {
                final int when   = waveStart + f;
                final float t    = f / (float) WAVE_FRAMES;
                final float eased= (float)Math.pow(t, 0.6); // ease-out radius growth
                final float rad  = MIN_RENDER_RADIUS + eased * (MAX_RADIUS - MIN_RENDER_RADIUS);
                final float fade = 1.0f - t;
                final Vec3  cNow = center;

                server.getServer().tell(new TickTask(when, () -> {
                    // Same "ring + spikes" motif as Venomfang, but with citrine colors
                    spawnRingWithSpikesColored(server, cNow, rad, RINGS_HEIGHT, fade, CITRINE_MIX,  CITRINE_SCALE_SOFT);
                    spawnRingWithSpikesColored(server, cNow, rad, RINGS_HEIGHT, fade, CITRINE_DEEP, CITRINE_SCALE_DEEP);
                }));
            }
        }
    }

    private void spawnRingWithSpikesColored(ServerLevel level, Vec3 center, float radius, double height, float fade,
                                            Vector3f color, float scale) {
        if (radius <= MIN_RENDER_RADIUS) return;

        final double cx = center.x, cy = center.y, cz = center.z;

        float r01     = Math.min(1f, radius / MAX_RADIUS);
        float density = 0.25f + (float) Math.pow(r01, 1.6);

        int points = Math.max(12, (int) (radius * 18 * density));
        int layers = 8;

        DustParticleOptions dust = new DustParticleOptions(color, scale * (0.8f + 0.5f * fade));

        for (int i = 0; i < points; i++) {
            double a  = (Math.PI * 2 * i) / points;
            double px = cx + radius * Math.cos(a);
            double pz = cz + radius * Math.sin(a);
            double py = cy + height * 0.5;
            level.sendParticles(dust, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
        }

        if (radius >= MAX_RADIUS * 0.35f) {
            for (int k = 0; k < 8; k++) {
                double a  = (Math.PI / 4.0) * k;
                double px = cx + radius * Math.cos(a);
                double pz = cz + radius * Math.sin(a);
                for (int h = 0; h <= layers; h++) {
                    double py = cy + (h / (double) layers) * height;
                    level.sendParticles(dust, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
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
            if (e instanceof LivingEntity le) {
                le.hurt(server.damageSources().indirectMagic(source, source), MAGIC_DAMAGE);
            }
        }
    }
}