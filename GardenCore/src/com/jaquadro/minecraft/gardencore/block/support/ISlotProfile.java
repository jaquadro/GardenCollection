package com.jaquadro.minecraft.gardencore.block.support;

import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import net.minecraft.world.IBlockAccess;

public interface ISlotProfile
{
    public int[] getPlantSlots ();

    public boolean isPlantValidForSlot (IBlockAccess blockAccess, int x, int y, int z, int slot, PlantItem plant);

    public float getPlantOffsetX (IBlockAccess blockAccess, int x, int y, int z, int slot);
    public float getPlantOffsetY (IBlockAccess blockAccess, int x, int y, int z, int slot);
    public float getPlantOffsetZ (IBlockAccess blockAccess, int x, int y, int z, int slot);

    public int getNextAvailableSlot (IBlockAccess blockAccess, int x, int y, int z, int slot, PlantItem plant);

    public SlotMapping[] getSharedSlotMap (int slot);
}
