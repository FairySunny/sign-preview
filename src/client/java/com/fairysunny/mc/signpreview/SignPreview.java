package com.fairysunny.mc.signpreview;

import com.fairysunny.mc.signpreview.hud.PreviewHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class SignPreview implements ClientModInitializer {
    public static final String MOD_ID = "signpreview";

    public static SignPreviewConfig CONFIG = new SignPreviewConfig();

    public static final Identifier HUD_LAYER_PREVIEW = Identifier.fromNamespaceAndPath(MOD_ID, "preview");

    public static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category
            .register(Identifier.fromNamespaceAndPath(MOD_ID, "general"));
    public static final KeyMapping KEY_BINDING_PREVIEW = KeyBindingHelper
            .registerKeyBinding(new KeyMapping("key.signpreview.preview", GLFW.GLFW_KEY_V, KEY_CATEGORY));

    @Override
    public void onInitializeClient() {
        var configPath = SignPreviewConfig.getDefaultPath();
        var config = SignPreviewConfig.load(configPath);
        if (config == null) {
            CONFIG.save(configPath);
        } else {
            CONFIG = config;
        }

        var previewHud = new PreviewHud(Minecraft.getInstance());
        HudElementRegistry.addLast(HUD_LAYER_PREVIEW, previewHud::render);
    }
}
