package com.jaquadro.minecraft.hungerstrike;

import com.jaquadro.minecraft.hungerstrike.command.CommandHungerStrike;
import com.jaquadro.minecraft.hungerstrike.network.PacketPipeline;
import com.jaquadro.minecraft.hungerstrike.network.SyncConfigPacket;
import com.jaquadro.minecraft.hungerstrike.network.SyncExtendedPlayerPacket;
import com.jaquadro.minecraft.hungerstrike.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

@Mod(modid = HungerStrike.MOD_ID, name = HungerStrike.MOD_NAME, version = HungerStrike.MOD_VERSION)
public class HungerStrike
{
    public static final String MOD_ID = "hungerstrike";
    static final String MOD_NAME = "Hunger Strike";
    static final String MOD_VERSION = "1.7.2.3";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.hungerstrike.";

    @Mod.Instance(MOD_ID)
    public static HungerStrike instance;

    @SidedProxy(clientSide = SOURCE_PATH + "proxy.ClientProxy", serverSide = SOURCE_PATH + "proxy.CommonProxy")
    public static CommonProxy proxy;

    public static final PacketPipeline packetPipeline = new PacketPipeline();

    public static ConfigManager config = new ConfigManager();

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(proxy);

        config.setup(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void load (FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);

        packetPipeline.initialise();
        packetPipeline.registerPacket(SyncExtendedPlayerPacket.class);
        packetPipeline.registerPacket(SyncConfigPacket.class);
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        packetPipeline.postInitialise();

        if (config.getFoodStackSize() > -1) {
            for (Object obj : GameData.itemRegistry) {
                Item item = (Item) obj;
                if (item instanceof ItemFood)
                    item.setMaxStackSize(config.getFoodStackSize());
            }
        }
    }

    @Mod.EventHandler
    public void serverStarted (FMLServerStartedEvent event) {
        CommandHandler handler = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        handler.registerCommand(new CommandHungerStrike());
    }


}
