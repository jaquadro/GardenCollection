package com.jaquadro.minecraft.gardenstuff;

import com.jaquadro.minecraft.gardenstuff.core.CommonProxy;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GardenStuff.MOD_ID, name = GardenStuff.MOD_NAME, version = GardenStuff.MOD_VERSION, dependencies = "required-after:GardenCore")
public class GardenStuff
{
    public static final String MOD_ID = "GardenStuff";
    public static final String MOD_NAME = "Garden Stuff";
    public static final String MOD_VERSION = "@VERSION@";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.gardenstuff.";

    @Mod.Instance(MOD_ID)
    public static GardenStuff instance;

    @SidedProxy(clientSide = SOURCE_PATH + "core.ClientProxy", serverSide = SOURCE_PATH + "core.CommonProxy")
    public static CommonProxy proxy;

    public static final ModBlocks blocks = new ModBlocks();

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        blocks.init();
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        proxy.registerRenderers();
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
    }

}
