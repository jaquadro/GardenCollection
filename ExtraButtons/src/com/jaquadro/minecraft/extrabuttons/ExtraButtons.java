package com.jaquadro.minecraft.extrabuttons;

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

@Mod(modid = "ExtraButtons", name = "ExtraButtons", version = "0.1.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class ExtraButtons
{
    private static int capacitiveBlockId;
    private static int playerDetectorRailId;
    private static int playerPoweredRailId;
    private static int panelButtonId;
    private static int illuminatedButtonId;

    public static Block capacitiveTouchBlock;
    public static Block panelButton;
    public static Block playerDetectorRail;
    public static Block playerPoweredRail;
    public static Block illuminatedButton;

    @Mod.Instance("ExtraButtons")
    public static ExtraButtons instance;

    @SidedProxy(clientSide = "com.jaquadro.minecraft.extrabuttons.client.ClientProxy",
            serverSide = "com.jaquadro.minecraft.extrabuttons.CommonProxy")
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
        panelButtonId = config.getBlock("PanelButton", 563).getInt();
        illuminatedButtonId = config.getBlock("IlluminatedButton", 564).getInt();

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

        LanguageRegistry.addName(panelButton, "Panel Button");
        GameRegistry.registerBlock(panelButton, "panelButton");

        LanguageRegistry.addName(playerDetectorRail, "Player Detector Rail");
        GameRegistry.registerBlock(playerDetectorRail, "playerDetectorRail");

        LanguageRegistry.addName(playerPoweredRail, "Player Powered Rail");
        GameRegistry.registerBlock(playerPoweredRail, "playerPoweredRail");

        GameRegistry.registerBlock(illuminatedButton, IlluminatedButtonItemBlock.class, "illuminatedButton");

        for (int i = 0; i < 16; i++) {
            ItemStack illumStack = new ItemStack(illuminatedButtonId, 1, i);

            LanguageRegistry.addName(illumStack, colors[i] + " Illuminated Button");
        }

        GameRegistry.registerTileEntity(TileEntityButton.class, "illumButton");

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

        if (panelButtonId > -1)
            panelButton = new PanelButton(panelButtonId, Block.stone.blockIndexInTexture)
                    .setHardness(0.5F)
                    .setStepSound(Block.soundStoneFootstep)
                    .setRequiresSelfNotify()
                    .setBlockName("panelButton");

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
            illuminatedButton = new IlluminatedButton(illuminatedButtonId, 16)
                    .setHardness(0.5f)
                    .setStepSound(Block.soundStoneFootstep)
                    .setRequiresSelfNotify()
                    .setBlockName("illuminatedButton");
    }
}
