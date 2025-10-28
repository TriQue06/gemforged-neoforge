package net.trique.gemforged.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;
import net.trique.gemforged.Gemforged;

public class GemforgedEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Gemforged.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<ThunderPrismEntity>> THUNDER_PRISM =
            ENTITIES.register("thunder_prism",
                    () -> EntityType.Builder.<ThunderPrismEntity>of(ThunderPrismEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("thunder_prism"));

    public static final DeferredHolder<EntityType<?>, EntityType<VerdantTotemEntity>> VERDANT_TOTEM =
            ENTITIES.register("verdant_totem",
                    () -> EntityType.Builder.<VerdantTotemEntity>of(VerdantTotemEntity::new, MobCategory.MISC)
                            .sized(0.6F, 0.6F)
                            .clientTrackingRange(8)
                            .updateInterval(2)
                            .build("verdant_totem"));

    public static final DeferredHolder<EntityType<?>, EntityType<GhostArrow>> GHOST_ARROW =
            ENTITIES.register("ghost_arrow",
                    () -> EntityType.Builder.<GhostArrow>of(GhostArrow::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("ghost_arrow"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
