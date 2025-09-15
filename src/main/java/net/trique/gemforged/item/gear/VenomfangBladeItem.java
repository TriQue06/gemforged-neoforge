package net.trique.gemforged.item.gear;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class VenomfangBladeItem extends SwordItem {

    private static final int HIT_THRESHOLD = 3;
    private static final float MAGIC_DAMAGE = 2.0f;

    private static final float WAVE_MAX_RADIUS = 7.0f;
    private static final int   WAVE_FRAMES     = 16;
    private static final int   WAVE_FRAME_STEP = 2;
    private static final int   WAVE_COUNT      = 3;
    private static final int   WAVE_GAP_TICKS  = 6;

    private static final int    POISON_DURATION   = 8 * 20;
    private static final double KNOCKBACK_STRENGTH= 1.25;
    private static final double KNOCKBACK_VERTICAL= 0.20;

    private static final Vector3f MALACHITE      = new Vector3f(0.25f, 0.90f, 0.60f);
    private static final float    MALACHITE_SCALE= 1.8f;

    public VenomfangBladeItem(Item.Properties props) {
        super(Tiers.DIAMOND, props
                .durability(1800)
                .attributes(SwordItem.createAttributes(Tiers.DIAMOND, 0, -2.4f)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean res = super.hurtEnemy(stack, target, attacker);

        if (attacker instanceof Player player && !player.level().isClientSide) {
            CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag tag = data.copyTag();
            int count = tag.getInt("venomfang_hits") + 1;

            if (count >= HIT_THRESHOLD) {
                triggerVenomWaves((ServerLevel) player.level(), player, target);
                count = 0;
            }

            tag.putInt("venomfang_hits", count);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
        return res;
    }

    private void triggerVenomWaves(ServerLevel level, Player attacker, LivingEntity source) {
        Vec3 center = source.position().add(0, 0.05, 0);

        level.playSound(null, center.x, center.y, center.z, SoundEvents.WITCH_DRINK,        SoundSource.PLAYERS, 0.9f, 0.95f);
        level.playSound(null, center.x, center.y, center.z, SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 0.9f, 1.25f);

        final int start = level.getServer().getTickCount();

        for (int w = 0; w < WAVE_COUNT; w++) {
            final int waveStart = start + w * WAVE_GAP_TICKS;

            level.getServer().tell(new TickTask(waveStart, () -> {
                level.playSound(null, center.x, center.y, center.z, SoundEvents.SLIME_SQUISH,  SoundSource.PLAYERS, 0.75f, 0.8f);
                level.playSound(null, center.x, center.y, center.z, SoundEvents.SPIDER_AMBIENT, SoundSource.PLAYERS, 0.6f, 0.55f); // “tıslama” hissi, yavaş pitch
            }));

            for (int f = 0; f <= WAVE_FRAMES; f++) {
                final int when = waveStart + f * WAVE_FRAME_STEP;
                final float t = f / (float) WAVE_FRAMES;
                final float radius = t * WAVE_MAX_RADIUS;
                final float fade = 1.0f - t;
                final Vec3 cNow = center;

                level.getServer().tell(new TickTask(when, () ->
                        spawnRingWithSpikes(level, cNow, radius, 2.0, fade)));
            }
        }

        AABB box = AABB.ofSize(center, WAVE_MAX_RADIUS * 2, 3.0, WAVE_MAX_RADIUS * 2);
        for (LivingEntity e : level.getEntitiesOfClass(LivingEntity.class, box, le -> le.isAlive() && le != attacker)) {
            e.addEffect(new MobEffectInstance(MobEffects.POISON, POISON_DURATION, 0));
            e.hurt(level.damageSources().indirectMagic(attacker, attacker), MAGIC_DAMAGE);

            Vec3 diff = e.position().subtract(center);
            Vec3 push = new Vec3(diff.x, 0, diff.z).normalize().scale(KNOCKBACK_STRENGTH);
            e.push(push.x, KNOCKBACK_VERTICAL, push.z);
            e.hurtMarked = true;
        }

        level.getServer().tell(new TickTask(start + WAVE_COUNT * WAVE_GAP_TICKS + WAVE_FRAMES * WAVE_FRAME_STEP, () ->
                level.playSound(null, center.x, center.y, center.z, SoundEvents.WITCH_THROW, SoundSource.PLAYERS, 0.6f, 1.35f)));
    }

    /**
     * Merkezde düşük yoğunluk, dış halkaya doğru yüksek yoğunluk.
     * Küçük yarıçaplarda partikül sayısını ciddi azaltır; büyük yarıçaplarda artırır.
     */
    private void spawnRingWithSpikes(ServerLevel level, Vec3 center, float radius, double height, float fade) {
        if (radius <= 0.05f) return;

        final double cx = center.x, cy = center.y, cz = center.z;

        // r01: 0..1; density: merkezde ~0.25x, dışta ~1.0x
        float r01 = Math.min(1f, radius / WAVE_MAX_RADIUS);
        float density = 0.25f + (float) Math.pow(r01, 1.6); // merkez çok düşük, dışta yüksek

        int points = Math.max(12, (int) (radius * 18 * density));
        int layers = 8;

        DustParticleOptions dust = new DustParticleOptions(MALACHITE, MALACHITE_SCALE * (0.8f + 0.5f * fade));

        // Yalnızca ring çevresi
        for (int i = 0; i < points; i++) {
            double a  = (Math.PI * 2 * i) / points;
            double px = cx + radius * Math.cos(a);
            double pz = cz + radius * Math.sin(a);
            double py = cy + height * 0.5;
            level.sendParticles(dust, px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
        }

        // Dikey “zehir dikenleri”: yalnızca yarıçap belirli eşiği geçince çiz (merkezde kalabalığı azaltır)
        if (radius >= WAVE_MAX_RADIUS * 0.35f) {
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
}
