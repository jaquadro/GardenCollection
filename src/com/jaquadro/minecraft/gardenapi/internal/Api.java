package com.jaquadro.minecraft.gardenapi.internal;

import com.jaquadro.minecraft.gardenapi.api.IGardenAPI;
import com.jaquadro.minecraft.gardenapi.api.IRegistryContainer;
import com.jaquadro.minecraft.gardenapi.internal.registry.RegistryContainer;

public class Api implements IGardenAPI
{
    public static Api instance = new Api();

    public RegistryContainer registries = new RegistryContainer();

    @Override
    public IRegistryContainer registries () {
        return registries;
    }
}
