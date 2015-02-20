package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.block.BlockHeavyChain;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemHeavyChain extends ItemMultiTexture
{
    public ItemHeavyChain (Block block) {
        super(block, block, BlockHeavyChain.types);
    }
}
