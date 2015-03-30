package com.jaquadro.minecraft.gardenstuff.integration.nei;

import codechicken.nei.api.API;
import com.jaquadro.minecraft.gardenstuff.client.gui.GuiBloomeryFurnace;

public class NEIHelper
{
    public static void registerNEI () {
        API.registerRecipeHandler(new BloomeryFurnaceRecipeHandler());
        API.registerUsageHandler(new BloomeryFurnaceRecipeHandler());
        API.registerGuiOverlay(GuiBloomeryFurnace.class, "bloomerySmelting");
    }
}
