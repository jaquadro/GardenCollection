package com.jaquadro.minecraft.gardenstuff.core;

import com.jaquadro.minecraft.gardencore.util.BindingStack;

import java.util.HashMap;
import java.util.Map;

public class ServerProxy extends CommonProxy
{
    private Map<Object, BindingStack> bindingStacks = new HashMap<Object, BindingStack>();

    @Override
    protected Map<Object, BindingStack> getBindingRegistry () {
        return bindingStacks;
    }
}
