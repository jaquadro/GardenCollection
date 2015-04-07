package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockStoneType extends Block implements IFuelHandler
{
    public BlockStoneType (String name) {
        super(Material.rock);

        setBlockName(name);
        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(5);
        setResistance(10);
        setStepSound(soundTypePiston);
        setBlockTextureName(GardenStuff.MOD_ID + ":charcoal_block");
    }

    @Override
    public int getBurnTime (ItemStack fuel) {
        if (fuel != null && Block.getBlockFromItem(fuel.getItem()) == this && fuel.getItemDamage() == 0)
            return 16000;

        return 0;
    }
}
