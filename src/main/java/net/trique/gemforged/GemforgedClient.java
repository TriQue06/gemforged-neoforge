package net.trique.gemforged;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.trique.gemforged.entity.GemforgedEntities;
import net.trique.gemforged.entity.GhostArrowRenderer;
import net.trique.gemforged.item.GemforgedItems;
import net.trique.gemforged.particle.BeamParticleTemplate;
import net.trique.gemforged.particle.GemforgedParticles;

@Mod(value = Gemforged.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Gemforged.MODID, value = Dist.CLIENT)
public class GemforgedClient {
    public GemforgedClient(ModContainer container) {
       container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GemforgedEntities.THUNDER_PRISM.get(),
                ctx -> new ThrownItemRenderer<>(ctx, 1.0F, false));
        event.registerEntityRenderer(GemforgedEntities.VERDANT_TOTEM.get(),
                ctx -> new ThrownItemRenderer<>(ctx, 1.0F, false));
        event.registerEntityRenderer(GemforgedEntities.GHOST_ARROW.get(), GhostArrowRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(GemforgedParticles.PHOENIX_BEAM.get(), BeamParticleTemplate.Provider::new);
    }

    @SubscribeEvent
    public static void registerItemModelProperties(ModelEvent.RegisterAdditional event) {
        ItemProperties.register(
                GemforgedItems.GHOST_BOW.get(),
                ResourceLocation.withDefaultNamespace("pulling"),
                (ItemStack stack, ClientLevel level, LivingEntity entity, int seed) ->
                        entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F
        );

        ItemProperties.register(
                GemforgedItems.GHOST_BOW.get(),
                ResourceLocation.withDefaultNamespace("pull"),
                (ItemStack stack, ClientLevel level, LivingEntity entity, int seed) -> {
                    if (entity == null || entity.getUseItem() != stack) return 0.0F;
                    int used = stack.getUseDuration(entity) - entity.getUseItemRemainingTicks();
                    return Math.min(1.0F, used / 20.0F);
                }
        );
    }
}