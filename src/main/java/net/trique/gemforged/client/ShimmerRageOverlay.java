package net.trique.gemforged.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.trique.gemforged.effect.GemforgedEffects;

public class ShimmerRageOverlay {
    private static final float BASE_ALPHA   = 0.40f;
    private static final float PULSE_ALPHA  = 0.20f;
    private static final int   PERIOD_TICKS = 40;
    private static final int   PURPLE_RGB   = 0x00A020F0;

    @SubscribeEvent
    public void onOverlay(RenderGuiLayerEvent.Post event) {
        if (!event.getName().equals(VanillaGuiLayers.CROSSHAIR)) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        if (!mc.options.getCameraType().isFirstPerson()) return;

        Holder<MobEffect> shimmer = player.level().registryAccess()
                .lookupOrThrow(Registries.MOB_EFFECT)
                .getOrThrow(GemforgedEffects.SHIMMER_RAGE_KEY);
        MobEffectInstance eff = player.getEffect(shimmer);
        if (eff == null) return;

        long gt = player.level().getGameTime();
        float phase = (gt % PERIOD_TICKS) / (float) PERIOD_TICKS;
        float alpha = BASE_ALPHA + PULSE_ALPHA * Mth.sin(phase * Mth.TWO_PI);
        alpha = Mth.clamp(alpha, 0f, 1f);

        GuiGraphics gg = event.getGuiGraphics();
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();

        int a = Mth.clamp(Math.round(alpha * 255f), 0, 255);
        int color = (a << 24) | PURPLE_RGB;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        gg.fill(0, 0, w, h, color);
        RenderSystem.disableBlend();
    }
}