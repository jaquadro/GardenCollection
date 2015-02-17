package com.jaquadro.minecraft.gardencore.api.plant;

public enum PlantSize
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
     * A plant with a base that fits within 3/4 of a block's width.
     * Parts of the plant above the base 2-3 pixels may take up a full block, like large plants.
     * Examples: Most larger flowers.
     */
    MEDIUM,

    /**
     * A plant that takes up a quarter of a block or less.
     * Examples: Most small plants.  Dandelions, tulips, ferns.
     */
    SMALL,
}
