package com.fairysunny.mc.signpreview;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SignPreviewConfig {
    public double maxPreviewDistance = 20.0;

    public void save(Path path) {
        try (var writer = Files.newBufferedWriter(path)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
        } catch (IOException | JsonIOException ignored) {}
    }

    public static SignPreviewConfig load(Path path) {
        try (var reader = Files.newBufferedReader(path)) {
            return new Gson().fromJson(reader, SignPreviewConfig.class);
        } catch (IOException | JsonIOException | JsonSyntaxException e) {
            return null;
        }
    }

    public static Path getDefaultPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(SignPreview.MOD_ID + ".json");
    }
}
