package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;

import java.util.List;

public class ItemChainLink extends Item
{
    private static final String[] types = { "iron", "gold", "wrought_iron" };

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public ItemChainLink (String unlocalizedName) {
        setUnlocalizedName(unlocalizedName);
        setHasSubtypes(true);
        setTextureName(GardenStuff.MOD_ID + ":chain_link");
        setCreativeTab(ModCreativeTabs.tabGardenCore);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int damage) {
        return iconArray[MathHelper.clamp_int(damage, 0, types.length - 1)];
    }

    @Override
    public void getSubItems (Item item, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < types.length; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    public String getUnlocalizedName (ItemStack item) {
        return super.getUnlocalizedName(item) + "." + types[MathHelper.clamp_int(item.getItemDamage(), 0, types.length - 1)];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister iconRegister) {
        iconArray = new IIcon[types.length];
        for (int i = 0; i < types.length; i++)
            iconArray[i] = iconRegister.registerIcon(getIconString() + "_" + types[i]);
    }
}
