package com.jaquadro.minecraft.gardencore.block.support;

import com.jaquadro.minecraft.gardencore.api.block.garden.ISlotMapping;

public class SlotMapping implements ISlotMapping
{
    public int slot;
    public int mappedSlot;
    public int mappedX;
    public int mappedZ;

    public SlotMapping (int slot, int mappedSlot, int mappedX, int mappedY) {
        this.slot = slot;
        this.mappedSlot = mappedSlot;
        this.mappedX = mappedX;
        this.mappedZ = mappedY;
    }

    @Override
    public int getSlot () {
        return slot;
    }

    @Override
    public int getMappedSlot () {
        return mappedSlot;
    }

    @Override
    public int getMappedX () {
        return mappedX;
    }

    @Override
    public int getMappedZ () {
        return mappedZ;
    }
}
