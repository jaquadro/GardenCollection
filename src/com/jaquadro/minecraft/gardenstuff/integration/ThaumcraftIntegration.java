package com.jaquadro.minecraft.gardenstuff.integration;


import com.jaquadro.minecraft.gardenapi.api.GardenAPI;
import com.jaquadro.minecraft.gardenapi.api.component.ILanternSource;
import com.jaquadro.minecraft.gardenstuff.integration.lantern.ThaumcraftCandleSource;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class ThaumcraftIntegration
{
    public static final String MOD_ID = "Thaumcraft";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        Block blockCandle = GameRegistry.findBlock(ThaumcraftIntegration.MOD_ID, "blockCandle");
        ILanternSource candleSource = new ThaumcraftCandleSource(blockCandle);

        GardenAPI.instance().registries().lanternSources().registerLanternSource(candleSource);
    }
}
