package com.jaquadro.minecraft.modularpots.addon;

import net.minecraft.world.World;

public interface IPlantHandler
{
    public boolean init ();
    public boolean applyBonemeal (World world, int x, int y, int z);
}
