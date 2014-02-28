package com.jaquadro.minecraft.hungerstrike;

import com.jaquadro.minecraft.hungerstrike.command.CommandHungerStrike;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.command.CommandHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

@Mod(modid = HungerStrike.MOD_ID, name = HungerStrike.MOD_NAME, version = HungerStrike.MOD_VERSION)
public class HungerStrike
{
    public static final String MOD_ID = "hungerstrike";
    static final String MOD_NAME = "Hunger Strike";
    static final String MOD_VERSION = "1.7.2.0";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.hungerstrike.";

    @Mod.Instance(MOD_ID)
    public static HungerStrike instance;

    @SidedProxy(clientSide = SOURCE_PATH + "client.ClientProxy", serverSide = SOURCE_PATH + "CommonProxy")
    public static CommonProxy proxy;

    public ConfigManager config = new ConfigManager();

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);

        config.setup(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void load (FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void serverStarted (FMLServerStartedEvent event) {
        CommandHandler handler = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        handler.registerCommand(new CommandHungerStrike());
    }

    @SubscribeEvent
    public void tick (TickEvent.PlayerTickEvent event) {
        proxy.playerHandler.tick(event.player, event.phase);
    }

    @SubscribeEvent
    public void entityConstructing (EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayerMP && ExtendedPlayer.get((EntityPlayer) event.entity) == null)
            ExtendedPlayer.register((EntityPlayer) event.entity);
    }

    @SubscribeEvent
    public void livingDeath (LivingDeathEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayerMP)
            proxy.playerHandler.storeData((EntityPlayer) event.entity);
    }

    @SubscribeEvent
    public void entityJoinWorld (EntityJoinWorldEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayerMP)
            proxy.playerHandler.restoreData((EntityPlayer) event.entity);
    }
}
