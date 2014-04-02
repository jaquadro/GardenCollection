package com.jaquadro.minecraft.hungerstrike;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayer implements IExtendedEntityProperties
{
    public final static String TAG = "HungerStrike";

    private final EntityPlayer player;

    private boolean hungerStrikeEnabled;
    private int startHunger;

    public ExtendedPlayer(EntityPlayer player) {
        this.player = player;
        this.hungerStrikeEnabled = false;
    }

    public static final void register (EntityPlayer player) {
        player.registerExtendedProperties(TAG, new ExtendedPlayer(player));
    }

    public static final ExtendedPlayer get (EntityPlayer player) {
        return (ExtendedPlayer) player.getExtendedProperties(TAG);
    }

    @Override
    public void init(Entity entity, World world) { }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = new NBTTagCompound();
        properties.setBoolean("Enabled", hungerStrikeEnabled);

        compound.setTag(TAG, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = (NBTTagCompound) compound.getTag(TAG);

        hungerStrikeEnabled = properties.getBoolean("Enabled");
    }

    public void enableHungerStrike (boolean enable) {
        hungerStrikeEnabled = enable;
    }

    public boolean isOnHungerStrike () {
        return hungerStrikeEnabled;
    }

    private boolean shouldTick () {
        ConfigManager.Mode mode = HungerStrike.instance.config.getMode();
        if (mode == ConfigManager.Mode.LIST)
            return hungerStrikeEnabled;
        else
            return mode == ConfigManager.Mode.ALL;
    }

    public void tick (TickEvent.Phase phase, Side side) {
        if (!shouldTick())
            return;

        if (phase == TickEvent.Phase.START)
            tickStart();
        else if (phase == TickEvent.Phase.END)
            tickEnd(side);
    }

    private void tickStart () {
        setFoodData(player.getFoodStats(), calcBaselineHunger(), 1);
        startHunger = player.getFoodStats().getFoodLevel();
    }

    private void tickEnd (Side side) {
        if (side == Side.SERVER) {
            int foodDiff = player.getFoodStats().getFoodLevel() - startHunger;
            if (foodDiff > 0)
                player.heal(foodDiff * (float)HungerStrike.instance.config.getFoodHealFactor());
        }

        setFoodData(player.getFoodStats(), calcBaselineHunger(), 1);
    }

    private void setFoodData (FoodStats foodStats, int foodLevel, float saturationLevel) {
        foodStats.addStats(1, (saturationLevel - foodStats.getSaturationLevel()) / 2);
        foodStats.addStats(foodLevel - foodStats.getFoodLevel(), 0);
    }

    private int calcBaselineHunger () {
        if (player.isPotionActive(Potion.hunger))
            return 5;
        else if (player.isPotionActive(Potion.regeneration))
            return 20;
        else
            return 10;
    }
}
