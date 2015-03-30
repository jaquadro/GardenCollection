package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardenstuff.integration.ColoredLightsIntegration;
import com.jaquadro.minecraft.gardenstuff.integration.NEIIntegration;
import com.jaquadro.minecraft.gardenstuff.integration.TwilightForestIntegration;
import cpw.mods.fml.common.Loader;

public class ModIntegration
{
    public void init () {

    }

    public void postInit () {
        if (Loader.isModLoaded("NotEnoughItems"))
            NEIIntegration.init();
        ColoredLightsIntegration.init();
        TwilightForestIntegration.init();
    }
}
