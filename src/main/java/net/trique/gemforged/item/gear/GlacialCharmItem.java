package net.trique.gemforged.item.gear;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
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

    private static final float RADIUS = 8f;
    private static final int COOLDOWN_TICKS = 20 * 30;
    private static final int USE_DURATION_TICKS = 20;
    private static final int DURATION_GFL = 20 * 15;
    private static final int AMP_GFL = 0;

    private static final DustParticleOptions COL_LIGHT =
            new DustParticleOptions(new Vector3f(0xa4 / 255f, 0xe9 / 255f, 0xfb / 255f), 2.2f);
    private static final DustParticleOptions COL_MID =
            new DustParticleOptions(new Vector3f(0x54 / 255f, 0xc5 / 255f, 0xf1 / 255f), 2.2f);
    private static final DustParticleOptions COL_DARK =
            new DustParticleOptions(new Vector3f(0x22 / 255f, 0x78 / 255f, 0xbb / 255f), 2.2f);

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
                player.awardStat(Stats.ITEM_USED.get(this));

                if (!creative) {
                    resource.shrink(1);
                    stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
                    player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
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

        spawnSnowflakeCoreFractal(level, center);
        spawn3DAxisFlakes(level, center);
        spawnSphereRings(level, center);
        spawnCloud(level, center);
        giveGlacialFist(level, player, center);
    }

    private void giveGlacialFist(ServerLevel level, Player caster, Vec3 center) {
        AABB area = new AABB(center.x - 8, center.y - 8, center.z - 8,
                center.x + 8, center.y + 8, center.z + 8);

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

    private static DustParticleOptions pick(int i) {
        int m = i % 3;
        return m == 0 ? COL_LIGHT : (m == 1 ? COL_MID : COL_DARK);
    }

    private void spawnSnowflakeCoreFractal(ServerLevel level, Vec3 c) {
        int arms = 8;
        int segments = 12;
        double cx = c.x, cy = c.y + 0.3, cz = c.z;

        for (int arm = 0; arm < arms; arm++) {
            double base = arm * (Math.PI * 2 / arms);

            for (int i = 1; i <= segments; i++) {
                double t = (double) i / segments;
                double dist = t * (RADIUS * 0.85);

                double sx = cx + Math.cos(base) * dist;
                double sz = cz + Math.sin(base) * dist;
                DustParticleOptions col = pick(i);

                level.sendParticles(col, sx, cy, sz, 2, 0.01, 0.01, 0.01, 0.006);

                double branch = base + (i % 2 == 0 ? Math.PI / 8 : -Math.PI / 8);
                double bd = dist * 0.35;
                double bx = sx + Math.cos(branch) * bd;
                double bz = sz + Math.sin(branch) * bd;
                level.sendParticles(col, bx, cy, bz, 1, 0.008, 0.008, 0.008, 0.004);
            }
        }
    }

    private void spawn3DAxisFlakes(ServerLevel level, Vec3 c) {
        int samples = 16;
        double cx = c.x, cy = c.y + 0.3, cz = c.z;

        for (int i = 1; i <= samples; i++) {
            double t = (double) i / samples;
            double d = t * RADIUS;

            DustParticleOptions col = pick(i);

            level.sendParticles(col, cx + d, cy, cz, 1, 0.01, 0.01, 0.01, 0.006);
            level.sendParticles(col, cx - d, cy, cz, 1, 0.01, 0.01, 0.01, 0.006);
            level.sendParticles(col, cx, cy + d, cz, 1, 0.01, 0.01, 0.01, 0.006);
            level.sendParticles(col, cx, cy - d, cz, 1, 0.01, 0.01, 0.01, 0.006);
            level.sendParticles(col, cx, cy, cz + d, 1, 0.01, 0.01, 0.01, 0.006);
            level.sendParticles(col, cx, cy, cz - d, 1, 0.01, 0.01, 0.01, 0.006);

            double rot = Math.PI / 4;
            double xr = d * Math.cos(rot);
            double zr = d * Math.sin(rot);

            level.sendParticles(col, cx + xr, cy, cz + zr, 1, 0.008, 0.008, 0.008, 0.004);
            level.sendParticles(col, cx - xr, cy, cz - zr, 1, 0.008, 0.008, 0.008, 0.004);
            level.sendParticles(col, cx + xr, cy, cz - zr, 1, 0.008, 0.008, 0.008, 0.004);
            level.sendParticles(col, cx - xr, cy, cz + zr, 1, 0.008, 0.008, 0.008, 0.004);

            level.sendParticles(col, cx + xr, cy + zr, cz, 1, 0.008, 0.008, 0.008, 0.004);
            level.sendParticles(col, cx - xr, cy - zr, cz, 1, 0.008, 0.008, 0.008, 0.004);
            level.sendParticles(col, cx - xr, cy + zr, cz, 1, 0.008, 0.008, 0.008, 0.004);
            level.sendParticles(col, cx + xr, cy - zr, cz, 1, 0.008, 0.008, 0.008, 0.004);

            level.sendParticles(col, cx, cy + xr, cz + zr, 1, 0.008, 0.008, 0.008, 0.004);
            level.sendParticles(col, cx, cy - xr, cz - zr, 1, 0.008, 0.008, 0.008, 0.004);
            level.sendParticles(col, cx, cy + xr, cz - zr, 1, 0.008, 0.008, 0.008, 0.004);
            level.sendParticles(col, cx, cy - xr, cz + zr, 1, 0.008, 0.008, 0.008, 0.004);
        }
    }

    private void spawnSphereRings(ServerLevel level, Vec3 c) {
        int rings = 2;
        int perRing = 64;

        double cx = c.x, cy = c.y + 0.3, cz = c.z;

        for (int r = 1; r <= rings; r++) {
            double rr = (RADIUS / rings) * r;

            for (int i = 0; i < perRing; i++) {
                double a = (2 * Math.PI * i) / perRing;
                DustParticleOptions col = pick(i);

                double x1 = cx + Math.cos(a) * rr;
                double z1 = cz + Math.sin(a) * rr;
                level.sendParticles(col, x1, cy, z1, 2, 0.02, 0.02, 0.02, 0.007);

                double y2 = cy + Math.cos(a) * rr;
                double z2 = cz + Math.sin(a) * rr;
                level.sendParticles(col, cx, y2, z2, 2, 0.02, 0.02, 0.02, 0.007);

                double x3 = cx + Math.cos(a) * rr;
                double y3 = cy + Math.sin(a) * rr;
                level.sendParticles(col, x3, y3, cz, 2, 0.02, 0.02, 0.02, 0.007);
            }
        }
    }

    private void spawnCloud(ServerLevel level, Vec3 c) {
        AreaEffectCloud cloud = new AreaEffectCloud(level, c.x, c.y + 0.1, c.z);
        cloud.setParticle(COL_MID);
        cloud.setRadius(RADIUS);
        cloud.setDuration(30);
        cloud.setRadiusPerTick(-0.15f);
        cloud.setNoGravity(true);
        level.addFreshEntity(cloud);
    }
}