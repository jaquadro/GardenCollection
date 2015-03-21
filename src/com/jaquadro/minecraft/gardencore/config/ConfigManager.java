package com.jaquadro.minecraft.gardencore.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class ConfigManager
{
    private final Configuration config;

    public boolean enableCompostBonemeal;
    public double compostBonemealStrength;
    public boolean enableTilledSoilGrowthBonus;

    public ConfigManager (File file) {
        config = new Configuration(file);

        Property propEnableCompostBonemeal = config.get(Configuration.CATEGORY_GENERAL, "enableCompostBonemeal", true);
        propEnableCompostBonemeal.comment = "Allows compost trigger plant growth like bonemeal.";
        enableCompostBonemeal = propEnableCompostBonemeal.getBoolean();

        Property propCompostBonemealStrength = config.get(Configuration.CATEGORY_GENERAL, "compostBonemealStrength", 0.5);
        propCompostBonemealStrength.comment = "The probability that compost will succeed when used as bonemeal relative to bonemeal.";
        compostBonemealStrength = propCompostBonemealStrength.getDouble();

        Property propEnableTilledSoilGrowthBonus = config.get(Configuration.CATEGORY_GENERAL, "enableTilledSoilGrowthBonus", true).setRequiresMcRestart(true);
        propEnableTilledSoilGrowthBonus.comment = "Allows tilled garden soil to advance crop growth more quickly.  Enables random ticks.";
        enableTilledSoilGrowthBonus = propEnableTilledSoilGrowthBonus.getBoolean();

        config.save();
    }
}
