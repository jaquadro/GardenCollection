package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.LargePot;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemLargePotColored extends ItemBlock
{
    public ItemLargePotColored (Block block) {
        super(block);
        setHasSubtypes(true);
        setUnlocalizedName("largePotColored");
    }

    @Override
    public int getColorFromItemStack (ItemStack itemStack, int data) {
        return ModularPots.largePotColored.getRenderColor(itemStack.getItemDamage());
    }

    @Override
    public IIcon getIconFromDamage (int data) {
        LargePot block = (LargePot) ModularPots.largePotColored;
        return block.getIcon(0, LargePot.getBlockFromDye(data));
    }

    @Override
    public int getMetadata (int damageValue) {
        return damageValue;
    }

    @Override
    public String getUnlocalizedName (ItemStack itemStack) {
        return super.getUnlocalizedName() + "." + ItemDye.field_150923_a[itemStack.getItemDamage()];
    }
}
