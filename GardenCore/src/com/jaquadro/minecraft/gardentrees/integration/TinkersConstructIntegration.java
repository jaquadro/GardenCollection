package com.jaquadro.minecraft.gardentrees.integration;

import com.jaquadro.minecraft.gardentrees.core.recipe.WoodPostRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class TinkersConstructIntegration
{
    public static final String MODID = "TConstruct";

    public static void init () {
        if (!Loader.isModLoaded(MODID))
            return;

        Item toolHatchet = GameRegistry.findItem(MODID, "hatchet");
        Item toolLumberAxe = GameRegistry.findItem(MODID, "lumberaxe");

        WoodPostRecipe.axeList.add(toolHatchet);
        WoodPostRecipe.axeList.add(toolLumberAxe);
    }
}
