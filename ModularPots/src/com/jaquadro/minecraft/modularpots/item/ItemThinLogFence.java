package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModBlocks;
import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.BlockThinLogFence;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemThinLogFence extends ItemBlock
{
    public ItemThinLogFence (Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata (int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName (ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        if (meta < 0 || meta >= BlockThinLogFence.subNames.length)
            meta = 0;

        return super.getUnlocalizedName() + "." + BlockThinLogFence.subNames[meta];
    }

    @Override
    public IIcon getIconFromDamage (int meta) {
        return ModBlocks.thinLogFence.getIcon(0, meta);
    }

    @Override
    public int getColorFromItemStack (ItemStack itemStack, int meta) {
        return ModBlocks.thinLogFence.getRenderColor(itemStack.getItemDamage());
    }
}
