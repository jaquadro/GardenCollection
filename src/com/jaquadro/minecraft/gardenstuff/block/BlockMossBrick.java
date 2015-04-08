package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockMossBrick extends Block
{
    public static final String[] subNames = new String[] { "mossy_2", "mossy_3", "mossy_4", "cracked_mossy_1", "cracked_mossy_2", "cracked_mossy_3", "cracked_mossy_4" };

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockMossBrick (String name) {
        super(Material.rock);

        setBlockName(name);
        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(1.5f);
        setResistance(10);
        setStepSound(soundTypePiston);
        setBlockTextureName(GardenStuff.MOD_ID + ":stonebrick");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta) {
        if (meta < 0 || meta >= subNames.length)
            meta = 0;

        return icons[meta];
    }

    @Override
    public int damageDropped (int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (Item item, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < subNames.length; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[subNames.length];

        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(getTextureName() + "_" + subNames[i]);
    }
}
