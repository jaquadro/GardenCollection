package com.jaquadro.minecraft.modularpots.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigManager
{
    private final Configuration config;

    private ConfigCategory categoryPatterns;

    private String[] overlayImages = new String[256];
    private String[] overlayNames = new String[256];

    public ConfigManager (File file) {
        config = new Configuration(file);

        if (!config.hasCategory("patterns")) {
            categoryPatterns = config.getCategory("patterns");

            config.get(categoryPatterns.getQualifiedName(), "pattern.1", "large_pot_1; Serpent");
            config.get(categoryPatterns.getQualifiedName(), "pattern.2", "large_pot_2; Lattice");
            config.get(categoryPatterns.getQualifiedName(), "pattern.3", "large_pot_3; Offset Squares");
            config.get(categoryPatterns.getQualifiedName(), "pattern.4", "large_pot_4; Inset");
            config.get(categoryPatterns.getQualifiedName(), "pattern.5", "large_pot_5; Turtle");
            config.get(categoryPatterns.getQualifiedName(), "pattern.6", "large_pot_6; Creeper");
        }
        else
            categoryPatterns = config.getCategory("patterns");

        for (int i = 1; i < 256; i++) {
            if (config.hasKey(categoryPatterns.getQualifiedName(), "pattern." + i)) {
                String entry = config.get(categoryPatterns.getQualifiedName(), "pattern." + i, "").getString();
                String[] parts = entry.split("[ ]*;[ ]*");

                overlayImages[i] = parts[0];
                if (parts.length > 1)
                    overlayNames[i] = parts[1];
            }
        }

        config.save();
    }

    public String getOverlayImage (int index) {
        return overlayImages[index];
    }

    public String getOverlayName (int index) {
        return overlayNames[index];
    }
}
