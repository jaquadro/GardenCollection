package com.jaquadro.minecraft.modularpots.core;

import com.jaquadro.minecraft.modularpots.integration.BiomesOPlentyIntegration;
import com.jaquadro.minecraft.modularpots.integration.PlantMegaPackIntegration;
import com.jaquadro.minecraft.modularpots.integration.TreecapitatorIntegration;

public class ModIntegration
{
    public void init () {
        TreecapitatorIntegration.init();
        PlantMegaPackIntegration.init();
    }

    public void postInit () {
        BiomesOPlentyIntegration.init();

    }
}
