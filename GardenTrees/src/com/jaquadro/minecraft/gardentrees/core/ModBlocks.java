package com.jaquadro.minecraft.gardentrees.core;

import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardentrees.GardenTrees;
import com.jaquadro.minecraft.gardentrees.block.BlockFlowerLeaves;
import com.jaquadro.minecraft.gardentrees.block.BlockGTSapling;
import com.jaquadro.minecraft.gardentrees.block.BlockThinLog;
import com.jaquadro.minecraft.gardentrees.block.BlockThinLogFence;
import com.jaquadro.minecraft.gardentrees.block.tile.TileEntityWoodProxy;
import com.jaquadro.minecraft.gardentrees.item.ItemGTSapling;
import com.jaquadro.minecraft.gardentrees.item.ItemThinLog;
import com.jaquadro.minecraft.gardentrees.item.ItemThinLogFence;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;
import org.apache.logging.log4j.Level;

public class ModBlocks
{
    public static BlockThinLog thinLog;
    public static BlockThinLogFence thinLogFence;
    public static BlockFlowerLeaves flowerLeaves;
    public static BlockGTSapling sapling;

    public void init () {
        thinLog = new BlockThinLog("thinLog");
        thinLogFence = new BlockThinLogFence("thinLogFence");
        flowerLeaves = new BlockFlowerLeaves("flowerLeaves");
        sapling = new BlockGTSapling("sapling");

        GameRegistry.registerBlock(sapling, ItemGTSapling.class, "sapling");
        //GameRegistry.registerBlock(sapling, ItemMultiTexture.class, "sapling", (Block)sapling, sapling.types);
        GameRegistry.registerBlock(thinLog, ItemThinLog.class, "thin_log");
        GameRegistry.registerBlock(thinLogFence, ItemThinLogFence.class, "thin_log_fence");
        //GameRegistry.registerBlock(flowerLeaves, "flower_leaves");

        GameRegistry.registerTileEntity(TileEntityWoodProxy.class, ModBlocks.getQualifiedName(thinLog));
    }

    public static Block get (String name) {
        return GameRegistry.findBlock(GardenTrees.MOD_ID, name);
    }

    public static String getQualifiedName (Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }

    public static UniqueMetaIdentifier getUniqueMetaID (Block block, int meta) {
        String name = GameData.getBlockRegistry().getNameForObject(block);
        if (name == null) {
            FMLLog.log(GardenTrees.MOD_ID, Level.WARN, "Tried to make a UniqueMetaIdentifier from an invalid block");
            return null;
        }

        return new UniqueMetaIdentifier(name, meta);
    }
}
