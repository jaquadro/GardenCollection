package com.jaquadro.minecraft.gardencontainers.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockWindowBoxStone extends BlockWindowBox
{
    public  static final String[] subTypes = new String[] { "stone_slab", "stone_brick", "mossy_stone_brick", "brick_block", "nether_brick", "sandstone" };

    public BlockWindowBoxStone (String blockName) {
        super(blockName, Material.rock);

        setHardness(1.0f);
        setStepSound(Block.soundTypeStone);
    }

    public String[] getSubTypes () {
        return subTypes;
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List blockList) {
        for (int i = 0; i < subTypes.length; i++)
            blockList.add(new ItemStack(item, 1, i));

    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return getBlockFromMeta(meta).getIcon(side, getMetaFromMeta(meta));
    }

    private Block getBlockFromMeta (int meta) {
        switch (meta) {
            case 0: return Blocks.stone_slab;
            case 1: return Blocks.stonebrick;
            case 2: return Blocks.stonebrick;
            case 3: return Blocks.brick_block;
            case 4: return Blocks.nether_brick;
            case 5: return Blocks.sandstone;
            default: return null;
        }
    }

    private int getMetaFromMeta (int meta) {
        switch (meta) {
            case 3: return 1;
            default: return 0;
        }
    }
}
