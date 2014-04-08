package com.jaquadro.minecraft.modularpots.integration;

import com.jaquadro.minecraft.modularpots.block.support.SaplingRegistry;
import com.jaquadro.minecraft.modularpots.block.support.WoodRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

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

        Block leaf1 = GameRegistry.findBlock(MOD_ID, "leaves1");
        Block leaf2 = GameRegistry.findBlock(MOD_ID, "leaves2");
        Block leaf3 = GameRegistry.findBlock(MOD_ID, "leaves3");
        Block leaf4 = GameRegistry.findBlock(MOD_ID, "leaves4");
        Block leafc1 = GameRegistry.findBlock(MOD_ID, "colorizedLeaves1");
        Block leafc2 = GameRegistry.findBlock(MOD_ID, "colorizedLeaves2");
        Block leafApple = GameRegistry.findBlock(MOD_ID, "appleLeaves");
        Block leafPersimmon = GameRegistry.findBlock(MOD_ID, "persimmonLeaves");

        Item sapling = GameRegistry.findItem(MOD_ID, "saplings");
        Item sapling2 = GameRegistry.findItem(MOD_ID, "colorizedSaplings");

        // Register Wood

        WoodRegistry.registerWoodType(log1, 0);
        WoodRegistry.registerWoodType(log1, 1);
        WoodRegistry.registerWoodType(log1, 2);
        WoodRegistry.registerWoodType(log1, 3);

        WoodRegistry.registerWoodType(log2, 0);
        WoodRegistry.registerWoodType(log2, 1);
        WoodRegistry.registerWoodType(log2, 2);
        WoodRegistry.registerWoodType(log2, 3);

        WoodRegistry.registerWoodType(log3, 0);
        WoodRegistry.registerWoodType(log3, 1);
        WoodRegistry.registerWoodType(log3, 2);
        WoodRegistry.registerWoodType(log3, 3);

        WoodRegistry.registerWoodType(log4, 0);
        WoodRegistry.registerWoodType(log4, 1);
        WoodRegistry.registerWoodType(log4, 2);
        WoodRegistry.registerWoodType(log4, 3);

        // Register Saplings

        SaplingRegistry.registerSapling(sapling, 0, Blocks.log, 0, leafApple, 0, "small_oak"); // Apple Tree
        SaplingRegistry.registerSapling(sapling, 1, Blocks.log, 2, leaf1, 0, "small_oak"); // Autumn Tree
        SaplingRegistry.registerSapling(sapling, 2, bamboo, 0, leaf1, 1, "small_pine"); // Bamboo
        SaplingRegistry.registerSapling(sapling, 3, log2, 1, leaf1, 2, "small_oak"); // Magic Tree
        SaplingRegistry.registerSapling(sapling, 4, log1, 2, leaf1, 3, "small_spruce"); // Dark Tree
        SaplingRegistry.registerSapling(sapling, 5, log3, 2, leaf2, 0, "small_oak"); // Dead Tree
        SaplingRegistry.registerSapling(sapling, 6, log1, 3, leaf2, 1, "small_spruce"); // Fir Tree
        SaplingRegistry.registerSapling(sapling, 7, log2, 0, leaf2, 2, "small_spruce"); // Loftwood Tree
        SaplingRegistry.registerSapling(sapling, 8, Blocks.log2, 1, leaf2, 3, "small_oak"); // Autumn Tree
        SaplingRegistry.registerSapling(sapling, 9, Blocks.log, 0, leaf3, 0, "small_oak"); // Origin Tree
        SaplingRegistry.registerSapling(sapling, 10, log1, 1, leaf3, 1, "small_oak"); // Pink Cherry Tree
        SaplingRegistry.registerSapling(sapling, 11, Blocks.log, 0, leaf3, 2, "small_oak"); // Maple Tree
        SaplingRegistry.registerSapling(sapling, 12, log1, 1, leaf3, 3, "small_oak"); // White Cherry Tree
        //SaplingRegistry.registerSapling(sapling, 13, log4, 1, leaf4, 0, "small_oak"); // Hellbark
        SaplingRegistry.registerSapling(sapling, 14, log4, 2, leaf4, 1, "small_oak"); // Jacaranda
        SaplingRegistry.registerSapling(sapling, 15, Blocks.log, 0, leafPersimmon, 0, "small_oak"); // Persimmon Tree

        SaplingRegistry.registerSapling(sapling2, 0, log1, 0, leafc1, 0, "large_oak"); // Sacred Oak Tree
        SaplingRegistry.registerSapling(sapling2, 1, log2, 2, leafc1, 1, "small_oak"); // Mangrove Tree
        SaplingRegistry.registerSapling(sapling2, 2, log2, 3, leafc1, 2 | 4, "small_palm"); // Palm Tree
        SaplingRegistry.registerSapling(sapling2, 3, log3, 0, leafc1, 3, "small_pine"); // Redwood Tree
        SaplingRegistry.registerSapling(sapling2, 4, log3, 1, leafc2, 0, "small_willow"); // Willow Tree
        SaplingRegistry.registerSapling(sapling2, 5, log4, 0, leafc2, 1, "small_pine"); // Pine Tree
        SaplingRegistry.registerSapling(sapling2, 6, log4, 3, leafc2, 2, "small_mahogany"); // Mahogany Tree
    }
}
