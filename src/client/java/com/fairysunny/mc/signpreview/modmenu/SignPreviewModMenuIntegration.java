package com.fairysunny.mc.signpreview.modmenu;

import com.fairysunny.mc.signpreview.clothconfig.SignPreviewConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

@SuppressWarnings("unused")
public class SignPreviewModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SignPreviewConfigScreen::create;
    }
}
