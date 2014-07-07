package com.jaquadro.minecraft.gardencontainers.core;

import com.jaquadro.minecraft.gardencontainers.item.ItemLargePot;
import com.jaquadro.minecraft.gardencontainers.item.ItemPotteryPattern;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItems
{
    public static ItemPotteryPattern potteryPattern;

    public void init () {
        potteryPattern = new ItemPotteryPattern("potteryPattern");

        GameRegistry.registerItem(potteryPattern, "pottery_pattern");
    }

    public static UniqueMetaIdentifier getUniqueMetaID (Item item, int meta) {
        String name = GameData.getItemRegistry().getNameForObject(item);
        return new UniqueMetaIdentifier(name, meta);
    }

    public static UniqueMetaIdentifier getUniqueMetaID (ItemStack itemStack) {
        if (itemStack.getItem() == null)
            return null;
        return getUniqueMetaID(itemStack.getItem(), itemStack.getItemDamage());
    }
}
