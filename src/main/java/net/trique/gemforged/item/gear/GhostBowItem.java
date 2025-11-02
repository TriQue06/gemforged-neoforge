package net.trique.gemforged.item.gear;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.trique.gemforged.entity.GemforgedEntities;
import net.trique.gemforged.entity.GhostArrowEntity;
import net.trique.gemforged.item.GemforgedItems;

public class GhostBowItem extends BowItem {

    public static final float BASE_DAMAGE = 4.0F;
    public static final float BONUS_MAGIC_DAMAGE = 1.0F;
    public static final int COOLDOWN_TICKS = 100;

    public GhostBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack bow = player.getItemInHand(hand);
        if (!hasNyxite(player)) {
            return InteractionResultHolder.fail(bow);
        }
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(bow);
    }

    private boolean hasNyxite(Player player) {
        return player.getAbilities().instabuild ||
                player.getInventory().contains(new ItemStack(GemforgedItems.NYXITE.get()));
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
        if (!(living instanceof Player player)) return;
        if (!hasNyxite(player)) return;

        int charge = this.getUseDuration(stack) - timeLeft;
        float power = getPowerForTime(charge);
        if (power < 0.1F) return;

        if (!level.isClientSide()) {
            GhostArrowEntity arrow = new GhostArrowEntity(
                    GemforgedEntities.GHOST_ARROW.get(),
                    player,
                    level,
                    new ItemStack(GemforgedItems.NYXITE.get()),
                    stack
            );

            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(),
                    0.0F, power * 3.0F, 1.0F);

            arrow.setBaseDamage(BASE_DAMAGE + BONUS_MAGIC_DAMAGE);

            if (power == 1.0F) arrow.setCritArrow(true);

            level.addFreshEntity(arrow);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                    1.0F, 1.0F + (level.getRandom().nextFloat() - 0.5F));

            if (!player.getAbilities().instabuild) {
                removeOneNyxite(player);
                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
    }

    private void removeOneNyxite(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack slot = player.getInventory().getItem(i);
            if (slot.is(GemforgedItems.NYXITE.get())) {
                slot.shrink(1);
                break;
            }
        }
    }

    public static float getPowerForTime(int time) {
        float f = (float) time / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        return Math.min(f, 1.0F);
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}