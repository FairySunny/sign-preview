package com.fairysunny.mc.signpreview.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSignEditScreen.class)
public interface AbstractSignEditScreenAccessor {
    @Accessor("signField")
    void signpreview$setSignField(TextFieldHelper signField);

    @Invoker("extractSign")
    void signpreview$invokeExtractSign(GuiGraphicsExtractor graphics);
}
