package com.fairysunny.mc.signpreview.clothconfig;

import com.fairysunny.mc.signpreview.SignPreview;
import com.fairysunny.mc.signpreview.SignPreviewConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class SignPreviewConfigScreen {
    public static Screen create(Screen parent) {
        var builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("title.signpreview.config"))
                .setSavingRunnable(() -> SignPreview.CONFIG.save(SignPreviewConfig.getDefaultPath()));

        var entryBuilder = builder.entryBuilder();

        builder.getOrCreateCategory(Text.translatable("category.signpreview.general"))
                .addEntry(entryBuilder
                        .startDoubleField(Text.translatable("option.signpreview.maxPreviewDistance"),
                                SignPreview.CONFIG.maxPreviewDistance)
                        .setMin(0.0)
                        .setMax(128.0)
                        .setDefaultValue(20.0)
                        .setSaveConsumer(value -> SignPreview.CONFIG.maxPreviewDistance = value)
                        .build());

        return builder.build();
    }
}
