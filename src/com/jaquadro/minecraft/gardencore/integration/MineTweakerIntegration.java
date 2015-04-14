package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardenstuff.integration.minetweaker.CompostBin;
import cpw.mods.fml.common.Loader;
import minetweaker.MineTweakerAPI;

public class MineTweakerIntegration
{
    public static final String MOD_ID = "MineTweaker3";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        MineTweakerAPI.registerClass(CompostBin.class);
    }
}
