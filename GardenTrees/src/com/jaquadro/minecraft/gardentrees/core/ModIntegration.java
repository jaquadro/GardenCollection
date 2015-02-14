package com.jaquadro.minecraft.gardentrees.core;

import com.jaquadro.minecraft.gardentrees.integration.*;

public class ModIntegration
{
    public void init () {
        TreecapitatorIntegration.init();
    }

    public void postInit () {
        GardenCoreIntegration.init();
        BiomesOPlentyIntegration.init();
        NaturaIntegration.init();
        TwilightForestIntegration.init();
        ThaumcraftIntegration.init();
        ExtraBiomesXLIntegration.init();
    }
}
