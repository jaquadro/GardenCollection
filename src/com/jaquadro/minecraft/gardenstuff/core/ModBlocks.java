package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.*;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLantern;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLatticeMetal;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLatticeWood;
import com.jaquadro.minecraft.gardenstuff.item.*;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCompressed;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraftforge.oredict.OreDictionary;

public class ModBlocks
{
    public static BlockHeavyChain heavyChain;
    public static BlockLightChain lightChain;
    public static BlockLargeMountingPlate largeMountingPlate;
    public static BlockLatticeMetal latticeMetal;
    public static BlockLatticeWood latticeWood;
    public static BlockRootCover rootCover;
    public static BlockLantern lantern;
    public static Block metalBlock;
    public static BlockFence fence;

    public void init () {
        heavyChain = new BlockHeavyChain(makeName("heavyChain"));
        lightChain = new BlockLightChain(makeName("lightChain"));
        largeMountingPlate = new BlockLargeMountingPlate(makeName("largeMountingPlate"));
        latticeMetal = new BlockLatticeMetal(makeName("latticeMetal"));
        latticeWood = new BlockLatticeWood(makeName("latticeWood"));
        rootCover = new BlockRootCover(makeName("rootCover"));
        lantern = new BlockLantern(makeName("lantern"));
        metalBlock = new BlockCompressed(MapColor.blackColor).setBlockName(makeName("metalBlock")).setCreativeTab(ModCreativeTabs.tabGardenCore).setBlockTextureName(GardenStuff.MOD_ID + ":wrought_iron_block");
        fence = new BlockFence(makeName("fence"));

        GameRegistry.registerBlock(heavyChain, ItemHeavyChain.class, "heavy_chain");
        GameRegistry.registerBlock(lightChain, ItemLightChain.class, "light_chain");
        GameRegistry.registerBlock(latticeMetal, ItemLatticeMetal.class, "lattice");
        GameRegistry.registerBlock(latticeWood, ItemLatticeWood.class, "lattice_wood");
        GameRegistry.registerBlock(rootCover, "root_cover");
        GameRegistry.registerBlock(lantern, ItemLantern.class, "lantern");
        GameRegistry.registerBlock(metalBlock, "metal_block");
        GameRegistry.registerBlock(fence, ItemFence.class, "fence");
        //GameRegistry.registerBlock(largeMountingPlate, "large_mounting_plate");

        GameRegistry.registerTileEntity(TileEntityLatticeMetal.class, ModBlocks.getQualifiedName(latticeMetal));
        GameRegistry.registerTileEntity(TileEntityLatticeWood.class, ModBlocks.getQualifiedName(latticeWood));
        GameRegistry.registerTileEntity(TileEntityLantern.class, ModBlocks.getQualifiedName(lantern));

        OreDictionary.registerOre("blockWroughtIron", metalBlock);
    }

    public static String makeName (String name) {
        return GardenStuff.MOD_ID.toLowerCase() + "." + name;
    }

    public static Block get (String name) {
        return GameRegistry.findBlock(GardenStuff.MOD_ID, name);
    }

    public static String getQualifiedName (Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }
}
