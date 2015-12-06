package com.jaquadro.minecraft.gardenapi.api.connect;

import net.minecraft.world.IBlockAccess;

/**
 * Used to indicate that generic connective structures can extend a given depth in order to fully attach
 * to a surface face.  Can be implemented directly on blocks, or registered with a block association in the
 * attachable registry.
 */
public interface IAttachable
{
    boolean isAttachable (IBlockAccess blockAccess, int x, int y, int z, int side);

    double getAttachDepth (IBlockAccess blockAccess, int x, int y, int z, int side);
}
