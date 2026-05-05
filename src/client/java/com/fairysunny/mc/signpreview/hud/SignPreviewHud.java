package com.fairysunny.mc.signpreview.hud;

import com.fairysunny.mc.signpreview.mixin.AbstractSignEditScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;

import java.util.function.Consumer;

public class SignPreviewHud {
    private static class SignRenderer extends SignEditScreen implements Consumer<GuiGraphics> {
        SignRenderer(SignBlockEntity blockEntity, boolean front, Minecraft client) {
            super(blockEntity, front, client.isTextFilteringEnabled());

            width = client.getWindow().getGuiScaledWidth();
            init();
            ((AbstractSignEditScreenAccessor)this).signpreview$setFrame(6);
        }

        @Override
        public void accept(GuiGraphics context) {
            ((AbstractSignEditScreenAccessor)this).signpreview$invokeRenderSign(context);
        }
    }

    private static class HangingSignRenderer extends HangingSignEditScreen implements Consumer<GuiGraphics> {
        HangingSignRenderer(SignBlockEntity blockEntity, boolean front, Minecraft client) {
            super(blockEntity, front, client.isTextFilteringEnabled());

            width = client.getWindow().getGuiScaledWidth();
            init();
            ((AbstractSignEditScreenAccessor)this).signpreview$setFrame(6);
        }

        @Override
        public void accept(GuiGraphics context) {
            ((AbstractSignEditScreenAccessor)this).signpreview$invokeRenderSign(context);
        }
    }

    private final Minecraft client;

    public SignPreviewHud(Minecraft client) {
        this.client = client;
    }

    public void render(GuiGraphics context, SignBlockEntity blockEntity, boolean front) {
        Consumer<GuiGraphics> renderer;
        if (blockEntity instanceof HangingSignBlockEntity) {
            renderer = new HangingSignRenderer(blockEntity, front, client);
        } else {
            renderer = new SignRenderer(blockEntity, front, client);
        }

        renderer.accept(context);
    }
}
