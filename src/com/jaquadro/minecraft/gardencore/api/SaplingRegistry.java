package com.jaquadro.minecraft.gardencore.api;

import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModItems;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;

public final class SaplingRegistry
{
    private static class SaplingRecord
    {
        public UniqueMetaIdentifier saplingType;
        public UniqueMetaIdentifier woodType;
        public UniqueMetaIdentifier leafType;
        public HashMap<String, Object> extraData = new HashMap<String, Object>();
    }

    private final UniqueMetaRegistry<SaplingRecord> registry;

    private static final SaplingRegistry instance;
    static {
        instance = new SaplingRegistry();
    }

    public static SaplingRegistry instance () {
        return instance;
    }

    private SaplingRegistry () {
        registry = new UniqueMetaRegistry<SaplingRecord>();

        Item sapling = Item.getItemFromBlock(Blocks.sapling);

        registerSapling(sapling, 0, Blocks.log, 0, Blocks.leaves, 0);
        registerSapling(sapling, 1, Blocks.log, 1, Blocks.leaves, 1);
        registerSapling(sapling, 2, Blocks.log, 2, Blocks.leaves, 2);
        registerSapling(sapling, 3, Blocks.log, 3, Blocks.leaves, 3);
        registerSapling(sapling, 4, Blocks.log2, 0, Blocks.leaves2, 0);
        registerSapling(sapling, 5, Blocks.log2, 1, Blocks.leaves2, 1);
    }

    public void registerSapling (Item sapling, int saplingMeta, Block wood, int woodMeta, Block leaf, int leafMeta) {
        if (sapling == null || wood == null || leaf == null)
            return;

        registerSapling(ModItems.getUniqueMetaID(sapling, saplingMeta), ModBlocks.getUniqueMetaID(wood, woodMeta), ModBlocks.getUniqueMetaID(leaf, leafMeta));
    }

    public void registerSapling (UniqueMetaIdentifier sapling, UniqueMetaIdentifier wood, UniqueMetaIdentifier leaf) {
        SaplingRecord record = new SaplingRecord();
        record.saplingType = sapling;
        record.woodType = wood;
        record.leafType = leaf;

        registry.register(sapling, record);
    }

    public UniqueMetaIdentifier getLeavesForSapling (Item sapling) {
        return getLeavesForSapling(sapling, OreDictionary.WILDCARD_VALUE);
    }

    public UniqueMetaIdentifier getLeavesForSapling (Item sapling, int saplingMeta) {
        return getLeavesForSapling(ModItems.getUniqueMetaID(sapling, saplingMeta));
    }

    public UniqueMetaIdentifier getLeavesForSapling (UniqueMetaIdentifier sapling) {
        SaplingRecord record = registry.getEntry(sapling);
        if (record == null)
            return null;

        return record.leafType;
    }

    public UniqueMetaIdentifier getWoodForSapling (Item sapling) {
        return getWoodForSapling(sapling, OreDictionary.WILDCARD_VALUE);
    }

    public UniqueMetaIdentifier getWoodForSapling (Item sapling, int saplingMeta) {
        return getWoodForSapling(ModItems.getUniqueMetaID(sapling, saplingMeta));
    }

    public UniqueMetaIdentifier getWoodForSapling (UniqueMetaIdentifier sapling) {
        SaplingRecord record = registry.getEntry(sapling);
        if (record == null)
            return null;

        return record.woodType;
    }

    public Object getExtendedData (Item sapling, String key) {
        return getExtendedData(sapling, OreDictionary.WILDCARD_VALUE, key);
    }

    public Object getExtendedData (Item sapling, int saplingMeta, String key) {
        return getExtendedData(ModItems.getUniqueMetaID(sapling, saplingMeta), key);
    }

    public Object getExtendedData (UniqueMetaIdentifier sapling, String key) {
        SaplingRecord record = registry.getEntry(sapling);
        if (record == null)
            return null;

        return record.extraData.get(key);
    }

    public void putExtendedData (Item sapling, String key, Object data) {
        putExtendedData(sapling, OreDictionary.WILDCARD_VALUE, key, data);
    }

    public void putExtendedData (Item sapling, int saplingMeta, String key, Object data) {
        putExtendedData(ModItems.getUniqueMetaID(sapling, saplingMeta), key, data);
    }

    public void putExtendedData (UniqueMetaIdentifier sapling, String key, Object data) {
        SaplingRecord record = registry.getEntry(sapling);
        if (record == null) {
            registerSapling(sapling, null, null);
            record = registry.getEntry(sapling);
        }

        record.extraData.put(key, data);
    }
}
