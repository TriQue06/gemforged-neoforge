package net.trique.gemforged.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.trique.gemforged.effect.PhoenixEffect;

@EventBusSubscriber(modid = "gemforged")
public class PhoenixCharmEvents {
    @SubscribeEvent
    public static void onLivingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        float damage = event.getAmount();
        float health = player.getHealth();

        if (damage >= health) {
            boolean revived = PhoenixEffect.tryRevive(player);
            if (revived) {
                event.setAmount(0.0F);
            }
        }
    }
}