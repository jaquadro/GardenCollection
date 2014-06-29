package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.client.renderer.DecorativePotRenderer;
import com.jaquadro.minecraft.gardencore.client.renderer.GardenProxyRenderer;
import com.jaquadro.minecraft.gardencore.client.renderer.WindowBoxRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int renderPass = 0;

    public static int gardenProxyRenderID;
    public static int windowBoxRenderID;
    public static int decorativePotRenderID;

    @Override
    public void registerRenderers ()
    {
        gardenProxyRenderID = RenderingRegistry.getNextAvailableRenderId();
        windowBoxRenderID = RenderingRegistry.getNextAvailableRenderId();
        decorativePotRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(gardenProxyRenderID, new GardenProxyRenderer());
        RenderingRegistry.registerBlockHandler(windowBoxRenderID, new WindowBoxRenderer());
        RenderingRegistry.registerBlockHandler(decorativePotRenderID, new DecorativePotRenderer());
    }
}
