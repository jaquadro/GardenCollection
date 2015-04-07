package com.jaquadro.minecraft.gardenapi.api.machine;

public interface ICompostMaterial
{
    /**
     * Gets the number of ticks required to fully decompose this material.
     * Typical times for most materials are 100 - 150 ticks, or 5-8 seconds.
     */
    int getDecomposeTime ();

    /**
     * Gets the fractional amount of compost produced by decomposing this material.
     * For future use.  All materials yield 0.125 unit at this time.
     */
    float getCompostYield ();
}
