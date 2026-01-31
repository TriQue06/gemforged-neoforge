package net.trique.gemforged.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.trique.gemforged.Gemforged;
import net.trique.gemforged.item.gear.ShadowstepDaggerItem;

@EventBusSubscriber(modid = Gemforged.MODID)
public class ShadowstepEvents {

    @SubscribeEvent
    public static void onPlayerTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide) return;

        ItemStack main = player.getMainHandItem();
        if (!(main.getItem() instanceof ShadowstepDaggerItem dagger)) {
            cleanup(player);
            return;
        }

        CustomData cd = main.get(DataComponents.CUSTOM_DATA);
        if (cd == null) return;

        CompoundTag tag = cd.copyTag();
        long until = tag.getLong(ShadowstepDaggerItem.TAG_ACTIVE_UNTIL);

        if (until > 0 && level.getGameTime() > until) {
            dagger.clearCombo(player, main);
            player.getCooldowns().addCooldown(dagger,
                    ShadowstepDaggerItem.COOLDOWN_TICKS);
        }
    }

    private static void cleanup(Player player) {
        var dmg = player.getAttribute(Attributes.ATTACK_DAMAGE);
        var spd = player.getAttribute(Attributes.ATTACK_SPEED);
        if (dmg != null)
            dmg.removeModifier(ShadowstepDaggerItem.MOD_DAMAGE_ID);
        if (spd != null)
            spd.removeModifier(ShadowstepDaggerItem.MOD_SPEED_ID);
    }
}