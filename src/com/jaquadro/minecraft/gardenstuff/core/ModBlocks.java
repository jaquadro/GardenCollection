package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.BlockHeavyChain;
import com.jaquadro.minecraft.gardenstuff.block.BlockLargeMountingPlate;
import com.jaquadro.minecraft.gardenstuff.block.BlockLightChain;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class ModBlocks
{
    public static BlockHeavyChain heavyChain;
    public static BlockLightChain lightChain;
    public static BlockLargeMountingPlate largeMountingPlate;

    public void init () {
        heavyChain = new BlockHeavyChain("heavyChain");
        lightChain = new BlockLightChain("lightChain");
        largeMountingPlate = new BlockLargeMountingPlate("largeMountingPlate");

        GameRegistry.registerBlock(heavyChain, "heavy_chain");
        GameRegistry.registerBlock(lightChain, "light_chain");
        //GameRegistry.registerBlock(largeMountingPlate, "large_mounting_plate");
    }

    public static Block get (String name) {
        return GameRegistry.findBlock(GardenStuff.MOD_ID, name);
    }

    public static String getQualifiedName (Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }
}
