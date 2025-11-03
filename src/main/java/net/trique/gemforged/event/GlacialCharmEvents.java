package net.trique.gemforged.event;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.trique.gemforged.effect.GemforgedEffects;
import net.trique.gemforged.item.GemforgedItems;
import net.trique.gemforged.item.gear.GlacialCharmItem;

@EventBusSubscriber(modid = "gemforged")
public class GlacialCharmEvents {

    @SubscribeEvent
    public static void onHit(LivingIncomingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        LivingEntity target = event.getEntity();
        if (target == null) return;

        if (!player.hasEffect(GemforgedEffects.GLACIAL_FIST)) return;

        boolean hasCharm =
                player.getMainHandItem().is(GemforgedItems.GLACIAL_CHARM.get()) ||
                        player.getOffhandItem().is(GemforgedItems.GLACIAL_CHARM.get());

        if (!hasCharm) return;

        target.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                20 * 5,
                2
        ));
    }
}