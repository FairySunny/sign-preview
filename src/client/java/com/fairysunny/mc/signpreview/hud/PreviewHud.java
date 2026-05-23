package com.fairysunny.mc.signpreview.hud;

import com.fairysunny.mc.signpreview.SignPreview;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class PreviewHud {
    private final Minecraft minecraft;
    private final SignPreviewHud signPreviewHud;
    private final ItemFrameMapPreviewHud itemFrameMapPreviewHud;

    public PreviewHud(Minecraft minecraft) {
        this.minecraft = minecraft;
        signPreviewHud = new SignPreviewHud(minecraft);
        itemFrameMapPreviewHud = new ItemFrameMapPreviewHud(minecraft);
    }

    public void render(GuiGraphics graphics, float tickDelta) {
        if (!SignPreview.KEY_BINDING_PREVIEW.isDown()) return;

        var camera = minecraft.getCameraEntity();
        var level = minecraft.level;
        if (camera == null || level == null) return;
        double maxDistance = SignPreview.CONFIG.maxPreviewDistance;
        maxDistance = Double.isNaN(maxDistance) ? 0.0 : Mth.clamp(maxDistance, 0.0, 128.0);

        var hitResult = pick(camera, maxDistance, tickDelta);

        switch (hitResult.getType()) {
            case BLOCK:
                var blockEntity = level.getBlockEntity(((BlockHitResult)hitResult).getBlockPos());
                if (blockEntity instanceof SignBlockEntity signBlockEntity) {
                    boolean front = isCameraFacingSignFront(camera, signBlockEntity, tickDelta);
                    signPreviewHud.render(graphics, signBlockEntity, front);
                }
                break;
            case ENTITY:
                var entity = ((EntityHitResult)hitResult).getEntity();
                if (entity instanceof ItemFrame itemFrame) {
                    var mapId = itemFrame.getFramedMapId();
                    if (mapId.isPresent()) {
                        itemFrameMapPreviewHud.render(graphics, mapId.getAsInt());
                    }
                }
                break;
        }
    }

    private static HitResult pick(Entity camera, double maxDistance, float tickDelta) {
        // GameRenderer.pick
        var hitResult = camera.pick(maxDistance, tickDelta, false);
        var vec3 = camera.getEyePosition(tickDelta);
        double d = maxDistance + 1.5;
        double e = hitResult.getLocation().distanceToSqr(vec3);
        var vec32 = camera.getViewVector(1F);
        var vec33 = vec3.add(vec32.x * d, vec32.y * d, vec32.z * d);
        var aabb = camera.getBoundingBox().expandTowards(vec32.scale(d)).inflate(1.0, 1.0, 1.0);
        var entityHitResult = ProjectileUtil.getEntityHitResult(
                camera, vec3, vec33, aabb, entity -> !entity.isSpectator() && entity.isPickable(), e);
        if (entityHitResult != null) {
            var vec34 = entityHitResult.getLocation();
            double h = vec3.distanceToSqr(vec34);
            if (h < e) {
                return entityHitResult;
            }
        }
        return hitResult;
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
