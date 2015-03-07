package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;

public class ItemLantern extends ItemBlock
{
    public ItemLantern (Block block) {
        super(block);
        setTextureName(GardenStuff.MOD_ID + ":lantern");
    }
}
