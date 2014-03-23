package com.jaquadro.minecraft.modularpots;

import com.jaquadro.minecraft.modularpots.item.ItemPotteryPattern;
import com.jaquadro.minecraft.modularpots.item.ItemSoilKit;
import com.jaquadro.minecraft.modularpots.item.ItemUsedSoilKit;
import cpw.mods.fml.common.registry.GameRegistry;

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

        GameRegistry.registerItem(soilTestKit, MOD_ID + ":soil_test_kit");
        GameRegistry.registerItem(usedSoilTestKit, MOD_ID + ":soil_test_kit_used");
        GameRegistry.registerItem(potteryPattern, MOD_ID + ":pottery_pattern");
    }
}
