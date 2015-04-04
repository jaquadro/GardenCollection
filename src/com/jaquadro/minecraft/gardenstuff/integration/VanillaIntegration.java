package com.jaquadro.minecraft.gardenstuff.integration;

import com.jaquadro.minecraft.gardenapi.api.GardenAPI;
import com.jaquadro.minecraft.gardenapi.api.component.ILanternSourceRegistry;
import com.jaquadro.minecraft.gardenstuff.integration.lantern.VanillaLanternSource;

public class VanillaIntegration
{
    public static void init () {
        ILanternSourceRegistry registry = GardenAPI.instance().registries().lanternSources();

        registry.registerLanternSource(new VanillaLanternSource.TorchLanternSource());
        registry.registerLanternSource(new VanillaLanternSource.RedstoneTorchSource());
        registry.registerLanternSource(new VanillaLanternSource.GlowstoneSource());
    }
}
