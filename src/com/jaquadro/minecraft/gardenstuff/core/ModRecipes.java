package com.jaquadro.minecraft.gardenstuff.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes
{
    public void init () {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.chainLink, 3, 0), "xx ", "x x", " xx", 'x', "nuggetIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.chainLink, 3, 1), "xx ", "x x", " xx", 'x', "nuggetGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.chainLink, 3, 1), "xx ", "x x", " xx", 'x', "nuggetBrass"));

        ItemStack linkIron = new ItemStack(ModItems.chainLink, 1, 0);
        ItemStack linkGold = new ItemStack(ModItems.chainLink, 1, 1);

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.heavyChain, 1, 0), "xx", "xx", "xx", 'x', linkIron);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.heavyChain, 1, 1), "xx", "xx", "xx", 'x', linkGold);

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lightChain, 1, 0), "x", "x", "x", 'x', linkIron);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lightChain, 1, 1), "x", "x", "x", 'x', linkGold);
    }
}
