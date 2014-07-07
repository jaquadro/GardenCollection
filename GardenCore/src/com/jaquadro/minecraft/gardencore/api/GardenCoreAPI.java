package com.jaquadro.minecraft.gardencore.api;

import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaSet;
import net.minecraft.block.Block;

import java.util.HashSet;

public final class GardenCoreAPI
{
    private UniqueMetaSet<UniqueMetaIdentifier> smallFlameHostBlocks;

    private static GardenCoreAPI instance;
    static {
        instance = new GardenCoreAPI();
    }

    public static GardenCoreAPI instance () {
        return instance;
    }

    private GardenCoreAPI () {
        smallFlameHostBlocks = new UniqueMetaSet<UniqueMetaIdentifier>();
    }

    public void registerSmallFlameHostBlock (Block block, int meta) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (id != null)
            smallFlameHostBlocks.register(id);
    }

    public boolean blockCanHostSmallFlame (Block block, int meta) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        return smallFlameHostBlocks.contains(id);
    }
}
