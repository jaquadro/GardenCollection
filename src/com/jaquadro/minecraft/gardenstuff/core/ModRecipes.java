package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardenstuff.item.ItemLantern;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes
{
    private static String[] paneGlassOreDict = { "paneGlassBlack", "paneGlassRed", "paneGlassGreen", "paneGlassBrown",
        "paneGlassBlue", "paneGlassPurple", "paneGlassCyan", "paneGlassLightGray",
        "paneGlassGray", "paneGlassPink", "paneGlassLime", "paneGlassYellow",
        "paneGlassLightBlue", "paneGlassMagenta", "paneGlassOrange", "paneGlassWhite",
    };

    public void init () {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.chainLink, 3, 0), "xx ", "x x", " xx", 'x', "nuggetIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.chainLink, 3, 1), "xx ", "x x", " xx", 'x', "nuggetGold"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.chainLink, 3, 1), "xx ", "x x", " xx", 'x', "nuggetBrass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.chainLink, 3, 2), "xx ", "x x", " xx", 'x', "nuggetWroughtIron"));

        ItemStack linkIron = new ItemStack(ModItems.chainLink, 1, 0);
        ItemStack linkGold = new ItemStack(ModItems.chainLink, 1, 1);
        ItemStack linkWroughtIron = new ItemStack(ModItems.chainLink, 1, 2);
        ItemStack ironNugget = new ItemStack(ModItems.ironNugget);
        ItemStack heavyChainIron = new ItemStack(ModBlocks.heavyChain);
        ItemStack lightChainIron = new ItemStack(ModBlocks.lightChain);
        ItemStack latticeIron = new ItemStack(ModBlocks.latticeMetal);
        ItemStack vine = new ItemStack(Blocks.vine);
        ItemStack blockWroughtIron = new ItemStack(ModBlocks.metalBlock);
        ItemStack wroughtIronIngot = new ItemStack(ModItems.wroughtIronIngot);
        ItemStack wroughtIronNugget = new ItemStack(ModItems.wroughtIronNugget);
        ItemStack blockCharcoal = new ItemStack(ModBlocks.stoneBlock);

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.heavyChain, 4, 0), "xx", "xx", "xx", 'x', linkIron);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.heavyChain, 4, 1), "xx", "xx", "xx", 'x', linkGold);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.heavyChain, 4, 4), "xx", "xx", "xx", 'x', linkWroughtIron);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.heavyChain, 8, 5), "xxx", "xyx", "xxx", 'x', heavyChainIron, 'y', vine);

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lightChain, 2, 0), "x", "x", "x", 'x', linkIron);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lightChain, 2, 1), "x", "x", "x", 'x', linkGold);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lightChain, 2, 4), "x", "x", "x", 'x', linkWroughtIron);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lightChain, 8, 5), "xxx", "xyx", "xxx", 'x', lightChainIron, 'y', vine);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.latticeMetal, 16, 0), " x ", "xxx", " x ", 'x', "ingotIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.latticeMetal, 16, 2), " x ", "xxx", " x ", 'x', "ingotWroughtIron"));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.latticeMetal, 8, 3), "xxx", "xyx", "xxx", 'x', latticeIron, 'y', vine);

        GameRegistry.addShapedRecipe(new ItemStack(Items.iron_ingot), "xxx", "xxx", "xxx", 'x', ironNugget);
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.ironNugget, 9), "x", 'x', new ItemStack(Items.iron_ingot));

        GameRegistry.addShapedRecipe(blockCharcoal, "xxx", "xxx", "xxx", 'x', new ItemStack(Items.coal, 1, 1));
        GameRegistry.addShapedRecipe(new ItemStack(Items.coal, 9, 1), "x", 'x', blockCharcoal);

        GameRegistry.addShapedRecipe(blockWroughtIron, "xxx", "xxx", "xxx", 'x', wroughtIronIngot);
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.wroughtIronIngot, 9), "x", 'x', blockWroughtIron);
        GameRegistry.addShapedRecipe(wroughtIronIngot, "xxx", "xxx", "xxx", 'x', wroughtIronNugget);
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.wroughtIronNugget, 9), "x", 'x', wroughtIronIngot);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.fence, 16, 0), "xxx", "xxx", 'x', "ingotWroughtIron"));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.fence, 1, 1), "x", 'x', new ItemStack(ModBlocks.fence, 1, 0));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.fence, 1, 2), "x", 'x', new ItemStack(ModBlocks.fence, 1, 1));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.fence, 1, 3), "x", 'x', new ItemStack(ModBlocks.fence, 1, 2));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.fence, 1, 0), "x", 'x', new ItemStack(ModBlocks.fence, 1, 3));

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.bloomeryFurnace), "xxx", "xyx", "xxx",
            'x', Items.brick, 'y', new ItemStack(Blocks.furnace));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.lantern, 4, 0), " x ", "y y", "yxy",
            'x', "ingotWroughtIron", 'y', new ItemStack(ModBlocks.fence, 1, 0)));
        for (int i = 0; i < 16; i++) {
            ItemStack target = ((ItemLantern)Item.getItemFromBlock(ModBlocks.lantern)).makeItemStack(4, i, true);
            GameRegistry.addRecipe(new ShapedOreRecipe(target, " x ", "yzy", "yxy",
                'x', "ingotWroughtIron", 'y', new ItemStack(ModBlocks.fence, 1, 0), 'z', paneGlassOreDict[15 - i]));

            target = ((ItemLantern)Item.getItemFromBlock(ModBlocks.lantern)).makeItemStack(1, i, true);
            GameRegistry.addRecipe(new ShapelessOreRecipe(target, new ItemStack(ModBlocks.lantern, 1, 0), paneGlassOreDict[15 - i]));
        }

        for (int i = 0; i < 6; i++) {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.latticeWood, 8, i), " x ", "xxx", " x ", 'x', new ItemStack(Blocks.planks, 1, i));
        }

        GameRegistry.addSmelting(wroughtIronIngot, new ItemStack(Items.iron_ingot), 0);
    }
}
