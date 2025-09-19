package net.trique.gemforged.effect;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

    private static final DustParticleOptions ORANGE =
            new DustParticleOptions(new Vector3f(1.0f, 0.4f, 0.05f), 2.0f);
    private static final DustParticleOptions YELLOW =
            new DustParticleOptions(new Vector3f(1.0f, 0.85f, 0.2f), 2.0f);

    public PhoenixEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFF9933);
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
                    SoundEvents.BLAZE_BURN, SoundSource.PLAYERS, 1.5f, 0.8f);

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

                    DustParticleOptions dust = (i % 2 == 0) ? ORANGE : YELLOW;
                    level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0);

                    if (i % 15 == 0) {
                        level.sendParticles(ParticleTypes.FLAME, px, py, pz,
                                2, 0.05, 0.05, 0.05, 0.01);
                    }
                }
            }

            for (int col = 0; col < 16; col++) {
                double angle = (Math.PI * 2 * col) / 16.0;
                double px = pos.x + Math.cos(angle) * 2.0;
                double pz = pos.z + Math.sin(angle) * 2.0;

                for (int h = 0; h < 20; h++) {
                    double py = pos.y + 0.2 + h * 0.15;
                    DustParticleOptions dust = (h % 2 == 0) ? ORANGE : YELLOW;
                    level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0);
                    if (h % 5 == 0) {
                        level.sendParticles(ParticleTypes.FLAME, px, py, pz,
                                1, 0.05, 0.05, 0.05, 0.01);
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