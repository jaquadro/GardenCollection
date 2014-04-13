package com.jaquadro.minecraft.hungerstrike.proxy;

import com.jaquadro.minecraft.hungerstrike.ExtendedPlayer;
import com.jaquadro.minecraft.hungerstrike.HungerStrike;
import com.jaquadro.minecraft.hungerstrike.PlayerHandler;
import com.jaquadro.minecraft.hungerstrike.network.SyncConfigPacket;
import com.jaquadro.minecraft.hungerstrike.network.SyncExtendedPlayerPacket;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class CommonProxy
{
    public PlayerHandler playerHandler;

    public CommonProxy () {
        playerHandler = new PlayerHandler();
    }

    @SubscribeEvent
    public void tick (TickEvent.PlayerTickEvent event) {
        playerHandler.tick(event.player, event.phase, event.side);
    }

    @SubscribeEvent
    public void entityConstructing (EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer) event.entity) == null)
            ExtendedPlayer.register((EntityPlayer) event.entity);
    }

    @SubscribeEvent
    public void livingDeath (LivingDeathEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayerMP)
            playerHandler.storeData((EntityPlayer) event.entity);
    }

    @SubscribeEvent
    public void entityJoinWorld (EntityJoinWorldEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayerMP) {
            playerHandler.restoreData((EntityPlayer) event.entity);
            HungerStrike.packetPipeline.sendTo(new SyncExtendedPlayerPacket((EntityPlayer) event.entity), (EntityPlayerMP) event.entity);
            HungerStrike.packetPipeline.sendTo(new SyncConfigPacket(), (EntityPlayerMP) event.entity);
        }
    }
}
