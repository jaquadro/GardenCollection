package com.jaquadro.minecraft.gardentrees.core;

import com.jaquadro.minecraft.gardentrees.integration.BiomesOPlentyIntegration;
import com.jaquadro.minecraft.gardentrees.integration.GardenCoreIntegration;
import com.jaquadro.minecraft.gardentrees.integration.NaturaIntegration;
import com.jaquadro.minecraft.gardentrees.integration.TreecapitatorIntegration;

public class ModIntegration
{
    public void init () {
        TreecapitatorIntegration.init();
    }

    public void postInit () {
        GardenCoreIntegration.init();
        BiomesOPlentyIntegration.init();
        NaturaIntegration.init();
    }
}
