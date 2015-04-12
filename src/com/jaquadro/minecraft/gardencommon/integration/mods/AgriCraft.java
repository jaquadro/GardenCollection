package com.jaquadro.minecraft.gardencommon.integration.mods;

import com.jaquadro.minecraft.gardencommon.integration.IntegrationModule;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import net.minecraft.block.Block;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Set;

public class AgriCraft extends IntegrationModule
{
    static Class classGrowthRequirements;
    static Class classBlockWithMeta;

    static Constructor constBlockWithMeta;

    static Field fieldDefaultSoils;

    @Override
    public String getModID () {
        return "AgriCraft";
    }

    @Override
    public void init () throws Throwable {
        classGrowthRequirements = Class.forName("com.InfinityRaider.AgriCraft.farming.GrowthRequirements");
        classBlockWithMeta = Class.forName("com.InfinityRaider.AgriCraft.utility.BlockWithMeta");

        constBlockWithMeta = classBlockWithMeta.getConstructor(Block.class, int.class);

        fieldDefaultSoils = classGrowthRequirements.getField("defaultSoils");
    }

    @Override
    public void postInit () throws Throwable {
        Set defaultSoils = (Set) fieldDefaultSoils.get(null);

        defaultSoils.add(constBlockWithMeta.newInstance(ModBlocks.gardenFarmland, 0));
    }
}
