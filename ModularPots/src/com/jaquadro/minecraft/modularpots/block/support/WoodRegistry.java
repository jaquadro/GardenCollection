package com.jaquadro.minecraft.modularpots.block.support;

import com.jaquadro.minecraft.modularpots.ModBlocks;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WoodRegistry
{
    private static final Map<UniqueMetaIdentifier, Block> registry = new HashMap<UniqueMetaIdentifier, Block>();

    public static void registerWoodType (Block block, int meta) {
        if (block == null)
            return;

        UniqueMetaIdentifier id = new UniqueMetaIdentifier(ModBlocks.getQualifiedName(block), meta);
        if (!registry.containsKey(id))
            registry.put(id, block);
    }

    public static Set<Entry<UniqueMetaIdentifier, Block>> registeredTypes () {
        return registry.entrySet();
    }
}
