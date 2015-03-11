package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.block.BlockFence;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemFence extends ItemMultiTexture
{
    public ItemFence (Block block) {
        super(block, block, BlockFence.subNames);
    }
}
