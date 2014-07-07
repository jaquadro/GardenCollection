package com.jaquadro.minecraft.gardentrees;

import com.jaquadro.minecraft.gardentrees.core.CommonProxy;
import com.jaquadro.minecraft.gardentrees.core.ModBlocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GardenTrees.MOD_ID, name = GardenTrees.MOD_NAME, version = GardenTrees.MOD_VERSION, dependencies = "required-after:gardencore")
public class GardenTrees
{
    public static final String MOD_ID = "gardentrees";
    public static final String MOD_NAME = "Garden Trees";
    public static final String MOD_VERSION = "1.0.0-1.7.2";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.gardentrees.";

    private static final ModBlocks blocks = new ModBlocks();

    @Mod.Instance(MOD_ID)
    public static GardenTrees instance;

    @SidedProxy(clientSide = SOURCE_PATH + "core.ClientProxy", serverSide = SOURCE_PATH + "core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        blocks.init();
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        proxy.registerRenderers();
    }
}
