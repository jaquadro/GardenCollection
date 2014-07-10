package com.jaquadro.minecraft.gardencore.api.plant;

import com.jaquadro.minecraft.gardencore.api.IPlantMetaResolver;
import net.minecraft.block.Block;

public interface IPlantInfo extends IPlantMetaResolver
{
    public PlantTypeClass getPlantTypeClass (Block block, int meta);

    public PlantSizeClass getPlantSizeClass (Block block, int meta);

    public int getPlantMaxHeight (Block block, int meta);
}
