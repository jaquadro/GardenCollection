package com.jaquadro.minecraft.gardentrees;

import com.jaquadro.minecraft.gardencore.core.ModIntegration;
import com.jaquadro.minecraft.gardentrees.core.CommonProxy;
import com.jaquadro.minecraft.gardentrees.core.ModBlocks;
import com.jaquadro.minecraft.gardentrees.core.ModRecipes;
import com.jaquadro.minecraft.gardentrees.core.handlers.ForgeEventHandler;
import com.jaquadro.minecraft.gardentrees.core.handlers.FuelHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = GardenTrees.MOD_ID, name = GardenTrees.MOD_NAME, version = GardenTrees.MOD_VERSION, dependencies = "required-after:gardencore")
public class GardenTrees
{
    public static final String MOD_ID = "gardentrees";
    public static final String MOD_NAME = "Garden Trees";
    public static final String MOD_VERSION = "1.0.0-1.7.2";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.gardentrees.";

    public static final ModIntegration integration = new ModIntegration();
    public static final ModBlocks blocks = new ModBlocks();
    public static final ModRecipes recipes = new ModRecipes();

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
        integration.init();

        FMLCommonHandler.instance().bus().register(new ForgeEventHandler());

        GameRegistry.registerFuelHandler(new FuelHandler());
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        integration.postInit();
        recipes.init();
    }
}
