package com.jaquadro.minecraft.gardentrees.world.gen;

import net.minecraft.block.Block;

public interface OrnamentalTreeFactory
{
    WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta);
}
