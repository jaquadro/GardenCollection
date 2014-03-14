package com.jaquadro.minecraft.modularpots;

import com.jaquadro.minecraft.modularpots.block.*;
import com.jaquadro.minecraft.modularpots.creativetab.ModularPotsCreativeTab;
import com.jaquadro.minecraft.modularpots.item.*;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.BonemealEvent;

@Mod(modid = ModularPots.MOD_ID, name = ModularPots.MOD_NAME, version = ModularPots.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = true)
public class ModularPots
{
    public static final String MOD_ID = "modularpots";
    static final String MOD_NAME = "Modular Flower Pots";
    static final String MOD_VERSION = "1.6.4.0";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.modularpots.";

    public static CreativeTabs tabModularPots = new ModularPotsCreativeTab("modularPots");

    public static int largePotId;
    public static int largePotColoredId;
    public static int largePotPlantProxyId;
    public static int thinLogId;
    public static int thinLogFenceId;
    //private static int flowerLeavesId;

    public static int soilTestKitId;
    public static int soilTestKitUsedId;

    public static Block largePot;
    public static Block largePotColored;
    public static Block largePotPlantProxy;
    public static Block thinLog;
    public static Block thinLogFence;
    //public static Block flowerLeaves;

    public static Item soilTestKit;
    public static Item soilTestKitUsed;

    @Mod.Instance(MOD_ID)
    public static ModularPots instance;

    @SidedProxy(clientSide = SOURCE_PATH + "client.ClientProxy", serverSide = SOURCE_PATH + "CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        largePotId = config.getBlock(MOD_ID + ":large_pot", 570).getInt();
        largePotColoredId = config.getBlock(MOD_ID + ":large_pot_colored", 571).getInt();
        largePotPlantProxyId = config.getBlock(MOD_ID + ":large_pot_plant_proxy", 572).getInt();
        thinLogId = config.getBlock(MOD_ID + ":thin_log", 573).getInt();
        thinLogFenceId = config.getBlock(MOD_ID + ":thin_log_fence", 574).getInt();
        //flowerLeavesId = config.getBlock(MOD_ID + ":large_pot", 570).getInt();

        soilTestKitId = config.getItem(MOD_ID + ":soil_test_kit", 14070).getInt();
        soilTestKitUsedId = config.getItem(MOD_ID + ":soil_test_kit_used", 14071).getInt();

        config.save();

        initializeBlocks();

        GameRegistry.registerBlock(largePot, MOD_ID + ":large_pot");
        GameRegistry.registerBlock(largePotColored, ItemLargePotColored.class, MOD_ID + ":large_pot_colored");
        GameRegistry.registerBlock(largePotPlantProxy, MOD_ID + ":large_pot_plant_proxy");
        GameRegistry.registerBlock(thinLog, ItemThinLog.class, MOD_ID + ":thin_log");
        GameRegistry.registerBlock(thinLogFence, ItemThinLogFence.class, MOD_ID + ":thin_log_fence");
        //GameRegistry.registerBlock(flowerLeaves, MOD_ID + ":flower_leaves");

        LanguageRegistry.addName(largePot, "Large Flower Pot");
        LanguageRegistry.addName(largePotPlantProxy, "Large Flower Pot Proxy");

        for (int i = 0; i < 16; i++) {
            ItemStack potColorStack = new ItemStack(largePotColoredId, 1, i);
            LanguageRegistry.addName(potColorStack, colors[15 - i] + " Large Flower Pot");
        }

        for (int i = 0; i < woods.length; i++) {
            ItemStack postStack = new ItemStack(thinLogId, 1, i);
            LanguageRegistry.addName(postStack, woods[i] + " Wood Post");

            ItemStack fenceStack = new ItemStack(thinLogFenceId, 1, i);
            LanguageRegistry.addName(fenceStack, woods[i] + " Wood Post Fence");
        }

        GameRegistry.registerItem(soilTestKit, MOD_ID + ":soil_test_kit");
        GameRegistry.registerItem(soilTestKitUsed, MOD_ID + ":soil_test_kit_used");

        LanguageRegistry.addName(soilTestKit, "Soil Test Kit");
        LanguageRegistry.addName(soilTestKitUsed, "Used Soil Test Kit");

        GameRegistry.registerTileEntity(TileEntityLargePot.class, MOD_ID + ":large_pot");

        ItemStack hardenedClayStack = new ItemStack(Block.hardenedClay);
        GameRegistry.addRecipe(new ItemStack(largePot, 3), "x x", "x x", "xxx",
            'x', hardenedClayStack);

        for (int i = 0; i < 16; i++) {
            ItemStack stainedClayStack = new ItemStack(Block.stainedClay, 1, i);
            GameRegistry.addRecipe(new ItemStack(largePotColored, 3, i), "x x", "x x", "xxx",
                'x', stainedClayStack);
        }

        for (int i = 0; i < ThinLog.subNames.length; i++) {
            GameRegistry.addRecipe(new ItemStack(thinLogFence, 2, i), "xyx", " y ",
                'x', Item.silk, 'y', new ItemStack(thinLog, 1, i));

            if (i / 4 == 0)
                GameRegistry.addRecipe(new ItemStack(Block.wood, 1, i % 4), "xx", "xx",
                    'x', new ItemStack(thinLog, 1, i));
        }

        ItemStack itemDyeRed = new ItemStack(Item.dyePowder, 1, 1);
        ItemStack itemDyeGreen = new ItemStack(Item.dyePowder, 1, 2);

        GameRegistry.addRecipe(new ItemStack(soilTestKit), "xy", "zz",
            'x', itemDyeRed, 'y', itemDyeGreen, 'z', Item.glassBottle);
        GameRegistry.addRecipe(new ItemStack(soilTestKit), "yx", "zz",
            'x', itemDyeRed, 'y', itemDyeGreen, 'z', Item.glassBottle);
    }

