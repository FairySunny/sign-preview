package com.fairysunny.mc.signpreview.mixin;

import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.model.Model;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SignEditScreen.class)
public interface SignEditScreenAccessor {
    @Accessor("signModel")
    void signpreview$setSignModel(Model.Simple signModel);
}
