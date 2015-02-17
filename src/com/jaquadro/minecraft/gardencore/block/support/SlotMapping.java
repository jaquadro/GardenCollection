package com.jaquadro.minecraft.gardencore.block.support;

public class SlotMapping
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
}
