package com.jaquadro.minecraft.gardencore.api.block.garden;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * A profile to query whether or not adjacent blocks are "connected" to a given block.
 *
 * The definition of connected is dependent on the particular garden block, but generally the blocks must be of the same
 * type with the same attributes (color, material, fill material).  Connected blocks visually connect in the world.
 */
public interface IConnectionProfile
{
    /**
     * Test whether an adjacent block is connected to the given block.
     *
     * @param blockAccess Minecraft block accessor (world).
     * @param x X coordinate of the target block.
     * @param y Y coordinate of the target block.
     * @param z Z coordinate of the target block.
     * @param side The side facing the adjacent block to test.  Uses {@link ForgeDirection} ordinals.
     * @return True if the blocks form a valid connection; false otherwise.
     */
    public boolean isAttachedNeighbor (IBlockAccess blockAccess, int x, int y, int z, int side);

    /**
     * Test whether an adjacent block is connected to the given block.
     *
     * @param blockAccess
     * @param x X coordinate of the target block.
     * @param y Y coordinate of the target block.
     * @param z Z coordinate of the target block.
     * @param nx X coordinate of the adjacent block.
     * @param ny Y coordinate of the adjacent block.
     * @param nz Z coordinate of the adjacent block.
     * @return True if the blocks form a valid connection; false otherwise.
     */
    public boolean isAttachedNeighbor (IBlockAccess blockAccess, int x, int y, int z, int nx, int ny, int nz);
}
