package net.trique.gemforged;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.trique.gemforged.entity.GemforgedEntities;

@Mod(value = Gemforged.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = Gemforged.MODID, value = Dist.CLIENT)
public class GemforgedClient {
    public GemforgedClient(ModContainer container) {
       container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(GemforgedEntities.ZIRCON_PRISM.get(),
                ctx -> new ThrownItemRenderer<>(ctx, 1.0F, false));
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        Gemforged.LOGGER.info("HELLO FROM CLIENT SETUP");
        Gemforged.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}
