package net.trique.gemforged.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trique.gemforged.Gemforged;

public class GhostArrowEntityRenderer extends ArrowRenderer<GhostArrowEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Gemforged.MODID, "textures/entity/projectiles/ghost_arrow.png");

    public GhostArrowEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(GhostArrowEntity entity) {
        return TEXTURE;
    }
}