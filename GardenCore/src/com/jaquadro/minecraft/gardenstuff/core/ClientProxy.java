package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardenstuff.renderer.LightChainRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int lightChainRenderID;

    @Override
    public void registerRenderers () {
        lightChainRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(lightChainRenderID, new LightChainRenderer());
    }
}
