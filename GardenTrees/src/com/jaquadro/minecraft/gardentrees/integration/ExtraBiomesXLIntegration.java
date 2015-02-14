package com.jaquadro.minecraft.gardentrees.integration;

import com.jaquadro.minecraft.gardencore.api.SaplingRegistry;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardentrees.world.gen.OrnamentalTreeFactory;
import com.jaquadro.minecraft.gardentrees.world.gen.OrnamentalTreeRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

public class ExtraBiomesXLIntegration
{
    public static final String MOD_ID = "ExtrabiomesXL";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        Map<String, int[]> saplingBank1 = new HashMap<String, int[]>();
        saplingBank1.put("small_oak", new int[] { 0, 1, 2, 3 });
        saplingBank1.put("large_spruce", new int[] { 4 });
        saplingBank1.put("large_oak", new int[] { 5 });
        saplingBank1.put("small_acacia", new int[] { 6 });
        saplingBank1.put("small_cyprus", new int[] { 7 });

        Map<String, int[]> saplingBank2 = new HashMap<String, int[]>();
        saplingBank2.put("small_cyprus", new int[] { 0 });
        saplingBank2.put("small_oak", new int[] { 3, 4 });
        saplingBank2.put("small_canopy", new int[] { 1 });
        saplingBank2.put("small_shrub", new int[] { 2 });

        Map<Item, Map<String, int[]>> banks = new HashMap<Item, Map<String, int[]>>();
        banks.put(GameRegistry.findItem(MOD_ID, "saplings_1"), saplingBank1);
        banks.put(GameRegistry.findItem(MOD_ID, "saplings_2"), saplingBank2);

        SaplingRegistry saplingReg = SaplingRegistry.instance();

        for (Map.Entry<Item, Map<String, int[]>> entry : banks.entrySet()) {
            Item sapling = entry.getKey();

            for (Map.Entry<String, int[]> bankEntry : entry.getValue().entrySet()) {
                OrnamentalTreeFactory factory = OrnamentalTreeRegistry.getTree(bankEntry.getKey());
                if (factory == null)
                    continue;

                for (int i : bankEntry.getValue()) {
                    UniqueMetaIdentifier woodBlock = saplingReg.getWoodForSapling(sapling, i);
                    UniqueMetaIdentifier leafBlock = saplingReg.getLeavesForSapling(sapling, i);
                    if (woodBlock == null && leafBlock == null)
                        continue;

                    saplingReg.putExtendedData(sapling, i, "sm_generator",
                        factory.create(woodBlock.getBlock(), woodBlock.meta, leafBlock.getBlock(), leafBlock.meta));
                }
            }
        }
    }
}
