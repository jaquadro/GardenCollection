package com.jaquadro.minecraft.gardencore.api.plant;

public enum PlantTypeClass
{
    /**
     * Plants that grow out of the ground and occupy nontrivial volume.
     * Examples: Most plants.  Crops, grass, flowers, saplings.
     */
    NORMAL,

    /**
     * Plants that cover the ground with almost no height.
     * Examples: Lily pads.  Third party clovers, leaf litter, moss.
     */
    COVER_GROUND,

    /**
     * Plants that cover a side surface with almost no depth.
     * Examples: Third party moss.
     */
    COVER_SIDE,

    /**
     * Plants that hang from a ceiling surface and occupy volume in the middle of a block.
     * Examples: Third party hanging roots.
     */
    HANGING,

    /**
     * Plants that hang from the edge of a block and may extend below.
     * Examples: Vines.  Third party ivy.
     */
    HANGING_SIDE,

    /**
     * Like NORMAL, but plants must be placed underwater.
     * Examples: Third party corals, kelp.
     */
    AQUATIC,

    /**
     * A plant that must be rooted underwater but can grow above water.
     * Examples: Third party reeds.
     */
    AQUATIC_NORMAL,
}
