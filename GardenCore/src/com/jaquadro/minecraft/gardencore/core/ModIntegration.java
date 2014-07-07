package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.integration.BiomesOPlentyIntegration;

public class ModIntegration
{
    public void init () {

    }

    public void postInit () {
        BiomesOPlentyIntegration.init();
    }
}
