package com.jaquadro.minecraft.gardencore.block.support;

import net.minecraft.world.IBlockAccess;

public class Slot14Profile extends BasicSlotProfile
{
    public static final int SLOT_CENTER = 0;
    public static final int SLOT_COVER = 1;
    public static final int SLOT_NW = 2;
    public static final int SLOT_NE = 3;
    public static final int SLOT_SW = 4;
    public static final int SLOT_SE = 5;
    public static final int SLOT_TOP_LEFT = 6;
    public static final int SLOT_TOP = 7;
    public static final int SLOT_TOP_RIGHT = 8;
    public static final int SLOT_RIGHT = 9;
    public static final int SLOT_BOTTOM_RIGHT = 10;
    public static final int SLOT_BOTTOM = 11;
    public static final int SLOT_BOTTOM_LEFT = 12;
    public static final int SLOT_LEFT = 13;

    private static float[] plantOffsetX = new float[] {
        0, 0, -.252f, .25f, -.25f, .252f, -.5f, -.001f, .5f, .5f, .5f, -.001f, -.5f, -.5f
    };

    private static float[] plantOffsetZ = new float[] {
        0, 0, -.25f, -.252f, .252f, .25f, -.501f, -.5f, -.501f, 0, .449f, .5f, .449f, 0
    };

    public Slot14Profile (Slot[] slots) {
        super(slots);

        if (slots.length != 14)
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
