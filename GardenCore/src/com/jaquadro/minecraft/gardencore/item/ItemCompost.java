package com.jaquadro.minecraft.gardencore.item;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import net.minecraft.item.Item;

public class ItemCompost extends Item
{
    public ItemCompost (String unlocalizedName) {
        setUnlocalizedName(unlocalizedName);
        setMaxStackSize(64);
        setTextureName(GardenCore.MOD_ID + ":compost_pile");
        setCreativeTab(ModCreativeTabs.tabGardenCore);
    }
}
