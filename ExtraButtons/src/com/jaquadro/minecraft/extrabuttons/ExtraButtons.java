package com.jaquadro.minecraft.extrabuttons;

import com.jaquadro.minecraft.extrabuttons.block.*;
import com.jaquadro.minecraft.extrabuttons.item.ItemToggleButton;
import com.jaquadro.minecraft.extrabuttons.tileentity.TileEntityButton;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

@Mod(modid = ExtraButtons.MOD_ID, name = ExtraButtons.MOD_NAME, version = ExtraButtons.MOD_VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ExtraButtons
{
    static final String MOD_ID = "ExtraButtons";
    static final String MOD_NAME = "ExtraButtons";
    static final String MOD_VERSION = "1.4.7.0";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.extrabuttons.";
    static final String RESOURCE_PATH = "/jaquadro/extrabuttons/";

    private static int capacitiveBlockId;
    private static int playerDetectorRailId;
    private static int playerPoweredRailId;
    private static int stonePanelButtonId;
    private static int woodPanelButtonId;
    private static int illuminatedButtonId;

    public static Block capacitiveTouchBlock;
    public static Block stonePanelButton;
    public static Block woodPanelButton;
    public static Block playerDetectorRail;
    public static Block playerPoweredRail;
    public static Block illuminatedButton;

    @Mod.Instance(MOD_ID)
    public static ExtraButtons instance;

    @SidedProxy(clientSide = SOURCE_PATH + "client.ClientProxy", serverSide = SOURCE_PATH + "CommonProxy")
    public static CommonProxy proxy;

    @Mod.PreInit
    public void preInit (FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        config.getCategory(Configuration.CATEGORY_BLOCK).setComment("Set block IDs to -1 to disable feature");

        capacitiveBlockId = config.getBlock("CapacitiveTouchBlock", 560).getInt();
        playerDetectorRailId = config.getBlock("PlayerDetectorRail", 561).getInt();
        playerPoweredRailId = config.getBlock("PlayerPoweredRail", 562).getInt();
        stonePanelButtonId = config.getBlock("StonePanelButton", 563).getInt();
        woodPanelButtonId = config.getBlock("WoodPanelButton", 564).getInt();
        illuminatedButtonId = config.getBlock("IlluminatedButton", 565).getInt();

        config.save();

        initializeBlocks();
    }

    private static final String[] colors = {
            "White", "Orange", "Magenta", "Light Blue",
            "Yellow", "Light Green", "Pink", "Dark Grey",
            "Light Grey", "Cyan", "Purple", "Blue",
            "Brown", "Green", "Red", "Black",
    };

    @Mod.Init
    public void load (FMLInitializationEvent event)
    {
        LanguageRegistry.addName(capacitiveTouchBlock, "Capacitive Touch Block");
        GameRegistry.registerBlock(capacitiveTouchBlock, "capacitiveTouchBlock");

        LanguageRegistry.addName(stonePanelButton, "Stone Panel Button");
        GameRegistry.registerBlock(stonePanelButton, "stonePanelButton");

        LanguageRegistry.addName(woodPanelButton, "Wood Panel Button");
        GameRegistry.registerBlock(woodPanelButton, "woodPanelButton");

        LanguageRegistry.addName(playerDetectorRail, "Player Detector Rail");
        GameRegistry.registerBlock(playerDetectorRail, "playerDetectorRail");

        LanguageRegistry.addName(playerPoweredRail, "Player Powered Rail");
        GameRegistry.registerBlock(playerPoweredRail, "playerPoweredRail");

        GameRegistry.registerBlock(illuminatedButton, ItemToggleButton.class, "illuminatedButton");

        for (int i = 0; i < 16; i++) {
            ItemStack illumStack = new ItemStack(illuminatedButtonId, 1, i);

            LanguageRegistry.addName(illumStack, colors[i] + " Illuminated Toggle Button");
        }

        GameRegistry.registerTileEntity(TileEntityButton.class, "toggleButton");

        ItemStack ironStack = new ItemStack(Item.ingotIron);
        ItemStack torchStack = new ItemStack(Block.torchRedstoneActive);
        ItemStack stoneStack = new ItemStack(Block.stone);

        GameRegistry.addRecipe(new ItemStack(capacitiveTouchBlock), "xyx", "zzz",
                'x', torchStack, 'y', ironStack, 'z', stoneStack);

        ItemStack goldStack = new ItemStack(Item.ingotGold);
        ItemStack stonePlateStack = new ItemStack(Block.pressurePlateStone);
        ItemStack redstoneStack = new ItemStack(Item.redstone);

        GameRegistry.addRecipe(new ItemStack(playerDetectorRail), "x x", "xyx", "xzx",
                'x', goldStack, 'y', stonePlateStack, 'z', redstoneStack);

        ItemStack stickStack = new ItemStack(Item.stick);

        GameRegistry.addRecipe(new ItemStack(playerPoweredRail), "xwx", "xyx", "xzx",
                'x', goldStack, 'y', stickStack, 'z', redstoneStack, 'w', stonePlateStack);

        ItemStack stoneButtonStack = new ItemStack(Block.stoneButton);
        ItemStack glowStoneStack = new ItemStack(Item.lightStoneDust);

        for (int i = 0; i < 16; i++) {
            ItemStack dyeStack = new ItemStack(Item.dyePowder, 1, 15 - i);

            GameRegistry.addRecipe(new ItemStack(illuminatedButtonId, 1, i), " x ", " y ", " z ",
                    'x', dyeStack, 'y', glowStoneStack, 'z', stoneButtonStack);
        }

        ItemStack woodButtonStack = new ItemStack(Block.woodenButton);

        GameRegistry.addRecipe(new ItemStack(stonePanelButton), "xx", 'x', stoneButtonStack);
        GameRegistry.addRecipe(new ItemStack(woodPanelButton), "xx", 'x', woodButtonStack);

        proxy.registerRenderers();
    }

    @Mod.PostInit
    public void postInit (FMLPostInitializationEvent event) {

    }

    private void initializeBlocks ()
    {
        if (capacitiveBlockId > -1)
            capacitiveTouchBlock = new CapacitiveTouchBlock(capacitiveBlockId, 1)
                    .setHardness(0.5f)
                    .setStepSound(Block.soundMetalFootstep)
                    .setRequiresSelfNotify()
                    .setBlockName("capacitiveTouchBlock");

        if (stonePanelButtonId > -1)
            stonePanelButton = new PanelButton(stonePanelButtonId, Block.stone.blockIndexInTexture, false)
                    .setHardness(0.5F)
                    .setStepSound(Block.soundStoneFootstep)
                    .setRequiresSelfNotify()
                    .setBlockName("stonePanelButton");

        if (woodPanelButtonId > -1)
            woodPanelButton = new PanelButton(woodPanelButtonId, Block.planks.blockIndexInTexture, true)
                    .setHardness(0.5F)
                    .setStepSound(Block.soundWoodFootstep)
                    .setRequiresSelfNotify()
                    .setBlockName("woodPanelButton");

        if (playerDetectorRailId > -1)
            playerDetectorRail = new PlayerDetectorRail(playerDetectorRailId, 3)
                    .setHardness(0.7f)
                    .setStepSound(Block.soundMetalFootstep)
                    .setRequiresSelfNotify()
                    .setBlockName("playerDetectorRail");

        if (playerPoweredRailId > -1)
            playerPoweredRail = new PlayerPoweredRail(playerPoweredRailId, 5)
                    .setHardness(0.7f)
                    .setStepSound(Block.soundMetalFootstep)
                    .setRequiresSelfNotify()
                    .setBlockName("playerPoweredRail");

        if (illuminatedButtonId > -1)
            illuminatedButton = new ToggleButton(illuminatedButtonId, 16)
                    .setHardness(0.5f)
                    .setStepSound(Block.soundStoneFootstep)
                    .setRequiresSelfNotify()
                    .setBlockName("illuminatedButton");
    }
}
