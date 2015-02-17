package com.jaquadro.minecraft.gardencore.api.registry;

import com.jaquadro.minecraft.gardencore.api.util.IUniqueID;
import net.minecraft.block.Block;

import java.util.Map;
import java.util.Set;

public interface IWoodRegistry
{
    void registerWoodType (Block block, int meta);

    void registerWoodType (IUniqueID wood);

    public Set<Map.Entry<IUniqueID, Block>> registeredTypes ();

    public boolean contains (Block wood, int woodMeta);

    public boolean contains (IUniqueID wood);
}
