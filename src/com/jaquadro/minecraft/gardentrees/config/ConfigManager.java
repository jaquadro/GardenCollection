package com.jaquadro.minecraft.gardentrees.config;

import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager
{
    private final Configuration config;

    private ItemStack[] strangePlantDrops = null;

    public double strangePlantDropChance;
    public int strangePlantDropMin;
    public int strangePlantDropMax;

    public boolean compostGrowsOrnamentalTrees;

    public ConfigManager (File file) {
        config = new Configuration(file);

        Property propStrangePlantDrops = config.get(Configuration.CATEGORY_GENERAL, "strangePlantDrops", new String[0]);
        propStrangePlantDrops.comment = "A list of zero or more item IDs.  Breaking the plant will drop an item picked at random from the list.  Ex: minecraft:coal:1";

        Property propStrangePlantDropChance = config.get(Configuration.CATEGORY_GENERAL, "strangePlantDropChance", 1.0);
        propStrangePlantDropChance.comment = "The probability from 0.0 - 1.0 that breaking a strange plant will drop its contents.";
        strangePlantDropChance = propStrangePlantDropChance.getDouble();

        Property propStrangePlantDropMin = config.get(Configuration.CATEGORY_GENERAL, "strangePlantDropMin", 1);
        propStrangePlantDropMin.comment = "The minimum number of items dropped when breaking a strange plant.";
        strangePlantDropMin = propStrangePlantDropMin.getInt();

        Property propStrangePlantDropMax = config.get(Configuration.CATEGORY_GENERAL, "strangePlantDropMax", 1);
        propStrangePlantDropMax.comment = "The maximum number of items dropped when breaking a strange plant.";
        strangePlantDropMax = propStrangePlantDropMax.getInt();

        Property propCompostGrowsOrnamentalTrees = config.get(Configuration.CATEGORY_GENERAL, "compostGrowsOrnamentalTrees", true);
        propCompostGrowsOrnamentalTrees.comment = "Using compost on saplings will grow ornamental (miniature) trees instead of normal trees.";
        compostGrowsOrnamentalTrees = propCompostGrowsOrnamentalTrees.getBoolean();

        config.save();
    }

    public void postInit () {

    }

    private void parseStrangePlantItems (Property property) {
        String[] entries = property.getStringList();
        if (entries == null || entries.length == 0) {
            strangePlantDrops = new ItemStack[0];
            return;
        }

        List<ItemStack> results = new ArrayList<ItemStack>();

        for (String entry : entries) {
            UniqueMetaIdentifier uid = new UniqueMetaIdentifier(entry);
            int meta = (uid.meta == OreDictionary.WILDCARD_VALUE) ? 0 : uid.meta;

            Item item = GameRegistry.findItem(uid.modId, uid.name);
            if (item != null) {
                results.add(new ItemStack(item, 1, meta));
                continue;
            }

            Block block = GameRegistry.findBlock(uid.modId, uid.name);
            if (block != null) {
                item = Item.getItemFromBlock(block);
                if (item != null) {
                    results.add(new ItemStack(item, 1, meta));
                    continue;
                }
            }
        }

        strangePlantDrops = new ItemStack[results.size()];
        for (int i = 0; i < results.size(); i++)
            strangePlantDrops[i] = results.get(i);
    }

    public ItemStack[] getStrangePlantDrops () {
        if (strangePlantDrops == null) {
            Property propStrangePlantDrops = config.get(Configuration.CATEGORY_GENERAL, "strangePlantDrops", new String[0]);
            parseStrangePlantItems(propStrangePlantDrops);
        }

        return strangePlantDrops;
    }
}
