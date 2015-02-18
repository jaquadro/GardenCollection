package com.jaquadro.minecraft.gardencore.api.block;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Justin on 2/17/2015.
 */
public interface IGardenBlock
{
    ItemStack getGardenSubstrate (IBlockAccess blockAccess, int x, int y, int z, int slot);
}
