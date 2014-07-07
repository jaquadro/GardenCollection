package com.jaquadro.minecraft.gardentrees.core;

import com.jaquadro.minecraft.gardentrees.client.renderer.FlowerLeafRenderer;
import com.jaquadro.minecraft.gardentrees.client.renderer.ThinLogFenceRenderer;
import com.jaquadro.minecraft.gardentrees.client.renderer.ThinLogRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int renderPass = 0;

    public static int thinLogRenderID;
    public static int flowerLeafRenderID;
    public static int thinLogFenceRenderID;

    @Override
    public void registerRenderers ()
    {
        thinLogRenderID = RenderingRegistry.getNextAvailableRenderId();
        flowerLeafRenderID = RenderingRegistry.getNextAvailableRenderId();
        thinLogFenceRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(thinLogRenderID, new ThinLogRenderer());
        RenderingRegistry.registerBlockHandler(flowerLeafRenderID, new FlowerLeafRenderer());
        RenderingRegistry.registerBlockHandler(thinLogFenceRenderID, new ThinLogFenceRenderer());
    }
}
