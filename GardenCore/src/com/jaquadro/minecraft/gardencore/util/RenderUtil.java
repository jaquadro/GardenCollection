package com.jaquadro.minecraft.gardencore.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public final class RenderUtil
{
    public static final int CONNECT_YNEG = 1 << 0;
    public static final int CONNECT_YPOS = 1 << 1;
    public static final int CONNECT_ZNEG = 1 << 2;
    public static final int CONNECT_ZPOS = 1 << 3;
    public static final int CONNECT_XNEG = 1 << 4;
    public static final int CONNECT_XPOS = 1 << 5;
    public static final int CONNECT_YNEG_ZNEG = 1 << 6;
    public static final int CONNECT_YNEG_ZPOS = 1 << 7;
    public static final int CONNECT_YNEG_XNEG = 1 << 8;
    public static final int CONNECT_YNEG_XPOS = 1 << 9;
    public static final int CONNECT_YPOS_ZNEG = 1 << 10;
    public static final int CONNECT_YPOS_ZPOS = 1 << 11;
    public static final int CONNECT_YPOS_XNEG = 1 << 12;
    public static final int CONNECT_YPOS_XPOS = 1 << 13;
    public static final int CONNECT_ZNEG_XNEG = 1 << 14;
    public static final int CONNECT_ZNEG_XPOS = 1 << 15;
    public static final int CONNECT_ZPOS_XNEG = 1 << 16;
    public static final int CONNECT_ZPOS_XPOS = 1 << 17;

    public static final int CUT_YNEG = 1 << 0;
    public static final int CUT_YPOS = 1 << 1;
    public static final int CUT_ZNEG = 1 << 2;
    public static final int CUT_ZPOS = 1 << 3;
    public static final int CUT_XNEG = 1 << 4;
    public static final int CUT_XPOS = 1 << 5;

    private RenderUtil () { }

    public static void calculateBaseColor (float[] target, int color) {
        float r = (float)(color >> 16 & 255) / 255f;
        float g = (float)(color >> 8 & 255) / 255f;
        float b = (float)(color & 255) / 255f;

        if (EntityRenderer.anaglyphEnable) {
            float gray = (r * 30f + g * 59f + b * 11f) / 100f;
            float rg = (r * 30f + g * 70f) / 100f;
            float rb = (r * 30f + b * 70f) / 100f;

            r = gray;
            g = rg;
            b = rb;
        }

        target[0] = r;
        target[1] = g;
        target[2] = b;
    }

    public static void scaleColor (float[] target, float[] source, float scale) {
        target[0] = source[0] * scale;
        target[1] = source[1] * scale;
        target[2] = source[2] * scale;
    }

    public static void setTessellatorColor (Tessellator tessellator, float[] color) {
        tessellator.setColorOpaque_F(color[0], color[1], color[2]);
    }

    public static void renderEmptyPlane (Block block, int x, int y, int z, RenderBlocks renderer) {
        renderer.setRenderBounds(0, 0, 0, 0, 0, 0);
        renderer.renderFaceYNeg(block, x, y, z, Blocks.dirt.getIcon(0, 0));
    }
}
