package com.jaquadro.minecraft.modularpots.core;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.support.UniqueMetaIdentifier;
import com.jaquadro.minecraft.modularpots.item.ItemPotteryPattern;
import com.jaquadro.minecraft.modularpots.item.ItemSoilKit;
import com.jaquadro.minecraft.modularpots.item.ItemUsedSoilKit;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItems
{
    public static ItemSoilKit soilTestKit;
    public static ItemUsedSoilKit usedSoilTestKit;
    public static ItemPotteryPattern potteryPattern;

    public void init () {
        soilTestKit = new ItemSoilKit("soilTestKit");
        usedSoilTestKit = new ItemUsedSoilKit("soilTestKitUsed");
        potteryPattern = new ItemPotteryPattern("potteryPattern");

        String MOD_ID = ModularPots.MOD_ID;

        GameRegistry.registerItem(soilTestKit, "soil_test_kit");
        GameRegistry.registerItem(usedSoilTestKit, "soil_test_kit_used");
        GameRegistry.registerItem(potteryPattern, "pottery_pattern");
    }

    public static UniqueMetaIdentifier getUniqueMetaID (Item item, int meta) {
        String name = GameData.itemRegistry.getNameForObject(item);
        return new UniqueMetaIdentifier(name, meta);
    }

    public static UniqueMetaIdentifier getUniqueMetaID (ItemStack itemStack) {
        if (itemStack.getItem() == null)
            return null;
        return getUniqueMetaID(itemStack.getItem(), itemStack.getItemDamage());
    }
}
