package com.jaquadro.minecraft.gardencore.block.tile;

public class TileEntityGardenSingle extends TileEntityGarden
{
    public static final int SLOT_CENTER = 0;
    public static final int SLOT_COVER = 1;

    private static final int[] PLANT_SLOTS = new int[] {
        SLOT_CENTER, SLOT_COVER,
    };

    @Override
    protected int containerSlotCount () {
        return PLANT_SLOTS.length;
    }

    @Override
    public int[] getPlantSlots () {
        return PLANT_SLOTS;
    }

    @Override
    protected SlotMapping[] getNeighborMappingsForSlot (int slot) {
        return null;
    }
}
