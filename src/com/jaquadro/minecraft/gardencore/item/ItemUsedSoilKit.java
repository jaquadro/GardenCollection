package com.jaquadro.minecraft.gardencore.item;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ColorizerGrass;

import java.util.List;

public class ItemUsedSoilKit extends Item
{
    @SideOnly(Side.CLIENT)
    private IIcon icon;

    @SideOnly(Side.CLIENT)
    private IIcon iconOverlay;

    public ItemUsedSoilKit (String unlocalizedName) {
        setUnlocalizedName(unlocalizedName);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setTextureName("soil_test_kit");
        setCreativeTab(ModCreativeTabs.tabGardenCore);
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
        return (int)(humidity * 127) << 7 | (int)(temp * 127);
    }

    @SideOnly(Side.CLIENT)
    public int getColorFromDamage (int damage) {
        int temperature = damage & 127;
        int humidity = (damage >> 7) & 127;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        float temperature = (itemStack.getItemDamage() & 127) / 127f;
        float humidity = ((itemStack.getItemDamage() >> 7) & 127) / 127f;

        EnumChatFormatting tempColor = EnumChatFormatting.BLUE;
        if (temperature >= .2)
            tempColor = EnumChatFormatting.DARK_GREEN;
        if (temperature >= 1)
            tempColor = EnumChatFormatting.DARK_RED;

        EnumChatFormatting humidColor = EnumChatFormatting.DARK_RED;
        if (humidity >= .3)
            humidColor = EnumChatFormatting.GOLD;
        if (humidity >= .6)
            humidColor = EnumChatFormatting.DARK_GREEN;

        String temperatureStr = StatCollector.translateToLocal("soilkit.temperature") + ": " + tempColor + String.format("%.2f", temperature) ;
        String humidityStr = StatCollector.translateToLocal("soilkit.rainfall") + ": " + humidColor + String.format("%.2f", humidity);

        list.add(temperatureStr);
        list.add(humidityStr);
    }

    @Override
    public void getSubItems (Item item, CreativeTabs creativeTabs, List list) {
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
        icon = iconRegister.registerIcon(GardenCore.MOD_ID + ":soil_test_kit_used");
        iconOverlay = iconRegister.registerIcon(GardenCore.MOD_ID + ":soil_test_kit_overlay");
    }
}
