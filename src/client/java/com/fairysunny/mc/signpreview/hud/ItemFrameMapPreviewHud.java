package com.fairysunny.mc.signpreview.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.item.MapItem;

public class ItemFrameMapPreviewHud {
    private final Minecraft minecraft;

    public ItemFrameMapPreviewHud(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void render(GuiGraphics graphics, int mapId) {
        var level = minecraft.level;
        if (level == null) return;
        var mapData = MapItem.getSavedData(mapId, level);
        if (mapData == null) return;
        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();

        // CartographyTableScreen.renderMap
        graphics.pose().pushPose();
        graphics.pose().translate(width / 2.0F - 64.0F, height / 2.0F - 64.0F, 1.0F);
        minecraft.gameRenderer.getMapRenderer()
                .render(graphics.pose(), graphics.bufferSource(), mapId, mapData, true, LightTexture.FULL_BRIGHT);
        graphics.flush();
        graphics.pose().popPose();
    }
}
