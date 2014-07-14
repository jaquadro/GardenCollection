package com.jaquadro.minecraft.gardencore.block.support;

import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import net.minecraft.world.IBlockAccess;

public interface ISlotProfile
{
    public boolean isPlantValidForSlot (IBlockAccess blockAccess, int x, int y, int z, int slot, PlantItem plant);

    public int getNextAvailableSlot (IBlockAccess blockAccess, int x, int y, int z, int slot, PlantItem plant);

    public SlotMapping[] getSharedSlotMap (int slot);
}
