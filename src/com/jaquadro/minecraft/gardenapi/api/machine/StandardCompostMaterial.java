package com.jaquadro.minecraft.gardenapi.api.machine;

public class StandardCompostMaterial implements ICompostMaterial
{
    private int decompTime = 150;
    private float yield = 0.125f;

    public StandardCompostMaterial () { }

    public StandardCompostMaterial (int decompTime, float yield) {
        this.decompTime = decompTime;
        this.yield = yield;
    }

    @Override
    public int getDecomposeTime () {
        return decompTime;
    }

    @Override
    public float getCompostYield () {
        return yield;
    }
}
