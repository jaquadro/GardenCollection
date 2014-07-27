package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.integration.BiomesOPlentyIntegration;
import com.jaquadro.minecraft.gardencore.integration.NaturaIntegration;
import com.jaquadro.minecraft.gardencore.integration.TwilightForestIntegration;

public class ModIntegration
{
    public void init () {

    }

    public void postInit () {
        BiomesOPlentyIntegration.init();
        NaturaIntegration.init();
        TwilightForestIntegration.init();
    }
}
