package com.jaquadro.minecraft.gardencore.block.support;

import java.util.Map;
import java.util.HashMap;

public class SlotShare8Profile implements ISlotShareProfile
{
    int indexBase;
    SlotMapping[][] map;

    public SlotShare8Profile (int slotXZNN, int slotZN, int slotXZPN, int slotXP, int slotXZPP, int slotZP, int slotXZNP, int slotXN) {
        indexBase = min(slotXZNN, slotZN, slotXZPN, slotXP, slotXZPP, slotZP, slotXZNP, slotXN);
        int length = max(slotXZNN, slotZN, slotXZPN, slotXP, slotXZPP, slotZP, slotXZNP, slotXN) - indexBase + 1;

        map = new SlotMapping[length][];

        map[slotZN - indexBase] = new SlotMapping[] { new SlotMapping(slotZN, slotZP, 0, -1) };
        map[slotXP - indexBase] = new SlotMapping[] { new SlotMapping(slotXP, slotXN, 1, 0) };
        map[slotZP - indexBase] = new SlotMapping[] { new SlotMapping(slotZP, slotZN, 0, 1) };
        map[slotXN - indexBase] = new SlotMapping[] { new SlotMapping(slotXN, slotXP, -1, 0) };

        map[slotXZNN - indexBase] = new SlotMapping[] {
            new SlotMapping(slotXZNN, slotXZPN, -1, 0),
            new SlotMapping(slotXZNN, slotXZNP, 0, -1),
            new SlotMapping(slotXZNN, slotXZPP, -1, -1)
        };
        map[slotXZPN - indexBase] = new SlotMapping[] {
            new SlotMapping(slotXZPN, slotXZNN, 1, 0),
            new SlotMapping(slotXZPN, slotXZPP, 0, -1),
            new SlotMapping(slotXZPN, slotXZNP, 1, -1)
        };
        map[slotXZPP - indexBase] = new SlotMapping[] {
            new SlotMapping(slotXZPP, slotXZNP, 1, 0),
            new SlotMapping(slotXZPP, slotXZPN, 0, 1),
            new SlotMapping(slotXZPP, slotXZNN, 1, 1)
        };
        map[slotXZNP - indexBase] = new SlotMapping[] {
            new SlotMapping(slotXZNP, slotXZPP, -1, 0),
            new SlotMapping(slotXZNP, slotXZNN, 0, 1),
            new SlotMapping(slotXZNP, slotXZPN, -1, 1)
        };
    }

    @Override
    public SlotMapping[] getNeighborsForSlot (int slot) {
        if (slot < indexBase || slot >= indexBase + map.length)
            return null;

        return map[slot - indexBase];
    }

    private int min (int... values) {
        int minValue = Integer.MAX_VALUE;
        for (int val : values)
            minValue = Math.min(minValue, val);

        return minValue;
    }

    private int max (int... values) {
        int maxValue = Integer.MIN_VALUE;
        for (int val : values)
            maxValue = Math.max(maxValue, val);

        return maxValue;
    }
}
