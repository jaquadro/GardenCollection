package com.jaquadro.minecraft.gardenapi.api.component;

public interface ILanternSourceRegistry
{
    void registerLanternSource (ILanternSource lanternSource);

    ILanternSource getLanternSource (String key);
}
