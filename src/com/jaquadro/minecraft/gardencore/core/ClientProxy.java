package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.client.renderer.*;
import com.jaquadro.minecraft.gardencore.util.BindingStack;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

import java.util.HashMap;
import java.util.Map;

public class ClientProxy extends CommonProxy
{
    public static int renderPass = 0;

    public static int gardenProxyRenderID;
    public static int smallFireRenderID;
    public static int compostBinRenderID;

    public static ISimpleBlockRenderingHandler gardenProxyRenderer;
    public static ISimpleBlockRenderingHandler smallFireRenderer;
    public static ISimpleBlockRenderingHandler compostBinRenderer;

    private Map<Object, BindingStack> bindingStacks = new HashMap<Object, BindingStack>();

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

    @Override
    protected Map<Object, BindingStack> getBindingRegistry () {
        return bindingStacks;
    }
}
