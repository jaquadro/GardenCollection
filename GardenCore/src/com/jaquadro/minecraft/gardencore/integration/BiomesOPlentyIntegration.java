package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.SaplingRegistry;
import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
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

        SaplingRegistry saplingReg = SaplingRegistry.instance();

        saplingReg.registerSapling(sapling, 0, Blocks.log, 0, leafApple, 0);
        saplingReg.registerSapling(sapling, 1, Blocks.log, 2, leaf1, 0); // Autumn Tree
        saplingReg.registerSapling(sapling, 2, bamboo, 0, leaf1, 1); // Bamboo
        saplingReg.registerSapling(sapling, 3, log2, 1, leaf1, 2); // Magic Tree
        saplingReg.registerSapling(sapling, 4, log1, 2, leaf1, 3); // Dark Tree
        saplingReg.registerSapling(sapling, 5, log3, 2, leaf2, 0); // Dead Tree
        saplingReg.registerSapling(sapling, 6, log1, 3, leaf2, 1); // Fir Tree
        saplingReg.registerSapling(sapling, 7, log2, 0, leaf2, 2); // Loftwood Tree
        saplingReg.registerSapling(sapling, 8, Blocks.log2, 1, leaf2, 3); // Autumn Tree
        saplingReg.registerSapling(sapling, 9, Blocks.log, 0, leaf3, 0); // Origin Tree
        saplingReg.registerSapling(sapling, 10, log1, 1, leaf3, 1); // Pink Cherry Tree
        saplingReg.registerSapling(sapling, 11, Blocks.log, 0, leaf3, 2); // Maple Tree
        saplingReg.registerSapling(sapling, 12, log1, 1, leaf3, 3); // White Cherry Tree
        saplingReg.registerSapling(sapling, 13, log4, 1, leaf4, 0); // Hellbark
        saplingReg.registerSapling(sapling, 14, log4, 2, leaf4, 1); // Jacaranda
        saplingReg.registerSapling(sapling, 15, Blocks.log, 0, leafPersimmon, 0); // Persimmon Tree

        saplingReg.registerSapling(sapling2, 0, log1, 0, leafc1, 0); // Sacred Oak Tree
        saplingReg.registerSapling(sapling2, 1, log2, 2, leafc1, 1); // Mangrove Tree
        saplingReg.registerSapling(sapling2, 2, log2, 3, leafc1, 2 | 4); // Palm Tree
        saplingReg.registerSapling(sapling2, 3, log3, 0, leafc1, 3); // Redwood Tree
        saplingReg.registerSapling(sapling2, 4, log3, 1, leafc2, 0); // Willow Tree
        saplingReg.registerSapling(sapling2, 5, log4, 0, leafc2, 1); // Pine Tree
        saplingReg.registerSapling(sapling2, 6, log4, 3, leafc2, 2); // Mahogany Tree
    }
}
