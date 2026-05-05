package com.fairysunny.mc.signpreview;

import com.fairysunny.mc.signpreview.hud.PreviewHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class SignPreview implements ClientModInitializer {
    public static final String MOD_ID = "signpreview";

    public static SignPreviewConfig CONFIG = new SignPreviewConfig();

    public static final ResourceLocation HUD_LAYER_PREVIEW =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "preview");

    public static final String KEY_CATEGORY = "key.categories.signpreview";
    public static final KeyMapping KEY_BINDING_PREVIEW =
            new KeyMapping("key.signpreview.preview", GLFW.GLFW_KEY_V, KEY_CATEGORY);

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
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer ->
                layeredDrawer.addLayer(IdentifiedLayer.of(HUD_LAYER_PREVIEW, previewHud::render)));

        KeyBindingHelper.registerKeyBinding(KEY_BINDING_PREVIEW);
    }
}
