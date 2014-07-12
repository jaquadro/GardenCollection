package com.jaquadro.minecraft.gardencore.api.plant;

public enum PlantType
{
    /**
     * Technical IPlantables that should not be recognized as plants.
     * Examples: Third party stalagmites.
     */
    INVALID,

    /**
     * Plants that grow out of the ground and occupy nontrivial volume.
     * Examples: Most plants.  Crops, grass, flowers, saplings.
     */
    GROUND,

    /**
     * Plants that cover the ground with almost no height.
     * Examples: Third party clovers, leaf litter, moss.
     */
    GROUND_COVER,

    /**
     * Plants that cover a side surface with almost no depth.
     * Examples: Third party moss.
     */
    SIDE_COVER,

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
     * Like GROUND, but plants must be placed underwater.
     * Examples: Third party corals, kelp.
     */
    AQUATIC,

    /**
     * Plants that cover water surface with almost no height.
     * Examples: Lily pads.
     */
    AQUATIC_COVER,

    /**
     * Plants that sit on the surface of water and occupy nontrivial volume.
     * Examples: Third party water lily.
     */
    AQUATIC_SURFACE,

    /**
     * A plant that must be rooted underwater but can grow above water.
     * Examples: Third party reeds.
     */
    AQUATIC_EMERGENT,
}
