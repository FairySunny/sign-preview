package com.fairysunny.mc.signpreview.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.world.level.saveddata.maps.MapId;

public class ItemFrameMapPreviewHud {
    private final Minecraft minecraft;

    public ItemFrameMapPreviewHud(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void render(GuiGraphicsExtractor graphics, MapId mapId) {
        var level = this.minecraft.level;
        if (level == null) return;
        var mapData = level.getMapData(mapId);
        if (mapData == null) return;
        int width = this.minecraft.getWindow().getGuiScaledWidth();
        int height = this.minecraft.getWindow().getGuiScaledHeight();

        // CartographyTableScreen.renderMap
        graphics.pose().pushMatrix();
        graphics.pose().translate(width / 2.0F - 64.0F, height / 2.0F - 64.0F);
        var mapRenderState = new MapRenderState();
        this.minecraft.getMapRenderer().extractRenderState(mapId, mapData, mapRenderState);
        graphics.map(mapRenderState);
        graphics.pose().popMatrix();
    }
}
