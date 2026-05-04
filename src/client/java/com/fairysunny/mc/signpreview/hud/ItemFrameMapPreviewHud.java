package com.fairysunny.mc.signpreview.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.MapRenderState;
import net.minecraft.component.type.MapIdComponent;

public class ItemFrameMapPreviewHud {
    private final MinecraftClient client;

    public ItemFrameMapPreviewHud(MinecraftClient client) {
        this.client = client;
    }

    public void render(DrawContext context, MapIdComponent mapId) {
        var world = this.client.world;
        if (world == null) return;
        var mapState = world.getMapState(mapId);
        if (mapState == null) return;
        int width = this.client.getWindow().getScaledWidth();
        int height = this.client.getWindow().getScaledHeight();

        context.getMatrices().push();
        context.getMatrices().translate(width / 2.0F - 64.0F, height / 2.0F - 64.0F, 0.0F);
        var mapRenderState = new MapRenderState();
        var mapRenderer = this.client.getMapRenderer();
        mapRenderer.update(mapId, mapState, mapRenderState);
        context.draw(vertexConsumers ->
                mapRenderer.draw(mapRenderState, context.getMatrices(), vertexConsumers, true, 15728880));
        context.getMatrices().pop();
    }
}
