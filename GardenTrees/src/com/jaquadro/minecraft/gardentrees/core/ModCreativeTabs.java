package com.jaquadro.minecraft.gardentrees.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModCreativeTabs
{
    private ModCreativeTabs () { }

    public static final CreativeTabs tabGardenTrees = new CreativeTabs("gardenTrees") {
        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem()
        {
            return Item.getItemFromBlock(ModBlocks.thinLogFence);
        }
    };
}
