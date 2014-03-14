package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.ThinLogFence;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemThinLogFence extends ItemBlock
{
    public ItemThinLogFence (int id) {
        super(id);
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
        if (meta < 0 || meta >= ThinLogFence.subNames.length)
            meta = 0;

        return super.getUnlocalizedName() + "." + ThinLogFence.subNames[meta];
    }

    @Override
    public Icon getIconFromDamage (int meta) {
        return ModularPots.thinLogFence.getIcon(0, meta);
    }

    @Override
    public int getColorFromItemStack (ItemStack itemStack, int meta) {
        return ModularPots.thinLogFence.getRenderColor(itemStack.getItemDamage());
    }
}
