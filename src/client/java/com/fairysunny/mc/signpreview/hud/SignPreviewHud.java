package com.fairysunny.mc.signpreview.hud;

import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.WoodType;
import net.minecraft.block.entity.HangingSignBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.AbstractSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector3f;

import java.util.stream.IntStream;

public class SignPreviewHud {
    private static final Vector3f SIGN_TEXT_SCALE = new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);
    private static final Vector3f HANGING_SIGN_TEXT_SCALE = new Vector3f(1.0F, 1.0F, 1.0F);
    private final MinecraftClient client;

    public SignPreviewHud(MinecraftClient client) {
        this.client = client;
    }

    public void render(DrawContext context, SignBlockEntity blockEntity, boolean front) {
        int width = this.client.getWindow().getScaledWidth();

        DiffuseLighting.disableGuiDepthLighting();
        context.getMatrices().push();
        context.getMatrices().translate(width / 2.0F, 125.0F, 50.0F);
        context.getMatrices().push();
        this.renderSignBackground(context, blockEntity);
        context.getMatrices().pop();
        this.renderSignText(context, blockEntity, front);
        context.getMatrices().pop();
        DiffuseLighting.enableGuiDepthLighting();
    }

    private void renderSignBackground(DrawContext context, SignBlockEntity blockEntity) {
        WoodType signType = AbstractSignBlock.getWoodType(blockEntity.getCachedState().getBlock());

        if (blockEntity instanceof HangingSignBlockEntity) {
            Identifier texture = Identifier.ofVanilla("textures/gui/hanging_signs/" + signType.name() + ".png");

            context.getMatrices().translate(0.0F, -13.0F, 0.0F);
            context.getMatrices().scale(4.5F, 4.5F, 1.0F);
            context.drawTexture(RenderLayer::getGuiTextured, texture, -8, -8, 0.0F, 0.0F, 16, 16, 16, 16);
        } else {
            Model model = SignBlockEntityRenderer.createSignModel(this.client.getLoadedEntityModels(), signType, false);

            context.getMatrices().translate(0.0F, 31.0F, 0.0F);
            context.getMatrices().scale(62.500004F, 62.500004F, -62.500004F);
            context.draw(vertexConsumers -> {
                SpriteIdentifier spriteIdentifier = TexturedRenderLayers.getSignTextureId(signType);
                VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, model::getLayer);
                model.render(context.getMatrices(), vertexConsumer, 15728880, OverlayTexture.DEFAULT_UV);
            });
        }
    }

    private void renderSignText(DrawContext context, SignBlockEntity blockEntity, boolean front) {
        TextRenderer textRenderer = this.client.textRenderer;
        boolean filtered = this.client.shouldFilterText();
        SignText text = blockEntity.getText(front);
        String[] messages = IntStream.range(0, 4).mapToObj(line -> text.getMessage(line, filtered)).map(Text::getString).toArray(String[]::new);

        context.getMatrices().translate(0.0F, 0.0F, 4.0F);
        Vector3f vector3f = blockEntity instanceof HangingSignBlockEntity ? HANGING_SIGN_TEXT_SCALE : SIGN_TEXT_SCALE;
        context.getMatrices().scale(vector3f.x(), vector3f.y(), vector3f.z());
        int i = text.isGlowing() ? text.getColor().getSignColor() : AbstractSignBlockEntityRenderer.getTextColor(text);
        int l = 4 * blockEntity.getTextLineHeight() / 2;

        for (int n = 0; n < messages.length; n++) {
            String string = messages[n];
            if (string != null) {
                if (textRenderer.isRightToLeft()) {
                    string = textRenderer.mirror(string);
                }

                int o = -textRenderer.getWidth(string) / 2;
                context.drawText(textRenderer, string, o, n * blockEntity.getTextLineHeight() - l, i, false);
            }
        }
    }
}
