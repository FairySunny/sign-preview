package com.fairysunny.mc.signpreview.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.world.level.saveddata.maps.MapId;

public class ItemFrameMapPreviewHud {
    private final Minecraft minecraft;

    public ItemFrameMapPreviewHud(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void render(GuiGraphics graphics, MapId mapId) {
        var level = minecraft.level;
        if (level == null) return;
        var mapData = level.getMapData(mapId);
        if (mapData == null) return;
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();

        // CartographyTableScreen.renderMap
        graphics.pose().pushPose();
        graphics.pose().translate(width / 2.0F - 64.0F, height / 2.0F - 64.0F, 0.0F);
        var mapRenderer = minecraft.getMapRenderer();
        var mapRenderState = new MapRenderState();
        mapRenderer.extractRenderState(mapId, mapData, mapRenderState);
        graphics.drawSpecial(multiBufferSource ->
                mapRenderer.render(mapRenderState, graphics.pose(), multiBufferSource,
                        true, LightTexture.FULL_BRIGHT));
        graphics.pose().popPose();
    }
}
