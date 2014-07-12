package com.jaquadro.minecraft.gardencore.block.tile;

import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;

public class TileEntityGardenSingle extends TileEntityGarden
{
    public static final int SLOT_CENTER = 0;
    public static final int SLOT_COVER = 1;

    private static final int[] PLANT_SLOTS = new int[] {
        SLOT_CENTER, SLOT_COVER,
    };

    protected static class SlotProfileSingle extends SlotProfile
    {
        public SlotProfileSingle () {
            PlantType[] commonType = new PlantType[] { PlantType.GROUND, PlantType.AQUATIC, PlantType.AQUATIC_EMERGENT};
            PlantSize[] allSize = new PlantSize[] { PlantSize.FULL, PlantSize.LARGE, PlantSize.SMALL };

            slots = new Slot[] {
                new Slot(SLOT_CENTER, commonType, allSize),
                new Slot(SLOT_COVER, new PlantType[] { PlantType.GROUND_COVER}, allSize),
            };
        }
    }

    private static final SlotProfileSingle profile = new SlotProfileSingle();

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

    @Override
    protected SlotProfile getSlotProfile () {
        return profile;
    }
}
