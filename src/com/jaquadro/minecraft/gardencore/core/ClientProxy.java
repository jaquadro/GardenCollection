package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.client.renderer.*;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int renderPass = 0;

    public static int gardenProxyRenderID;
    public static int smallFireRenderID;
    public static int compostBinRenderID;

    public static ISimpleBlockRenderingHandler gardenProxyRenderer;
    public static ISimpleBlockRenderingHandler smallFireRenderer;
    public static ISimpleBlockRenderingHandler compostBinRenderer;

    @Override
    public void registerRenderers ()
    {
        gardenProxyRenderID = RenderingRegistry.getNextAvailableRenderId();
        smallFireRenderID = RenderingRegistry.getNextAvailableRenderId();
        compostBinRenderID = RenderingRegistry.getNextAvailableRenderId();

        gardenProxyRenderer = new GardenProxyRenderer();
        smallFireRenderer = new SmallFireRenderer();
        compostBinRenderer = new CompostBinRenderer();

        RenderingRegistry.registerBlockHandler(gardenProxyRenderID, gardenProxyRenderer);
        RenderingRegistry.registerBlockHandler(smallFireRenderID, smallFireRenderer);
        RenderingRegistry.registerBlockHandler(compostBinRenderID, compostBinRenderer);
    }
}
