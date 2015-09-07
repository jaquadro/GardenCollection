package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardencore.util.BindingStack;
import com.jaquadro.minecraft.gardenstuff.renderer.*;
import com.jaquadro.minecraft.gardenstuff.renderer.item.LanternItemRenderer;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.HashMap;
import java.util.Map;

public class ClientProxy extends CommonProxy
{
    public static int heavyChainRenderID;
    public static int lightChainRenderID;
    public static int latticeRenderID;
    public static int lanternRenderID;
    public static int fenceRenderID;

    public static LanternRenderer lanternRenderer;

    private Map<Object, BindingStack> bindingStacks = new HashMap<Object, BindingStack>();

    @Override
    public void registerRenderers () {
        heavyChainRenderID = RenderingRegistry.getNextAvailableRenderId();
        lightChainRenderID = RenderingRegistry.getNextAvailableRenderId();
        latticeRenderID = RenderingRegistry.getNextAvailableRenderId();
        lanternRenderID = RenderingRegistry.getNextAvailableRenderId();
        fenceRenderID = RenderingRegistry.getNextAvailableRenderId();

        lanternRenderer = new LanternRenderer();

        RenderingRegistry.registerBlockHandler(heavyChainRenderID, new HeavyChainRenderer());
        RenderingRegistry.registerBlockHandler(lightChainRenderID, new LightChainRenderer());
        RenderingRegistry.registerBlockHandler(latticeRenderID, new LatticeRenderer());
        RenderingRegistry.registerBlockHandler(lanternRenderID, lanternRenderer);
        RenderingRegistry.registerBlockHandler(fenceRenderID, new FenceRenderer());

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.lantern), new LanternItemRenderer());
    }

    @Override
    protected Map<Object, BindingStack> getBindingRegistry () {
        return bindingStacks;
    }
}
