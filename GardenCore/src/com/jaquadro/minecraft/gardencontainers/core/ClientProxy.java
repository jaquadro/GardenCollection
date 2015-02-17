package com.jaquadro.minecraft.gardencontainers.core;

import com.jaquadro.minecraft.gardencontainers.client.renderer.*;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int renderPass = 0;

    public static int windowBoxRenderID;
    public static int decorativePotRenderID;
    public static int largePotRenderID;
    public static int mediumPotRenderID;

    @Override
    public void registerRenderers ()
    {
        windowBoxRenderID = RenderingRegistry.getNextAvailableRenderId();
        decorativePotRenderID = RenderingRegistry.getNextAvailableRenderId();
        largePotRenderID = RenderingRegistry.getNextAvailableRenderId();
        mediumPotRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(windowBoxRenderID, new WindowBoxRenderer());
        RenderingRegistry.registerBlockHandler(decorativePotRenderID, new DecorativePotRenderer());
        RenderingRegistry.registerBlockHandler(largePotRenderID, new LargePotRenderer());
        RenderingRegistry.registerBlockHandler(mediumPotRenderID, new MediumPotRenderer());
    }
}
