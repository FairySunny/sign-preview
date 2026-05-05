package com.fairysunny.mc.signpreview.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.world.level.saveddata.maps.MapId;

public class ItemFrameMapPreviewHud {
    private final Minecraft client;

    public ItemFrameMapPreviewHud(Minecraft client) {
        this.client = client;
    }

    public void render(GuiGraphics context, MapId mapId) {
        var level = this.client.level;
        if (level == null) return;
        var mapData = level.getMapData(mapId);
        if (mapData == null) return;
        int width = this.client.getWindow().getGuiScaledWidth();
        int height = this.client.getWindow().getGuiScaledHeight();

        // CartographyTableScreen.renderMap
        context.pose().pushPose();
        context.pose().translate(width / 2.0F - 64.0F, height / 2.0F - 64.0F, 0.0F);
        var mapRenderer = this.client.getMapRenderer();
        var mapRenderState = new MapRenderState();
        mapRenderer.extractRenderState(mapId, mapData, mapRenderState);
        context.drawSpecial(multiBufferSource ->
                mapRenderer.render(mapRenderState, context.pose(), multiBufferSource,
                        true, LightTexture.FULL_BRIGHT));
        context.pose().popPose();
    }
}
