package com.jaquadro.minecraft.gardencontainers.core.handlers;

import com.jaquadro.minecraft.gardencontainers.GardenContainers;
import com.jaquadro.minecraft.gardencontainers.core.ModItems;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

public class VillagerTradeHandler implements VillagerRegistry.IVillageTradeHandler
{
    private static VillagerTradeHandler instance = new VillagerTradeHandler();

    public static VillagerTradeHandler instance () {
        return instance;
    }

    public void load () {
        if (GardenContainers.config.enableVillagerTrading) {
            VillagerRegistry.instance().registerVillageTradeHandler(1, this);
            VillagerRegistry.instance().registerVillageTradeHandler(2, this);
            VillagerRegistry.instance().registerVillageTradeHandler(3, this);
        }
    }

    @Override
    public void manipulateTradesForVillager (EntityVillager villager, MerchantRecipeList recipeList, Random random) {
        float buyProb = 1.5f / GardenContainers.config.getPatternCount();
        float sellProb = .5f / GardenContainers.config.getPatternCount();

        for (int i = 1; i < 256; i++) {
            if (GardenContainers.config.hasPattern(i)) {
                if (random.nextFloat() < buyProb) {
                    int emeralds = 1 + random.nextInt(4);
                    recipeList.add(new MerchantRecipe(new ItemStack(ModItems.potteryPattern, 1, i), new ItemStack(Items.emerald, emeralds)));
                }
                else if (random.nextFloat() < sellProb) {
                    int emeralds = 8 + random.nextInt(16);
                    recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, emeralds), new ItemStack(ModItems.potteryPattern, 1, i)));
                }
            }
        }
    }
}
