package com.jaquadro.minecraft.modularpots.client;

import com.jaquadro.minecraft.modularpots.CommonProxy;
import com.jaquadro.minecraft.modularpots.client.renderer.*;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int renderPass = 0;

    public static int largePotRenderID;
    public static int transformPlantRenderID;
    public static int thinLogRenderID;
    public static int flowerLeafRenderID;
    public static int thinLogFenceRenderID;
    public static int gardenProxyRenderID;

    @Override
    public void registerRenderers ()
    {
        largePotRenderID = RenderingRegistry.getNextAvailableRenderId();
        transformPlantRenderID = RenderingRegistry.getNextAvailableRenderId();
        thinLogRenderID = RenderingRegistry.getNextAvailableRenderId();
        flowerLeafRenderID = RenderingRegistry.getNextAvailableRenderId();
        thinLogFenceRenderID = RenderingRegistry.getNextAvailableRenderId();
        gardenProxyRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(largePotRenderID, new LargePotRenderer());
        RenderingRegistry.registerBlockHandler(transformPlantRenderID, new TransformPlantRenderer());
        RenderingRegistry.registerBlockHandler(thinLogRenderID, new ThinLogRenderer());
        RenderingRegistry.registerBlockHandler(flowerLeafRenderID, new FlowerLeafRenderer());
        RenderingRegistry.registerBlockHandler(thinLogFenceRenderID, new ThinLogFenceRenderer());
        RenderingRegistry.registerBlockHandler(gardenProxyRenderID, new GardenProxyRenderer());
    }
}
