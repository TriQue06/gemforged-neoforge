package net.trique.gemforged.item.gear;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.trique.gemforged.entity.ThunderPrismEntity;

public class ThunderPrismItem extends Item {

    public ThunderPrismItem(Properties props) {
        super(props.durability(100));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this)) return InteractionResultHolder.fail(stack);

            level.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.9F, 0.6F + level.random.nextFloat() * 0.2F);

            ItemStack prismStack = stack.copy();
            prismStack.setCount(1);

            ThunderPrismEntity prism = new ThunderPrismEntity(level, player.getX(), player.getY() + 1.0, player.getZ(), player);
            prism.setItem(prismStack);
            level.addFreshEntity(prism);

            player.getCooldowns().addCooldown(this, 20 * 60);

            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}