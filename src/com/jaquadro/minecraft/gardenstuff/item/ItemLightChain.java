package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.block.BlockLightChain;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

/**
 * Created by Justin on 2/20/2015.
 */
public class ItemLightChain extends ItemMultiTexture
{
    public ItemLightChain (Block block) {
        super(block, block, BlockLightChain.types);
    }
}
