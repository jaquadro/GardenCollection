package com.jaquadro.minecraft.gardenstuff.integration;

import coloredlightscore.src.api.CLApi;
import cpw.mods.fml.common.Loader;

public class ColoredLightsIntegration
{
    public static final String MOD_ID = "easycoloredlights";

    private static int packedColors[] = new int[16];
    private static boolean initialized;

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        initPackedColors();
        initialized = true;
    }

    public static boolean isInitialized () {
        return initialized;
    }

    public static int getPackedColor(int meta) {
        return packedColors[meta];
    }

    private static void initPackedColors() {
        for(int i = 0; i < 16; i++)
            packedColors[i] = CLApi.makeRGBLightValue(CLApi.r[15 - i], CLApi.g[15 - i], CLApi.b[15 - i]);
        packedColors[15] = CLApi.makeRGBLightValue(5, 5, 5);
    }
}
