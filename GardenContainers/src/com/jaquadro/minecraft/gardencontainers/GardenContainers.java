package com.jaquadro.minecraft.gardencontainers;

import com.jaquadro.minecraft.gardencontainers.config.ConfigManager;
import com.jaquadro.minecraft.gardencontainers.config.PatternConfig;
import com.jaquadro.minecraft.gardencontainers.core.CommonProxy;
import com.jaquadro.minecraft.gardencontainers.core.ModBlocks;
import com.jaquadro.minecraft.gardencontainers.core.ModItems;
import com.jaquadro.minecraft.gardencontainers.core.ModRecipes;
import com.jaquadro.minecraft.gardencontainers.core.handlers.GuiHandler;
import com.jaquadro.minecraft.gardencontainers.core.handlers.VillagerTradeHandler;
import com.jaquadro.minecraft.gardencore.api.GardenCoreAPI;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import java.io.File;

@Mod(modid = GardenContainers.MOD_ID, name = GardenContainers.MOD_NAME, version = GardenContainers.MOD_VERSION, dependencies = "required-after:GardenCore@[1.2,1.3)")
public class GardenContainers
{
    public static final String MOD_ID = "GardenContainers";
    public static final String MOD_NAME = "Garden Containers";
    public static final String MOD_VERSION = "1.2.1";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.gardencontainers.";

    public static final ModBlocks blocks = new ModBlocks();
    public static final ModItems items = new ModItems();
    public static final ModRecipes recipes = new ModRecipes();

    public static ConfigManager config;

    @Mod.Instance(MOD_ID)
    public static GardenContainers instance;

    @SidedProxy(clientSide = SOURCE_PATH + "core.ClientProxy", serverSide = SOURCE_PATH + "core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        config = new ConfigManager(new File(event.getModConfigurationDirectory(), MOD_ID + ".patterns.cfg"));

        blocks.init();
        items.init();
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        proxy.registerRenderers();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        for (int i = 1; i < 256; i++) {
            if (!config.hasPattern(i))
                continue;

            PatternConfig pattern = config.getPattern(i);
            for (int j = 0; j < pattern.getLocationCount(); j++)
                ChestGenHooks.addItem(pattern.getGenLocation(j), new WeightedRandomChestContent(items.potteryPattern, i, 1, 1, pattern.getGenRarity(j)));
        }

        VillagerTradeHandler.instance().load();
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        GardenCoreAPI.instance().registerSmallFlameHostBlock(ModBlocks.decorativePot, 0);
        recipes.init();
    }
}
