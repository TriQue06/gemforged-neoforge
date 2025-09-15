package net.trique.gemforged.effect;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ShimmerRageEffect extends MobEffect {
    private static final String MODID = "gemforged";

    // Attribute modifier ID’leri
    private static final ResourceLocation MOVE_ID   =
            ResourceLocation.fromNamespaceAndPath(MODID, "shimmer_rage_move");
    private static final ResourceLocation ATKDAM_ID =
            ResourceLocation.fromNamespaceAndPath(MODID, "shimmer_rage_attack_damage");
    private static final ResourceLocation SCALE_ID  =
            ResourceLocation.fromNamespaceAndPath(MODID, "shimmer_rage_scale");

    // Çarpanlar (Attack Speed YOK)
    private static final double MOVE_MULT   = 0.40D; // biraz hızlı
    private static final double ATKDAM_MULT = 0.80D; // çok güçlü
    private static final double SCALE_MULT  = 0.40D; // %40 daha iri

    // Mor/pembe toz partikülleri
    private static final DustParticleOptions PURPLE =
            new DustParticleOptions(new Vector3f(0.60f, 0.10f, 0.70f), 2.0f);
    private static final DustParticleOptions PINK =
            new DustParticleOptions(new Vector3f(1.00f, 0.30f, 0.60f), 2.0f);

    public ShimmerRageEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xB400FF); // efekt bar rengi

        addAttributeModifier(Attributes.MOVEMENT_SPEED, MOVE_ID,
                MOVE_MULT, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, ATKDAM_ID,
                ATKDAM_MULT, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        addAttributeModifier(Attributes.SCALE, SCALE_ID,
                SCALE_MULT, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true; // her tick işlesin
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity.level() instanceof ServerLevel level)) return true;

        // Aşırı regeneration (sürekli tazele)
        entity.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                40,   // 2 sn; her tick yenilenecek
                2,    // Regeneration III
                true, true, true));

        // === ARKA KUYRUK PARTİKÜLLERİ ===
        Vec3 look = entity.getLookAngle();
        Vec3 pos = entity.position();

        double backDist = 0.8 + entity.getRandom().nextDouble() * 0.4;
        double baseX = pos.x - look.x * backDist;
        double baseY = pos.y + 0.6; // gövde hizası
        double baseZ = pos.z - look.z * backDist;

        int segments = 4;           // kuyruk uzunluğu
        int perSegParticles = 8;    // daha az partikül
        double segStep = 0.55;      // segmentler arası mesafe

        Vec3 up = new Vec3(0, 1, 0);
        Vec3 side = up.cross(look).normalize();
        Vec3 upTilt = look.cross(side).normalize();

        for (int s = 0; s < segments; s++) {
            double tBack = s * segStep;
            double cx = baseX - look.x * tBack;
            double cy = baseY - 0.05 * s;
            double cz = baseZ - look.z * tBack;

            double r = 0.25 + 0.05 * s; // biraz daha geniş yay

            for (int i = 0; i < perSegParticles; i++) {
                double a = (Math.PI * 2 * i) / perSegParticles + entity.tickCount * 0.25;
                Vec3 offset = side.scale(r * Mth.cos((float) a))
                        .add(upTilt.scale(r * Mth.sin((float) a) * 2.0)); // 2 blok yüksekliğe kadar

                double px = cx + offset.x;
                double py = cy + offset.y;
                double pz = cz + offset.z;

                DustParticleOptions dust = ((s + i) % 2 == 0) ? PURPLE : PINK;
                level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0);

                if (i % 4 == 0) {
                    level.sendParticles(ParticleTypes.END_ROD, px, py, pz, 1, 0, 0, 0, 0.0);
                }
            }
        }

        // Ses imzası: ~3 sn’de bir
        if (entity.tickCount % 60 == 0) {
            level.playSound(null, pos.x, pos.y, pos.z,
                    SoundEvents.BLAZE_BURN, SoundSource.PLAYERS,
                    1.0f, 0.55f + entity.getRandom().nextFloat() * 0.15f);
        }

        return true;
    }
}
