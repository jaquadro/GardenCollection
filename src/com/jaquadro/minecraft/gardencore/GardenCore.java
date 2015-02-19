package com.jaquadro.minecraft.gardencore;

import com.jaquadro.minecraft.gardencore.api.GardenCoreAPI;
import com.jaquadro.minecraft.gardencore.core.*;
import com.jaquadro.minecraft.gardencore.core.handlers.ForgeEventHandler;
import com.jaquadro.minecraft.gardencore.core.handlers.GuiHandler;
import com.jaquadro.minecraft.gardencore.core.handlers.VanillaBonemealHandler;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraftforge.common.MinecraftForge;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

@Mod(modid = GardenCore.MOD_ID, name = GardenCore.MOD_NAME, version = GardenCore.MOD_VERSION)
public class GardenCore
{
    public static final String MOD_ID = "GardenCore";
    public static final String MOD_NAME = "Garden Core";
    public static final String MOD_VERSION = "@VERSION@";
    static final String SOURCE_PATH = "com.jaquadro.minecraft.gardencore.";

    public static final ModIntegration integration = new ModIntegration();
    public static final ModBlocks blocks = new ModBlocks();
    public static final ModItems items = new ModItems();
    public static final ModRecipes recipes = new ModRecipes();

    @Mod.Instance(MOD_ID)
    public static GardenCore instance;

    @SidedProxy(clientSide = SOURCE_PATH + "core.ClientProxy", serverSide = SOURCE_PATH + "core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        blocks.init();
        items.init();
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        proxy.registerRenderers();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandler());

        integration.init();
        GardenCoreAPI.instance().registerBonemealHandler(new VanillaBonemealHandler());
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent event) {
        integration.postInit();
        recipes.init();

        /*try {
            List<ModContainer> loadedMods = Loader.instance().getActiveModList();
            for (ModContainer mod : loadedMods) {
                String baseAssetPath = "assets/" + mod.getModId() + "/textures/blocks/";
                Pattern assetPattern = Pattern.compile("assets/.+/textures/blocks/.+\\.png");
                JarFile modJar;

                try {
                    modJar = new JarFile(mod.getSource());
                }
                catch (IOException e) {
                     continue;
                }

                Enumeration<JarEntry> modJarEntries = modJar.entries();
                while (modJarEntries.hasMoreElements()) {
                    JarEntry element = modJarEntries.nextElement();
                    if (element.isDirectory() || !assetPattern.matcher(element.getName()).matches())
                        continue;

                    BufferedImage image = ImageIO.read(modJar.getInputStream(element));
                    if (image.getWidth() != image.getHeight())
                        continue;

                    int xStart = 0;
                    int xStop = image.getWidth();
                    int yStart = 0;
                    int yStop = image.getHeight();

                    SearchYStart:
                    for (; yStart < yStop; yStart++, yStart++) {
                        for (int x = xStart; x < xStop; x++) {
                            if (((image.getRGB(x, yStart) >> 24) & 0xFF) != 0)
                                break SearchYStart;
                        }
                    }

                    SearchYStop:
                    for (; yStop > yStart; yStop--) {
                        for (int x = xStart; x < xStop; x++) {
                            if (((image.getRGB(x, yStop - 1) >> 24) & 0xFF) != 0)
                                break SearchYStop;
                        }
                    }

                    SearchXStart:
                    for (; xStart < xStop; xStart++) {
                        for (int y = yStart; y < yStop; y++) {
                            if (((image.getRGB(xStart, y) >> 24) & 0xFF) != 0)
                                break SearchXStart;
                        }
                    }

                    SearchXStop:
                    for (; xStop > xStart; xStop--) {
                        for (int y = yStart; y < yStop; y++) {
                            if (((image.getRGB(xStop - 1, y) >> 24) & 0xFF) != 0)
                                break SearchXStop;
                        }
                    }

                    FMLLog.info("%s area: %d,%d; %dx%d", element.getName(), xStart, yStart, xStop - xStart, yStop - yStart);
                }

            }
        }
        catch (IOException e) {
            FMLLog.severe("Error processing jars: %s", e.getMessage());
        }*/
    }
}
