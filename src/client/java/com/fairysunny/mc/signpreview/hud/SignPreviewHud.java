package com.fairysunny.mc.signpreview.hud;

import com.fairysunny.mc.signpreview.mixin.AbstractSignEditScreenAccessor;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.function.Consumer;

public class SignPreviewHud {
    private static class SignRenderer extends SignEditScreen implements Consumer<GuiGraphics> {
        SignRenderer(SignBlockEntity blockEntity, boolean front, Minecraft minecraft) {
            super(blockEntity, front, minecraft.isTextFilteringEnabled());

            this.minecraft = minecraft;
            font = minecraft.font;
            width = minecraft.getWindow().getGuiScaledWidth();
            init();
            ((AbstractSignEditScreenAccessor)this).signpreview$setFrame(6);
        }

        @Override
        public void accept(GuiGraphics graphics) {
            ((AbstractSignEditScreenAccessor)this).signpreview$invokeRenderSign(graphics);
        }
    }

    private static class HangingSignRenderer extends HangingSignEditScreen implements Consumer<GuiGraphics> {
        HangingSignRenderer(SignBlockEntity blockEntity, boolean front, Minecraft minecraft) {
            super(blockEntity, front, minecraft.isTextFilteringEnabled());

            this.minecraft = minecraft;
            font = minecraft.font;
            width = minecraft.getWindow().getGuiScaledWidth();
            init();
            ((AbstractSignEditScreenAccessor)this).signpreview$setFrame(6);
        }

        @Override
        public void accept(GuiGraphics graphics) {
            ((AbstractSignEditScreenAccessor)this).signpreview$invokeRenderSign(graphics);
        }
    }

    private final Minecraft minecraft;

    public SignPreviewHud(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void render(GuiGraphics graphics, SignBlockEntity blockEntity, boolean front) {
        Consumer<GuiGraphics> renderer;
        if (blockEntity instanceof HangingSignBlockEntity) {
            renderer = new HangingSignRenderer(blockEntity, front, minecraft);
        } else {
            renderer = new SignRenderer(blockEntity, front, minecraft);
        }

        Lighting.setupForFlatItems();
        renderer.accept(graphics);
        Lighting.setupFor3DItems();
    }
}
