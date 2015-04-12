package com.jaquadro.minecraft.gardenapi.internal.registry;

import com.jaquadro.minecraft.gardenapi.api.IRegistryContainer;
import com.jaquadro.minecraft.gardenapi.api.component.ILanternSourceRegistry;
import com.jaquadro.minecraft.gardenapi.api.connect.IAttachableRegistry;
import com.jaquadro.minecraft.gardenapi.api.machine.ICompostRegistry;
import com.jaquadro.minecraft.gardenapi.api.plant.IPlantRegistry;
import com.jaquadro.minecraft.gardenapi.api.plant.ISaplingRegistry;
import com.jaquadro.minecraft.gardenapi.api.plant.IWoodRegistry;

public class RegistryContainer implements IRegistryContainer
{
    public LanternSourceRegistry lanternSources = new LanternSourceRegistry();
    public CompostRegistry compost = new CompostRegistry();
    public AttachableRegistry attachable = new AttachableRegistry();

    @Override
    public IPlantRegistry plants () {
        return null;
    }

    @Override
    public ISaplingRegistry saplings () {
        return null;
    }

    @Override
    public IWoodRegistry wood () {
        return null;
    }

    @Override
    public ILanternSourceRegistry lanternSources () {
        return lanternSources;
    }

    @Override
    public ICompostRegistry compost () {
        return compost;
    }

    @Override
    public IAttachableRegistry attachable () {
        return attachable;
    }
}
