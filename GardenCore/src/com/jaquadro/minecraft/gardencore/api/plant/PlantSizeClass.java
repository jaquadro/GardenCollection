package com.jaquadro.minecraft.gardencore.api.plant;

public enum PlantSizeClass
{
    /**
     * A plant that takes up a full block and can't share between-slots with neighbors.
     * Examples: Crop-type plants.
     */
    FULL,

    /**
     * A plant that takes up a full block but can share between-slots with neighbors.
     * Examples: Most bigger plants with crossed-squares renderer.  Grass, Saplings.
     */
    LARGE,

    /**
     * A plant that takes up a quarter of a block or less.
     * Examples: Most small plants.  Dandelions, tulips, ferns.
     */
    SMALL,
}
