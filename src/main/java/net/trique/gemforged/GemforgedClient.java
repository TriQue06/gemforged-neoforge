package net.trique.gemforged;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.trique.gemforged.entity.GemforgedEntities;
import net.trique.gemforged.entity.GhostArrowRenderer;

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
}