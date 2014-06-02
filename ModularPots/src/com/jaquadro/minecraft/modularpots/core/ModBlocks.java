package com.jaquadro.minecraft.modularpots.core;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.*;
import com.jaquadro.minecraft.modularpots.block.support.UniqueMetaIdentifier;
import com.jaquadro.minecraft.modularpots.item.ItemLargePot;
import com.jaquadro.minecraft.modularpots.item.ItemLargePotColored;
import com.jaquadro.minecraft.modularpots.item.ItemThinLog;
import com.jaquadro.minecraft.modularpots.item.ItemThinLogFence;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityGarden;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityPotteryTable;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityWoodProxy;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import org.apache.logging.log4j.Level;

public class ModBlocks
{
    public static BlockLargePot largePot;
    public static BlockLargePot largePotColored;
    public static BlockLargePotPlantProxy largePotPlantProxy;
    public static BlockPotteryTable potteryTable;
    public static BlockThinLog thinLog;
    public static BlockThinLogFence thinLogFence;
    public static BlockFlowerLeaves flowerLeaves;

    public static BlockGardenSoil gardenSoil;
    public static BlockGardenProxy gardenProxy;

    public void init () {
        largePot = new BlockLargePot("largePot", false);
        largePotColored = new BlockLargePot("largePotColored", true);
        largePotPlantProxy = new BlockLargePotPlantProxy("largePotPlantProxy");
        potteryTable = new BlockPotteryTable("potteryTable");
        thinLog = new BlockThinLog("thinLog");
        thinLogFence = new BlockThinLogFence("thinLogFence");
        flowerLeaves = new BlockFlowerLeaves("flowerLeaves");
        gardenSoil = new BlockGardenSoil("gardenSoil");
        gardenProxy = new BlockGardenProxy("gardenProxy");

        String MOD_ID = ModularPots.MOD_ID;

        GameRegistry.registerBlock(largePot, ItemLargePot.class, "large_pot");
        GameRegistry.registerBlock(largePotColored, ItemLargePotColored.class, "large_pot_colored");
        GameRegistry.registerBlock(largePotPlantProxy, "large_pot_plant_proxy");
        GameRegistry.registerBlock(thinLog, ItemThinLog.class, "thin_log");
        GameRegistry.registerBlock(thinLogFence, ItemThinLogFence.class, "thin_log_fence");
        GameRegistry.registerBlock(potteryTable, "pottery_table");
        //GameRegistry.registerBlock(flowerLeaves, "flower_leaves");
        GameRegistry.registerBlock(gardenSoil, "garden_soil");
        GameRegistry.registerBlock(gardenProxy, "garden_proxy");

        GameRegistry.registerTileEntity(TileEntityLargePot.class, ModBlocks.getQualifiedName(largePot));
        GameRegistry.registerTileEntity(TileEntityPotteryTable.class, ModBlocks.getQualifiedName(potteryTable));
        GameRegistry.registerTileEntity(TileEntityWoodProxy.class, ModBlocks.getQualifiedName(thinLog));
        GameRegistry.registerTileEntity(TileEntityGarden.class, ModBlocks.getQualifiedName(gardenSoil));
    }

    public static Block get (String name) {
        return GameRegistry.findBlock(ModularPots.MOD_ID, name);
    }

    public static String getQualifiedName (Block block) {
        return GameData.blockRegistry.getNameForObject(block);
    }

    public static UniqueMetaIdentifier getUniqueMetaID (Block block, int meta) {
        String name = GameData.blockRegistry.getNameForObject(block);
        if (name == null) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Tried to make a UniqueMetaIdentifier from an invalid block");
            return null;
        }

        return new UniqueMetaIdentifier(name, meta);
    }
}
