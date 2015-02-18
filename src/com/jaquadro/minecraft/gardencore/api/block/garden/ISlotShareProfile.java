package com.jaquadro.minecraft.gardencore.api.block.garden;

/**
 * A profile to query which neighbor(s) are sharing the given slot in a garden block.
 *
 * Some garden blocks support plant slots located between two adjacent blocks if the blocks form a valid connection.
 * Either one of the neighbors may be the owner of the current slot contents.
 */
public interface ISlotShareProfile
{
    /**
     * Gets the list of all neighbors sharing the given slot and their respective remapping for that slot.
     *
     * @param slot The slot to query.
     * @return An array of zero or more neighbors sharing the slot, or null if the slot is not shared.
     */
    ISlotMapping[] getNeighborsForSlot (int slot);
}
