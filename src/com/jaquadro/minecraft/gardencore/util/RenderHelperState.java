package com.jaquadro.minecraft.gardencore.util;

public class RenderHelperState
{
    public float colorMultYNeg;
    public float colorMultYPos;
    public float colorMultZNeg;
    public float colorMultZPos;
    public float colorMultXNeg;
    public float colorMultXPos;

    public RenderHelperState () {
        resetColorMult();
    }

    public void resetColorMult () {
        colorMultYNeg = 0.5f;
        colorMultYPos = 1.0f;
        colorMultZNeg = 0.8f;
        colorMultZPos = 0.8f;
        colorMultXNeg = 0.6f;
        colorMultXPos = 0.6f;
    }
}
