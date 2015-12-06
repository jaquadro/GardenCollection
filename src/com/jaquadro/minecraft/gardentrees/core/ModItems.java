package com.jaquadro.minecraft.gardentrees.core;

import com.jaquadro.minecraft.gardentrees.GardenTrees;
import com.jaquadro.minecraft.gardentrees.item.ItemSeeds;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ModItems
{
    public static Item candelilla_seeds;
    public static Item candelilla;

    public void init () {
        candelilla_seeds = new ItemSeeds(ModBlocks.candelilla).setUnlocalizedName(makeName("candelillaSeeds")).setTextureName(GardenTrees.MOD_ID + ":candelilla_seeds");
        candelilla = new Item().setUnlocalizedName(makeName("candelilla")).setCreativeTab(ModCreativeTabs.tabGardenTrees).setTextureName(GardenTrees.MOD_ID + ":candelilla");

        GameRegistry.registerItem(candelilla_seeds, "candelilla_seeds");
        GameRegistry.registerItem(candelilla, "candelilla");
    }

    public static String makeName (String name) {
        return GardenTrees.MOD_ID.toLowerCase() + "." + name;
    }
}
