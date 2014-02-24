package com.jaquadro.minecraft.extrabuttons;

import com.jaquadro.minecraft.extrabuttons.block.*;
import com.jaquadro.minecraft.extrabuttons.item.ItemDelayButton;
import com.jaquadro.minecraft.extrabuttons.item.ItemToggleButton;
import com.jaquadro.minecraft.extrabuttons.tileentity.TileEntityButton;
import com.jaquadro.minecraft.extrabuttons.tileentity.TileEntityDelayButton;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

// To-do?
// Signal relay: take's input signal, re-emits in all directions.  Possibly directed versions.
// Variable-length buttons: Versions of standard button with longer duration.  Possibly apply to panels/capt touch

@Mod(modid = ExtraButtons.MOD_ID, name = ExtraButtons.MOD_NAME, version = ExtraButtons.MOD_VERSION)
public class ExtraButtons
{
    public static final String MOD_ID = "extrabuttons";
    static final String MOD_NAME = "ExtraButtons";
    static final String MOD_VERSION = "1.7.2.0";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.extrabuttons.";

    private static int capacitiveBlockId;
    private static int playerDetectorRailId;
    private static int playerPoweredRailId;
    private static int stonePanelButtonId;
    private static int woodPanelButtonId;
    private static int illuminatedButtonId;
    private static int delayButtonId;

    public static Block capacitiveTouchBlock;
    public static Block stonePanelButton;
    public static Block woodPanelButton;
    public static Block playerDetectorRail;
    public static Block playerPoweredRail;
    public static Block illuminatedButtonOn;
    public static Block illuminatedButtonOff;
    public static Block delayButton;

    @Mod.Instance(MOD_ID)
    public static ExtraButtons instance;

