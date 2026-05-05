package com.fairysunny.mc.signpreview.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractSignEditScreen.class)
public interface AbstractSignEditScreenAccessor {
    @Accessor("frame")
    void signpreview$setFrame(int frame);

    @Invoker("renderSign")
    void signpreview$invokeRenderSign(GuiGraphics context);
}
