package com.jaquadro.minecraft.gardentrees.core;

import com.jaquadro.minecraft.gardentrees.client.renderer.FlowerLeafRenderer;
import com.jaquadro.minecraft.gardentrees.client.renderer.IvyRenderer;
import com.jaquadro.minecraft.gardentrees.client.renderer.ThinLogFenceRenderer;
import com.jaquadro.minecraft.gardentrees.client.renderer.ThinLogRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int renderPass = 0;

    public static int thinLogRenderID;
    public static int flowerLeafRenderID;
    public static int thinLogFenceRenderID;
    public static int ivyRenderID;

    @Override
    public void registerRenderers ()
    {
        thinLogRenderID = RenderingRegistry.getNextAvailableRenderId();
        flowerLeafRenderID = RenderingRegistry.getNextAvailableRenderId();
        thinLogFenceRenderID = RenderingRegistry.getNextAvailableRenderId();
        ivyRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(thinLogRenderID, new ThinLogRenderer());
        RenderingRegistry.registerBlockHandler(flowerLeafRenderID, new FlowerLeafRenderer());
        RenderingRegistry.registerBlockHandler(thinLogFenceRenderID, new ThinLogFenceRenderer());
        RenderingRegistry.registerBlockHandler(ivyRenderID, new IvyRenderer());
    }
}
