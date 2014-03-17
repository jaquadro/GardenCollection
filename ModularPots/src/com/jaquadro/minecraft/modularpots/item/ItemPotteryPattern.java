package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModularPots;
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
    private IIcon[] iconArray = new IIcon[16];

    public ItemPotteryPattern () {
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
            if (ModularPots.config.getOverlayImage(i) != null)
                list.add(new ItemStack(item, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        String name = ModularPots.config.getOverlayName(itemStack.getItemDamage());
        if (name != null)
            list.add(name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister iconRegister) {
        for (int i = 0; i < 16; i++)
            iconArray[i] = iconRegister.registerIcon(ModularPots.MOD_ID + ":pottery_pattern_" + i);
    }
}
