package com.jaquadro.minecraft.modularpots.client;

import com.jaquadro.minecraft.modularpots.CommonProxy;
import com.jaquadro.minecraft.modularpots.client.renderer.LargePotRenderer;
import com.jaquadro.minecraft.modularpots.client.renderer.TransformPlantRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int largePotRenderID;
    public static int transformPlantRendererID;

    @Override
    public void registerRenderers ()
    {
        largePotRenderID = RenderingRegistry.getNextAvailableRenderId();
        transformPlantRendererID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(largePotRenderID, new LargePotRenderer());
        RenderingRegistry.registerBlockHandler(transformPlantRendererID, new TransformPlantRenderer());
    }
}
