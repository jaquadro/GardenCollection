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
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class ExtraBiomesXLIntegration
{
    public static final String MOD_ID = "ExtrabiomesXL";

    public static void init () {

        if (!Loader.isModLoaded(MOD_ID))
            return;

        initWood();

        PlantRegistry plantReg = PlantRegistry.instance();

        // buttercup, lavender, toadstools
        for (int i : new int[] { 2, 3, 6 })
            plantReg.registerPlantInfo(MOD_ID, "flower1", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "flower1", 4, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL)); // tinycactus

        // marshmarigold, pansy, poppy
        for (int i : new int[] { 4, 5, 6 })
            plantReg.registerPlantInfo(MOD_ID, "flower3", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));

        plantReg.registerPlantInfo(MOD_ID, "grass", new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        plantReg.registerPlantInfo(MOD_ID, "leaf_pile", new SimplePlantInfo(PlantType.GROUND_COVER, PlantSize.FULL));
        plantReg.registerPlantInfo(MOD_ID, "vines", new SimplePlantInfo(PlantType.HANGING_SIDE, PlantSize.FULL));

        plantReg.registerPlantInfo(MOD_ID, "plants4", new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL)); // cattail
        plantReg.registerPlantInfo(MOD_ID, "waterplant1", new SimplePlantInfo(PlantType.AQUATIC, PlantSize.FULL)); // eelgrass
    }

    private static void initWood () {
        Block log1 = GameRegistry.findBlock(MOD_ID, "log1");
        Block log2 = GameRegistry.findBlock(MOD_ID, "log2");
        Block log3 = GameRegistry.findBlock(MOD_ID, "mini_log_1");

        Block leaf1 = GameRegistry.findBlock(MOD_ID, "leaves_1"); // Brown, Orange, Purple, Yellow
        Block leaf2 = GameRegistry.findBlock(MOD_ID, "leaves_2"); // Bald Cyprus, Japanese Maple, Shrub, Rainbow Eucalyptus
        Block leaf3 = GameRegistry.findBlock(MOD_ID, "leaves_3"); // Sakura
        Block leaf4 = GameRegistry.findBlock(MOD_ID, "leaves_4"); // Fir, Redwood, Acacia, Cyprus

        Item sapling = GameRegistry.findItem(MOD_ID, "saplings_1");
        Item sapling2 = GameRegistry.findItem(MOD_ID, "saplings_2");

        WoodRegistry woodReg = WoodRegistry.instance();

        woodReg.registerWoodType(log1, 0); // Fir
        woodReg.registerWoodType(log1, 1); // Acacia
        woodReg.registerWoodType(log1, 2); // Cyprus
        woodReg.registerWoodType(log1, 3); // Japanese Maple

        woodReg.registerWoodType(log2, 0); // Rainbow Eucalyptus
        woodReg.registerWoodType(log2, 1); // Autumn
        woodReg.registerWoodType(log2, 2); // Bald Cyprus
        woodReg.registerWoodType(log2, 3); // Redwood

        woodReg.registerWoodType(log3, 0); // Sakura

        SaplingRegistry saplingReg = SaplingRegistry.instance();

        saplingReg.registerSapling(sapling, 0, log2, 1, leaf1, 0); // Umber Autumn
        saplingReg.registerSapling(sapling, 1, log2, 1, leaf1, 1); // Goldenrod Autumn
        saplingReg.registerSapling(sapling, 2, log2, 1, leaf1, 2); // Vermillion Autumn
        saplingReg.registerSapling(sapling, 3, log2, 1, leaf1, 3); // Citrine Autumn
        saplingReg.registerSapling(sapling, 4, log1, 0, leaf4, 0); // Fir
        saplingReg.registerSapling(sapling, 5, log2, 3, leaf4, 1); // Redwood
        saplingReg.registerSapling(sapling, 6, log1, 1, leaf4, 2); // Acacia
        saplingReg.registerSapling(sapling, 7, log1, 2, leaf4, 3); // Cyprus

        saplingReg.registerSapling(sapling2, 0, log2, 2, leaf2, 0); // Bald Cyprus
        saplingReg.registerSapling(sapling2, 1, log1, 3, leaf2, 1); // Japanese Maple
        saplingReg.registerSapling(sapling2, 2, log1, 3, leaf2, 2); // Japanese Maple Shrub
        saplingReg.registerSapling(sapling2, 3, log2, 0, leaf2, 3); // Rainbow Eucalyptus
        saplingReg.registerSapling(sapling2, 4, log3, 0, leaf3, 0); // Sakura
    }
}
