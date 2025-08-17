package com.fairysunny.mc.signpreview;

import com.fairysunny.mc.signpreview.hud.PreviewHud;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;

public class SignPreview implements ClientModInitializer {
    public static final String MOD_ID = "signpreview";

    public static SignPreviewConfig CONFIG = new SignPreviewConfig();

    public static final Identifier HUD_LAYER_PREVIEW = Identifier.of(MOD_ID, "preview");

    public static final String KEY_CATEGORY = "key.categories.signpreview";
    public static final KeyBinding KEY_BINDING_PREVIEW = new KeyBinding("key.signpreview.preview", GLFW.GLFW_KEY_V, KEY_CATEGORY);

    @Override
    public void onInitializeClient() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json");
        SignPreviewConfig config = SignPreviewConfig.load(configPath);
        if (config == null) {
            CONFIG.save(configPath);
        } else {
            CONFIG = config;
        }

        PreviewHud previewHud = new PreviewHud(MinecraftClient.getInstance());
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.addLayer(IdentifiedLayer.of(HUD_LAYER_PREVIEW, previewHud::render)));

        KeyBindingHelper.registerKeyBinding(KEY_BINDING_PREVIEW);
    }
}
