package com.jaquadro.minecraft.extrabuttons.client;

import com.jaquadro.minecraft.extrabuttons.CommonProxy;
import com.jaquadro.minecraft.extrabuttons.client.renderer.DelayButtonRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int delayButtonRenderID;

    @Override
    public void registerRenderers ()
    {
        delayButtonRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(delayButtonRenderID, new DelayButtonRenderer());
    }
}
