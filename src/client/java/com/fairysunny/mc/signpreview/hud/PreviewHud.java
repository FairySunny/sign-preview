package com.fairysunny.mc.signpreview.hud;

import com.fairysunny.mc.signpreview.SignPreview;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PreviewHud {
    private final MinecraftClient client;
    private final SignPreviewHud signPreviewHud;
    private final ItemFrameMapPreviewHud itemFrameMapPreviewHud;

    public PreviewHud(MinecraftClient client) {
        this.client = client;
        this.signPreviewHud = new SignPreviewHud(client);
        this.itemFrameMapPreviewHud = new ItemFrameMapPreviewHud(client);
    }

    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if (!SignPreview.KEY_BINDING_PREVIEW.isPressed()) return;

        Entity camera = this.client.getCameraEntity();
        ClientWorld world = this.client.world;
        if (camera == null || world == null) return;
        float tickDelta = tickCounter.getTickDelta(true);

        HitResult hitResult = findCrosshairTarget(camera, 20, tickDelta);

        if (hitResult instanceof BlockHitResult blockHitResult) {
            BlockEntity blockEntity = world.getBlockEntity(blockHitResult.getBlockPos());

            if (blockEntity instanceof SignBlockEntity signBlockEntity) {
                boolean front = isCameraFacingSignFront(camera, signBlockEntity, tickDelta);

                this.signPreviewHud.render(context, signBlockEntity, front);
            }
        } else if (hitResult instanceof EntityHitResult entityHitResult) {
            Entity entity = entityHitResult.getEntity();

            if (entity instanceof ItemFrameEntity itemFrameEntity) {
                MapIdComponent mapId = itemFrameEntity.getMapId(itemFrameEntity.getHeldItemStack());
                if (mapId == null) return;

                this.itemFrameMapPreviewHud.render(context, mapId);
            }
        }
    }

    private HitResult findCrosshairTarget(Entity camera, double maxDistance, float tickDelta) {
        double d = maxDistance;
        double e = MathHelper.square(d);
        Vec3d vec3d = camera.getCameraPosVec(tickDelta);
        HitResult hitResult = camera.raycast(d, tickDelta, false);
        double f = hitResult.getPos().squaredDistanceTo(vec3d);
        if (hitResult.getType() != HitResult.Type.MISS) {
            e = f;
            d = Math.sqrt(f);
        }

        Vec3d vec3d2 = camera.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
        Box box = camera.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(camera, vec3d, vec3d3, box, EntityPredicates.CAN_HIT, e);
        return entityHitResult != null && entityHitResult.getPos().squaredDistanceTo(vec3d) < f ? entityHitResult : hitResult;
    }

    private static boolean isCameraFacingSignFront(Entity camera, SignBlockEntity sign, float tickDelta) {
        Vec3d cameraPos = camera.getCameraPosVec(tickDelta);
        if (sign.getCachedState().getBlock() instanceof AbstractSignBlock abstractSignBlock) {
            Vec3d vec3d = abstractSignBlock.getCenter(sign.getCachedState());
            double d = cameraPos.x - (sign.getPos().getX() + vec3d.x);
            double e = cameraPos.z - (sign.getPos().getZ() + vec3d.z);
            float f = abstractSignBlock.getRotationDegrees(sign.getCachedState());
            float g = (float)(MathHelper.atan2(e, d) * 180.0F / (float)Math.PI) - 90.0F;
            return MathHelper.angleBetween(f, g) <= 90.0F;
        } else {
            return false;
        }
    }
}
