package com.jaquadro.minecraft.modularpots;

import com.jaquadro.minecraft.modularpots.addon.PlantHandlerRegistry;
import com.jaquadro.minecraft.modularpots.block.*;
import com.jaquadro.minecraft.modularpots.block.support.WoodRegistry;
import com.jaquadro.minecraft.modularpots.client.renderer.PlacementGrid;
import com.jaquadro.minecraft.modularpots.config.ConfigManager;
import com.jaquadro.minecraft.modularpots.config.PatternConfig;
import com.jaquadro.minecraft.modularpots.core.ModBlocks;
import com.jaquadro.minecraft.modularpots.core.ModIntegration;
import com.jaquadro.minecraft.modularpots.core.ModItems;
import com.jaquadro.minecraft.modularpots.core.ModRecipes;
import com.jaquadro.minecraft.modularpots.core.handlers.GuiHandler;
import com.jaquadro.minecraft.modularpots.core.handlers.VillagerTradeHandler;
import com.jaquadro.minecraft.modularpots.creativetab.ModularPotsCreativeTab;
import com.jaquadro.minecraft.modularpots.item.ItemThinLog;
import com.jaquadro.minecraft.modularpots.registry.PlantRegistry;
import com.jaquadro.minecraft.modularpots.registry.RegistryGroup;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.player.BonemealEvent;

import java.io.File;

@Mod(modid = ModularPots.MOD_ID, name = ModularPots.MOD_NAME, version = ModularPots.MOD_VERSION)
public class ModularPots
{
    public static final String MOD_ID = "modularpots";
    static final String MOD_NAME = "Modular Flower Pots";
    static final String MOD_VERSION = "1.7.2.13";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.modularpots.";

    public static CreativeTabs tabModularPots = new ModularPotsCreativeTab("modularPots");

    public static final ModBlocks blocks = new ModBlocks();
    public static final ModItems items = new ModItems();
    public static final ModRecipes recipes = new ModRecipes();
    public static final ModIntegration integration = new ModIntegration();

    public static int potteryTableGuiID = 0;
    public static int gardenGuiID = 1;

    public static ConfigManager config;

    @Mod.Instance(MOD_ID)
    public static ModularPots instance;

    @SidedProxy(clientSide = SOURCE_PATH + "client.ClientProxy", serverSide = SOURCE_PATH + "CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        config = new ConfigManager(new File(event.getModConfigurationDirectory(), MOD_ID + ".patterns.cfg"));

        blocks.init();
        items.init();
    }

    @Mod.EventHandler
    public void load (FMLInitializationEvent event) {
        proxy.registerRenderers();
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        for (int i = 1; i < 256; i++) {
            if (!config.hasPattern(i))
                continue;

            PatternConfig pattern = config.getPattern(i);
            for (int j = 0; j < pattern.getLocationCount(); j++)
                ChestGenHooks.addItem(pattern.getGenLocation(j), new WeightedRandomChestContent(items.potteryPattern, i, 1, 1, pattern.getGenRarity(j)));
        }

        VillagerTradeHandler.instance().load();
        integration.init();
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        PlantHandlerRegistry.init();
        integration.postInit();

        recipes.init();
    }

    @Mod.EventHandler
    public void interModComs (FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equals("plantBlacklist")) {
                if (message.isItemStackMessage())
                    PlantRegistry.instance().addToBlacklist(message.getItemStackValue());
                else if (message.isNBTMessage())
                    PlantRegistry.instance().addToBlacklist(message.getNBTValue());
            }
            else if (message.key.equals("bonemealBlacklist")) {
                if (message.isItemStackMessage())
                    PlantRegistry.instance().addToBlacklist(message.getItemStackValue(), RegistryGroup.Bonemeal);
                else if (message.isNBTMessage())
                    PlantRegistry.instance().addToBlacklist(message.getNBTValue(), RegistryGroup.Bonemeal);
            }
        }
    }

    @SubscribeEvent
    public void applyBonemeal (BonemealEvent event) {
        if (event.block == blocks.largePotPlantProxy) {
            BlockLargePotPlantProxy proxyBlock = blocks.largePotPlantProxy;
            if (proxyBlock.applyBonemeal(event.world, event.x, event.y, event.z))
                event.setResult(Result.ALLOW); // Stop further processing and consume bonemeal
        }
    }

    @SubscribeEvent
    public void handleCrafting (PlayerEvent.ItemCraftedEvent event) {
        if (event.crafting.getItem() instanceof ItemThinLog) {
            for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
                ItemStack item = event.craftMatrix.getStackInSlot(i);
                if (item != null && isValidAxe(item) && item.getItemDamage() < item.getMaxDamage()) {
                    event.craftMatrix.setInventorySlotContents(i, new ItemStack(item.getItem(), item.stackSize + 1, item.getItemDamage() + 1));
                }
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void drawBlockHighlight (DrawBlockHighlightEvent event) {
        if (event.target != null && event.target.sideHit == 1 && event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            Block block = event.player.worldObj.getBlock(event.target.blockX, event.target.blockY, event.target.blockZ);
            if (block instanceof BlockLargePot) {
                PlacementGrid grid = new PlacementGrid();
                grid.render(event.player, event.target, event.partialTicks);
                event.setCanceled(true);
            }
        }
    }

    private boolean isValidAxe (ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item == Items.wooden_axe
            || item == Items.stone_axe
            || item == Items.iron_axe
            || item == Items.golden_axe
            || item == Items.diamond_axe;
    }
}
