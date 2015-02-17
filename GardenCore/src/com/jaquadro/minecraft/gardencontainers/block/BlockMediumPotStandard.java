package com.jaquadro.minecraft.gardencontainers.block;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockMediumPotStandard extends BlockMediumPot
{
    public static final String[] subTypes = new String[] { "default" };

    public BlockMediumPotStandard (String blockName) {
        super(blockName);
    }

    @Override
    public String[] getSubTypes () {
        return subTypes;
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List blockList) {
        blockList.add(new ItemStack(item, 1, 0));
    }

    @Override
    public IIcon getIcon (int side, int data) {
        return Blocks.hardened_clay.getIcon(side, 0);
    }
}
