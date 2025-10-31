package net.trique.gemforged;

import com.mojang.logging.LogUtils;
import net.trique.gemforged.block.GemforgedBlocks;
import net.trique.gemforged.client.RageOverlay;
import net.trique.gemforged.effect.GemforgedEffects;
import net.trique.gemforged.entity.GemforgedEntities;
import net.trique.gemforged.item.GemforgedCreativeModeTabs;
import net.trique.gemforged.item.GemforgedItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.trique.gemforged.particle.GemforgedParticles;
import net.trique.gemforged.util.GemforgedLootTableModifiers;
import org.slf4j.Logger;

@Mod(Gemforged.MODID)
public class Gemforged {
    public static final String MODID = "gemforged";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Gemforged(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        GemforgedBlocks.register(modEventBus);
        GemforgedEntities.register(modEventBus);
        GemforgedItems.register(modEventBus);
        GemforgedCreativeModeTabs.register(modEventBus);
        GemforgedEffects.EFFECTS.register(modEventBus);
        GemforgedLootTableModifiers.register();
        GemforgedParticles.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(GemforgedClient::registerEntityRenderers);
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new RageOverlay());
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Things are working fine, probably.");
    }
}