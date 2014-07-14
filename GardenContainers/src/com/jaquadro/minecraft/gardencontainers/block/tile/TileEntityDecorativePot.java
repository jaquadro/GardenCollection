package com.jaquadro.minecraft.gardencontainers.block.tile;

import com.jaquadro.minecraft.gardencontainers.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;

public class TileEntityDecorativePot extends TileEntityGarden
{
    @Override
    protected int containerSlotCount () {
        return ModBlocks.decorativePot.getSlotProfile().getPlantSlots().length;
    }
}
