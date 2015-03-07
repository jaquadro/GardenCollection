package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardenstuff.renderer.HeavyChainRenderer;
import com.jaquadro.minecraft.gardenstuff.renderer.LanternRenderer;
import com.jaquadro.minecraft.gardenstuff.renderer.LatticeRenderer;
import com.jaquadro.minecraft.gardenstuff.renderer.LightChainRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    public static int heavyChainRenderID;
    public static int lightChainRenderID;
    public static int latticeRenderID;
    public static int lanternRenderID;

    public static LanternRenderer lanternRenderer;

    @Override
    public void registerRenderers () {
        heavyChainRenderID = RenderingRegistry.getNextAvailableRenderId();
        lightChainRenderID = RenderingRegistry.getNextAvailableRenderId();
        latticeRenderID = RenderingRegistry.getNextAvailableRenderId();
        lanternRenderID = RenderingRegistry.getNextAvailableRenderId();

        lanternRenderer = new LanternRenderer();

        RenderingRegistry.registerBlockHandler(heavyChainRenderID, new HeavyChainRenderer());
        RenderingRegistry.registerBlockHandler(lightChainRenderID, new LightChainRenderer());
        RenderingRegistry.registerBlockHandler(latticeRenderID, new LatticeRenderer());
        RenderingRegistry.registerBlockHandler(lanternRenderID, lanternRenderer);
    }
}
