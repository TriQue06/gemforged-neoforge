package net.trique.gemforged.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;
import net.trique.gemforged.Gemforged;

public class GemforgedEntities {

    // Entity registry
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Gemforged.MODID);

    // Zircon Prism entity
    public static final DeferredHolder<EntityType<?>, EntityType<ZirconPrismEntity>> ZIRCON_PRISM =
            ENTITIES.register("zircon_prism",
                    () -> EntityType.Builder.<ZirconPrismEntity>of(ZirconPrismEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("zircon_prism"));

    // 🔹 senin istediğin register metodu
    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}