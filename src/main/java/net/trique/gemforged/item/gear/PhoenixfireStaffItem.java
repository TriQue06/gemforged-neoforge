package net.trique.gemforged.item.gear;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.trique.gemforged.item.GemforgedItems;
import net.trique.gemforged.particle.GemforgedParticles;

import java.util.HashSet;
import java.util.Set;

public class PhoenixfireStaffItem extends Item {

    public static final float MAGIC_DAMAGE = 3.0F;
    public static final int FIRE_SECONDS = 3;
    public static final float EXPLOSION_POWER = 1.0F;
    public static final int COOLDOWN_TICKS = 20 * 20;
    public static final int RANGE = 20;
    public static final double ENTITY_HIT_RADIUS = 1.2D;
    public static final float PARTICLE_HEIGHT_OFFSET = 1.6F;
    public static final float VERTICAL_KNOCKBACK_MULTIPLIER = 0.5F;
    public static final float HORIZONTAL_KNOCKBACK_MULTIPLIER = 2.0F;

    public PhoenixfireStaffItem(Properties props) {
        super(props.attributes(createAttributeModifiers()));
    }

    private static ItemAttributeModifiers createAttributeModifiers() {
        return ItemAttributeModifiers.builder().build();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20;
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        super.onUseTick(level, user, stack, remainingUseTicks);
        if (getUseDuration(stack, user) - remainingUseTicks == 1) {
            level.playSound(null, user.getX(), user.getY(), user.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 2.0F, 1.2F);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {
            boolean creative = player.getAbilities().instabuild;

            ItemStack fuel = findFuel(player);
            if (creative || !fuel.isEmpty()) {
                firePhoenixBeam(level, user);

                if (!creative) {
                    fuel.shrink(1);
                    player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
                    stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
                }
            }
        }
        return super.finishUsingItem(stack, level, user);
    }

    private ItemStack findFuel(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (s.is(GemforgedItems.PHOENIXTONE.get())) return s;
        }
        return ItemStack.EMPTY;
    }

    private void firePhoenixBeam(Level level, LivingEntity user) {
        level.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.PLAYERS, 4.0F, 1.0F);
        level.playSound(null, user.getX(), user.getY(), user.getZ(),
                SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 4.0F, 1.0F);

        Vec3 source = user.position().add(0.0, PARTICLE_HEIGHT_OFFSET, 0.0);
        Vec3 target = user.position().add(user.getLookAngle().scale(RANGE));
        Vec3 toTarget = target.subtract(source);
        Vec3 dir = toTarget.normalize();

        Set<Entity> hit = new HashSet<>();
        int steps = Mth.floor(toTarget.length()) + 10;
        for (int i = 1; i < steps; i++) {
            Vec3 p = source.add(dir.scale(i));

            if (level instanceof ServerLevel sl) {
                sl.sendParticles(GemforgedParticles.PHOENIX_BEAM.get(),
                        p.x, p.y, p.z,
                        1, 0, 0, 0, 0);
            }

            BlockPos bp = BlockPos.containing(p);
            AABB box = new AABB(bp).inflate(ENTITY_HIT_RADIUS);
            hit.addAll(level.getEntitiesOfClass(LivingEntity.class, box,
                    e -> e != user));
        }

        DamageSources sources = level.damageSources();
        for (Entity e : hit) {
            if (e instanceof LivingEntity living) {

                living.hurt(sources.magic(), MAGIC_DAMAGE);
                living.setRemainingFireTicks(FIRE_SECONDS * 20);

                if (level instanceof ServerLevel sl) {
                    sl.explode(user, living.getX(), living.getY(), living.getZ(),
                            EXPLOSION_POWER, Level.ExplosionInteraction.NONE);
                }

                double resist = living.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                double vertical = VERTICAL_KNOCKBACK_MULTIPLIER * (1.0 - resist);
                double horizontal = HORIZONTAL_KNOCKBACK_MULTIPLIER * (1.0 - resist);
                living.push(dir.x * horizontal, dir.y * vertical, dir.z * horizontal);
                living.hasImpulse = true;
            }
        }
    }
}