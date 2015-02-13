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

/**
 * Created by Justin on 2/12/2015.
 */
public class ThaumcraftIntegration
{
    public static final String MOD_ID = "Thaumcraft";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        initWood();

        PlantRegistry plantReg = PlantRegistry.instance();

        plantReg.registerPlantInfo(MOD_ID, "blockCustomPlant", 2, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
        plantReg.registerPlantInfo(MOD_ID, "blockCustomPlant", 4, new SimplePlantInfo(PlantType.INVALID, PlantSize.LARGE));
    }

    private static void initWood () {
        Block log = GameRegistry.findBlock(MOD_ID, "blockMagicalLog");
        Block leaf = GameRegistry.findBlock(MOD_ID, "blockMagicalLeaves");

        Item sapling = Item.getItemFromBlock(GameRegistry.findBlock(MOD_ID, "blockCustomPlant"));

        WoodRegistry woodReg = WoodRegistry.instance();
        woodReg.registerWoodType(log, 0);
        woodReg.registerWoodType(log, 1);

        SaplingRegistry saplingReg = SaplingRegistry.instance();
        saplingReg.registerSapling(sapling, 0, log, 0, leaf, 0); // Greatwood
        saplingReg.registerSapling(sapling, 1, log, 1, leaf, 1); // Silverwood
    }

    //public static void init () {
        // Fibrous Taint (0)
        // Tainted Grass (1)
        // Tainted Plant (2)
        // Spore Stalk (3)
    //}
}
