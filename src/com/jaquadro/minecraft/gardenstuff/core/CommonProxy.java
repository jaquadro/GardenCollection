package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardencore.util.BindingStack;

import java.util.Map;

public abstract class CommonProxy
{
    public void registerRenderers ()
    { }

    public void postInit () {
        registerBindingStack(ModBlocks.lantern);
    }

    protected abstract Map<Object, BindingStack> getBindingRegistry ();

    public void registerBindingStack (Object object) {
        getBindingRegistry().put(object, new BindingStack());
    }

    public BindingStack getBindingStack (Object object) {
        return getBindingRegistry().get(object);
    }
}