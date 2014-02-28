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

    public void setup (File location) {
        config = new Configuration(location);
        config.load();

        modeProperty = config.get(Configuration.CATEGORY_GENERAL, "Mode", "LIST");
        modeProperty.comment = "Mode can be set to NONE, LIST, or ALL\n" +
            "- NONE: Hunger Strike is disabled for all players.\n" +
            "- LIST: Hunger Strike is enabled for players added in-game with /hungerstrike command.\n" +
            "- ALL: Hunger Strike is enabled for all players.";

        mode = Mode.fromValueIgnoreCase(modeProperty.getString());

        config.save();
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
