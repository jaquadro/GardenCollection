package com.jaquadro.minecraft.gardencommon.integration;

import com.jaquadro.minecraft.gardencommon.integration.mods.AgriCraft;
import com.jaquadro.minecraft.gardencommon.integration.mods.BiomesOPlenty;
import com.jaquadro.minecraft.gardencommon.integration.mods.Botania;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class IntegrationRegistry
{
    private static IntegrationRegistry instance;

    private List<IntegrationModule> registry;

    static {
        IntegrationRegistry reg = instance();
        if (Loader.isModLoaded("AgriCraft"))
            reg.add(new AgriCraft());
        if (Loader.isModLoaded("BiomesOPlenty"))
            reg.add(new BiomesOPlenty());
        if (Loader.isModLoaded("Botania"))
            reg.add(new Botania());
    }

    private IntegrationRegistry () {
        registry = new ArrayList<IntegrationModule>();
    }

    public static IntegrationRegistry instance () {
        if (instance == null)
            instance = new IntegrationRegistry();

        return instance;
    }

    public void add (IntegrationModule module) {
        registry.add(module);
    }

    public void init () {
        for (int i = 0; i < registry.size(); i++) {
            IntegrationModule module = registry.get(i);
            if (module.getModID() != null && !Loader.isModLoaded(module.getModID())) {
                registry.remove(i--);
                continue;
            }

            try {
                module.init();
            }
            catch (Throwable t) {
                registry.remove(i--);
                FMLLog.log(GardenStuff.MOD_ID, Level.INFO, "Could not load integration module: " + module.getClass().getName() + " (init)");
            }
        }
    }

    public void postInit () {
        for (int i = 0; i < registry.size(); i++) {
            IntegrationModule module = registry.get(i);

            try {
                module.postInit();
            }
            catch (Throwable t) {
                registry.remove(i--);
                FMLLog.log(GardenStuff.MOD_ID, Level.INFO, "Could not load integration module: " + module.getClass().getName() + " (post-init)");
            }
        }
    }
}
