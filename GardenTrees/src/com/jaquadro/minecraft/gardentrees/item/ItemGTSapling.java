package com.jaquadro.minecraft.gardentrees.item;

import com.jaquadro.minecraft.gardentrees.block.BlockGTSapling;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemGTSapling extends ItemMultiTexture
{
    public ItemGTSapling (Block block) {
        super(block, block, BlockGTSapling.types);
    }
}
