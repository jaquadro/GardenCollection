package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.block.BlockGardenProxy;
import com.jaquadro.minecraft.gardencore.block.BlockGardenSoil;
import com.jaquadro.minecraft.gardencore.block.BlockWindowBox;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import org.apache.logging.log4j.Level;

public class ModBlocks
{
    public static BlockGardenSoil gardenSoil;
    public static BlockGardenProxy gardenProxy;
    public static BlockWindowBox windowBox;

    public void init () {
        gardenSoil = new BlockGardenSoil("gardenSoil");
        gardenProxy = new BlockGardenProxy("gardenProxy");
        windowBox = new BlockWindowBox("windowBox");

        GameRegistry.registerBlock(gardenSoil, "garden_soil");
        GameRegistry.registerBlock(gardenProxy, "garden_proxy");
        GameRegistry.registerBlock(windowBox, "window_box");

        GameRegistry.registerTileEntity(TileEntityGarden.class, ModBlocks.getQualifiedName(gardenSoil));
        GameRegistry.registerTileEntity(TileEntityGarden.class, ModBlocks.getQualifiedName(windowBox));
    }

    public static Block get (String name) {
        return GameRegistry.findBlock(GardenCore.MOD_ID, name);
    }

    public static String getQualifiedName (Block block) {
        return GameData.getBlockRegistry().getNameForObject(block);
    }

    public static UniqueMetaIdentifier getUniqueMetaID (Block block, int meta) {
        String name = GameData.getBlockRegistry().getNameForObject(block);
        if (name == null) {
            FMLLog.log(GardenCore.MOD_ID, Level.WARN, "Tried to make a UniqueMetaIdentifier from an invalid block");
            return null;
        }

        return new UniqueMetaIdentifier(name, meta);
    }
}
