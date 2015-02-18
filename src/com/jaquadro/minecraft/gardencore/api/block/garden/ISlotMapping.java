package com.jaquadro.minecraft.gardencore.api.block.garden;

/**
 * Represents how a plant slot is mapped by a neighboring block when both blocks share the slot.
 */
public interface ISlotMapping
{
    /**
     * The plant slot index of the target block (the block that owns this record).
     */
    int getSlot ();

    /**
     * The plant slot index of neighbor implicated by this record.  Two blocks that share a slot usually will not
     * use the same local slot index to reference it.
     */
    int getMappedSlot ();

    /**
     * Gets the X-axis offset of the neighbor block.
     */
    int getMappedX ();

    /**
     * Gets the Z-axis offset of the neighbor block.
     */
    int getMappedZ ();
}
