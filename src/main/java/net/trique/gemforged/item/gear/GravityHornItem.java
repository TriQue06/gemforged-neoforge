package net.trique.gemforged.item.gear;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

import java.util.List;

public class GravityHornItem extends Item {
    private static final float RADIUS = 16f;
    private static final int COOLDOWN_TICKS = 1;
    private static final int USE_DURATION_TICKS = 20;

    private static final DustParticleOptions PURPLE =
            new DustParticleOptions(new Vector3f(0.7f, 0.2f, 0.9f), 2.0f);
    private static final DustParticleOptions LILAC =
            new DustParticleOptions(new Vector3f(0.9f, 0.5f, 1.0f), 2.0f);

    public GravityHornItem(Item.Properties props) {
        super(props.durability(250));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!level.isClientSide) {
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 2.0f, 0.6f);
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 2.5f, 0.7f);
        }
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.TOOT_HORN; }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) { return USE_DURATION_TICKS; }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {
            triggerBurst((ServerLevel) level, player);
            stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }
        return super.finishUsingItem(stack, level, user);
    }

    private void triggerBurst(ServerLevel level, Player player) {
        Vec3 c = player.position();

        level.playSound(null, c.x, c.y, c.z,
                SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 3.0f, 0.5f);
        level.playSound(null, c.x, c.y, c.z,
                SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 3.0f, 0.8f);
        level.playSound(null, c.x, c.y, c.z,
                SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, 2.5f, 0.9f);

        spawnRing(level, c.add(0, 0.3, 0), RADIUS, 800, PURPLE, 0.7f);
        spawnRing(level, c.add(0, 0.35, 0), RADIUS, 800, LILAC, 0.7f);

        spawnStar(level, c.add(0, 0.5, 0), 9.0, 5, PURPLE);
        spawnStar(level, c.add(0, 0.5, 0), 9.0, 5, LILAC);

        // Oyuncu merkezli 16x16x16 küp
        AABB box = new AABB(
                c.x - 8, c.y - 8, c.z - 8,
                c.x + 8, c.y + 8, c.z + 8
        );

        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, box,
                e -> e.isAlive() && e != player);

        for (LivingEntity e : targets) {
            e.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 20 * 5, 4, true, true));
        }
    }

    private void spawnRing(ServerLevel level, Vec3 center, float radius, int points, DustParticleOptions dust, float heightSpread) {
        double cx = center.x, cy = center.y, cz = center.z;
        for (int i = 0; i < points; i++) {
            double a = (Math.PI * 2 * i) / points;
            double px = cx + radius * Math.cos(a);
            double pz = cz + radius * Math.sin(a);
            double py = cy + (level.random.nextDouble() - 0.5) * heightSpread * 2;
            level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0.0);
        }
    }

    private void spawnStar(ServerLevel level, Vec3 center, double radius, int points, DustParticleOptions dust) {
        double cx = center.x, cy = center.y, cz = center.z;
        double[] xs = new double[points];
        double[] zs = new double[points];

        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points - Math.PI / 2;
            xs[i] = cx + radius * Math.cos(angle);
            zs[i] = cz + radius * Math.sin(angle);
        }

        for (int i = 0; i < points; i++) {
            int j = (i + 2) % points;
            spawnLine(level, cy, xs[i], zs[i], xs[j], zs[j], dust);
        }
    }

    private void spawnLine(ServerLevel level, double cy, double x1, double z1, double x2, double z2, DustParticleOptions dust) {
        int steps = 60;
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            double px = x1 + (x2 - x1) * t;
            double pz = z1 + (z2 - z1) * t;
            level.sendParticles(dust, px, cy, pz, 1, 0, 0, 0, 0.0);
        }
    }
}
