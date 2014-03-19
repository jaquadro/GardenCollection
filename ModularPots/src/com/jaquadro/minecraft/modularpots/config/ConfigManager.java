package com.jaquadro.minecraft.modularpots.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigManager
{
    private final Configuration config;

    private ConfigCategory categoryPatterns;
    private ConfigCategory categoryPatternSettings;

    private ConfigCategory defaultPatternSettings;
    private ConfigCategory[] patternSettings = new ConfigCategory[256];

    private String[] overlayImages = new String[256];
    private String[] overlayNames = new String[256];

    public ConfigManager (File file) {
        config = new Configuration(file);

        if (!config.hasCategory("patterns")) {
            categoryPatterns = config.getCategory("patterns");
            categoryPatterns.setComment("Patterns are additional textures that can be overlaid on top of large pots, both normal and colored.\n"
                + "For each pattern defined, a corresponding 'stamp' item is registered.  The stamp is used with the\n"
                + "pottery table to apply patterns to raw clay pots.\n\n"
                + "This mod can support up to 255 registered patterns.  To add a new pattern, create a new entry in the\n"
                + "config below using the form:\n\n"
                + "  S:pattern.#=texture_name; A Name\n\n"
                + "Where # is an id between 1 and 255 inclusive.\n"
                + "Place a corresponding texture_name.png file into the mod's jar file in assets/modularpots/textures/blocks.\n"
                + "To further control aspects of the pattern, seeing the next section, pattern_settings.");

            config.get(categoryPatterns.getQualifiedName(), "pattern.1", "large_pot_1; Serpent");
            config.get(categoryPatterns.getQualifiedName(), "pattern.2", "large_pot_2; Lattice");
            config.get(categoryPatterns.getQualifiedName(), "pattern.3", "large_pot_3; Offset Squares");
            config.get(categoryPatterns.getQualifiedName(), "pattern.4", "large_pot_4; Inset");
            config.get(categoryPatterns.getQualifiedName(), "pattern.5", "large_pot_5; Turtle");
            config.get(categoryPatterns.getQualifiedName(), "pattern.6", "large_pot_6; Creeper");

            categoryPatternSettings = config.getCategory("pattern_settings");
            categoryPatternSettings.setComment("Specifies all the attributes for patterns.  Attributes control how patterns can be found in the world.\n"
                + "In the future, they might control other aspects, such as how patterns are rendered.\n\n"
                + "By default, all patterns will take their attributes from the 'pattern_default' subcategory.  To\n"
                + "customize some or all attributes for a pattern, create a new subcategory modeled like this:\n\n"
                + "  pattern_# {\n"
                + "      S:gen=dungeonChest, 5; mineshaftCorridor, 5\n"
                + "  }\n\n"
                + "The S:gen option controls what kinds of dungeon chests the pattern's stamp item will appear in, and the\n"
                + "rarity of the item appearing.  The location and rarity are separated by a comma (,), and multiple locations\n"
                + "are separated with a semicolon (;).  Rarity is a value between 1 and 100, with 1 being very rare.  Golden\n"
                + "apples and diamond horse armor also have a rarity of 1.  Most vanilla items have a rarity of 10.  The valid\n"
                + "location strings are:\n\n"
                + "  mineshaftCorridor, pyramidDesertChest, pyramidJungleChest, strongholdCorridor, strongholdLibrary,\n"
                + "  strongholdCrossing, villageBlacksmith, dungeonChest");

            defaultPatternSettings = config.getCategory("pattern_settings.pattern_default");
            config.get(defaultPatternSettings.getQualifiedName(), "name", "Unknown");
            config.get(defaultPatternSettings.getQualifiedName(), "gen", "dungeonChest, 3; mineshaftCorridor, 3");

            patternSettings[2] = config.getCategory("pattern_settings.pattern_2");
            config.get(patternSettings[2].getQualifiedName(), "gen", "dungeonChest, 5; mineshaftCorridor, 3");
        }
        else {
            categoryPatterns = config.getCategory("patterns");
            categoryPatternSettings = config.getCategory("pattern_settings");

            defaultPatternSettings = config.getCategory("pattern_settings.pattern_default");
            for (int i = 1; i < 256; i++) {
                if (config.hasCategory("pattern_settings.pattern_" + i))
                    patternSettings[i] = config.getCategory("pattern_settings.pattern_" + i);
            }
        }

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
