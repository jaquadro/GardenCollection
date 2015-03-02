package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.block.BlockLatticeWood;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemLatticeWood extends ItemMultiTexture
{
    public ItemLatticeWood (Block block) {
        super(block, block, BlockLatticeWood.subNames);
    }
}
