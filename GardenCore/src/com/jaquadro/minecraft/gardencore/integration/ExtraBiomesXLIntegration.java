package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.api.plant.SimplePlantInfo;
import cpw.mods.fml.common.Loader;

public class ExtraBiomesXLIntegration
{
    public static final String MOD_ID = "extrabiomesxl";

    public static void init () {

        if (!Loader.isModLoaded(MOD_ID))
            return;

        PlantRegistry plantReg = PlantRegistry.instance();

        // buttercup, lavender, toadstools
        for (int i : new int[] { 2, 3, 6 })
            plantReg.registerPlantInfo(MOD_ID, "flower1", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "flower1", 4, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL)); // tinycactus

        // marshmarigold, pansy, poppy
        for (int i : new int[] { 4, 5, 6 })
            plantReg.registerPlantInfo(MOD_ID, "flower3", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));

        plantReg.registerPlantInfo(MOD_ID, "tile.extrabiomes.tallgrass.extrabiomes.blocks.BlockCustomTallGrass", new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "tile.extrabiomes.leafpile.extrabiomes.blocks.BlockLeafPile", new SimplePlantInfo(PlantType.GROUND_COVER, PlantSize.FULL));
        plantReg.registerPlantInfo(MOD_ID, "tile.extrabiomes.vine.gloriosa.extrabiomes.blocks.BlockCustomVine", new SimplePlantInfo(PlantType.HANGING_SIDE, PlantSize.FULL));

        plantReg.registerPlantInfo(MOD_ID, "plants4", new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL)); // cattail
        plantReg.registerPlantInfo(MOD_ID, "waterplant1", new SimplePlantInfo(PlantType.AQUATIC, PlantSize.FULL)); // eelgrass
    }
}
