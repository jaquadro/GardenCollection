package com.jaquadro.minecraft.gardencore.api;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IPlantRenderer
{
    public void render (IBlockAccess world, int x, int y, int z, RenderBlocks renderer, Block block, int meta, int height);
}
