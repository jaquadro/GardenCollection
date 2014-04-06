package com.jaquadro.minecraft.modularpots.world.gen.feature;

import net.minecraft.block.Block;

public interface OrnamentalTreeFactory
{
    WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta);
}
