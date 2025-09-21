package net.trique.gemforged.item.gear;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.trique.gemforged.entity.ZirconPrismEntity;

public class ZirconPrismItem extends Item {

    public ZirconPrismItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return InteractionResultHolder.fail(stack);
            }

            ZirconPrismEntity prism = new ZirconPrismEntity(level, player.getX(), player.getY() + 1.5, player.getZ(), player);
            prism.setItem(stack.copyWithCount(1));
            level.addFreshEntity(prism);

            if (!player.isCreative()) {
                stack.shrink(1);
            }

            player.getCooldowns().addCooldown(this, 1);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}