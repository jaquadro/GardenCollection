package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import com.jaquadro.minecraft.gardencore.api.SaplingRegistry;
import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.api.plant.SimplePlantInfo;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class TwilightForestIntegration
{
    public static final String MOD_ID = "TwilightForest";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        initWood();

        PlantRegistry plantReg = PlantRegistry.instance();

        plantReg.registerPlantInfo(MOD_ID, "tile.TFPlant", 3, new SimplePlantInfo(PlantType.GROUND_COVER, PlantSize.FULL));  // Moss patch
        plantReg.registerPlantInfo(MOD_ID, "tile.TFPlant", 5, new SimplePlantInfo(PlantType.GROUND_COVER, PlantSize.FULL));  // Clover patch
        plantReg.registerPlantInfo(MOD_ID, "tile.TFPlant", 13, new SimplePlantInfo(PlantType.HANGING, PlantSize.LARGE));     // Torchberry
        plantReg.registerPlantInfo(MOD_ID, "tile.TFPlant", 14, new SimplePlantInfo(PlantType.HANGING, PlantSize.FULL));      // Root srand
    }

    private static void initWood () {
        Block log = GameRegistry.findBlock(MOD_ID, "tile.TFLog");
        Block magicLog = GameRegistry.findBlock(MOD_ID, "tile.TFMagicLog");

        Block leaves = GameRegistry.findBlock(MOD_ID, "tile.TFLeaves");  // (oak, canopy, mangrove, rainbow)
        Block magicLeaves = GameRegistry.findBlock(MOD_ID, "tile.TFMagicLeaves"); // (time, trans, mine, sort)
        Block darkLeaves = GameRegistry.findBlock(MOD_ID, "tile.DarkLeaves");

        Item sapling = Item.getItemFromBlock(GameRegistry.findBlock(MOD_ID, "tile.TFSapling"));

        WoodRegistry woodReg = WoodRegistry.instance();

        woodReg.registerWoodType(log, 0);   // Oak
        woodReg.registerWoodType(log, 1);   // Canopy
        woodReg.registerWoodType(log, 2);   // Mangrove
        woodReg.registerWoodType(log, 3);   // Darkwood

        woodReg.registerWoodType(magicLog, 0);  // Time
        woodReg.registerWoodType(magicLog, 1);  // Transform
        woodReg.registerWoodType(magicLog, 2);  // Mine
        woodReg.registerWoodType(magicLog, 3);  // Sort

        SaplingRegistry saplingReg = SaplingRegistry.instance();

        saplingReg.registerSapling(sapling, 0, log, 0, leaves, 0);  // Oak
        saplingReg.registerSapling(sapling, 1, log, 1, leaves, 1);  // Canopy
        saplingReg.registerSapling(sapling, 2, log, 2, leaves, 2);  // mangrove
        saplingReg.registerSapling(sapling, 3, log, 3, darkLeaves, 0);  // Darkwood
        saplingReg.registerSapling(sapling, 4, log, 0, leaves, 0);  // Hollow Oak
        saplingReg.registerSapling(sapling, 5, magicLog, 0, magicLeaves, 0);  // Time
        saplingReg.registerSapling(sapling, 6, magicLog, 1, magicLeaves, 1);  // Transform
        saplingReg.registerSapling(sapling, 7, magicLog, 2, magicLeaves, 2);  // Mine
        saplingReg.registerSapling(sapling, 8, magicLog, 3, magicLeaves, 3);  // Sort
        saplingReg.registerSapling(sapling, 9, log, 0, leaves, 3);  // Rainbow
    }
}
