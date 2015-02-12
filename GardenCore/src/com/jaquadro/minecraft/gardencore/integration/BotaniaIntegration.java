package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.IPlantMetaResolver;
import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.api.plant.SimplePlantInfo;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;

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

        // Mystical and Glimmering versions
        // White Flower (0) Med
        // Orange Flower (1) Med
        // Magenta Flower (2) Sm
        // Light Blue Flower (3) Sm/Med
        // Yellow Flower (4) Lg
        // Lime Flower (5) Med
        // Pink Flower (6) Sm
        // Gray Flower (7) Med
        // Light Gray Flower (8) Med
        // Cyan Flower (9) Sm
        // Purple Flower (10) Med
        // Blue Flower (11) Med
        // Brown Flower (12) Med
        // Green Flower (13) Med
        // Red Flower (14) Med
        // Black Flower (15) Sm

        // Tile Entity
        // Pure Daisy - Sm
        // Manastar - Med

        // Black Lotus - Med
        // Void Lotus - Med
    }
}
