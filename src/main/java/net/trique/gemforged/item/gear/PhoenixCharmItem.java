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

    private static final DustParticleOptions COL1 =
            new DustParticleOptions(new Vector3f(0xfe / 255f, 0xc1 / 255f, 0x5c / 255f), 2.0f); // #fec15c
    private static final DustParticleOptions COL2 =
            new DustParticleOptions(new Vector3f(0xf2 / 255f, 0x6a / 255f, 0x1f / 255f), 2.0f); // #f26a1f
    private static final DustParticleOptions COL3 =
            new DustParticleOptions(new Vector3f(0xcc / 255f, 0x3e / 255f, 0x08 / 255f), 2.0f); // #cc3e08

    public PhoenixCharmItem(Properties props) {
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
    public UseAnim getUseAnimation(ItemStack stack) { return UseAnim.BOW; }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) { return USE_DURATION_TICKS; }

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
                SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.6f, 1.2f);

        AABB area = AABB.ofSize(c, 8, 8, 8);

        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area,
                e -> e.isAlive() && (e == user || e.isAlliedTo(user)));

        for (LivingEntity e : targets) {
            e.addEffect(new MobEffectInstance(GemforgedEffects.PHOENIX, EFFECT_DURATION, 0, true, true));
            spawnPhoenixWings(level, e.position());
        }
    }

    private void spawnPhoenixWings(ServerLevel level, Vec3 base) {
        for (int wing = -1; wing <= 1; wing += 2) {
            for (int i = 0; i < 80; i++) {
                double p = i / 80.0;
                double curve = Math.sin(p * Math.PI);
                double lift = Math.pow(p, 0.4) * 3.2;
                double spread = 2.2 * curve;

                double px = base.x + wing * spread;
                double py = base.y + 0.4 + lift;
                double pz = base.z + (p - 0.5) * 3.0;

                DustParticleOptions col = (i % 3 == 0) ? COL1 : (i % 3 == 1) ? COL2 : COL3;
                level.sendParticles(col, px, py, pz, 1, 0, 0, 0, 0);
            }
        }

        for (int i = 0; i < 60; i++) {
            double angle = (Math.PI * 2 * i) / 60.0;
            double r = 0.9 + (i % 3) * 0.1;
            double px = base.x + r * Math.cos(angle);
            double pz = base.z + r * Math.sin(angle);
            double py = base.y + 0.3 + (i % 6) * 0.07;

            DustParticleOptions col = (i % 3 == 0) ? COL1 : (i % 3 == 1) ? COL2 : COL3;
            level.sendParticles(col, px, py, pz, 1, 0, 0, 0, 0);
        }
    }
}