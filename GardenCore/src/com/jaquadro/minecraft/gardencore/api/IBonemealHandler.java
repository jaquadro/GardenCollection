package com.jaquadro.minecraft.gardencore.api;

import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import net.minecraft.world.World;

public interface IBonemealHandler
{
    public boolean applyBonemeal (World world, int x, int y, int z, BlockGarden hostBlock, int slot);
}
