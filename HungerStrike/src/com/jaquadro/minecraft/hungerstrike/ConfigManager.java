package com.jaquadro.minecraft.hungerstrike;

import com.jaquadro.minecraft.hungerstrike.network.SyncExtendedPlayerPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
    private int foodStackSize = -1;
    private boolean hideHungerBar = true;

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

        Property foodStackSizeProperty = config.get(Configuration.CATEGORY_GENERAL, "MaxFoodStackSize", -1);
        foodStackSizeProperty.comment = "Globally overrides the maximum stack size of food items.\n" +
            "This property affects all Vanilla and Mod food items that derive from ItemFood.\n" +
            "Set to -1 to retain the default stack size of each food item.  Note: This will affect the\n" +
            "entire server, not just players on hunger strike.";

        Property hideHungerBarProperty = config.get(Configuration.CATEGORY_GENERAL, "HideHungerBar", true);
        hideHungerBarProperty.comment = "Controls whether or not the hunger bar is hidden for players\n" +
            "on hunger strike.  If the hunger bar is left visible, it will remain filled at half capacity,\n" +
            "except when certain potion effects are active like hunger and regeneration.";

        mode = Mode.fromValueIgnoreCase(modeProperty.getString());
        foodHealFactor = foodHealProperty.getDouble(.5);
        foodStackSize = foodStackSizeProperty.getInt(-1);
        hideHungerBar = hideHungerBarProperty.getBoolean(true);

        config.save();
    }

    public double getFoodHealFactor () {
        return foodHealFactor;
    }

    public int getFoodStackSize () {
        return foodStackSize;
    }

    public boolean isHungerBarHidden () {
        return hideHungerBar;
    }

    public Mode getMode () {
        return mode;
    }

    public void setMode (Mode value) {
        setModeSoft(value);

        modeProperty.set(mode.toString());
        config.save();
    }

    public void setModeSoft (Mode value) {
        mode = value;
    }
}
