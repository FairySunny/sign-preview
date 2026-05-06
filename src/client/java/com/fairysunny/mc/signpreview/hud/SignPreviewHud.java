package com.fairysunny.mc.signpreview.hud;

import com.fairysunny.mc.signpreview.mixin.AbstractSignEditScreenAccessor;
import com.fairysunny.mc.signpreview.mixin.SignEditScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.HangingSignEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.renderer.blockentity.StandingSignRenderer;
import net.minecraft.world.level.block.PlainSignBlock;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.apache.commons.lang3.function.Consumers;
import org.apache.commons.lang3.function.Predicates;
import org.apache.commons.lang3.function.Suppliers;

import java.util.function.Consumer;

public class SignPreviewHud {
    private static class DummyTextFieldHelper extends TextFieldHelper {
        DummyTextFieldHelper() {
            super(() -> "", Consumers.nop(), Suppliers.nul(), Consumers.nop(), Predicates.falsePredicate());
        }

        @Override
        public int getCursorPos() {
            return -1;
        }
    }

    private static class SignRenderer extends SignEditScreen implements Consumer<GuiGraphicsExtractor> {
        SignRenderer(SignBlockEntity blockEntity, boolean front, Minecraft minecraft) {
            super(blockEntity, front, minecraft.isTextFilteringEnabled());

            width = minecraft.getWindow().getGuiScaledWidth();
            ((AbstractSignEditScreenAccessor)this).signpreview$setSignField(new DummyTextFieldHelper());

            // SignEditScreen.init
            var attachment = PlainSignBlock.getAttachmentPoint(sign.getBlockState());
            var signModel = StandingSignRenderer.createSignModel(minecraft.getEntityModels(), woodType, attachment);
            ((SignEditScreenAccessor)this).signpreview$setSignModel(signModel);
        }

        @Override
        public void accept(GuiGraphicsExtractor graphics) {
            ((AbstractSignEditScreenAccessor)this).signpreview$invokeExtractSign(graphics);
        }
    }

    private static class HangingSignRenderer extends HangingSignEditScreen implements Consumer<GuiGraphicsExtractor> {
        HangingSignRenderer(SignBlockEntity blockEntity, boolean front, Minecraft minecraft) {
            super(blockEntity, front, minecraft.isTextFilteringEnabled());

            width = minecraft.getWindow().getGuiScaledWidth();
            ((AbstractSignEditScreenAccessor)this).signpreview$setSignField(new DummyTextFieldHelper());
        }

        @Override
        public void accept(GuiGraphicsExtractor graphics) {
            ((AbstractSignEditScreenAccessor)this).signpreview$invokeExtractSign(graphics);
        }
    }

    private final Minecraft minecraft;

    public SignPreviewHud(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void render(GuiGraphicsExtractor graphics, SignBlockEntity blockEntity, boolean front) {
        Consumer<GuiGraphicsExtractor> renderer;
        if (blockEntity instanceof HangingSignBlockEntity) {
            renderer = new HangingSignRenderer(blockEntity, front, minecraft);
        } else {
            renderer = new SignRenderer(blockEntity, front, minecraft);
        }

        renderer.accept(graphics);
    }
}
