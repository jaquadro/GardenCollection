package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardenstuff.integration.ColoredLightsIntegration;
import com.jaquadro.minecraft.gardenstuff.integration.TwilightForestIntegration;

public class ModIntegration
{
    public void init () {

    }

    public void postInit () {
        ColoredLightsIntegration.init();
        TwilightForestIntegration.init();
    }
}
