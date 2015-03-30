package com.jaquadro.minecraft.gardenstuff.integration;

import codechicken.nei.api.API;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.client.gui.GuiCompostBin;
import com.jaquadro.minecraft.gardencore.core.ModItems;
import com.jaquadro.minecraft.gardenstuff.client.gui.GuiBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.integration.nei.BloomeryFurnaceRecipeHandler;
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
        API.registerRecipeHandler(new BloomeryFurnaceRecipeHandler());
        API.registerUsageHandler(new BloomeryFurnaceRecipeHandler());
        API.registerGuiOverlay(GuiBloomeryFurnace.class, "bloomerySmelting");
    }
}
