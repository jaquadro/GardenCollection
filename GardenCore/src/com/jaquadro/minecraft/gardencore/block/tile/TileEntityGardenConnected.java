package com.jaquadro.minecraft.gardencore.block.tile;

import com.jaquadro.minecraft.gardencore.api.plant.IPlantInfo;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import net.minecraftforge.common.IPlantable;

public class TileEntityGardenConnected extends TileEntityGarden
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

    private static final int[] PLANT_SLOTS = new int[] {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13
    };

    protected static class SlotProfileConnected extends SlotProfile
    {
        public SlotProfileConnected () {
            PlantType[] commonType = new PlantType[] { PlantType.GROUND, PlantType.AQUATIC, PlantType.AQUATIC_EMERGENT};

            PlantSize[] smallSize = new PlantSize[] { PlantSize.SMALL };
            PlantSize[] commonSize = new PlantSize[] { PlantSize.LARGE, PlantSize.SMALL };
            PlantSize[] allSize = new PlantSize[] { PlantSize.FULL, PlantSize.LARGE, PlantSize.SMALL };

            slots = new Slot[] {
                new Slot(SLOT_CENTER, commonType, allSize),
                new Slot(SLOT_COVER, new PlantType[] { PlantType.GROUND_COVER}, allSize),
                new Slot(SLOT_NW, commonType, smallSize),
                new Slot(SLOT_NE, commonType, smallSize),
                new Slot(SLOT_SW, commonType, smallSize),
                new Slot(SLOT_SE, commonType, smallSize),
                new Slot(SLOT_TOP_LEFT, commonType, commonSize),
                new Slot(SLOT_TOP, commonType, commonSize),
                new Slot(SLOT_TOP_RIGHT, commonType, commonSize),
                new Slot(SLOT_RIGHT, commonType, commonSize),
                new Slot(SLOT_BOTTOM_RIGHT, commonType, commonSize),
                new Slot(SLOT_BOTTOM, commonType, commonSize),
                new Slot(SLOT_BOTTOM_LEFT, commonType, commonSize),
                new Slot(SLOT_LEFT, commonType, commonSize),
            };
        }

        @Override
        public boolean isValidPlant (TileEntityGarden garden, int slot, PlantItem plant) {
            if (!super.isValidPlant(garden, slot, plant))
                return false;

            return true;
        }

        protected PlantSize getContainerInteriorSizeClass (TileEntityGarden garden) {
            return null;
        }

        protected boolean isContainerAquatic (TileEntityGarden garden) {
            return false;
        }
    }

    private static final SlotMapping[][] SLOT_MAP = new SlotMapping[][] {
        null, null, null, null, null, null,
        new SlotMapping[] {
            new SlotMapping(SLOT_TOP_LEFT, SLOT_TOP_RIGHT, -1, 0),
            new SlotMapping(SLOT_TOP_LEFT, SLOT_BOTTOM_LEFT, 0, -1),
            new SlotMapping(SLOT_TOP_LEFT, SLOT_BOTTOM_RIGHT, -1, -1)
        },
        new SlotMapping[] { new SlotMapping(SLOT_TOP, SLOT_BOTTOM, 0, -1) },
        new SlotMapping[] {
            new SlotMapping(SLOT_TOP_RIGHT, SLOT_TOP_LEFT, 1, 0),
            new SlotMapping(SLOT_TOP_RIGHT, SLOT_BOTTOM_RIGHT, 0, -1),
            new SlotMapping(SLOT_TOP_RIGHT, SLOT_BOTTOM_LEFT, 1, -1)
        },
        new SlotMapping[] { new SlotMapping(SLOT_RIGHT, SLOT_LEFT, 1, 0) },
        new SlotMapping[] {
            new SlotMapping(SLOT_BOTTOM_RIGHT, SLOT_BOTTOM_LEFT, 1, 0),
            new SlotMapping(SLOT_BOTTOM_RIGHT, SLOT_TOP_RIGHT, 0, 1),
            new SlotMapping(SLOT_BOTTOM_RIGHT, SLOT_TOP_LEFT, 1, 1)
        },
        new SlotMapping[] { new SlotMapping(SLOT_BOTTOM, SLOT_TOP, 0, 1) },
        new SlotMapping[] {
            new SlotMapping(SLOT_BOTTOM_LEFT, SLOT_BOTTOM_RIGHT, -1, 0),
            new SlotMapping(SLOT_BOTTOM_LEFT, SLOT_TOP_LEFT, 0, 1),
            new SlotMapping(SLOT_BOTTOM_LEFT, SLOT_TOP_RIGHT, -1, 1)
        },
        new SlotMapping[] { new SlotMapping(SLOT_LEFT, SLOT_RIGHT, -1, 0) },
    };

    private static final SlotProfileConnected profile = new SlotProfileConnected();

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
        if (slot >= SLOT_MAP.length)
            return null;

        return SLOT_MAP[slot];
    }

    @Override
    protected SlotProfile getSlotProfile () {
        return profile;
    }
}
