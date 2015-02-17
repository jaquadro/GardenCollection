package com.jaquadro.minecraft.gardentrees.world.gen;

import java.util.HashMap;
import java.util.Map;

public class OrnamentalTreeRegistry
{
    private static Map<String, OrnamentalTreeFactory> registry = new HashMap<String, OrnamentalTreeFactory>();

    public static void registerTree (String name, OrnamentalTreeFactory treeFactory) {
        registry.put(name, treeFactory);
    }

    public static OrnamentalTreeFactory getTree (String name) {
        return registry.get(name);
    }

    static {
        registerTree("small_oak", WorldGenStandardOrnTree.SmallOakTree.FACTORY);
        registerTree("small_spruce", WorldGenStandardOrnTree.SmallSpruceTree.FACTORY);
        registerTree("small_jungle", WorldGenStandardOrnTree.SmallJungleTree.FACTORY);
        registerTree("small_acacia", WorldGenStandardOrnTree.SmallAcaciaTree.FACTORY);
        registerTree("small_palm", WorldGenStandardOrnTree.SmallPalmTree.FACTORY);
        registerTree("small_willow", WorldGenStandardOrnTree.SmallWillowTree.FACTORY);
        registerTree("small_pine", WorldGenStandardOrnTree.SmallPineTree.FACTORY);
        registerTree("small_mahogany", WorldGenStandardOrnTree.SmallMahoganyTree.FACTORY);
        registerTree("small_shrub", WorldGenStandardOrnTree.SmallShrubTree.FACTORY);
        registerTree("small_canopy", WorldGenStandardOrnTree.SmallCanopyTree.FACTORY);
        registerTree("small_cyprus", WorldGenStandardOrnTree.SmallCyprusTree.FACTORY);
        registerTree("tall_small_oak", WorldGenStandardOrnTree.TallSmallOakTree.FACTORY);
        registerTree("large_oak", WorldGenStandardOrnTree.LargeOakTree.FACTORY);
        registerTree("large_spruce", WorldGenStandardOrnTree.LargeSpruceTree.FACTORY);
    }
}
