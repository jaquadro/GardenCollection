package com.jaquadro.minecraft.gardenapi.api.connect;

import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

public interface IChainSingleAttachable
{
    Vec3 getChainAttachPoint (IBlockAccess blockAccess, int x, int y, int z, int side);
}
