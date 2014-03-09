package com.jaquadro.minecraft.modularpots.creativetab;

import com.jaquadro.minecraft.modularpots.ModularPots;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModularPotsCreativeTab extends CreativeTabs
{
    public ModularPotsCreativeTab (String label) {
        super(label);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Item getTabIconItem () {
        return Item.getItemFromBlock(ModularPots.largePot);
    }
}
