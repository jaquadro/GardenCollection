package com.jaquadro.minecraft.gardencore.block.tile;

import com.jaquadro.minecraft.gardencore.core.ModBlocks;

/**
 * Created by Justin on 7/19/2014.
 */
public class TileEntityGardenFarmland extends TileEntityGarden
{
    @Override
    protected int containerSlotCount () {
        return ModBlocks.gardenFarmland.getSlotProfile().getPlantSlots().length;
    }
}
