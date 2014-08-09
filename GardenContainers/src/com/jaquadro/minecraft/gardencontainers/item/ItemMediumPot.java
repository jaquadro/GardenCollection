package com.jaquadro.minecraft.gardencontainers.item;

import com.jaquadro.minecraft.gardencontainers.block.BlockMediumPot;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemMediumPot extends ItemMultiTexture
{
    public ItemMediumPot (Block block) {
        super(block, block, getSubTypes(block));
    }

    private static String[] getSubTypes (Block block) {
        if (block instanceof BlockMediumPot)
            return ((BlockMediumPot) block).getSubTypes();
        return new String[0];
    }
}
