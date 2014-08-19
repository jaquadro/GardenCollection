package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.integration.BiomesOPlentyIntegration;
import com.jaquadro.minecraft.gardencore.integration.NaturaIntegration;
import com.jaquadro.minecraft.gardencore.integration.TwilightForestIntegration;
import com.jaquadro.minecraft.gardencore.integration.WeeeFlowersIntegration;

public class ModIntegration
{
    public void init () {

    }

    public void postInit () {
        BiomesOPlentyIntegration.init();
        NaturaIntegration.init();
        TwilightForestIntegration.init();
        WeeeFlowersIntegration.init();
    }
}
