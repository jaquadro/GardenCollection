package com.jaquadro.minecraft.gardencore.api.block;

import com.jaquadro.minecraft.gardencore.api.block.garden.IConnectionProfile;
import com.jaquadro.minecraft.gardencore.api.block.garden.ISlotProfile;
import com.jaquadro.minecraft.gardencore.api.block.garden.ISlotShareProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

/**
 * Standard interface for getting information about garden blocks.
 */
public interface IGardenBlock
{
    /**
     * Gets the default plant slot in a garden block.
     * Usually the center slot or slot that can hold the largest plants.
     *
     * @return The default slot, or -1 if there is no default.
     */
    int getDefaultSlot ();

    /**
     * Gets the substrate (soil type) in the garden block or container for the given slot.
     *
     * @param blockAccess Miencraft world access.
     * @param x X-coordinate of the target block.
     * @param y Y-coordinate of the target block.
     * @param z Z-coordinate of the target block.
     * @param slot The plant slot to query.
     * @return An ItemStack representing the substrate, or null if there is no substrate.
     */
    ItemStack getGardenSubstrate (IBlockAccess blockAccess, int x, int y, int z, int slot);

    /**
     * Gets the connection profile for the garden block.
     * Provides information on how adjacent blocks connect.
     */
    IConnectionProfile getConnectionProfile ();

    /**
     * Gets the slot profile for the garden block.
     * Provides information on the block's available plant slots.
     */
    ISlotProfile getSlotProfile ();

    /**
     * Gets the slot sharing profile for the garden block.
     * Provides information on slot sharing between adjacent blocks.
     */
    ISlotShareProfile getSlotShareProfile ();
}
