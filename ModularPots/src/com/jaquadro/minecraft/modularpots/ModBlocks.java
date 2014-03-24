package com.jaquadro.minecraft.modularpots;

import com.jaquadro.minecraft.modularpots.block.*;
import com.jaquadro.minecraft.modularpots.item.ItemLargePot;
import com.jaquadro.minecraft.modularpots.item.ItemLargePotColored;
import com.jaquadro.minecraft.modularpots.item.ItemThinLog;
import com.jaquadro.minecraft.modularpots.item.ItemThinLogFence;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityPotteryTable;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class ModBlocks
{
    public static BlockLargePot largePot;
    public static BlockLargePot largePotColored;
    public static BlockLargePotPlantProxy largePotPlantProxy;
    public static BlockPotteryTable potteryTable;
    public static BlockThinLog thinLog;
    public static BlockThinLogFence thinLogFence;
    public static BlockFlowerLeaves flowerLeaves;

    public void init () {
        largePot = new BlockLargePot("largePot", false);
        largePotColored = new BlockLargePot("largePotColored", true);
        largePotPlantProxy = new BlockLargePotPlantProxy("largePotPlantProxy");
        potteryTable = new BlockPotteryTable("potteryTable");
        thinLog = new BlockThinLog("thinLog");
        thinLogFence = new BlockThinLogFence("thinLogFence");
        flowerLeaves = new BlockFlowerLeaves("flowerLeaves");

        String MOD_ID = ModularPots.MOD_ID;

        GameRegistry.registerBlock(largePot, ItemLargePot.class, MOD_ID + ":large_pot");
        GameRegistry.registerBlock(largePotColored, ItemLargePotColored.class, MOD_ID + ":large_pot_colored");
        GameRegistry.registerBlock(largePotPlantProxy, MOD_ID + ":large_pot_plant_proxy");
        GameRegistry.registerBlock(thinLog, ItemThinLog.class, MOD_ID + ":thin_log");
        GameRegistry.registerBlock(thinLogFence, ItemThinLogFence.class, MOD_ID + ":thin_log_fence");
        GameRegistry.registerBlock(potteryTable, MOD_ID + ":pottery_table");
        //GameRegistry.registerBlock(flowerLeaves, MOD_ID + ":flower_leaves");

        GameRegistry.registerTileEntity(TileEntityLargePot.class, MOD_ID + ":large_pot");
        GameRegistry.registerTileEntity(TileEntityPotteryTable.class, MOD_ID + ":pottery_table");
    }

    public static Block get (String name) {
        return GameRegistry.findBlock(ModularPots.MOD_ID, name);
    }

    public static String getQualifiedName (Block block) {
        return GameData.blockRegistry.getNameForObject(block);
    }
}
