package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockFence extends BlockConnected
{
    public static final String[] subNames = new String[] { "0", "1", "2", "3" };
    public static final int[] postInterval = new int[] { 8, 16, 8, 8, };

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    @SideOnly(Side.CLIENT)
    private IIcon[] iconsTB;

    public BlockFence (String blockName) {
        super(blockName, Material.iron);

        setBlockTextureName(GardenStuff.MOD_ID + ":wrought_iron_fence");
    }

    @Override
    public int getRenderType () {
        return ClientProxy.fenceRenderID;
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < subNames.length; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    protected float getCollisionHeight () {
        return 1.5f;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int meta) {
        return iconsTB[meta % icons.length];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int meta) {
        return icons[meta % icons.length];
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconTB (int meta) {
        return iconsTB[meta % icons.length];
    }

    public int getPostInterval (int meta) {
        return postInterval[meta % icons.length];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[subNames.length];
        iconsTB = new IIcon[subNames.length];

        for (int i = 0; i < icons.length; i++) {
            icons[i] = register.registerIcon(getTextureName() + "_" + subNames[i]);
            iconsTB[i] = register.registerIcon(getTextureName() + "_" + subNames[i] + "_tb");
        }
    }
}
