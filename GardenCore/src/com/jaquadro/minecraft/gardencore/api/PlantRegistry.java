package com.jaquadro.minecraft.gardencore.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public final class PlantRegistry
{
    private static PlantRegistry instance;
    {
        instance = new PlantRegistry();
    }

    public static PlantRegistry instance () {
        return instance;
    }

    public boolean isPlantBlacklisted (ItemStack plant) {
        return false;
    }

    public boolean plantRespondsToBonemeal (ItemStack plant) {
        return false;
    }

    private PlantRegistry () {

    }
}
