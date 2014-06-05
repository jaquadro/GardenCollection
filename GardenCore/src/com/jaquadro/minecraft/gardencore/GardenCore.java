package com.jaquadro.minecraft.gardencore;

import com.jaquadro.minecraft.gardencore.core.CommonProxy;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModItems;
import com.jaquadro.minecraft.gardencore.core.handlers.GuiHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = GardenCore.MOD_ID, name = GardenCore.MOD_NAME, version = GardenCore.MOD_VERSION)
public class GardenCore
{
    public static final String MOD_ID = "gardencore";
    public static final String MOD_NAME = "Garden Core";
    public static final String MOD_VERSION = "1.0.0-1.7.2";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.gardencore.";

    private static final ModBlocks blocks = new ModBlocks();
    private static final ModItems items = new ModItems();

    @Mod.Instance(MOD_ID)
    public static GardenCore instance;

    @SidedProxy(clientSide = SOURCE_PATH + "core.ClientProxy", serverSide = SOURCE_PATH + "core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        blocks.init();
        items.init();
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        proxy.registerRenderers();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {

    }
}
