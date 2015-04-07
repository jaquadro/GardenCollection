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
    public boolean renderFromInside;
    public boolean enableAO;

    public float shiftU;
    public float shiftV;

    public float colorMultYNeg;
    public float colorMultYPos;
    public float colorMultZNeg;
    public float colorMultZPos;
    public float colorMultXNeg;
    public float colorMultXPos;

    public int brightnessTopLeft;
    public int brightnessBottomLeft;
    public int brightnessBottomRight;
    public int brightnessTopRight;

    public final float[] colorTopLeft = new float[3];
    public final float[] colorBottomLeft = new float[3];
    public final float[] colorBottomRight = new float[3];
    public final float[] colorTopRight = new float[3];

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

    public void setColor (float r, float g, float b) {
        colorTopLeft[0] = r;
        colorTopLeft[1] = g;
        colorTopLeft[2] = b;

        colorBottomLeft[0] = r;
        colorBottomLeft[1] = g;
        colorBottomLeft[2] = b;

        colorBottomRight[0] = r;
        colorBottomRight[1] = g;
        colorBottomRight[2] = b;

        colorTopRight[0] = r;
        colorTopRight[1] = g;
        colorTopRight[2] = b;
    }

    public void scaleColor (float[] color, float scale) {
        for (int i = 0; i < color.length; i++)
            color[i] *= scale;
    }
}
