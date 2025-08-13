package com.fairysunny.mc.signpreview;

import com.fairysunny.mc.signpreview.hud.PreviewHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class SignPreview implements ClientModInitializer {
    public static final String MOD_ID = "signpreview";

    public static final Identifier HUD_LAYER_PREVIEW = Identifier.of(MOD_ID, "preview");

    public static final String KEY_CATEGORY = "key.categories.signpreview";
    public static final KeyBinding KEY_BINDING_PREVIEW = new KeyBinding("key.signpreview.preview", GLFW.GLFW_KEY_Z, KEY_CATEGORY);

    @Override
    public void onInitializeClient() {
        PreviewHud previewHud = new PreviewHud(MinecraftClient.getInstance());
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.addLayer(IdentifiedLayer.of(HUD_LAYER_PREVIEW, previewHud::render)));

        KeyBindingHelper.registerKeyBinding(KEY_BINDING_PREVIEW);
    }
}
