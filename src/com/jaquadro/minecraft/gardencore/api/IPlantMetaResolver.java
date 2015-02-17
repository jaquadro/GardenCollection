package com.jaquadro.minecraft.gardencore.api;

import net.minecraft.block.Block;

public interface IPlantMetaResolver
{
    public int getPlantHeight (Block block, int meta);

    public int getPlantSectionMeta (Block block, int meta, int section);
}
