package com.jaquadro.minecraft.gardenstuff.integration.nei;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.client.gui.GuiBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.core.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class BloomeryFurnaceRecipeHandler extends TemplateRecipeHandler
{
    private static class FuelPair {
        public PositionedStack stack;
        public int burnTime;

        public FuelPair (ItemStack ingred, int burnTime) {
            this.stack = new PositionedStack(ingred, 51, 42, false);
            this.burnTime = burnTime;
        }
    }

    public class SmeltingPair extends CachedRecipe {
        PositionedStack ingred1;
        PositionedStack ingred2;
        PositionedStack result;

        public SmeltingPair (ItemStack ingred1, ItemStack ingred2, ItemStack result) {
            ingred1.stackSize = 1;
            ingred2.stackSize = 1;

            this.ingred1 = new PositionedStack(ingred1, 51, 6);
            this.ingred2 = new PositionedStack(ingred2, 30, 6);
            this.result = new PositionedStack(result, 111, 24);
        }

        @Override
        public List<PositionedStack> getIngredients () {
            return getCycledIngredients(BloomeryFurnaceRecipeHandler.this.cycleticks / 48, Arrays.asList(new PositionedStack[] { ingred1, ingred2 }));
        }

        @Override
        public PositionedStack getResult () {
            return result;
        }

        @Override
        public PositionedStack getOtherStack () {
            return (BloomeryFurnaceRecipeHandler.afuels.get(BloomeryFurnaceRecipeHandler.this.cycleticks / 48 % BloomeryFurnaceRecipeHandler.afuels.size())).stack;
        }
    }

    public static ArrayList<FuelPair> afuels;
    public static HashSet<Block> efuels;

    public BloomeryFurnaceRecipeHandler () { }

    public void loadTransferRects () {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "fuel", new Object[0]));
        this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "smelting", new Object[0]));
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass () {
        return GuiBloomeryFurnace.class;
    }

    @Override
    public String getGuiTexture () {
        return GardenStuff.MOD_ID + ":textures/gui/bloomery_furnace.png";
    }

    @Override
    public String getRecipeName () {
        return NEIClientUtils.translate("recipe.gardenstuff.bloomeryFurnace", new Object[0]);
    }

    @Override
    public TemplateRecipeHandler newInstance () {
        if (afuels == null || afuels.isEmpty())
            findFuels();

        return super.newInstance();
    }

    @Override
    public void loadCraftingRecipes (String outputId, Object... results) {
        if (outputId.equals("bloomerySmelting") && getClass() == BloomeryFurnaceRecipeHandler.class) {
            arecipes.add(new SmeltingPair(new ItemStack(Items.iron_ingot), new ItemStack(Blocks.sand), new ItemStack(ModItems.wroughtIronIngot)));
            arecipes.add(new SmeltingPair(new ItemStack(Blocks.iron_ore), new ItemStack(Blocks.sand), new ItemStack(ModItems.wroughtIronIngot)));
        }
        else
            super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes (ItemStack result) {
        if (NEIServerUtils.areStacksSameType(new ItemStack(ModItems.wroughtIronIngot), result)) {
            arecipes.add(new SmeltingPair(new ItemStack(Items.iron_ingot), new ItemStack(Blocks.sand), new ItemStack(ModItems.wroughtIronIngot)));
            arecipes.add(new SmeltingPair(new ItemStack(Blocks.iron_ore), new ItemStack(Blocks.sand), new ItemStack(ModItems.wroughtIronIngot)));
        }
    }

    @Override
    public void loadUsageRecipes (String inputId, Object... ingredients) {
        //if (inputId.equals("bloomerySmelting") && getClass() == BloomeryFurnaceRecipeHandler.class)
        //    loadCraftingRecipes("bloomerySmelting", new Object[0]);
        //else
            super.loadUsageRecipes(inputId, ingredients);
    }

    @Override
    public void loadUsageRecipes (ItemStack ingredient) {
        ItemStack[] primary = new ItemStack[] { new ItemStack(Items.iron_ingot), new ItemStack(Blocks.iron_ore) };
        ItemStack[] secondary = new ItemStack[] { new ItemStack(Blocks.sand) };

        for (ItemStack stack : primary) {
            if (NEIServerUtils.areStacksSameTypeCrafting(stack, ingredient)) {
                for (ItemStack secondaryStack : secondary) {
                    SmeltingPair arecipe = new SmeltingPair(stack, secondaryStack, new ItemStack(ModItems.wroughtIronIngot));
                    arecipe.setIngredientPermutation(Arrays.asList(new PositionedStack[]{arecipe.ingred1}), ingredient);
                    arecipes.add(arecipe);
                }
            }
        }

        for (ItemStack stack : secondary) {
            if (NEIServerUtils.areStacksSameTypeCrafting(stack, ingredient)) {
                for (ItemStack primaryStack : primary) {
                    SmeltingPair arecipe = new SmeltingPair(primaryStack, stack, new ItemStack(ModItems.wroughtIronIngot));
                    arecipe.setIngredientPermutation(Arrays.asList(new PositionedStack[]{arecipe.ingred2}), ingredient);
                    arecipes.add(arecipe);
                }
            }
        }
    }

    @Override
    public void drawExtras (int recipe) {
        this.drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
        this.drawProgressBar(74, 23, 176, 14, 24, 16, 48, 0);
    }

    private static void findFuels () {
        afuels = new ArrayList<FuelPair>();
        afuels.add(new FuelPair(new ItemStack(Items.coal, 1, 1), 1600));
    }

    @Override
    public String getOverlayIdentifier () {
        return "bloomerySmelting";
    }
}
