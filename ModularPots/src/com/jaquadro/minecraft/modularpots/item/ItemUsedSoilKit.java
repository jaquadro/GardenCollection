package com.jaquadro.minecraft.modularpots.item;

import com.jaquadro.minecraft.modularpots.ModularPots;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;

import java.text.DecimalFormat;
import java.util.List;

public class ItemUsedSoilKit extends Item
{
    @SideOnly(Side.CLIENT)
    private Icon icon;

    @SideOnly(Side.CLIENT)
    private Icon iconOverlay;

    public ItemUsedSoilKit (int id) {
        super(id);
        setMaxStackSize(1);
        setHasSubtypes(true);
        setCreativeTab(ModularPots.tabModularPots);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage (int damage) {
        return icon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamageForRenderPass (int damage, int pass) {
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

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation (ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        float temperature = (itemStack.getItemDamage() & 255) / 255f;
        float humidity = ((itemStack.getItemDamage() >> 8) & 255) / 255f;

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
    public void getSubItems (int itemId, CreativeTabs creativeTabs, List list) {
        //super.getSubItems(item, creativeTabs, list);

        list.add(new ItemStack(itemId, 1, PackTempHumidity(0, 0)));
        list.add(new ItemStack(itemId, 1, PackTempHumidity(.5f, 0)));
        list.add(new ItemStack(itemId, 1, PackTempHumidity(1f, 0)));
        list.add(new ItemStack(itemId, 1, PackTempHumidity(.5f, .5f)));
        list.add(new ItemStack(itemId, 1, PackTempHumidity(1f, .5f)));
        list.add(new ItemStack(itemId, 1, PackTempHumidity(1f, 1f)));
    }

    // addInformation

    @Override
    public void registerIcons (IconRegister iconRegister) {
        icon = iconRegister.registerIcon(ModularPots.MOD_ID + ":soil_test_kit_used");
        iconOverlay = iconRegister.registerIcon(ModularPots.MOD_ID + ":soil_test_kit_overlay");
    }
}
