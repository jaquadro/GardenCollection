package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.config.PatternConfig;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemPotteryPattern extends Item
{
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public ItemPotteryPattern (String unlocalizedName) {
        setUnlocalizedName(unlocalizedName);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(ModularPots.tabModularPots);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int damage) {
        return iconArray[damage & 15];
    }

    @Override
    public void getSubItems (Item item, CreativeTabs creativeTab, List list) {
        for (int i = 1; i < 256; i++) {
            if (ModularPots.config.hasPattern(i))
                list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        PatternConfig pattern = ModularPots.config.getPattern(itemStack.getItemDamage());
        if (pattern != null && pattern.getName() != null)
            list.add(pattern.getName());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister iconRegister) {
        iconArray = new IIcon[16];
        for (int i = 0; i < 16; i++)
            iconArray[i] = iconRegister.registerIcon(ModularPots.MOD_ID + ":pottery_pattern_" + i);
    }
}
