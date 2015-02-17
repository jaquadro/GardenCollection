package com.jaquadro.minecraft.gardencore.block.support;

import net.minecraft.world.IBlockAccess;

public class Slot2Profile extends BasicSlotProfile
{
    public static final int SLOT_CENTER = 0;
    public static final int SLOT_COVER = 1;

    private static float[] plantOffsetX = new float[] {
        0, 0
    };

    private static float[] plantOffsetZ = new float[] {
        0, 0
    };

    public Slot2Profile (BasicSlotProfile.Slot[] slots) {
        super(slots);

        if (slots.length != 2)
            throw new IllegalArgumentException("Invalid slot count");
    }

    @Override
    public float getPlantOffsetX (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        return plantOffsetX[slot];
    }

    @Override
    public float getPlantOffsetZ (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        return plantOffsetZ[slot];
    }
}
