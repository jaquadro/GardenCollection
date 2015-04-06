package com.jaquadro.minecraft.gardencore.util;

public class RenderHelperState
{
    public double renderMinX;
    public double renderMinY;
    public double renderMinZ;
    public double renderMaxX;
    public double renderMaxY;
    public double renderMaxZ;

    public boolean flipTexture;

    public float shiftU;
    public float shiftV;

    public float colorMultYNeg;
    public float colorMultYPos;
    public float colorMultZNeg;
    public float colorMultZPos;
    public float colorMultXNeg;
    public float colorMultXPos;

    public RenderHelperState () {
        resetColorMult();
    }

    public void setRenderBounds (double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        renderMinX = xMin;
        renderMinY = yMin;
        renderMinZ = zMin;
        renderMaxX = xMax;
        renderMaxY = yMax;
        renderMaxZ = zMax;
    }

    public void resetColorMult () {
        colorMultYNeg = 0.5f;
        colorMultYPos = 1.0f;
        colorMultZNeg = 0.8f;
        colorMultZPos = 0.8f;
        colorMultXNeg = 0.6f;
        colorMultXPos = 0.6f;
    }

    public void setTextureOffset (float u, float v) {
        shiftU = u;
        shiftV = v;
    }

    public void resetTextureOffset () {
        shiftU = 0;
        shiftV = 0;
    }
}
