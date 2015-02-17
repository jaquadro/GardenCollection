package com.jaquadro.minecraft.gardencore.api;

import com.jaquadro.minecraft.gardencore.api.registry.IPlantRegistry;
import com.jaquadro.minecraft.gardencore.api.registry.ISaplingRegistry;
import com.jaquadro.minecraft.gardencore.api.registry.IWoodRegistry;

public interface IGardenAPI
{
    IPlantRegistry getPlantRegistry ();

    ISaplingRegistry getSaplingRegistry ();

    IWoodRegistry getWoodRegistry ();
}
