package com.jaquadro.minecraft.modularpots;

import com.jaquadro.minecraft.modularpots.block.LargePot;
import com.jaquadro.minecraft.modularpots.block.LargePotPlantProxy;
import com.jaquadro.minecraft.modularpots.item.ItemLargePotColored;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

@Mod(modid = ModularPots.MOD_ID, name = ModularPots.MOD_NAME, version = ModularPots.MOD_VERSION)
public class ModularPots
{
    public static final String MOD_ID = "modularpots";
    static final String MOD_NAME = "Modular Flower Pots";
    static final String MOD_VERSION = "1.7.2.1";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.modularpots.";

    public static Block largePot;
    public static Block largePotColored;
    public static Block largePotPlantProxy;

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

        GameRegistry.registerTileEntity(TileEntityLargePot.class, MOD_ID + ":large_pot");

        ItemStack hardenedClayStack = new ItemStack(Blocks.hardened_clay);
        GameRegistry.addRecipe(new ItemStack(largePot, 3), "x x", "x x", "xxx",
            'x', hardenedClayStack);

        for (int i = 0; i < 16; i++) {
            ItemStack stainedClayStack = new ItemStack(Blocks.stained_hardened_clay, 1, i);
            GameRegistry.addRecipe(new ItemStack(largePotColored, 3, 15 - i), "x x", "x x", "xxx",
                'x', stainedClayStack);
        }
    }

    @Mod.EventHandler
    public void load (FMLInitializationEvent event) {
        proxy.registerRenderers();
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
    }
}
