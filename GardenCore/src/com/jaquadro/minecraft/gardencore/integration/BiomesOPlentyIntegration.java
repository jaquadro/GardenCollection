package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class BiomesOPlentyIntegration
{
    public static final String MOD_ID = "BiomesOPlenty";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        Block log1 = GameRegistry.findBlock(MOD_ID, "logs1");
        Block log2 = GameRegistry.findBlock(MOD_ID, "logs2");
        Block log3 = GameRegistry.findBlock(MOD_ID, "logs3");
        Block log4 = GameRegistry.findBlock(MOD_ID, "logs4");
        Block bamboo = GameRegistry.findBlock(MOD_ID, "bamboo");

        // Register Wood

        WoodRegistry woodReg = WoodRegistry.instance();

        woodReg.registerWoodType(log1, 0);
        woodReg.registerWoodType(log1, 1);
        woodReg.registerWoodType(log1, 2);
        woodReg.registerWoodType(log1, 3);

        woodReg.registerWoodType(log2, 0);
        woodReg.registerWoodType(log2, 1);
        woodReg.registerWoodType(log2, 2);
        woodReg.registerWoodType(log2, 3);

        woodReg.registerWoodType(log3, 0);
        woodReg.registerWoodType(log3, 1);
        woodReg.registerWoodType(log3, 2);
        woodReg.registerWoodType(log3, 3);

        woodReg.registerWoodType(log4, 0);
        woodReg.registerWoodType(log4, 1);
        woodReg.registerWoodType(log4, 2);
        woodReg.registerWoodType(log4, 3);

        woodReg.registerWoodType(bamboo, 0);
    }
}
