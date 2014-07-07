package com.jaquadro.minecraft.gardencore.api;

import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.Map.Entry;
import java.util.Set;

public final class WoodRegistry
{
    private UniqueMetaRegistry<Block> registry;

    private static WoodRegistry instance;
    static {
        instance = new WoodRegistry();
    }

    public static WoodRegistry instance () {
        return instance;
    }

    private WoodRegistry () {
        registry = new UniqueMetaRegistry<Block>();

        registerWoodType(Blocks.log, 0);
        registerWoodType(Blocks.log, 1);
        registerWoodType(Blocks.log, 2);
        registerWoodType(Blocks.log, 3);

        registerWoodType(Blocks.log2, 0);
        registerWoodType(Blocks.log2, 1);
    }

    public void registerWoodType (Block block, int meta) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (id != null)
            registry.register(id, block);
    }

    public Set<Entry<UniqueMetaIdentifier, Block>> registeredTypes () {
        return registry.entrySet();
    }

    //public static boolean contains (Block block, int meta) {
    //    UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
    //    return registry.containsKey(id);
    //}
}
