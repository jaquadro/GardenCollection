package com.jaquadro.minecraft.gardencontainers.item;

import com.jaquadro.minecraft.gardencontainers.block.BlockDecorativePot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockQuartz;
import net.minecraft.item.ItemMultiTexture;

public class ItemDecorativePot extends ItemMultiTexture
{
    public ItemDecorativePot (Block block) {
        super(block, block, getSubTypes(block));
    }

    private static String[] getSubTypes (Block block) {
        if (block instanceof BlockDecorativePot)
            return BlockQuartz.field_150191_a;
        return new String[0];
    }
}
