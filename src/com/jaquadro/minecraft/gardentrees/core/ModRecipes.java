package com.jaquadro.minecraft.gardentrees.core;

import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
import com.jaquadro.minecraft.gardencore.core.ModItems;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardentrees.block.BlockThinLog;
import com.jaquadro.minecraft.gardentrees.core.recipe.WoodBlockRecipe;
import com.jaquadro.minecraft.gardentrees.core.recipe.WoodFenceRecipe;
import com.jaquadro.minecraft.gardentrees.core.recipe.WoodPostRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

import java.util.Map;

public class ModRecipes
{
    // TODO: "Use Witchery Recipes" (Mutandis)

    private static final Item[] axeList = new Item[] {
        Items.wooden_axe, Items.stone_axe, Items.iron_axe, Items.golden_axe, Items.diamond_axe
    };

    public void init () {
        for (int i = 0; i < BlockThinLog.subNames.length; i++) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.thinLogFence, 3, i), "xyx", " y ",
                'x', Items.string, 'y', new ItemStack(ModBlocks.thinLog, 1, i));

            if (i / 4 == 0) {
                GameRegistry.addRecipe(new ItemStack(Blocks.log, 1, i % 4), "xx", "xx",
                    'x', new ItemStack(ModBlocks.thinLog, 1, i));

                for (int j = 0; j < axeList.length; j++)
                    GameRegistry.addRecipe(new ItemStack(ModBlocks.thinLog, 4, i), "x", "y",
                        'x', new ItemStack(axeList[j], 1, OreDictionary.WILDCARD_VALUE), 'y', new ItemStack(Blocks.log, 1, i % 4));
            } else if (i / 4 == 1) {
                GameRegistry.addRecipe(new ItemStack(Blocks.log2, 1, i % 4), "xx", "xx",
                    'x', new ItemStack(ModBlocks.thinLog, 1, i));

                for (int j = 0; j < axeList.length; j++)
                    GameRegistry.addRecipe(new ItemStack(ModBlocks.thinLog, 4, i), "x", "y",
                        'x', new ItemStack(axeList[j], 1, OreDictionary.WILDCARD_VALUE), 'y', new ItemStack(Blocks.log2, 1, i % 4));
            }
        }

        ItemStack enrichedSoil = new ItemStack(ModItems.compostPile);

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sapling), new ItemStack(Blocks.sapling, 1, 1), enrichedSoil);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sapling, 1, 1), new ItemStack(Blocks.sapling), new ItemStack(Blocks.vine), enrichedSoil);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sapling, 1, 2), new ItemStack(Blocks.sapling, 1, 2), enrichedSoil);

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ivy), new ItemStack(Blocks.vine), enrichedSoil);

        addExtraWoodRecipes();
    }

    private void addExtraWoodRecipes () {
        RecipeSorter.register("GardenTrees:WoodBlock", WoodBlockRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        RecipeSorter.register("GardenTrees:WoodPost", WoodPostRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        RecipeSorter.register("GardenTrees:WoodFence", WoodFenceRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");

        for (Map.Entry<UniqueMetaIdentifier, Block> entry : WoodRegistry.instance().registeredTypes()) {
            UniqueMetaIdentifier id = entry.getKey();

            CraftingManager.getInstance().getRecipeList().add(new WoodPostRecipe(id));
            CraftingManager.getInstance().getRecipeList().add(new WoodFenceRecipe(id));
            CraftingManager.getInstance().getRecipeList().add(new WoodBlockRecipe(id));
        }
    }
}
