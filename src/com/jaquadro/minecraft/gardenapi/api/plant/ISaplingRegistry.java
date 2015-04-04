package com.jaquadro.minecraft.gardenapi.api.plant;

import com.jaquadro.minecraft.gardenapi.api.util.IUniqueID;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface ISaplingRegistry
{
    void registerSapling (Item sapling, int saplingMeta, Block wood, int woodMeta, Block leaf, int leafMeta);

    void registerSapling (IUniqueID sapling, IUniqueID wood, IUniqueID leaf);

    IUniqueID getLeavesForSapling (Item sapling);

    IUniqueID getLeavesForSapling (Item sapling, int saplingMeta);

    IUniqueID getLeavesForSapling (IUniqueID sapling);

    IUniqueID getWoodForSapling (Item sapling);

    IUniqueID getWoodForSapling (Item sapling, int saplingMeta);

    IUniqueID getWoodForSapling (IUniqueID sapling);

    Object getExtendedData (Item sapling, String key);

    Object getExtendedData (Item sapling, int saplingMeta, String key);

    Object getExtendedData (IUniqueID sapling, String key);

    void putExtendedData (Item sapling, String key, Object data);

    void putExtendedData (Item sapling, int saplingMeta, String key, Object data);

    void putExtendedData (IUniqueID sapling, String key, Object data);
}
