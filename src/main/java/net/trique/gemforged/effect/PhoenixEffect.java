package net.trique.gemforged.effect;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PhoenixEffect extends MobEffect {

    private static final Map<UUID, SavedState> STATES = new HashMap<>();

    private static final DustParticleOptions RED =
            new DustParticleOptions(new Vector3f(1.00f, 0.05f, 0.02f), 2.0f);
    private static final DustParticleOptions ORANGE =
            new DustParticleOptions(new Vector3f(1.00f, 0.40f, 0.05f), 2.0f);
    private static final DustParticleOptions GOLD =
            new DustParticleOptions(new Vector3f(1.00f, 0.78f, 0.08f), 2.0f);

    public PhoenixEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF7A00); // turuncu taban rengi
    }

    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            Vec3 pos = player.position();
            STATES.put(player.getUUID(),
                    new SavedState(pos, player.getYRot(), player.getXRot(), player.getHealth()));
        }
    }

    public void onEffectRemoved(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            STATES.remove(player.getUUID());
        }
    }
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity.level() instanceof ServerLevel level)) return true;

        Vec3 look = entity.getLookAngle();
        Vec3 pos  = entity.position();

        double speed = entity.getDeltaMovement().length();
        int densityBoost = (int) Math.min(10, Math.floor(speed * 25.0));
        int perSegParticles = 6 + densityBoost;
        int segments = 3;
        double segStep = 0.5;

        double backDist = 0.75 + entity.getRandom().nextDouble() * 0.35;
        double baseX = pos.x - look.x * backDist;
        double baseY = pos.y + 0.55;
        double baseZ = pos.z - look.z * backDist;

        Vec3 up = new Vec3(0, 1, 0);
        Vec3 side = up.cross(look).normalize();
        Vec3 upTilt = look.cross(side).normalize();

        for (int s = 0; s < segments; s++) {
            double tBack = s * segStep;
            double cx = baseX - look.x * tBack;
            double cy = baseY - 0.05 * s;
            double cz = baseZ - look.z * tBack;

            double r = 0.24 + 0.06 * s;

            for (int i = 0; i < perSegParticles; i++) {
                double a = (Math.PI * 2 * i) / perSegParticles + entity.tickCount * 0.28;
                Vec3 offset = side.scale(r * Mth.cos((float) a))
                        .add(upTilt.scale(r * Mth.sin((float) a) * 1.8)); // ~2 blok yüksekliğe kadar

                double px = cx + offset.x;
                double py = cy + offset.y;
                double pz = cz + offset.z;

                int sel = (s + i) % 3;
                DustParticleOptions dust = (sel == 0) ? RED : (sel == 1) ? ORANGE : GOLD;
                level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0);

                if (i % 4 == 0) {
                    level.sendParticles(ParticleTypes.FLAME, px, py, pz, 1, 0.0, 0.0, 0.0, 0.01);
                }
            }
        }

        if (entity.tickCount % 50 == 0) {
            level.playSound(null, pos.x, pos.y, pos.z,
                    SoundEvents.BLAZE_BURN, SoundSource.PLAYERS,
                    0.8f, 0.9f + entity.getRandom().nextFloat() * 0.2f);
        }

        return true;
    }

    public static boolean tryRevive(Player player) {
        SavedState state = STATES.get(player.getUUID());
        if (state == null) return false;

        player.setHealth(state.health);
        player.teleportTo(state.pos.x, state.pos.y, state.pos.z);
        player.setYRot(state.yaw);
        player.setXRot(state.pitch);

        STATES.remove(player.getUUID());
        player.removeEffect(GemforgedEffects.PHOENIX);

        if (player.level() instanceof ServerLevel level) {
            Vec3 pos = player.position();

            level.playSound(null, pos.x, pos.y, pos.z,
                    SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 2.0f, 1.0f);
            level.playSound(null, pos.x, pos.y, pos.z,
                    SoundEvents.BLAZE_BURN,           SoundSource.PLAYERS, 1.5f, 0.8f);

            int rings = 5;
            int pointsPerRing = 120;
            double maxRadius = 8.0;

            for (int r = 0; r < rings; r++) {
                double radius = (maxRadius / rings) * (r + 1);
                double yBase = pos.y + 0.3 + r * 0.2;

                for (int i = 0; i < pointsPerRing; i++) {
                    double angle = (Math.PI * 2 * i) / pointsPerRing;
                    double px = pos.x + radius * Math.cos(angle);
                    double pz = pos.z + radius * Math.sin(angle);
                    double py = yBase + Math.sin(i * 0.15 + r) * 0.2;

                    int selector = (i + r) % 3;
                    DustParticleOptions dust = (selector == 0) ? RED : (selector == 1) ? ORANGE : GOLD;
                    level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0);

                    if (i % 15 == 0) {
                        level.sendParticles(ParticleTypes.FLAME, px, py, pz,
                                2, 0.05, 0.05, 0.05, 0.01);
                    }
                    if ((i + r) % 40 == 0) {
                        level.sendParticles(ParticleTypes.LAVA, px, py + 0.05, pz,
                                1, 0.02, 0.02, 0.02, 0.0);
                    }
                }
            }

            int columns = 16;
            for (int col = 0; col < columns; col++) {
                double angle = (Math.PI * 2 * col) / (double) columns;
                double px = pos.x + Math.cos(angle) * 2.0;
                double pz = pos.z + Math.sin(angle) * 2.0;

                for (int h = 0; h < 20; h++) {
                    double py = pos.y + 0.2 + h * 0.15;
                    DustParticleOptions dust = (h > 13) ? GOLD : (h > 6) ? ORANGE : RED;
                    level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0);

                    if (h % 5 == 0) {
                        level.sendParticles(ParticleTypes.FLAME, px, py, pz,
                                1, 0.05, 0.05, 0.05, 0.01);
                    }
                    if (h % 9 == 0) {
                        level.sendParticles(ParticleTypes.LAVA, px, py + 0.02, pz,
                                1, 0.01, 0.01, 0.01, 0.0);
                    }
                }
            }

            level.sendParticles(ParticleTypes.EXPLOSION, pos.x, pos.y + 1, pos.z,
                    4, 1.0, 1.0, 1.0, 0.0);
        }

        return true;
    }

    private record SavedState(Vec3 pos, float yaw, float pitch, float health) {}
}