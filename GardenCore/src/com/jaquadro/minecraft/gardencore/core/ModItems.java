package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.item.ItemSoilKit;
import com.jaquadro.minecraft.gardencore.item.ItemTrowel;
import com.jaquadro.minecraft.gardencore.item.ItemUsedSoilKit;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItems
{
    public static Item gardenTrowel;
    public static ItemSoilKit soilTestKit;
    public static ItemUsedSoilKit usedSoilTestKit;

    public void init () {
        gardenTrowel = new ItemTrowel("gardenTrowel", Item.ToolMaterial.IRON);
        soilTestKit = new ItemSoilKit("soilTestKit");
        usedSoilTestKit = new ItemUsedSoilKit("soilTestKitUsed");

        GameRegistry.registerItem(gardenTrowel, "garden_trowel");
        GameRegistry.registerItem(soilTestKit, "soil_test_kit");
        GameRegistry.registerItem(usedSoilTestKit, "soil_test_kit_used");
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
