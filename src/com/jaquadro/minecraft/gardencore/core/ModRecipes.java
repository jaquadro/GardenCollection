package com.jaquadro.minecraft.gardencore.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes
{
    public void init () {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.compostBin), "xxx", "xxx", "yyy",
            'x', "stickWood", 'y', "slabWood"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.soilTestKit), "xy", "zz",
            'x', "dyeRed", 'y', "dyeGreen", 'z', Items.glass_bottle));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.soilTestKit), "yx", "zz",
            'x', "dyeRed", 'y', "dyeGreen", 'z', Items.glass_bottle));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.gardenTrowel), "  z", " y ", "x  ",
            'x', "stickWood", 'y', "ingotIron", 'z', ModItems.compostPile));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.gardenTrowel), "  x", " y ", "z  ",
            'x', "stickWood", 'y', "ingotIron", 'z', ModItems.compostPile));

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.gardenSoil), Blocks.dirt, ModItems.compostPile);
    }
}
