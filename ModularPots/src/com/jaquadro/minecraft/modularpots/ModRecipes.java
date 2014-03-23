package com.jaquadro.minecraft.modularpots;

import com.jaquadro.minecraft.modularpots.block.BlockThinLog;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModRecipes
{
    public void init () {
        // Crafting

        GameRegistry.addRecipe(new ItemStack(ModBlocks.largePot, 3, 1), "x x", "x x", "xxx",
            'x', Blocks.clay);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.largePot, 3), "x x", "x x", "xxx",
            'x', Blocks.hardened_clay);

        for (int i = 0; i < 16; i++) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.largePotColored, 3, i), "x x", "x x", "xxx",
                'x', new ItemStack(Blocks.stained_hardened_clay, 1, 15 - i));

            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.largePotColored, 1, i),
                ModBlocks.largePot, new ItemStack(Items.dye, 1, i));
        }

        GameRegistry.addRecipe(new ItemStack(ModBlocks.potteryTable), "x", "y",
            'x', Items.clay_ball, 'y', Blocks.crafting_table);

        for (int i = 0; i < BlockThinLog.subNames.length; i++) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.thinLogFence, 2, i), "xyx", " y ",
                'x', Items.string, 'y', new ItemStack(ModBlocks.thinLog, 1, i));

            if (i / 4 == 0) {
                GameRegistry.addRecipe(new ItemStack(Blocks.log, 1, i % 4), "xx", "xx",
                    'x', new ItemStack(ModBlocks.thinLog, 1, i));
                GameRegistry.addRecipe(new ItemStack(ModBlocks.thinLog, 4, i), "x", "x",
                    'x', new ItemStack(Blocks.log, 1, i % 4));
            }
            else if (i / 4 == 1) {
                GameRegistry.addRecipe(new ItemStack(Blocks.log2, 1, i % 4), "xx", "xx",
                    'x', new ItemStack(ModBlocks.thinLog, 1, i));
                GameRegistry.addRecipe(new ItemStack(ModBlocks.thinLog, 4, i), "x", "x",
                    'x', new ItemStack(Blocks.log2, 1, i % 4));
            }
        }

        GameRegistry.addRecipe(new ItemStack(ModItems.soilTestKit), "xy", "zz",
            'x', new ItemStack(Items.dye, 1, 1), 'y', new ItemStack(Items.dye, 1, 2), 'z', Items.glass_bottle);
        GameRegistry.addRecipe(new ItemStack(ModItems.soilTestKit), "yx", "zz",
            'x', new ItemStack(Items.dye, 1, 1), 'y', new ItemStack(Items.dye, 1, 2), 'z', Items.glass_bottle);

        // Smelting

        GameRegistry.addSmelting(new ItemStack(ModBlocks.largePot, 1, 1), new ItemStack(ModBlocks.largePot, 1, 0), 0);

        for (int i = 1; i < 256; i++) {
            if (ModularPots.config.hasPattern(i))
                GameRegistry.addSmelting(new ItemStack(ModBlocks.largePot, 1, 1 | (i << 8)), new ItemStack(ModBlocks.largePot, 1, (i << 8)), 0);
        }
    }
}
