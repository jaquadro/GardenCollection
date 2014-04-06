package com.jaquadro.minecraft.modularpots;

import com.jaquadro.minecraft.modularpots.addon.PlantHandlerRegistry;
import com.jaquadro.minecraft.modularpots.block.*;
import com.jaquadro.minecraft.modularpots.config.ConfigManager;
import com.jaquadro.minecraft.modularpots.config.PatternConfig;
import com.jaquadro.minecraft.modularpots.creativetab.ModularPotsCreativeTab;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;

import java.io.File;

@Mod(modid = ModularPots.MOD_ID, name = ModularPots.MOD_NAME, version = ModularPots.MOD_VERSION)
public class ModularPots
{
    public static final String MOD_ID = "modularpots";
    static final String MOD_NAME = "Modular Flower Pots";
    static final String MOD_VERSION = "1.7.2.8";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.modularpots.";

    public static CreativeTabs tabModularPots = new ModularPotsCreativeTab("modularPots");

    public static final ModBlocks blocks = new ModBlocks();
    public static final ModItems items = new ModItems();
    public static final ModRecipes recipes = new ModRecipes();
    public static final ModIntegration integration = new ModIntegration();

    public static int potteryTableGuiID = 0;

    public static ConfigManager config;

    @Mod.Instance(MOD_ID)
    public static ModularPots instance;

    @SidedProxy(clientSide = SOURCE_PATH + "client.ClientProxy", serverSide = SOURCE_PATH + "CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        config = new ConfigManager(new File(event.getModConfigurationDirectory(), MOD_ID + ".patterns.cfg"));

        blocks.init();
        items.init();
        recipes.init();
    }

    @Mod.EventHandler
    public void load (FMLInitializationEvent event) {
        proxy.registerRenderers();
        MinecraftForge.EVENT_BUS.register(this);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        for (int i = 1; i < 256; i++) {
            if (!config.hasPattern(i))
                continue;

            PatternConfig pattern = config.getPattern(i);
            for (int j = 0; j < pattern.getLocationCount(); j++)
                ChestGenHooks.addItem(pattern.getGenLocation(j), new WeightedRandomChestContent(items.potteryPattern, i, 1, 1, pattern.getGenRarity(j)));
        }

        VillagerTradeHandler.instance().load();
        integration.init();
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        PlantHandlerRegistry.init();
        integration.postInit();
    }

    @SubscribeEvent
    public void applyBonemeal (BonemealEvent event) {
        if (event.block == blocks.largePotPlantProxy) {
            BlockLargePotPlantProxy proxyBlock = blocks.largePotPlantProxy;
            event.setCanceled(!proxyBlock.applyBonemeal(event.world, event.x, event.y, event.z));
        }
    }
}
