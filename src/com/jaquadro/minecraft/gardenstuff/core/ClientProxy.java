package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardenstuff.renderer.LatticeRenderer;
import com.jaquadro.minecraft.gardenstuff.renderer.LightChainRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int lightChainRenderID;
    public static int latticeRenderID;

    @Override
    public void registerRenderers () {
        lightChainRenderID = RenderingRegistry.getNextAvailableRenderId();
        latticeRenderID = RenderingRegistry.getNextAvailableRenderId();

        RenderingRegistry.registerBlockHandler(lightChainRenderID, new LightChainRenderer());
        RenderingRegistry.registerBlockHandler(latticeRenderID, new LatticeRenderer());
    }
}
