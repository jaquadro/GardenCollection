package com.jaquadro.minecraft.gardencore.api.plant;

import net.minecraft.block.Block;

public class SimplePlantInfo implements IPlantInfo
{
    private PlantTypeClass typeClass;
    private PlantSizeClass sizeClass;
    private int initialHeight;
    private int maxHeight;
    private int[] sectionMeta;

    public SimplePlantInfo (PlantTypeClass typeClass, PlantSizeClass sizeClass) {
        this.typeClass = typeClass;
        this.sizeClass = sizeClass;
        this.initialHeight = 1;
        this.maxHeight = 1;
    }

    public SimplePlantInfo (PlantTypeClass typeClass, PlantSizeClass sizeClass, int initialHeight, int maxHeight) {
        this.typeClass = typeClass;
        this.sizeClass = sizeClass;
        this.initialHeight = initialHeight;
        this.maxHeight = maxHeight;
    }

    public SimplePlantInfo (PlantTypeClass typeClass, PlantSizeClass sizeClass, int initialHeight, int maxHeight, int[] sectionMeta) {
        this(typeClass, sizeClass, initialHeight, maxHeight);

        this.sectionMeta = new int[sectionMeta.length];
        for (int i = 0; i < sectionMeta.length; i++)
            this.sectionMeta[i] = sectionMeta[i];
    }

    @Override
    public PlantTypeClass getPlantTypeClass (Block block, int meta) {
        return typeClass;
    }

    @Override
    public PlantSizeClass getPlantSizeClass (Block block, int meta) {
        return sizeClass;
    }

    @Override
    public int getPlantMaxHeight (Block block, int meta) {
        return maxHeight;
    }

    @Override
    public int getPlantHeight (Block block, int meta) {
        return initialHeight;
    }

    @Override
    public int getPlantSectionMeta (Block block, int meta, int section) {
        if (sectionMeta == null || section - 1 >= sectionMeta.length)
            return meta;

        return sectionMeta[section];
    }
}
