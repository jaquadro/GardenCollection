package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.api.plant.SimplePlantInfo;
import cpw.mods.fml.common.Loader;

public class BotaniaIntegration
{
    public static final String MOD_ID = "Botania";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        PlantRegistry plantReg = PlantRegistry.instance();

        plantReg.registerPlantRenderer(MOD_ID, "flower", PlantRegistry.CROSSED_SQUARES_RENDERER);
        plantReg.registerPlantRenderer(MOD_ID, "shinyFlower", PlantRegistry.CROSSED_SQUARES_RENDERER);

        for (int i : new int[] { 2, 3, 6, 9, 15 }) {
            plantReg.registerPlantInfo(MOD_ID, "flower", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
            plantReg.registerPlantInfo(MOD_ID, "shinyFlower", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
        }
        for (int i : new int[] { 4 }) {
            plantReg.registerPlantInfo(MOD_ID, "flower", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
            plantReg.registerPlantInfo(MOD_ID, "shinyFlower", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        }

        plantReg.registerPlantInfo(MOD_ID, "specialFlower", new SimplePlantInfo(PlantType.INVALID, PlantSize.MEDIUM));
    }
}
