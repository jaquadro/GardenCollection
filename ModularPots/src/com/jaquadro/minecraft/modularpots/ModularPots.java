package com.jaquadro.minecraft.modularpots;

import com.jaquadro.minecraft.modularpots.block.*;
import com.jaquadro.minecraft.modularpots.config.ConfigManager;
import com.jaquadro.minecraft.modularpots.config.PatternConfig;
import com.jaquadro.minecraft.modularpots.creativetab.ModularPotsCreativeTab;
import com.jaquadro.minecraft.modularpots.item.*;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityPotteryTable;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.omg.DynamicAny.DynEnum;

import javax.security.auth.login.Configuration;
import java.io.File;

@Mod(modid = ModularPots.MOD_ID, name = ModularPots.MOD_NAME, version = ModularPots.MOD_VERSION)
public class ModularPots
{
    public static final String MOD_ID = "modularpots";
    static final String MOD_NAME = "Modular Flower Pots";
    static final String MOD_VERSION = "1.7.2.4";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.modularpots.";

    public static CreativeTabs tabModularPots = new ModularPotsCreativeTab("modularPots");

    public static Block largePot;
    public static Block largePotColored;
    public static Block largePotPlantProxy;
    public static Block thinLog;
    public static Block thinLogFence;
    public static Block flowerLeaves;
    public static Block potteryTable;

    public static Item soilTestKit;
    public static Item soilTestKitUsed;
    public static Item potteryPattern;

    public static int potteryTableGuiID = 0;

    public static ConfigManager config;

    @Mod.Instance(MOD_ID)
    public static ModularPots instance;

    @SidedProxy(clientSide = SOURCE_PATH + "client.ClientProxy", serverSide = SOURCE_PATH + "CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        config = new ConfigManager(new File(event.getModConfigurationDirectory(), MOD_ID + ".patterns.cfg"));

        initializeBlocks();

        GameRegistry.registerBlock(largePot, ItemLargePot.class, MOD_ID + ":large_pot");
        GameRegistry.registerBlock(largePotColored, ItemLargePotColored.class, MOD_ID + ":large_pot_colored");
        GameRegistry.registerBlock(largePotPlantProxy, MOD_ID + ":large_pot_plant_proxy");
        GameRegistry.registerBlock(thinLog, ItemThinLog.class, MOD_ID + ":thin_log");
        GameRegistry.registerBlock(thinLogFence, ItemThinLogFence.class, MOD_ID + ":thin_log_fence");
        //GameRegistry.registerBlock(flowerLeaves, MOD_ID + ":flower_leaves");
        GameRegistry.registerBlock(potteryTable, MOD_ID + ":pottery_table");

        GameRegistry.registerItem(soilTestKit, MOD_ID + ":soil_test_kit");
        GameRegistry.registerItem(soilTestKitUsed, MOD_ID + ":soil_test_kit_used");
        GameRegistry.registerItem(potteryPattern, MOD_ID + ":pottery_pattern");

        GameRegistry.registerTileEntity(TileEntityLargePot.class, MOD_ID + ":large_pot");
        GameRegistry.registerTileEntity(TileEntityPotteryTable.class, MOD_ID + ":pottery_table");

        ItemStack hardenedClayStack = new ItemStack(Blocks.hardened_clay);
        GameRegistry.addRecipe(new ItemStack(largePot, 3), "x x", "x x", "xxx",
            'x', hardenedClayStack);

        for (int i = 0; i < 16; i++) {
            ItemStack stainedClayStack = new ItemStack(Blocks.stained_hardened_clay, 1, i);
            GameRegistry.addRecipe(new ItemStack(largePotColored, 3, 15 - i), "x x", "x x", "xxx",
                'x', stainedClayStack);
        }

        for (int i = 0; i < ThinLog.subNames.length; i++) {
            GameRegistry.addRecipe(new ItemStack(thinLogFence, 2, i), "xyx", " y ",
                'x', Items.string, 'y', new ItemStack(thinLog, 1, i));

            if (i / 4 == 0)
                GameRegistry.addRecipe(new ItemStack(Blocks.log, 1, i % 4), "xx", "xx",
                    'x', new ItemStack(thinLog, 1, i));
            else if (i / 4 == 1)
                GameRegistry.addRecipe(new ItemStack(Blocks.log2, 1, i % 4), "xx", "xx",
                    'x', new ItemStack(thinLog, 1, i));
        }

        GameRegistry.addRecipe(new ItemStack(potteryTable), "x", "y",
            'x', Items.clay_ball, 'y', Blocks.crafting_table);

        ItemStack itemDyeRed = new ItemStack(Items.dye, 1, 1);
        ItemStack itemDyeGreen = new ItemStack(Items.dye, 1, 2);

        GameRegistry.addRecipe(new ItemStack(soilTestKit), "xy", "zz",
            'x', itemDyeRed, 'y', itemDyeGreen, 'z', Items.glass_bottle);
        GameRegistry.addRecipe(new ItemStack(soilTestKit), "yx", "zz",
            'x', itemDyeRed, 'y', itemDyeGreen, 'z', Items.glass_bottle);

        GameRegistry.addSmelting(new ItemStack(largePot, 1, 1), new ItemStack(largePot, 1, 0), 0);
        for (int i = 1; i < 256; i++) {
            if (ModularPots.config.hasPattern(i))
                GameRegistry.addSmelting(new ItemStack(largePot, 1, 1 | (i << 8)), new ItemStack(largePot, 1, (i << 8)), 0);
        }

        //ItemStack axeStack = new ItemStack(Items.stone_axe, 1, OreDictionary.WILDCARD_VALUE);
        //GameRegistry.addShapelessRecipe(new ItemStack(thinLog, 4, 0), axeStack, new ItemStack(Blocks.log, 1, 0));
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
                ChestGenHooks.addItem(pattern.getGenLocation(j), new WeightedRandomChestContent(potteryPattern, i, 1, 1, pattern.getGenRarity(j)));
        }

