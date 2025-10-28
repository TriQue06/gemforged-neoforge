package net.trique.gemforged.entity;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.trique.gemforged.Gemforged;

public class GhostArrowRenderer extends ArrowRenderer<GhostArrow> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Gemforged.MODID, "textures/entity/projectiles/ghost_arrow.png");

    public GhostArrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(GhostArrow entity) {
        return TEXTURE;
    }
}