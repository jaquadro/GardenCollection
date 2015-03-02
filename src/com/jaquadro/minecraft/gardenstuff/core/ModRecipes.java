package com.jaquadro.minecraft.gardenstuff.core;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
        ItemStack ironNugget = new ItemStack(ModItems.ironNugget);
        ItemStack heavyChainIron = new ItemStack(ModBlocks.heavyChain);
        ItemStack lightChainIron = new ItemStack(ModBlocks.lightChain);
        ItemStack latticeIron = new ItemStack(ModBlocks.latticeMetal);
        ItemStack vine = new ItemStack(Blocks.vine);

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.heavyChain, 1, 0), "xx", "xx", "xx", 'x', linkIron);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.heavyChain, 1, 1), "xx", "xx", "xx", 'x', linkGold);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.heavyChain, 8, 5), "xxx", "xyx", "xxx", 'x', heavyChainIron, 'y', vine);

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lightChain, 1, 0), "x", "x", "x", 'x', linkIron);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lightChain, 1, 1), "x", "x", "x", 'x', linkGold);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lightChain, 8, 5), "xxx", "xyx", "xxx", 'x', lightChainIron, 'y', vine);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.latticeMetal, 16, 0), " x ", "xxx", " x ", 'x', "ingotIron"));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.latticeMetal, 8, 3), "xxx", "xyx", "xxx", 'x', latticeIron, 'y', vine);

        GameRegistry.addShapedRecipe(new ItemStack(Items.iron_ingot), "xxx", "xxx", "xxx", 'x', ironNugget);
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.ironNugget, 9), new ItemStack(Items.iron_ingot));

        for (int i = 0; i < 6; i++) {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.latticeWood, 8, i), " x ", "xxx", " x ", 'x', new ItemStack(Blocks.planks, 1, i));
        }

        GameRegistry.addSmelting(new ItemStack(ModBlocks.heavyChain, 1, 0), new ItemStack(ModBlocks.heavyChain, 1, 4), 0);
        GameRegistry.addSmelting(new ItemStack(ModBlocks.lightChain, 1, 0), new ItemStack(ModBlocks.lightChain, 1, 4), 0);
        GameRegistry.addSmelting(new ItemStack(ModBlocks.latticeMetal, 1, 0), new ItemStack(ModBlocks.latticeMetal, 1, 2), 0);
    }
}
