package com.jaquadro.minecraft.gardencontainers.config;

import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigManager
{
    private static final String CAT_PATTERNS = "1_patterns";
    private static final String CAT_SETTINGS = "2_pattern_settings";

    private final Configuration config;

    private ConfigCategory categoryPatterns;
    private ConfigCategory categoryPatternSettings;

    private ConfigCategory defaultPatternSettings;

    private PatternConfig defaultPattern;
    private PatternConfig[] patterns = new PatternConfig[256];
    private int patternCount;

    public ConfigManager (File file) {
        config = new Configuration(file);

        boolean firstGen = !config.hasCategory(CAT_PATTERNS);

        categoryPatterns = config.getCategory(CAT_PATTERNS);
        categoryPatterns.setComment("Patterns are additional textures that can be overlaid on top of large pots, both normal and colored.\n"
            + "For each pattern defined, a corresponding 'stamp' item is registered.  The stamp is used with the\n"
            + "pottery table to apply patterns to raw clay pots.\n\n"
            + "This mod can support up to 255 registered patterns.  To add a new pattern, create a new entry in the\n"
            + "config below using the form:\n\n"
            + "  S:pattern.#=texture_name; A Name\n\n"
            + "Where # is an id between 1 and 255 inclusive.\n"
            + "Place a corresponding texture_name.png file into the mod's jar file in assets/modularpots/textures/blocks.\n"
            + "To further control aspects of the pattern, seeing the next section, pattern_settings.\n\n"
            + "Note: Future versions of this mod may add new patterns.  If you haven't made any changes to this\n"
            + "configuration, simply delete it and let it regenerate.  Otherwise visit the mod's development thread\n"
            + "on Minecraft Forums to see what's changed.");

        categoryPatternSettings = config.getCategory(CAT_SETTINGS);
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

        populateDefaultPattern();

        if (firstGen) {
            config.get(categoryPatterns.getQualifiedName(), "pattern.1", "large_pot_1; Serpent");
            config.get(categoryPatterns.getQualifiedName(), "pattern.2", "large_pot_2; Lattice");
            config.get(categoryPatterns.getQualifiedName(), "pattern.3", "large_pot_3; Offset Squares");
            config.get(categoryPatterns.getQualifiedName(), "pattern.4", "large_pot_4; Inset");
            config.get(categoryPatterns.getQualifiedName(), "pattern.5", "large_pot_5; Turtle");
            config.get(categoryPatterns.getQualifiedName(), "pattern.6", "large_pot_6; Creeper");
            config.get(categoryPatterns.getQualifiedName(), "pattern.7", "large_pot_7; Freewheel");
            config.get(categoryPatterns.getQualifiedName(), "pattern.8", "large_pot_8; Creepy Castle");
            config.get(categoryPatterns.getQualifiedName(), "pattern.9", "large_pot_9; Savannah");

            config.getCategory(CAT_SETTINGS + ".pattern_2");
            config.get(CAT_SETTINGS + ".pattern_2", "gen", "dungeonChest, 3; mineshaftCorridor, 2");

            config.save();
        }

        for (int i = 1; i < 256; i++) {
            if (config.hasKey(categoryPatterns.getQualifiedName(), "pattern." + i)) {
                String entry = config.get(categoryPatterns.getQualifiedName(), "pattern." + i, "").getString();
                String[] parts = entry.split("[ ]*;[ ]*");

                String overlay = parts[0];
                String name = (parts.length > 1) ? parts[1] : null;

                patterns[i] = new PatternConfig(i, overlay, name);
                if (config.hasCategory(CAT_SETTINGS + ".pattern_" + i))
                    parsePatternAttribs(patterns[i], CAT_SETTINGS + ".pattern_" + i);
                else {
                    if (patterns[i].getName() == null)
                        patterns[i].setName(defaultPattern.getName());

                    for (int j = 0; j < defaultPattern.getLocationCount(); j++)
                        patterns[i].addGenLocation(defaultPattern.getGenLocation(j), defaultPattern.getGenRarity(j));
                }

                patternCount++;
            }
        }
    }

    private void populateDefaultPattern () {
        defaultPattern = new PatternConfig(0, "", "");

        defaultPatternSettings = config.getCategory(CAT_SETTINGS + ".pattern_default");
        String name = config.get(defaultPatternSettings.getQualifiedName(), "name", "Unknown").getString();
        String gen = config.get(defaultPatternSettings.getQualifiedName(), "gen", "dungeonChest, 2; mineshaftCorridor, 1").getString();

        defaultPattern.setName(name);
        parseGenString(defaultPattern, gen);
    }

    private void parsePatternAttribs (PatternConfig pattern, String category) {
        if (config.hasKey(category, "name")) {
            String name = config.get(defaultPatternSettings.getQualifiedName(), "name", "Unknown").getString();
            pattern.setName(name);
        }
        else if (pattern.getName() == null)
            pattern.setName(defaultPattern.getName());

        if (config.hasKey(category, "gen")) {
            String gen = config.get(defaultPatternSettings.getQualifiedName(), "gen", "").getString();
            parseGenString(pattern, gen);
        }
        else {
            for (int j = 0; j < defaultPattern.getLocationCount(); j++)
                pattern.addGenLocation(defaultPattern.getGenLocation(j), defaultPattern.getGenRarity(j));
        }
    }

    private void parseGenString (PatternConfig pattern, String genString) {
        String[] strParts = genString.split("[ ]*;[ ]*");
        for (int i = 0; i < strParts.length; i++) {
            String[] locParts = strParts[i].split("[ ]*,[ ]*");
            if (locParts.length != 2)
                continue;

            String location = mapGenLocation(locParts[0]);
            int rarity = Integer.parseInt(locParts[1]);
            if (location == null)
                continue;

            pattern.addGenLocation(location, rarity);
        }
    }

    private String mapGenLocation (String location) {
        if (location.equals("mineshaftCorridor"))
            return ChestGenHooks.MINESHAFT_CORRIDOR;
        if (location.equals("pyramidDesertChest"))
            return ChestGenHooks.PYRAMID_DESERT_CHEST;
        if (location.equals("pyramidJungleChest"))
            return ChestGenHooks.PYRAMID_JUNGLE_CHEST;
        if (location.equals("strongholdCorridor"))
            return ChestGenHooks.STRONGHOLD_CORRIDOR;
        if (location.equals("strongholdLibrary"))
            return ChestGenHooks.STRONGHOLD_LIBRARY;
        if (location.equals("strongholdCrossing"))
            return ChestGenHooks.STRONGHOLD_CROSSING;
        if (location.equals("villageBlacksmith"))
            return ChestGenHooks.VILLAGE_BLACKSMITH;
        if (location.equals("dungeonChest"))
            return ChestGenHooks.DUNGEON_CHEST;

        return null;
    }

    public boolean hasPattern (int index) {
        return patterns[index] != null;
    }

    public PatternConfig getPattern (int index) {
        return patterns[index];
    }

    public int getPatternCount () {
        return patternCount;
    }

    /*public String getOverlayImage (int index) {
        return overlayImages[index];
    }

    public String getOverlayName (int index) {
        return overlayNames[index];
    }*/
}
