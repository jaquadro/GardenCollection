package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModBlocks;
import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.BlockThinLog;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemThinLog extends ItemBlock
{
    public ItemThinLog (Block block) {
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
        if (meta < 0 || meta >= BlockThinLog.subNames.length)
            meta = 0;

        return super.getUnlocalizedName() + "." + BlockThinLog.subNames[meta];
    }

    @Override
    public IIcon getIconFromDamage (int meta) {
        return ModBlocks.thinLog.getIcon(0, meta);
    }

    @Override
    public int getColorFromItemStack (ItemStack itemStack, int meta) {
        return ModBlocks.thinLog.getRenderColor(itemStack.getItemDamage());
    }
}
