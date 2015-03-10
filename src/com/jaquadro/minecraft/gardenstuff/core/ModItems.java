package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.item.ItemChainLink;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModItems
{
    public static Item chainLink;
    public static Item ironNugget;
    public static Item wroughtIronIngot;
    public static Item wroughtIronNugget;

    public void init () {
        chainLink = new ItemChainLink(makeName("chainLink"));
        ironNugget = new Item().setUnlocalizedName(makeName("ironNugget")).setCreativeTab(ModCreativeTabs.tabGardenCore).setTextureName(GardenStuff.MOD_ID + ":iron_nugget");
        wroughtIronIngot = new Item().setUnlocalizedName(makeName("wroughtIronIngot")).setCreativeTab(ModCreativeTabs.tabGardenCore).setTextureName(GardenStuff.MOD_ID + ":wrought_iron_ingot");
        wroughtIronNugget = new Item().setUnlocalizedName(makeName("wroughtIronNugget")).setCreativeTab(ModCreativeTabs.tabGardenCore).setTextureName(GardenStuff.MOD_ID + ":wrought_iron_nugget");

        GameRegistry.registerItem(chainLink, "chain_link");
        GameRegistry.registerItem(ironNugget, "iron_nugget");
        GameRegistry.registerItem(wroughtIronIngot, "wrought_iron_ingot");
        GameRegistry.registerItem(wroughtIronNugget, "wrought_iron_nugget");

        OreDictionary.registerOre("nuggetIron", ironNugget);
        OreDictionary.registerOre("ingotWroughtIron", wroughtIronIngot);
        OreDictionary.registerOre("nuggetWroughtIron", wroughtIronNugget);
    }

    public static String makeName (String name) {
        return GardenStuff.MOD_ID.toLowerCase() + "." + name;
    }

    public static UniqueMetaIdentifier getUniqueMetaID (Item item, int meta) {
        String name = GameData.getItemRegistry().getNameForObject(item);
        if (name == null)
            return null;

        return new UniqueMetaIdentifier(name, meta);
    }

    public static UniqueMetaIdentifier getUniqueMetaID (ItemStack itemStack) {
        if (itemStack.getItem() == null)
            return null;
        return getUniqueMetaID(itemStack.getItem(), itemStack.getItemDamage());
    }
}
