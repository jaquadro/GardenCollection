package com.jaquadro.minecraft.modularpots.addon;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.BlockLargePotPlantProxy;
import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

public class ModPlantMegaPackHandler implements IPlantHandler
{
    private static final String PLANT_BLOCK_CLASS = "plantmegapack.bin.PMPBlockPlant";
    private static final String PLANT_API_EVENTS_CLASS = "plantmegapack.api.PMPEvents";

    private static Class<?> plantBlockClass;
    //private static Method plantGrowAction;

    private static Class<?> apiEventClass;
    private static Method apiApplyBonemeal;

    @Override
    public boolean init () {
        try {
            plantBlockClass = Class.forName(PLANT_BLOCK_CLASS);
            if (plantBlockClass == null)
                return false;

            /*plantGrowAction = plantBlockClass.getDeclaredMethod("growPlant", World.class, int.class, int.class, int.class);
            if (plantGrowAction == null)
                return false;*/

            apiEventClass = Class.forName(PLANT_API_EVENTS_CLASS);
            if (apiEventClass == null)
                return false;

            apiApplyBonemeal = apiEventClass.getDeclaredMethod("applyBoneMeal", World.class, int.class, int.class, int.class, Block.class);
            if (apiApplyBonemeal == null)
                return false;

            FMLLog.log(ModularPots.MOD_ID, Level.INFO, "Initialized Plant Mega Pack handler.");

            return true;
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.ERROR, e, "Could not initialize the Plant Mega Pack handler.");
            return false;
        }
    }

    @Override
    public boolean applyBonemeal (World world, int x, int y, int z) {
        BlockLargePotPlantProxy proxy = (BlockLargePotPlantProxy) world.getBlock(x, y, z);
        if (proxy == null)
            return false;

        Block block = proxy.getItemBlock(world, x, y, z);
        if (block == null)
            return false;

        if (!plantBlockClass.isAssignableFrom(block.getClass()))
            return false;

        Boolean result = false;
        try {
            result = (Boolean) apiApplyBonemeal.invoke(null, world, x, y, z, block);
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.ERROR, e, "Failed to apply bonemeal in Plant Mega Pack handler.");
            result = false;
        }

        /*
        // Convert proxy to native block
        if (world.getBlock(x, y + 1, z) == ModBlocks.largePotPlantProxy)
            world.setBlock(x, y + 1, z, block, world.getBlockMetadata(x, y + 1, z), 4);
        world.setBlock(x, y, z, block, world.getBlockMetadata(x, y, z), 4);
        if (world.getBlock(x, y - 1, z) == ModBlocks.largePotPlantProxy)
            world.setBlock(x, y - 1, z, block, world.getBlockMetadata(x, y - 1, z), 4);

        Boolean result = false;
        try {
            result = (Boolean) plantGrowAction.invoke(block, world, x, y, z);
        }
        catch (Exception e) {
            System.out.println("[MFP] Error encountered in Plant Mega Pack handler.");
            result = false;
        }

        // Convert native block to proxy
        if (world.getBlock(x, y + 1, z) == block)
            world.setBlock(x, y + 1, z, ModBlocks.largePotPlantProxy, world.getBlockMetadata(x, y + 1, z), 2);
        world.setBlock(x, y, z, ModBlocks.largePotPlantProxy, world.getBlockMetadata(x, y, z), 2);
        if (world.getBlock(x, y - 1, z) == block)
            world.setBlock(x, y - 1, z, ModBlocks.largePotPlantProxy, world.getBlockMetadata(x, y - 1, z), 2);*/

        return result;
    }
}
