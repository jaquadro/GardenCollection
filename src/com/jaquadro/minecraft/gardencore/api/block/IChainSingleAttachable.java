package com.jaquadro.minecraft.gardencore.api.block;

import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

public interface IChainSingleAttachable
{
    Vec3 getChainAttachPoint (IBlockAccess blockAccess, int x, int y, int z, int side);
}
