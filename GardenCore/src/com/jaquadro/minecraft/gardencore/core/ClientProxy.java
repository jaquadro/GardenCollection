package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.client.renderer.*;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int renderPass = 0;

    public static int gardenProxyRenderID;
    public static int windowBoxRenderID;
    public static int decorativePotRenderID;
    public static int smallFireRenderID;
    public static int compostBinRenderID;

    @Override
    public void registerRenderers ()
    {
        gardenProxyRenderID = RenderingRegistry.getNextAvailableRenderId();
        windowBoxRenderID = RenderingRegistry.getNextAvailableRenderId();
        decorativePotRenderID = RenderingRegistry.getNextAvailableRenderId();
        smallFireRenderID = RenderingRegistry.getNextAvailableRenderId();
        compostBinRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(gardenProxyRenderID, new GardenProxyRenderer());
        RenderingRegistry.registerBlockHandler(windowBoxRenderID, new WindowBoxRenderer());
        RenderingRegistry.registerBlockHandler(decorativePotRenderID, new DecorativePotRenderer());
        RenderingRegistry.registerBlockHandler(smallFireRenderID, new SmallFireRenderer());
        RenderingRegistry.registerBlockHandler(compostBinRenderID, new CompostBinRenderer());
    }
}
