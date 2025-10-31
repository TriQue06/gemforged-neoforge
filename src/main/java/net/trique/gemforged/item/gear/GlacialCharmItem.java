package net.trique.gemforged.item.gear;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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
import net.trique.gemforged.effect.GemforgedEffects;
import net.trique.gemforged.item.GemforgedItems;
import org.joml.Vector3f;

import java.util.List;

public class GlacialCharmItem extends Item {

    private static final float RADIUS = 9f;
    private static final int COOLDOWN_TICKS = 20 * 60;
    private static final int USE_DURATION_TICKS = 20;
    private static final int DURATION_GFL = 20 * 20;
    private static final int AMP_GFL = 0;
    public static final int SLOWNESS_DURATION = 20 * 5; // ticks
    public static final int SLOWNESS_AMPLIFIER = 2;

    private static final DustParticleOptions BIG_SNOW_1 =
            new DustParticleOptions(new Vector3f(0.85f, 0.95f, 1.0f), 2.4f);
    private static final DustParticleOptions BIG_SNOW_2 =
            new DustParticleOptions(new Vector3f(0.55f, 0.80f, 1.0f), 2.2f);
    private static final DustParticleOptions BIG_SNOW_3 =
            new DustParticleOptions(new Vector3f(0.30f, 0.60f, 1.0f), 2.0f);

    public GlacialCharmItem(Properties props) {
        super(props.durability(250));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 0.7f, 0.4f);
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BOW; }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) { return USE_DURATION_TICKS; }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {
            ItemStack resource = findChargeResource(player);
            boolean creative = player.getAbilities().instabuild;

            if (creative || !resource.isEmpty()) {
                activateGlacial((ServerLevel) level, player);

                if (!creative) {
                    resource.shrink(1);
                    player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
                    stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
                }
            }
        }
        return stack;
    }

    private ItemStack findChargeResource(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (s.is(GemforgedItems.PRISMYTE.get())) return s;
        }
        return ItemStack.EMPTY;
    }

    private void activateGlacial(ServerLevel level, Player player) {
        Vec3 center = player.position();

        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.6f, 0.4f);

        spawnSnowflake(level, center);
        giveGlacialFist(level, player, center);
        spawnCloud(level, center);
        spawnBlast(level, center);
    }

    private void giveGlacialFist(ServerLevel level, Player caster, Vec3 center) {
        AABB area = AABB.ofSize(center, 10.0, 8.0, 10.0);
        List<LivingEntity> allies = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e == caster || e.isAlliedTo(caster));

        for (LivingEntity ally : allies) {
            ally.addEffect(new MobEffectInstance(
                    GemforgedEffects.GLACIAL_FIST,
                    DURATION_GFL,
                    AMP_GFL,
                    true,
                    true
            ));
        }
    }

    // ❄️ Daha net kar kristali kolları
    private void spawnSnowflake(ServerLevel level, Vec3 c) {
        int arms = 6;
        int samples = 18;
        double cx = c.x, cy = c.y + 0.3, cz = c.z;

        for (int arm = 0; arm < arms; arm++) {
            double angle = arm * (Math.PI * 2 / arms);
            for (int i = 1; i <= samples; i++) {
                double dist = (i / (double) samples) * RADIUS;
                double x = cx + Math.cos(angle) * dist;
                double z = cz + Math.sin(angle) * dist;

                DustParticleOptions dust = switch (i % 3) {
                    case 0 -> BIG_SNOW_1;
                    case 1 -> BIG_SNOW_2;
                    default -> BIG_SNOW_3;
                };

                level.sendParticles(dust, x, cy, z, 2, 0.02, 0.02, 0.02, 0.01);
            }
        }
    }

    // 💥 Buz şok dalgası: çok daha görünür
    private void spawnBlast(ServerLevel level, Vec3 c) {
        int points = 90;
        double cx = c.x, cy = c.y + 0.6, cz = c.z;

        for (int i = 0; i < points; i++) {
            double a = (2 * Math.PI * i) / points;
            double px = cx + RADIUS * Math.cos(a);
            double pz = cz + RADIUS * Math.sin(a);

            DustParticleOptions dust = switch (i % 3) {
                case 0 -> BIG_SNOW_1;
                case 1 -> BIG_SNOW_2;
                default -> BIG_SNOW_3;
            };

            level.sendParticles(dust, px, cy, pz, 3, 0.05, 0.05, 0.05, 0.01);
        }
    }

    private void spawnCloud(ServerLevel level, Vec3 c) {
        AreaEffectCloud cloud = new AreaEffectCloud(level, c.x, c.y + 0.1, c.z);
        cloud.setParticle(BIG_SNOW_2);
        cloud.setRadius(RADIUS);
        cloud.setDuration(30);
        cloud.setRadiusPerTick(-0.1f);
        cloud.setNoGravity(true);
        level.addFreshEntity(cloud);
    }
}