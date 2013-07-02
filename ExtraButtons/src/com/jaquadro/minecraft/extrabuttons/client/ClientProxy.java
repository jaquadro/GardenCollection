package com.jaquadro.minecraft.extrabuttons.client;

import com.jaquadro.minecraft.extrabuttons.CommonProxy;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers ()
    {
        MinecraftForgeClient.preloadTexture(ITEMS_PNG);
        MinecraftForgeClient.preloadTexture(BLOCK_PNG);
    }
}
