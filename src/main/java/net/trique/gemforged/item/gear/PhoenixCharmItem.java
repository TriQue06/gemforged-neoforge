package net.trique.gemforged.item.gear;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.trique.gemforged.effect.GemforgedEffects;
import net.trique.gemforged.item.GemforgedItems;

import java.util.List;

public class PhoenixCharmItem extends Item {
    private static final int USE_DURATION_TICKS = 20;
    private static final int EFFECT_DURATION = 20 * 30;
    private static final int COOLDOWN_TICKS = 20 * 30 * 5;

    private static final DustParticleOptions ORANGE =
            new DustParticleOptions(new Vector3f(0.7725f, 0.2353f, 0.0627f), 2.0f);
    private static final DustParticleOptions YELLOW =
            new DustParticleOptions(new Vector3f(0.9725f, 0.7294f, 0.3843f), 2.0f);

    public PhoenixCharmItem(Item.Properties props) {
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
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION_TICKS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (!level.isClientSide && user instanceof Player player) {
            boolean creative = player.getAbilities().instabuild;
            ItemStack chargeResource = findChargeResource(player);

            if (creative || !chargeResource.isEmpty()) {
                applyPhoenixEffect((ServerLevel) level, player);

                if (!creative) {
                    chargeResource.shrink(1);
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
            if (s.is(GemforgedItems.PHOENIXTONE.get())) return s;
        }
        return ItemStack.EMPTY;
    }

    private void applyPhoenixEffect(ServerLevel level, Player user) {
        Vec3 c = user.position();
        level.playSound(null, c.x, c.y, c.z,
                SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.2f, 1.0f);
        level.playSound(null, c.x, c.y, c.z,
                SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 0.8f, 1.2f);

        AABB area = AABB.ofSize(c, 8, 8, 8);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e.isAlive() && (e == user || e.isAlliedTo(user)));

        for (LivingEntity e : targets) {
            e.addEffect(new MobEffectInstance(GemforgedEffects.PHOENIX, EFFECT_DURATION, 0, true, true));

            Vec3 base = e.position();

            for (int wing = -1; wing <= 1; wing += 2) {
                for (int i = 0; i < 50; i++) {
                    double progress = i / 50.0;
                    double angle = progress * Math.PI / 1.2;
                    double radius = 1.5 * Math.sin(angle);
                    double px = base.x + wing * radius;
                    double py = base.y + 0.5 + progress * 2.5;
                    double pz = base.z + (progress - 0.5) * 2.0;

                    DustParticleOptions dust = (i % 2 == 0) ? ORANGE : YELLOW;
                    level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0);
                }
            }

            for (int i = 0; i < 40; i++) {
                double angle = (Math.PI * 2 * i) / 40.0;
                double radius = 0.6;
                double px = base.x + radius * Math.cos(angle);
                double pz = base.z + radius * Math.sin(angle);
                double py = base.y + 0.3 + (i % 10) * 0.1;

                DustParticleOptions dust = (i % 2 == 0) ? ORANGE : YELLOW;
                level.sendParticles(dust, px, py, pz, 1, 0, 0, 0, 0);
            }
        }
    }
}