    private static final String[] colors = {
        "White", "Orange", "Magenta", "Light Blue",
        "Yellow", "Light Green", "Pink", "Dark Grey",
        "Light Grey", "Cyan", "Purple", "Blue",
        "Brown", "Green", "Red", "Black",
    };

    private static final String[] woods = {
        "Oak", "Spruce", "Birch", "Jungle"
    };

    @Mod.EventHandler
    public void load (FMLInitializationEvent event) {
        proxy.registerRenderers();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @ForgeSubscribe
    public void applyBonemeal (BonemealEvent event) {
        if (Block.blocksList[event.ID] == largePotPlantProxy) {
            LargePotPlantProxy proxyBlock = (LargePotPlantProxy) largePotPlantProxy;
            event.setCanceled(!proxyBlock.applyBonemeal(event.world, event.X, event.Y, event.Z));
        }
    }

    private void initializeBlocks () {
        largePot = new LargePot(largePotId, false)
            .setHardness(0.5f)
            .setResistance(5f)
            .setStepSound(Block.soundStoneFootstep)
            .setUnlocalizedName("largePot");

        largePotColored = new LargePot(largePotColoredId, true)
            .setHardness(0.5f)
            .setResistance(5f)
            .setStepSound(Block.soundStoneFootstep)
            .setUnlocalizedName("largePotColored");

        largePotPlantProxy = new LargePotPlantProxy(largePotPlantProxyId)
            .setHardness(0)
            .setLightOpacity(0)
            .setUnlocalizedName("largePotPlantProxy");

        thinLog = new ThinLog(thinLogId)
            .setHardness(1.5f)
            .setResistance(5f)
            .setLightOpacity(0)
            .setStepSound(Block.soundWoodFootstep)
            .setUnlocalizedName("thinLog");

        thinLogFence = new ThinLogFence(thinLogFenceId)
            .setHardness(1.5f)
            .setResistance(5f)
            .setLightOpacity(0)
            .setStepSound(Block.soundWoodFootstep)
            .setUnlocalizedName("thinLogFence");

        //flowerLeaves = new FlowerLeaves()
        //    .setBlockName("flowerLeaves")
        //    .setBlockTextureName("leaves");

        soilTestKit = new ItemSoilKit(soilTestKitId)
            .setTextureName("soil_test_kit")
            .setUnlocalizedName("soilTestKit");

        soilTestKitUsed = new ItemUsedSoilKit(soilTestKitUsedId)
            .setTextureName("soil_test_kit")
            .setUnlocalizedName("soilTestKitUsed");
    }
}
