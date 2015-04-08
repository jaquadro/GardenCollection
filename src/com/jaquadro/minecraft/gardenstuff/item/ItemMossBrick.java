package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.block.BlockMossBrick;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemMossBrick extends ItemMultiTexture
{
    public ItemMossBrick (Block block) {
        super(block, block, BlockMossBrick.subNames);
    }
}
