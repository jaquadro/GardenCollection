package com.jaquadro.minecraft.gardencore.block.support;

import net.minecraft.world.IBlockAccess;

public class Slot5Profile extends BasicSlotProfile
{
    public static final int SLOT_COVER = 0;
    public static final int SLOT_NW = 1;
    public static final int SLOT_NE = 2;
    public static final int SLOT_SW = 3;
    public static final int SLOT_SE = 4;

    private static float[] plantOffsetX = new float[] {
        0, -.252f, .25f, -.25f, .252f
    };

    private static float[] plantOffsetZ = new float[] {
        0, -.25f, -.252f, .252f, .25f
    };

    public Slot5Profile (Slot[] slots) {
        super(slots);

        if (slots.length != 5)
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
