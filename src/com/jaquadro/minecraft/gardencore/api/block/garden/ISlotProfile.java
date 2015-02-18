package com.jaquadro.minecraft.gardencore.api.block.garden;

import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

/**
 * A profile for querying information about available plant slots and slot contents.
 *
 * Garden blocks contain 1 or more slots for storing plants or objects.  Each slot may have its own unique restrictions
 * and geometrical placement in the world.  Some slots may be shared by multiple blocks.  This allows for complex
 * arrangements.
 */
public interface ISlotProfile
{
    /**
     * Gets a list of all the indexes of plant slots available in the block.
     *
     * @return An array of zero or more plant slot indexes.
     */
    public int[] getPlantSlots ();

    /**
     * Tests whether the given plant is valid for the given slot.
     * A plant may be rejected in a slot for size, type, or other reasons.
     *
     * @param blockAccess Minecraft block access.
     * @param x X coordinate of the given block.
     * @param y Y coordinate of the given block.
     * @param z Z coordinate of the given block.
     * @param slot The plant slot to test.
     * @param plant The plant definition to test.
     * @return True if the plant is valid for the slot, false otherwise.
     */
    public boolean isPlantValidForSlot (IBlockAccess blockAccess, int x, int y, int z, int slot, PlantItem plant);

    /**
     * Gets the render offset on the X-axis of objects placed in the given slot.
     *
     * @param blockAccess Minecraft block access.
     * @param x X coordinate of the given block.
     * @param y Y coordinate of the given block.
     * @param z Z coordinate of the given block.
     * @param slot The plant slot to test.
     * @return Floating-point offset relative to normal placement in a block.
     */
    public float getPlantOffsetX (IBlockAccess blockAccess, int x, int y, int z, int slot);

    /**
     * Gets the render offset on the Y-axis of objects placed in the given slot.
     *
     * @param blockAccess Minecraft block access.
     * @param x X coordinate of the given block.
     * @param y Y coordinate of the given block.
     * @param z Z coordinate of the given block.
     * @param slot The plant slot to test.
     * @return Floating-point offset relative to normal placement in a block.
     */
    public float getPlantOffsetY (IBlockAccess blockAccess, int x, int y, int z, int slot);

    /**
     * Gets the render offset on the Z-axis of objects placed in the given slot.
     *
     * @param blockAccess Minecraft block access.
     * @param x X coordinate of the given block.
     * @param y Y coordinate of the given block.
     * @param z Z coordinate of the given block.
     * @param slot The plant slot to test.
     * @return Floating-point offset relative to normal placement in a block.
     */
    public float getPlantOffsetZ (IBlockAccess blockAccess, int x, int y, int z, int slot);

    /**
     * Requests that the garden block opens its plant GUI.  The request may be ignored if the block does not support
     * a plant arrangement GUI or if the block is not configured correctly to use it.
     *
     * @param playerInventory The player's inventory.
     * @param gardenTile The tile entity of the garden block to open a GUI for.
     * @param client Whether or not the GUI is being opened on the client.
     * @return An object for the opened GUI, or null if not GUI was opened.
     */
    public Object openPlantGUI (InventoryPlayer playerInventory, TileEntity gardenTile, boolean client);

    /**
     * Gets the next available (empty) slot in a garden block.  May be used for automatically populating slots in
     * an implementation-defined order.
     *
     * @param blockAccess Minecraft block access.
     * @param x X coordinate of the given block.
     * @param y Y coordinate of the given block.
     * @param z Z coordinate of the given block.
     * @param slot The plant slot
     * @param plant
     * @return
     */
    //public int getNextAvailableSlot (IBlockAccess blockAccess, int x, int y, int z, int slot, PlantItem plant);

    //public SlotMapping[] getSharedSlotMap (int slot);
}
