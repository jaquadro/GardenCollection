package com.jaquadro.minecraft.modularpots.block.support;

import com.jaquadro.minecraft.modularpots.core.ModBlocks;
import com.jaquadro.minecraft.modularpots.core.ModItems;
import com.jaquadro.minecraft.modularpots.world.gen.feature.OrnamentalTreeFactory;
import com.jaquadro.minecraft.modularpots.world.gen.feature.OrnamentalTreeRegistry;
import com.jaquadro.minecraft.modularpots.world.gen.feature.WorldGenOrnamentalTree;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class SaplingRegistry
{
    public static class SaplingRecord
    {
        public UniqueMetaIdentifier saplingType;
        public UniqueMetaIdentifier woodType;
        public UniqueMetaIdentifier leafType;
        public WorldGenOrnamentalTree generator;
    }

    private static final Map<UniqueMetaIdentifier, SaplingRecord> registry = new HashMap<UniqueMetaIdentifier, SaplingRecord>();

    public static boolean registerSapling (UniqueMetaIdentifier sapling, UniqueMetaIdentifier wood, UniqueMetaIdentifier leaf, String generatorName) {
        OrnamentalTreeFactory tree = OrnamentalTreeRegistry.getTree(generatorName);
        if (tree == null)
            return false;

        WorldGenOrnamentalTree generator = tree.create(wood.getBlock(), wood.meta, leaf.getBlock(), leaf.meta);

        SaplingRecord record = new SaplingRecord();
        record.saplingType = sapling;
        record.woodType = wood;
        record.leafType = leaf;
        record.generator = generator;

        registry.put(sapling, record);

        return true;
    }

    public static boolean registerSapling (Item sapling, int saplingMeta, Block wood, int woodMeta, Block leaf, int leafMeta, String generatorName) {
        return registerSapling(ModItems.getUniqueMetaID(sapling, saplingMeta), ModBlocks.getUniqueMetaID(wood, woodMeta), ModBlocks.getUniqueMetaID(leaf, leafMeta), generatorName);
    }

    public static WorldGenOrnamentalTree getGenerator (Block block, int meta) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (!registry.containsKey(id))
            return null;

        SaplingRecord record = registry.get(id);
        return record.generator;
    }

    static {
        Item sapling = Item.getItemFromBlock(Blocks.sapling);

        registerSapling(sapling, 0, Blocks.log, 0, Blocks.leaves, 0, "small_oak");
        registerSapling(sapling, 1, Blocks.log, 1, Blocks.leaves, 1, "small_spruce");
        registerSapling(sapling, 2, Blocks.log, 2, Blocks.leaves, 2, "small_oak");
        registerSapling(sapling, 3, Blocks.log, 3, Blocks.leaves, 3, "small_jungle");
        registerSapling(sapling, 4, Blocks.log2, 0, Blocks.leaves2, 0, "small_acacia");
        registerSapling(sapling, 5, Blocks.log2, 1, Blocks.leaves2, 1, "small_oak");
    }
}
