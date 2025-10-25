package net.trique.gemforged.item.gear;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.trique.gemforged.entity.VerdantTotemEntity;
import net.trique.gemforged.item.GemforgedItems;

public class VerdantTotemItem extends Item {

    public VerdantTotemItem(Properties props) {
        super(props.durability(250));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if (player.getCooldowns().isOnCooldown(this))
                return InteractionResultHolder.fail(stack);

            boolean creative = player.getAbilities().instabuild;
            ItemStack chargeResource = findChargeResource(player);

            if (creative || !chargeResource.isEmpty()) {
                level.playSound(null, player.blockPosition(),
                        SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS,
                        0.9F, 0.6F + level.random.nextFloat() * 0.2F);

                VerdantTotemEntity totem = new VerdantTotemEntity(level,
                        player.getX(), player.getY() + 1.0, player.getZ(), player);
                level.addFreshEntity(totem);

                player.getCooldowns().addCooldown(this, 20 * 45);

                if (!creative) {
                    chargeResource.shrink(1);
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private ItemStack findChargeResource(Player player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack s = player.getInventory().getItem(i);
            if (s.is(GemforgedItems.VERDANTITE.get())) return s;
        }
        return ItemStack.EMPTY;
    }
}
