package com.jaquadro.minecraft.gardenapi.internal.registry;

import com.jaquadro.minecraft.gardenapi.api.component.ILanternSource;
import com.jaquadro.minecraft.gardenapi.api.component.ILanternSourceRegistry;
import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class LanternSourceRegistry implements ILanternSourceRegistry
{
    private Map<String, ILanternSource> registry = new HashMap<String, ILanternSource>();

    @Override
    public void registerLanternSource (ILanternSource lanternSource) {
        if (lanternSource == null)
            return;

        if (registry.containsKey(lanternSource.getSourceID())) {
            FMLLog.log("GardenStuff", Level.ERROR, "Key '%s' already registered as a lantern source.");
            return;
        }

        registry.put(lanternSource.getSourceID(), lanternSource);
    }

    @Override
    public ILanternSource getLanternSource (String key) {
        return registry.get(key);
    }

    public Collection<ILanternSource> getAllLanternSources () {
        return registry.values();
    }
}
