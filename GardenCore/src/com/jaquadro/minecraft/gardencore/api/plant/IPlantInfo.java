package com.jaquadro.minecraft.gardencore.api.plant;

import com.jaquadro.minecraft.gardencore.api.IPlantMetaResolver;
import net.minecraft.block.Block;

public interface IPlantInfo extends IPlantMetaResolver
{
    public PlantType getPlantTypeClass (Block block, int meta);

    public PlantSize getPlantSizeClass (Block block, int meta);

    public int getPlantMaxHeight (Block block, int meta);
}
