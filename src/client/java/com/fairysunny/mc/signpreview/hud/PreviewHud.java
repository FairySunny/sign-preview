package com.fairysunny.mc.signpreview.hud;

import com.fairysunny.mc.signpreview.SignPreview;
import com.fairysunny.mc.signpreview.mixin.GameRendererAccessor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class PreviewHud {
    private final Minecraft minecraft;
    private final SignPreviewHud signPreviewHud;
    private final ItemFrameMapPreviewHud itemFrameMapPreviewHud;

    public PreviewHud(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.signPreviewHud = new SignPreviewHud(minecraft);
        this.itemFrameMapPreviewHud = new ItemFrameMapPreviewHud(minecraft);
    }

    public void render(GuiGraphics context, DeltaTracker tickCounter) {
        if (!SignPreview.KEY_BINDING_PREVIEW.isDown()) return;

        var camera = minecraft.getCameraEntity();
        var level = minecraft.level;
        if (camera == null || level == null) return;
        double maxDistance = SignPreview.CONFIG.maxPreviewDistance;
        maxDistance = Double.isNaN(maxDistance) ? 0.0 : Mth.clamp(maxDistance, 0.0, 128.0);
        float tickDelta = tickCounter.getGameTimeDeltaPartialTick(true);

        var hitResult = ((GameRendererAccessor)minecraft.gameRenderer)
                .signpreview$invokePick(camera, maxDistance, maxDistance, tickDelta);

        switch (hitResult.getType()) {
            case BLOCK:
                var blockEntity = level.getBlockEntity(((BlockHitResult)hitResult).getBlockPos());
                if (blockEntity instanceof SignBlockEntity signBlockEntity) {
                    boolean front = isCameraFacingSignFront(camera, signBlockEntity, tickDelta);
                    signPreviewHud.render(context, signBlockEntity, front);
                }
                break;
            case ENTITY:
                var entity = ((EntityHitResult)hitResult).getEntity();
                if (entity instanceof ItemFrame itemFrame) {
                    var mapId = itemFrame.getFramedMapId(itemFrame.getItem());
                    if (mapId != null) {
                        itemFrameMapPreviewHud.render(context, mapId);
                    }
                }
                break;
        }
    }

    private static boolean isCameraFacingSignFront(Entity camera, SignBlockEntity sign, float tickDelta) {
        var cameraPos = camera.getEyePosition(tickDelta);

        // SignBlockEntity.isFacingFrontText
        if (sign.getBlockState().getBlock() instanceof SignBlock signBlock) {
            var vec3 = signBlock.getSignHitboxCenterPosition(sign.getBlockState());
            double d = cameraPos.x - (sign.getBlockPos().getX() + vec3.x);
            double e = cameraPos.z - (sign.getBlockPos().getZ() + vec3.z);
            float f = signBlock.getYRotationDegrees(sign.getBlockState());
            float g = (float)(Mth.atan2(e, d) * 180 / Math.PI) - 90F;
            return Mth.degreesDifferenceAbs(f, g) <= 90F;
        }
        return false;
    }
}
