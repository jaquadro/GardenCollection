package com.jaquadro.minecraft.hungerstrike;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class ConfigManager
{
    public enum Mode {
        NONE,
        LIST,
        ALL;

        public static Mode fromValueIgnoreCase (String value) {
            if (value.compareToIgnoreCase("NONE") == 0)
                return Mode.NONE;
            else if (value.compareToIgnoreCase("LIST") == 0)
                return Mode.LIST;
            else if (value.compareToIgnoreCase("ALL") == 0)
                return Mode.ALL;

            return Mode.LIST;
        }
    }
    private Configuration config;

    private Property modeProperty;

    private Mode mode = Mode.LIST;
    private double foodHealFactor = .5;

    public void setup (File location) {
        config = new Configuration(location);
        config.load();

        modeProperty = config.get(Configuration.CATEGORY_GENERAL, "Mode", "ALL");
        modeProperty.comment = "Mode can be set to NONE, LIST, or ALL\n" +
            "- NONE: Hunger Strike is disabled for all players.\n" +
            "- LIST: Hunger Strike is enabled for players added in-game with /hungerstrike command.\n" +
            "- ALL: Hunger Strike is enabled for all players.";

        Property foodHealProperty = config.get(Configuration.CATEGORY_GENERAL, "FoodHealFactor", .5f);
        foodHealProperty.comment = "How to translate food points into heart points when consuming food.\n" +
            "At the default value of 0.5, food fills your heart bar at half the rate it would fill hunger.";

        mode = Mode.fromValueIgnoreCase(modeProperty.getString());
        foodHealFactor = foodHealProperty.getDouble(.5);

        config.save();
    }

    public double getFoodHealFactor () {
        return foodHealFactor;
    }

    public Mode getMode () {
        return mode;
    }

    public void setMode (Mode value) {
        mode = value;

        modeProperty.set(mode.toString());
        config.save();
    }
}
