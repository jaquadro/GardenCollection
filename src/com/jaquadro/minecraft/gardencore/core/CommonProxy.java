package com.jaquadro.minecraft.gardencore.core;

import com.jaquadro.minecraft.gardencore.util.BindingStack;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class CommonProxy
{
    private Map<Object, BindingStack> bindingStacksClient = new HashMap<Object, BindingStack>();
    private Map<Object, BindingStack> bindingStacksServer = new HashMap<Object, BindingStack>();

    public void registerRenderers ()
    { }

    public void postInit () {
        registerBindingStack(ModBlocks.gardenProxy);
    }

    public void registerBindingStack (Object object) {
        bindingStacksClient.put(object, new BindingStack());
        bindingStacksServer.put(object, new BindingStack());
    }

    public BindingStack getClientBindingStack (Object object) {
        return bindingStacksClient.get(object);
    }

    public BindingStack getBindingStack (World world, Object object) {
        if (world.isRemote)
            return bindingStacksClient.get(object);
        else
            return bindingStacksServer.get(object);
    }
}

