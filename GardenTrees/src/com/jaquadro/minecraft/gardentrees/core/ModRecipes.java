package com.jaquadro.minecraft.gardentrees.core;

import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardentrees.block.BlockThinLog;
import com.jaquadro.minecraft.gardentrees.block.tile.TileEntityWoodProxy;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Map;

public class ModRecipes
{
    private static final Item[] axeList = new Item[] {
        Items.wooden_axe, Items.stone_axe, Items.iron_axe, Items.golden_axe, Items.diamond_axe
    };

    public void init () {
        for (int i = 0; i < BlockThinLog.subNames.length; i++) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.thinLogFence, 2, i), "xyx", " y ",
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

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sapling), new ItemStack(Blocks.sapling, 1, 1));

        addExtraWoodRecipes();
    }

    private void addExtraWoodRecipes () {
        for (Map.Entry<UniqueMetaIdentifier, Block> entry : WoodRegistry.instance().registeredTypes()) {
            UniqueMetaIdentifier id = entry.getKey();
            int meta = TileEntityWoodProxy.composeMetadata(id.getBlock(), id.meta);

            GameRegistry.addRecipe(new ItemStack(id.getBlock(), 1, id.meta), "xx", "xx",
                'x', new ItemStack(ModBlocks.thinLog, 1, meta));

            for (int j = 0; j < axeList.length; j++)
                GameRegistry.addRecipe(new ItemStack(ModBlocks.thinLog, 4, meta), "x", "y",
                    'x', new ItemStack(axeList[j], 1, OreDictionary.WILDCARD_VALUE), 'y', new ItemStack(id.getBlock(), 1, id.meta));

            GameRegistry.addRecipe(new ItemStack(ModBlocks.thinLogFence, 2, meta), "xyx", " y ",
                'x', Items.string, 'y', new ItemStack(ModBlocks.thinLog, 1, meta));
        }
    }
}
