package com.jaquadro.minecraft.gardencore.api.plant;

import net.minecraft.block.Block;

public class DefaultPlantInfo implements IPlantInfo
{
    @Override
    public PlantTypeClass getPlantTypeClass (Block block, int meta) {
        return PlantTypeClass.NORMAL;
    }

    @Override
    public PlantSizeClass getPlantSizeClass (Block block, int meta) {
        return block.getRenderType() == 6 ? PlantSizeClass.FULL : PlantSizeClass.LARGE;
    }

    @Override
    public int getPlantMaxHeight (Block block, int meta) {
        return 1;
    }

    @Override
    public int getPlantHeight (Block block, int meta) {
        return 1;
    }

    @Override
    public int getPlantSectionMeta (Block block, int meta, int section) {
        return meta;
    }
}
