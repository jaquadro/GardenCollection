package com.jaquadro.minecraft.modularpots.addon;

import com.jaquadro.minecraft.modularpots.core.ModBlocks;
import com.jaquadro.minecraft.modularpots.block.BlockLargePotPlantProxy;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.lang.reflect.Method;

public class ModPlantMegaPackHandler implements IPlantHandler
{
    private static final String PLANT_BLOCK_CLASS = "plantmegapack.bin.PMPBlockPlant";

    private static Class<?> plantBlockClass;
    private static Method plantGrowAction;

    @Override
    public boolean init () {
        try {
            plantBlockClass = Class.forName(PLANT_BLOCK_CLASS);
            if (plantBlockClass == null)
                return false;

            plantGrowAction = plantBlockClass.getDeclaredMethod("growPlant", World.class, int.class, int.class, int.class);
            if (plantGrowAction == null)
                return false;

            return true;
        }
        catch (Exception e) {
            System.out.println("[MFP] Could not initialize Plant Mega Pack handler.");
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
            world.setBlock(x, y - 1, z, ModBlocks.largePotPlantProxy, world.getBlockMetadata(x, y - 1, z), 2);

        return result;
    }
}