    @SidedProxy(clientSide = SOURCE_PATH + "client.ClientProxy", serverSide = SOURCE_PATH + "CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event)
    {
        /*Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        config.getCategory(Configuration.CATEGORY_BLOCK).setComment("Set block IDs to -1 to disable feature");

        capacitiveBlockId = config.getBlock("CapacitiveTouchBlock", 560).getInt();
        playerDetectorRailId = config.getBlock("PlayerDetectorRail", 561).getInt();
        playerPoweredRailId = config.getBlock("PlayerPoweredRail", 562).getInt();
        stonePanelButtonId = config.getBlock("StonePanelButton", 563).getInt();
        woodPanelButtonId = config.getBlock("WoodPanelButton", 564).getInt();
        illuminatedButtonId = config.getBlock("IlluminatedButton", 565).getInt();
        delayButtonId = config.getBlock("DelayButton", 566).getInt();

        config.save();*/

        initializeBlocks();

        GameRegistry.registerBlock(capacitiveTouchBlock, MOD_ID + "capacitive_touch_block");
        GameRegistry.registerBlock(stonePanelButton, MOD_ID + "stone_panel_button");
        GameRegistry.registerBlock(woodPanelButton, MOD_ID + "wood_panel_button");
        GameRegistry.registerBlock(playerDetectorRail, MOD_ID + "player_detector_rail");
        GameRegistry.registerBlock(playerPoweredRail, MOD_ID + "player_powered_rail");
        GameRegistry.registerBlock(delayButton, ItemDelayButton.class, MOD_ID + "delay_button");
        GameRegistry.registerBlock(illuminatedButtonOn, ItemToggleButton.class, MOD_ID + "illuminated_button_on");
        GameRegistry.registerBlock(illuminatedButtonOff, ItemToggleButton.class, MOD_ID + "illuminated_button");

        GameRegistry.registerTileEntity(TileEntityButton.class, MOD_ID + "toggle_button");
        GameRegistry.registerTileEntity(TileEntityDelayButton.class, MOD_ID + "delay_button");

        ItemStack ironStack = new ItemStack(Items.iron_ingot);
        ItemStack torchStack = new ItemStack(Blocks.redstone_torch);
        ItemStack stoneStack = new ItemStack(Blocks.stone);

        GameRegistry.addRecipe(new ItemStack(capacitiveTouchBlock), "xyx", "zzz",
                'x', torchStack, 'y', ironStack, 'z', stoneStack);

        ItemStack goldStack = new ItemStack(Items.gold_ingot);
        ItemStack stonePlateStack = new ItemStack(Blocks.stone_pressure_plate);
        ItemStack redstoneStack = new ItemStack(Items.redstone);

        GameRegistry.addRecipe(new ItemStack(playerDetectorRail), "x x", "xyx", "xzx",
                'x', goldStack, 'y', stonePlateStack, 'z', redstoneStack);

        ItemStack stickStack = new ItemStack(Items.stick);

        GameRegistry.addRecipe(new ItemStack(playerPoweredRail), "xwx", "xyx", "xzx",
                'x', goldStack, 'y', stickStack, 'z', redstoneStack, 'w', stonePlateStack);

        ItemStack stoneButtonStack = new ItemStack(Blocks.stone_button);

        for (int i = 0; i < 16; i++) {
            ItemStack stainedGlassStack = new ItemStack(Blocks.stained_glass_pane, 1, i);

            GameRegistry.addRecipe(new ItemStack(illuminatedButtonOff, 1, i), " x ", " y ", " z ",
                    'x', stainedGlassStack, 'y', redstoneStack, 'z', stoneButtonStack);
        }

        ItemStack woodButtonStack = new ItemStack(Blocks.wooden_button);

        GameRegistry.addRecipe(new ItemStack(stonePanelButton), "xx", 'x', stoneButtonStack);
        GameRegistry.addRecipe(new ItemStack(woodPanelButton), "xx", 'x', woodButtonStack);

        ItemStack repeaterStack = new ItemStack(Items.repeater);

        GameRegistry.addRecipe(new ItemStack(delayButton), "x", "y", 'x', stoneButtonStack, 'y', repeaterStack);
    }

    @Mod.EventHandler
    public void load (FMLInitializationEvent event)
    {
        proxy.registerRenderers();
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event)
    { }

    private void initializeBlocks ()
    {
        if (capacitiveBlockId > -1)
            capacitiveTouchBlock = new CapacitiveTouchBlock()
                    .setHardness(0.5f)
                    .setStepSound(Block.soundTypeMetal)
                    .setBlockName("capacitiveTouchBlock");

        if (stonePanelButtonId > -1)
            stonePanelButton = new StonePanelButton()
                    .setHardness(0.5F)
                    .setStepSound(Block.soundTypeStone)
                    .setBlockName("stonePanelButton");

        if (woodPanelButtonId > -1)
            woodPanelButton = new WoodPanelButton()
                    .setHardness(0.5F)
                    .setStepSound(Block.soundTypeWood)
                    .setBlockName("woodPanelButton");

        if (playerDetectorRailId > -1)
            playerDetectorRail = new PlayerDetectorRail()
                    .setHardness(0.7f)
                    .setStepSound(Block.soundTypeMetal)
                    .setBlockName("playerDetectorRail");

        if (playerPoweredRailId > -1)
            playerPoweredRail = new PlayerPoweredRail()
                    .setHardness(0.7f)
                    .setStepSound(Block.soundTypeMetal)
                    .setBlockName("playerPoweredRail");

        if (illuminatedButtonId > -1) {
            illuminatedButtonOff = new ToggleButton(false)
                    .setHardness(0.5f)
                    .setStepSound(Block.soundTypeStone)
                    .setBlockName("illuminatedButton");
            illuminatedButtonOn = new ToggleButton(true)
                    .setHardness(0.5f)
                    .setLightLevel(0.4375f)
                    .setStepSound(Block.soundTypeStone)
                    .setBlockName("illuminatedButtonOn");
        }

        if (delayButtonId > -1)
            delayButton = new DelayButton()
                    .setHardness(0.7f)
                    .setStepSound(Block.soundTypeStone)
                    .setBlockName("delayButton");
    }
}
