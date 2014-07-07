package com.jaquadro.minecraft.gardencontainers.core;

import com.jaquadro.minecraft.gardencontainers.GardenContainers;
import com.jaquadro.minecraft.gardencontainers.block.*;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityLargePot;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityPotteryTable;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityWindowBox;
import com.jaquadro.minecraft.gardencontainers.item.ItemLargePot;
import com.jaquadro.minecraft.gardencontainers.item.ItemWindowBox;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenSingle;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import org.apache.logging.log4j.Level;

public class ModBlocks
{
    public static BlockWindowBox windowBox;
    public static BlockDecorativePot decorativePot;
    public static BlockLargePot largePot;
    public static BlockLargePot largePotColored;
    public static BlockPotteryTable potteryTable;

    public void init () {
        windowBox = new BlockWindowBox("windowBox");
        decorativePot = new BlockDecorativePot("decorativePot");
        largePot = new BlockLargePotStandard("largePot");
        largePotColored = new BlockLargePotColored("largePotColored");
        potteryTable = new BlockPotteryTable("potteryTable");

        GameRegistry.registerBlock(windowBox, ItemWindowBox.class, "window_box");
        GameRegistry.registerBlock(decorativePot, "decorative_pot");
        GameRegistry.registerBlock(largePot, ItemLargePot.class, "large_pot");
        GameRegistry.registerBlock(largePotColored, ItemLargePot.class, "large_pot_colored");
        GameRegistry.registerBlock(potteryTable, "pottery_table");

        GameRegistry.registerTileEntity(TileEntityWindowBox.class, ModBlocks.getQualifiedName(windowBox));
        GameRegistry.registerTileEntity(TileEntityGardenSingle.class, ModBlocks.getQualifiedName(decorativePot));
        GameRegistry.registerTileEntity(TileEntityLargePot.class, ModBlocks.getQualifiedName(largePot));
        GameRegistry.registerTileEntity(TileEntityLargePot.class, ModBlocks.getQualifiedName(largePotColored));
        GameRegistry.registerTileEntity(TileEntityPotteryTable.class, ModBlocks.getQualifiedName(potteryTable));
    }

    public static Block get (String name) {
        return GameRegistry.findBlock(GardenContainers.MOD_ID, name);
    }

    public static String getQualifiedName (Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }

    public static UniqueMetaIdentifier getUniqueMetaID (Block block, int meta) {
        String name = GameData.getBlockRegistry().getNameForObject(block);
        if (name == null) {
            FMLLog.log(GardenContainers.MOD_ID, Level.WARN, "Tried to make a UniqueMetaIdentifier from an invalid block");
            return null;
        }

        return new UniqueMetaIdentifier(name, meta);
    }
}
