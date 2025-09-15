package net.trique.gemforged;

import com.mojang.logging.LogUtils;
import net.trique.gemforged.client.GarnetRageOverlay;
import net.trique.gemforged.client.ShimmerRageOverlay;
import net.trique.gemforged.effect.GemforgedEffects;
import net.trique.gemforged.item.GemforgedCreativeModeTabs;
import net.trique.gemforged.item.GemforgedItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(Gemforged.MODID)
public class Gemforged {
    public static final String MODID = "gemforged";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Gemforged(IEventBus modEventBus, ModContainer modContainer) {
        // ---- Mod lifecycle ----
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        // ---- Registries ----
        GemforgedItems.register(modEventBus);
        GemforgedCreativeModeTabs.register(modEventBus);
        GemforgedEffects.EFFECTS.register(modEventBus);

        // ---- Config ----
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        // ---- Global (game) event'leri dinlemek için bu sınıfı kaydet ----
        NeoForge.EVENT_BUS.register(this);

        // ---- CLIENT event'leri: RageOverlay sadece client'ta kaydedilir ----
        if (FMLEnvironment.dist.isClient()) {
            NeoForge.EVENT_BUS.register(new GarnetRageOverlay());
            NeoForge.EVENT_BUS.register(new ShimmerRageOverlay());
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Things are working fine, probably.");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // creative tab içerikleri ekliyorsan burada kalabilir
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Gemforged summons magic properly!");
    }
}
