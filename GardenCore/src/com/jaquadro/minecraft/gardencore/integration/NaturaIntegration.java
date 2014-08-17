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

public class NaturaIntegration
{
    public static final String MOD_ID = "Natura";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        initWood();

        PlantRegistry plantReg = PlantRegistry.instance();

        for (int i : new int[] { 0, 1, 2 })
            plantReg.registerPlantInfo(MOD_ID, "Glowshroom", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
        for (int i : new int[] { 0 })
            plantReg.registerPlantInfo(MOD_ID, "Bluebells", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.MEDIUM));

        // Redwood (0): Super tall / huge
        // Eucalyptis (1): large branching
        // Hopseed (2): ground tree, 2x2 trunk
        // Flowering Cherry (3): standard globe
        // GhostWood (4): standard globe?
        // Blood (5): upside-down
        // DarkWood (6): standard globe
        // FuseWood (7): standard globe (bit wider?)

        // Maple (0): standard globe
        // Silverbell (1): standard globe
        // Amaranth (2): tall / small
        // Tigerwood (3): standard globe
        // Willow (4): willow
    }

    private static void initWood () {
        Block logTree = GameRegistry.findBlock(MOD_ID, "tree");             // TreeBlock
        Block logRedwood = GameRegistry.findBlock(MOD_ID, "redwood");       // SimpleLog
        Block logWillow = GameRegistry.findBlock(MOD_ID, "willow");         // WillowBlock
        Block logBlood = GameRegistry.findBlock(MOD_ID, "bloodwood");       // LogTwoxTwo
        Block logDark = GameRegistry.findBlock(MOD_ID, "Dark Tree");        // DarkTreeBlock
        Block logOverworld = GameRegistry.findBlock(MOD_ID, "Rare Tree");   // OverworldTreeBlock

        Block leafNorm = GameRegistry.findBlock(MOD_ID, "floraleaves");            // NLeaves (redwood, eucalyptus, hopseed)
        Block leafNoColor = GameRegistry.findBlock(MOD_ID, "floraleavesnocolor");  // NLeavesNocolor (sakura, ghostwood, bloodwood, willow)
        Block leafDark = GameRegistry.findBlock(MOD_ID, "Dark Leaves");            // NLeavesDark (darkwood, darkwood_flower, darkwood_fruit, fusewood)
        Block leafOverworld = GameRegistry.findBlock(MOD_ID, "Rare Leaves");       // OverworldLeaves (maple, silverbell, purpleheart, tiger)

        Item saplingNorm = GameRegistry.findItem(MOD_ID, "florasapling");          // NSaplingBlock
        Item saplingOverworld = GameRegistry.findItem(MOD_ID, "Rare Sapling");     // OverworldSapling

        WoodRegistry woodReg = WoodRegistry.instance();

        woodReg.registerWoodType(logTree, 0);       // Eucalyptus
        woodReg.registerWoodType(logTree, 1);       // Sakura
        woodReg.registerWoodType(logTree, 2);       // Ghostwood
        woodReg.registerWoodType(logTree, 3);       // Hopseed

        woodReg.registerWoodType(logRedwood, 0);    // Redwood (bark)
        woodReg.registerWoodType(logRedwood, 1);    // Redwood (heart)

        woodReg.registerWoodType(logWillow, 0);     // Willow

        woodReg.registerWoodType(logBlood, 15);     // Bloodwood

        woodReg.registerWoodType(logDark, 0);       // Darkwood
        woodReg.registerWoodType(logDark, 1);       // Fusewood

        woodReg.registerWoodType(logOverworld, 0);  // Maple
        woodReg.registerWoodType(logOverworld, 1);  // Silverbell
        woodReg.registerWoodType(logOverworld, 2);  // Purpleheart
        woodReg.registerWoodType(logOverworld, 3);  // Tiger

        SaplingRegistry saplingReg = SaplingRegistry.instance();

        saplingReg.registerSapling(saplingNorm, 0, logRedwood, 0, leafNorm, 0);   // Redwood
        saplingReg.registerSapling(saplingNorm, 1, logTree, 0, leafNorm, 1);   // Eucalyptus
        saplingReg.registerSapling(saplingNorm, 2, logTree, 3, leafNorm, 2);   // Hopseed
        saplingReg.registerSapling(saplingNorm, 3, logTree, 1, leafNoColor, 0);   // Sakura
        saplingReg.registerSapling(saplingNorm, 4, logTree, 2, leafNoColor, 1);   // Ghostwood
        //saplingReg.registerSapling(saplingNorm, 5, logBlood, 1, leafNoColor, 2);   // Bloodwood
        saplingReg.registerSapling(saplingNorm, 6, logDark, 0, leafDark, 0);   // Darkwood
        saplingReg.registerSapling(saplingNorm, 7, logDark, 1, leafDark, 3);   // Fusewood

        saplingReg.registerSapling(saplingOverworld, 0, logOverworld, 0, leafOverworld, 0);   // Maple
        saplingReg.registerSapling(saplingOverworld, 1, logOverworld, 1, leafOverworld, 1);   // Silverbell
        saplingReg.registerSapling(saplingOverworld, 2, logOverworld, 2, leafOverworld, 2);   // Purpleheart
        saplingReg.registerSapling(saplingOverworld, 3, logOverworld, 3, leafOverworld, 3);   // Tiger
        saplingReg.registerSapling(saplingOverworld, 4, logWillow, 0, leafNoColor, 3);   // Willow
    }
}
