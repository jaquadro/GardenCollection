package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardenstuff.integration.*;
import cpw.mods.fml.common.Loader;

public class ModIntegration
{
    public void init () {

    }

    public void postInit () {
        if (Loader.isModLoaded("NotEnoughItems"))
            NEIIntegration.init();

        VanillaIntegration.init();
        ColoredLightsIntegration.init();
        TwilightForestIntegration.init();
        ThaumcraftIntegration.init();
    }
}
