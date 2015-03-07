package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.*;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLatticeMetal;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLatticeWood;
import com.jaquadro.minecraft.gardenstuff.item.*;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class ModBlocks
{
    public static BlockHeavyChain heavyChain;
    public static BlockLightChain lightChain;
    public static BlockLargeMountingPlate largeMountingPlate;
    public static BlockLatticeMetal latticeMetal;
    public static BlockLatticeWood latticeWood;
    public static BlockRootCover rootCover;
    public static BlockLantern lantern;

    public void init () {
        heavyChain = new BlockHeavyChain("heavyChain");
        lightChain = new BlockLightChain("lightChain");
        largeMountingPlate = new BlockLargeMountingPlate("largeMountingPlate");
        latticeMetal = new BlockLatticeMetal("latticeMetal");
        latticeWood = new BlockLatticeWood("latticeWood");
        rootCover = new BlockRootCover("rootCover");
        lantern = new BlockLantern("lantern");

        GameRegistry.registerBlock(heavyChain, ItemHeavyChain.class, "heavy_chain");
        GameRegistry.registerBlock(lightChain, ItemLightChain.class, "light_chain");
        GameRegistry.registerBlock(latticeMetal, ItemLatticeMetal.class, "lattice");
        GameRegistry.registerBlock(latticeWood, ItemLatticeWood.class, "lattice_wood");
        GameRegistry.registerBlock(rootCover, "root_cover");
        GameRegistry.registerBlock(lantern, ItemLantern.class, "lantern");
        //GameRegistry.registerBlock(largeMountingPlate, "large_mounting_plate");

        GameRegistry.registerTileEntity(TileEntityLatticeMetal.class, ModBlocks.getQualifiedName(latticeMetal));
        GameRegistry.registerTileEntity(TileEntityLatticeWood.class, ModBlocks.getQualifiedName(latticeWood));
    }

    public static Block get (String name) {
        return GameRegistry.findBlock(GardenStuff.MOD_ID, name);
    }

    public static String getQualifiedName (Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }
}
