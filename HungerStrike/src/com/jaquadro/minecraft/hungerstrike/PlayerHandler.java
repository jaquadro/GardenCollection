package com.jaquadro.minecraft.hungerstrike;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerHandler
{
    private static final Map<GameProfile, Map<String, NBTTagCompound>> dataStore = new HashMap<GameProfile, Map<String, NBTTagCompound>>();

    public static List<EntityPlayer> getStrikingPlayers () {
        return getPlayers(true);
    }

    public static List<EntityPlayer> getNonStrikingPlayers () {
        return getPlayers(false);
    }

    private static List<EntityPlayer> getPlayers (boolean isStriking) {
        List<EntityPlayer> players = new ArrayList<EntityPlayer>();
        for (Object player : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            EntityPlayer playerEnt = (EntityPlayer) player;
            ExtendedPlayer playerExt = ExtendedPlayer.get(playerEnt);
            if (playerExt != null && playerExt.isOnHungerStrike() == isStriking)
                players.add(playerEnt);
        }

        return players;
    }

    public void storeData (EntityPlayer player) {
        ExtendedPlayer playerExt = ExtendedPlayer.get(player);

        NBTTagCompound data = new NBTTagCompound();
        playerExt.saveNBTData(data);

        storeData(player, ExtendedPlayer.TAG, data);
    }

    public void storeData (EntityPlayer player, String name, NBTTagCompound data) {
        storeData(player.getGameProfile(), name, data);
    }

    public void storeData (GameProfile profile, String name, NBTTagCompound data) {
        Map<String, NBTTagCompound> store = dataStore.get(profile);
        if (store == null) {
            store = new HashMap<String, NBTTagCompound>();
            dataStore.put(profile, store);
        }

        store.put(name, data);
    }

    public void restoreData (EntityPlayer player) {
        ExtendedPlayer playerExt = ExtendedPlayer.get(player);

        NBTTagCompound data = getData(player, ExtendedPlayer.TAG);
        if (data != null)
            playerExt.loadNBTData(data);
    }

    public NBTTagCompound getData (EntityPlayer player, String name) {
        return getData(player.getGameProfile(), name);
    }

    public NBTTagCompound getData (GameProfile profile, String name) {
        Map<String, NBTTagCompound> store = dataStore.get(profile);
        if (store == null)
            return null;

        return store.remove(name);
    }

    public void tick (EntityPlayer player, TickEvent.Phase phase, Side side) {
        ExtendedPlayer playerExt = ExtendedPlayer.get(player);
        if (playerExt != null)
            playerExt.tick(phase, side);
    }

    public boolean isOnHungerStrike (EntityPlayer player) {
        ExtendedPlayer playerExt = ExtendedPlayer.get(player);
        if (playerExt != null)
            return playerExt.isOnHungerStrike();

        return false;
    }
}
