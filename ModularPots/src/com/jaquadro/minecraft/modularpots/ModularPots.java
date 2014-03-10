package com.jaquadro.minecraft.modularpots;

import com.jaquadro.minecraft.modularpots.block.*;
import com.jaquadro.minecraft.modularpots.creativetab.ModularPotsCreativeTab;
import com.jaquadro.minecraft.modularpots.item.ItemLargePotColored;
import com.jaquadro.minecraft.modularpots.item.ItemThinLog;
import com.jaquadro.minecraft.modularpots.item.ItemThinLogFence;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = ModularPots.MOD_ID, name = ModularPots.MOD_NAME, version = ModularPots.MOD_VERSION)
public class ModularPots
{
    public static final String MOD_ID = "modularpots";
    static final String MOD_NAME = "Modular Flower Pots";
    static final String MOD_VERSION = "1.7.2.2";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.modularpots.";

    public static CreativeTabs tabModularPots = new ModularPotsCreativeTab("modularPots");

    public static Block largePot;
    public static Block largePotColored;
    public static Block largePotPlantProxy;
    public static Block thinLog;
    public static Block thinLogFence;
    public static Block flowerLeaves;

    @Mod.Instance(MOD_ID)
    public static ModularPots instance;

    @SidedProxy(clientSide = SOURCE_PATH + "client.ClientProxy", serverSide = SOURCE_PATH + "CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        initializeBlocks();

        GameRegistry.registerBlock(largePot, MOD_ID + ":large_pot");
        GameRegistry.registerBlock(largePotColored, ItemLargePotColored.class, MOD_ID + ":large_pot_colored");
        GameRegistry.registerBlock(largePotPlantProxy, MOD_ID + ":large_pot_plant_proxy");
        GameRegistry.registerBlock(thinLog, ItemThinLog.class, MOD_ID + ":thin_log");
        GameRegistry.registerBlock(thinLogFence, ItemThinLogFence.class, MOD_ID + ":thin_log_fence");
        //GameRegistry.registerBlock(flowerLeaves, MOD_ID + ":flower_leaves");

        GameRegistry.registerTileEntity(TileEntityLargePot.class, MOD_ID + ":large_pot");

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
                    'x', new ItemStack(thinLogFence, 1, i));
            else if (i / 4 == 1)
                GameRegistry.addRecipe(new ItemStack(Blocks.log, 1, i % 4), "xx", "xx",
                    'x', new ItemStack(thinLogFence, 1, i));
        }

        //ItemStack axeStack = new ItemStack(Items.stone_axe, 1, OreDictionary.WILDCARD_VALUE);
        //GameRegistry.addShapelessRecipe(new ItemStack(thinLog, 4, 0), axeStack, new ItemStack(Blocks.log, 1, 0));
    }

    @Mod.EventHandler
    public void load (FMLInitializationEvent event) {
        proxy.registerRenderers();
        MinecraftForge.EVENT_BUS.register(this);
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
    }
}
