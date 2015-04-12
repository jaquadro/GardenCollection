package com.jaquadro.minecraft.gardenapi.api.connect;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface IAttachableRegistry
{
    void registerAttachable (String modId, String blockId, int meta, IAttachable attachable);

    void registerAttachable (String modId, String blockId, IAttachable attachable);

    void registerAttachable (Block block, int meta, IAttachable attachable);

    void registerAttachable (Block block, IAttachable attachable);

    void registerAttachable (ItemStack blockItemStack, IAttachable attachable);

    IAttachable getAttachable (Block block, int meta);
}
