package com.jaquadro.minecraft.gardencommon.integration;

public abstract class IntegrationModule
{
    public String getModID () {
        return null;
    }

    public abstract void init () throws Throwable;

    public abstract void postInit () throws Throwable;
}
