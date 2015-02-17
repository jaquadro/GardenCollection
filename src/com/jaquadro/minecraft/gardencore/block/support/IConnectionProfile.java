package com.jaquadro.minecraft.gardencore.block.support;

import net.minecraft.world.IBlockAccess;

public interface IConnectionProfile
{
    public boolean isAttachedNeighbor (IBlockAccess blockAccess, int x, int y, int z, int side);
    public boolean isAttachedNeighbor (IBlockAccess blockAccess, int x, int y, int z, int nx, int ny, int nz);
}
