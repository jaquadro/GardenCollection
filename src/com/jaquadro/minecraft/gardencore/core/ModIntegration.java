package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.integration.*;
import cpw.mods.fml.common.Loader;

public class ModIntegration
{
    public void init () {

    }

    public void postInit () {
        //NEIIntegration.init();
        BiomesOPlentyIntegration.init();
        ExtraBiomesXLIntegration.init();
        NaturaIntegration.init();
        TwilightForestIntegration.init();
        WeeeFlowersIntegration.init();
        BotaniaIntegration.init();
        ThaumcraftIntegration.init();
        AgriCraftIntegration.init();

        if (Loader.isModLoaded("plantmegapack"))
            PlantMegaPackIntegration.init();
    }
}
