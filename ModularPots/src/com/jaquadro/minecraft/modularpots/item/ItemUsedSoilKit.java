package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModularPots;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;

import java.util.List;

public class ItemUsedSoilKit extends Item
{
    @SideOnly(Side.CLIENT)
    private IIcon icon;

    @SideOnly(Side.CLIENT)
    private IIcon iconOverlay;

    public ItemUsedSoilKit () {
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(ModularPots.tabModularPots);
    }

    @Override
    public ItemStack onItemRightClick (ItemStack itemStack, World world, EntityPlayer player) {
        return super.onItemRightClick(itemStack, world, player);
    }

    @Override
    public boolean onItemUse (ItemStack itemStack, EntityPlayer player, World world, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
        return super.onItemUse(itemStack, player, world, par4, par5, par6, par7, par8, par9, par10);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int damage) {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamageForRenderPass (int damage, int pass) {
        return pass == 0 ? iconOverlay : super.getIconFromDamageForRenderPass(damage, pass);
    }

    public static int PackTempHumidity (float temp, float humidity) {
        return (int)(humidity * 255) << 8 | (int)(temp * 255);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromDamage (int damage) {
        int temperature = damage & 255;
        int humidity = (damage >> 8) & 255;
        return ColorizerGrass.getGrassColor(temperature / 255f, humidity / 255f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack (ItemStack itemStack, int pass) {
        return pass > 0 ? 16777215 : getColorFromDamage(itemStack.getItemDamage());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses () {
        return true;
    }

    @Override
    public void getSubItems (Item item, CreativeTabs creativeTabs, List list) {
        super.getSubItems(item, creativeTabs, list);

        list.add(new ItemStack(item, 1, PackTempHumidity(0, 0)));
        list.add(new ItemStack(item, 1, PackTempHumidity(.5f, 0)));
        list.add(new ItemStack(item, 1, PackTempHumidity(1f, 0)));
        list.add(new ItemStack(item, 1, PackTempHumidity(.5f, .5f)));
        list.add(new ItemStack(item, 1, PackTempHumidity(1f, .5f)));
        list.add(new ItemStack(item, 1, PackTempHumidity(1f, 1f)));
    }

    // addInformation

    @Override
    public void registerIcons (IIconRegister iconRegister) {
        icon = iconRegister.registerIcon(ModularPots.MOD_ID + ":soil_test_kit_used");
        iconOverlay = iconRegister.registerIcon(ModularPots.MOD_ID + ":soil_test_kit_overlay");
    }
}
