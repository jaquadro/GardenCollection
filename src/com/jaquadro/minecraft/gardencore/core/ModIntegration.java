package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencommon.integration.IntegrationRegistry;
import com.jaquadro.minecraft.gardencommon.integration.mods.Botania;
import com.jaquadro.minecraft.gardencore.integration.*;
import cpw.mods.fml.common.Loader;

public class ModIntegration
{
    public void init () {
        IntegrationRegistry.instance().init();
    }

    public void postInit () {
        IntegrationRegistry.instance().postInit();

        //BiomesOPlenty.init();
        ExtraBiomesXLIntegration.init();
        NaturaIntegration.init();
        TwilightForestIntegration.init();
        WeeeFlowersIntegration.init();
        //Botania.init();
        ThaumcraftIntegration.init();
        //AgriCraft.init();
        MineTweakerIntegration.init();
        WitcheryIntegration.init();

        if (Loader.isModLoaded("plantmegapack"))
            PlantMegaPackIntegration.init();
    }
}