        VillagerTradeHandler.instance().load();
    }

    @SubscribeEvent
    public void applyBonemeal (BonemealEvent event) {
        if (event.block == largePotPlantProxy) {
            LargePotPlantProxy proxyBlock = (LargePotPlantProxy) largePotPlantProxy;
            event.setCanceled(!proxyBlock.applyBonemeal(event.world, event.x, event.y, event.z));
        }
    }

    private void initializeBlocks () {
        largePot = new LargePot(false)
            .setHardness(0.5f)
            .setResistance(5f)
            .setStepSound(Block.soundTypeStone)
            .setBlockName("largePot");

        largePotColored = new LargePot(true)
            .setHardness(0.5f)
            .setResistance(5f)
            .setStepSound(Block.soundTypeStone)
            .setBlockName("largePotColored");

        largePotPlantProxy = new LargePotPlantProxy()
            .setHardness(0)
            .setLightOpacity(0)
            .setBlockName("largePotPlantProxy");

        thinLog = new ThinLog()
            .setHardness(1.5f)
            .setResistance(5f)
            .setLightOpacity(0)
            .setStepSound(Block.soundTypeWood)
            .setBlockName("thinLog");

        thinLogFence = new ThinLogFence()
            .setHardness(1.5f)
            .setResistance(5f)
            .setLightOpacity(0)
            .setStepSound(Block.soundTypeWood)
            .setBlockName("thinLogFence");

        flowerLeaves = new FlowerLeaves()
            .setBlockName("flowerLeaves")
            .setBlockTextureName("leaves");

        potteryTable = new PotteryTable()
            .setHardness(2.5f)
            .setStepSound(Block.soundTypeWood)
            .setBlockName("potteryTable");

        soilTestKit = new ItemSoilKit()
            .setTextureName("soil_test_kit")
            .setUnlocalizedName("soilTestKit");

        soilTestKitUsed = new ItemUsedSoilKit()
            .setTextureName("soil_test_kit")
            .setUnlocalizedName("soilTestKitUsed");

        potteryPattern = new ItemPotteryPattern()
            .setUnlocalizedName("potteryPattern");
    }
}
