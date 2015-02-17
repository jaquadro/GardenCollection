package com.jaquadro.minecraft.gardencore.api;

import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Justin on 11/30/2014.
 */
public interface IPlantProxy
{
    //BlockGarden getGardenBlock (IBlockAccess blockAccess, int x, int y, int z);

    TileEntityGarden getGardenEntity (IBlockAccess blockAccess, int x, int y, int z);

    boolean applyBonemeal (World world, int x, int y, int z);
}
