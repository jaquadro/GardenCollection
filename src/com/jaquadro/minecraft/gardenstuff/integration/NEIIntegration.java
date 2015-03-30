package com.jaquadro.minecraft.gardenstuff.integration;

import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import com.jaquadro.minecraft.gardenstuff.integration.nei.NEIHelper;
import cpw.mods.fml.common.Loader;

public class NEIIntegration
{
    public static final String MOD_ID = "NotEnoughItems";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        if (GardenStuff.proxy instanceof ClientProxy)
            NEIHelper.registerNEI();
    }
}
