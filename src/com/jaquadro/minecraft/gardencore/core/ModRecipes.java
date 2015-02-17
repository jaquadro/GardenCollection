package com.jaquadro.minecraft.gardencore.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModRecipes
{
    public void init () {
        for (int i = 0; i < 6; i++)
            GameRegistry.addRecipe(new ItemStack(ModBlocks.compostBin), "xxx", "xxx", "yyy",
                'x', Items.stick, 'y', new ItemStack(Blocks.wooden_slab, 1, i));

        GameRegistry.addRecipe(new ItemStack(ModItems.soilTestKit), "xy", "zz",
            'x', new ItemStack(Items.dye, 1, 1), 'y', new ItemStack(Items.dye, 1, 2), 'z', Items.glass_bottle);
        GameRegistry.addRecipe(new ItemStack(ModItems.soilTestKit), "yx", "zz",
            'x', new ItemStack(Items.dye, 1, 1), 'y', new ItemStack(Items.dye, 1, 2), 'z', Items.glass_bottle);

        GameRegistry.addRecipe(new ItemStack(ModItems.gardenTrowel), "  z", " y ", "x  ",
            'x', Items.stick, 'y', Items.iron_ingot, 'z', ModItems.compostPile);
        GameRegistry.addRecipe(new ItemStack(ModItems.gardenTrowel), "  x", " y ", "z  ",
            'x', Items.stick, 'y', Items.iron_ingot, 'z', ModItems.compostPile);

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.gardenSoil), Blocks.dirt, ModItems.compostPile);
    }
}
