package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by Justin on 2/19/2015.
 */
public class AgriCraftIntegration
{
    public static final String MOD_ID = "AgriCraft";

    private static Class classGrowthRequirements;
    private static Class classBlockWithMeta;

    private static Constructor constBlockWithMeta;

    private static Field fieldDefaultSoils;

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;


        try {
            classGrowthRequirements = Class.forName("com.InfinityRaider.AgriCraft.farming.GrowthRequirements");
            classBlockWithMeta = Class.forName("com.InfinityRaider.AgriCraft.utility.BlockWithMeta");

            constBlockWithMeta = classBlockWithMeta.getConstructor(Block.class, int.class);

            fieldDefaultSoils = classGrowthRequirements.getField("defaultSoils");

            Set defaultSoils = (Set) fieldDefaultSoils.get(null);

            defaultSoils.add(constBlockWithMeta.newInstance(ModBlocks.gardenFarmland, 0));
        }
        catch (Throwable t) { }
    }
}
