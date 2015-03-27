package com.jaquadro.minecraft.gardencore.integration;

import codechicken.nei.api.API;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.client.gui.GuiCompostBin;
import com.jaquadro.minecraft.gardencore.core.ModItems;
import cpw.mods.fml.common.Loader;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class NEIIntegration
{
    public static final String MOD_ID = "NotEnoughItems";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        registerNEI();
    }

    private static void registerNEI () {
        API.registerRecipeHandler(new CompostBinRecipeHandler());
        API.registerGuiOverlay(GuiCompostBin.class, "composting");
    }

    public static class CompostBinRecipeHandler extends TemplateRecipeHandler
    {
        @Override
        public Class<? extends GuiContainer> getGuiClass () {
            return GuiCompostBin.class;
        }

        @Override
        public String getRecipeName () {
            return "Compost Bin!";
        }

        @Override
        public TemplateRecipeHandler newInstance () {
            return super.newInstance();
        }

        @Override
        public void loadCraftingRecipes (String outputId, Object... results) {
            for (Object obj : results) {
                if (!(obj instanceof ItemStack))
                    continue;

                if (((ItemStack) obj).getItem() == ModItems.compostPile) {

                }
            }
            super.loadCraftingRecipes(outputId, results);
        }

        @Override
        public void loadCraftingRecipes (ItemStack result) {
            super.loadCraftingRecipes(result);
        }

        @Override
        public String getGuiTexture () {
            return GardenCore.MOD_ID + ":textures/gui/compostBin.png";
        }

        @Override
        public String getOverlayIdentifier () {
            return "composting";
        }
    }
}
