package com.jaquadro.minecraft.gardenapi.internal.registry;

import com.jaquadro.minecraft.gardenapi.api.connect.IAttachable;
import com.jaquadro.minecraft.gardenapi.api.connect.IAttachableRegistry;
import com.jaquadro.minecraft.gardenapi.api.connect.StandardAttachable;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaRegistry;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class AttachableRegistry implements IAttachableRegistry
{
    private UniqueMetaRegistry<IAttachable> registry;

    public AttachableRegistry () {
        registry = new UniqueMetaRegistry<IAttachable>();
        init();
    }

    private void init () {
        for (int i = 0; i < 8; i++) {
            registerAttachable(Blocks.stone_slab, i, StandardAttachable.createTop(0.5));
            registerAttachable(Blocks.wooden_slab, i, StandardAttachable.createTop(0.5));
        }

        for (int i = 8; i < 16; i++) {
            registerAttachable(Blocks.stone_slab, i, StandardAttachable.createBottom(0.5));
            registerAttachable(Blocks.wooden_slab, i, StandardAttachable.createBottom(0.5));
        }

        registerAttachable(ModBlocks.candelabra, StandardAttachable.createBottom(0.0625));
    }

    @Override
    public void registerAttachable (String modId, String blockId, int meta, IAttachable attachable) {
        if (modId != null && blockId != null && attachable != null) {
            UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, blockId, meta);
            registry.register(id, attachable);
        }
    }

    @Override
    public void registerAttachable (String modId, String blockId, IAttachable attachable) {
        if (modId != null && blockId != null && attachable != null) {
            UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, blockId);
            registry.register(id, attachable);
        }
    }

    @Override
    public void registerAttachable (Block block, int meta, IAttachable attachable) {
        if (block != null && attachable != null) {
            UniqueMetaIdentifier id = UniqueMetaIdentifier.createFor(block, meta);
            registry.register(id, attachable);
        }
    }

    @Override
    public void registerAttachable (Block block, IAttachable attachable) {
        if (block != null && attachable != null) {
            UniqueMetaIdentifier id = UniqueMetaIdentifier.createFor(block);
            registry.register(id, attachable);
        }
    }

    @Override
    public void registerAttachable (ItemStack blockItemStack, IAttachable attachable) {
        UniqueMetaIdentifier id = UniqueMetaIdentifier.createFor(blockItemStack);
        if (id != null && attachable != null)
            registry.register(id, attachable);
    }

    @Override
    public IAttachable getAttachable (Block block, int meta) {
        UniqueMetaIdentifier id = UniqueMetaIdentifier.createFor(block, meta);
        if (id != null)
            return registry.getEntry(id);

        return null;
    }
}
