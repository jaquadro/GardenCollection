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

    /*public static final int CONNECT_XNEG_ZNEG_YNEG = 1 << 18;
    public static final int CONNECT_XNEG_ZPOS_YNEG = 1 << 19;
    public static final int CONNECT_XPOS_ZNEG_YNEG = 1 << 20;
    public static final int CONNECT_XPOS_ZPOS_YNEG = 1 << 21;
    public static final int CONNECT_XNEG_ZNEG_YPOS = 1 << 22;
    public static final int CONNECT_XNEG_ZPOS_YPOS = 1 << 23;
    public static final int CONNECT_XPOS_ZNEG_YPOS = 1 << 24;
    public static final int CONNECT_XPOS_ZPOS_YPOS = 1 << 25;*/

    /*public static final int CONNECT_X_AXIS = CONNECT_ZNEG | CONNECT_ZPOS | CONNECT_YNEG | CONNECT_YPOS
        | CONNECT_YNEG_ZNEG | CONNECT_YPOS_ZNEG | CONNECT_YNEG_ZPOS | CONNECT_YPOS_ZPOS;
    public static final int CONNECT_Y_AXIS = CONNECT_XNEG | CONNECT_XPOS | CONNECT_ZNEG | CONNECT_ZPOS
        | CONNECT_ZNEG_XNEG | CONNECT_ZPOS_XNEG | CONNECT_ZNEG_XPOS | CONNECT_ZPOS_XPOS;
    public static final int CONNECT_Z_AXIS = CONNECT_XNEG | CONNECT_XPOS | CONNECT_YNEG | CONNECT_YPOS
        | CONNECT_YNEG_XNEG | CONNECT_YPOS_XNEG | CONNECT_YNEG_XPOS | CONNECT_YPOS_XPOS;*/

    //public static final int CONNECT_XNEG_ANY = CONNECT_XNEG
    //    | CONNECT_ZNEG_XNEG | CONNECT_ZPOS_XNEG | CONNECT_YNEG_XNEG | CONNECT_YPOS_XNEG
    //    | CONNECT_XNEG_ZPOS_YNEG | CONNECT_XNEG_ZPOS_YPOS | CONNECT_XNEG_ZNEG_YNEG  | CONNECT_XNEG_ZNEG_YPOS;

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

    private static final int TEST_YNEG_ZNEG = CUT_YNEG | CUT_ZNEG;
    private static final int TEST_YNEG_ZPOS = CUT_YNEG | CUT_ZPOS;
    private static final int TEST_YNEG_XNEG = CUT_YNEG | CUT_XNEG;
    private static final int TEST_YNEG_XPOS = CUT_YNEG | CUT_XPOS;
    private static final int TEST_YPOS_ZNEG = CUT_YPOS | CUT_ZNEG;
    private static final int TEST_YPOS_ZPOS = CUT_YPOS | CUT_ZPOS;
    private static final int TEST_YPOS_XNEG = CUT_YPOS | CUT_XNEG;
    private static final int TEST_YPOS_XPOS = CUT_YPOS | CUT_XPOS;
    private static final int TEST_ZNEG_XNEG = CUT_ZNEG | CUT_XNEG;
    private static final int TEST_ZNEG_XPOS = CUT_ZNEG | CUT_XPOS;
    private static final int TEST_ZPOS_XNEG = CUT_ZPOS | CUT_XNEG;
    private static final int TEST_ZPOS_XPOS = CUT_ZPOS | CUT_XPOS;

    private static final int TEST_YNEG_ZNEG_XNEG = CUT_YNEG | CUT_ZNEG | CUT_XNEG;
    private static final int TEST_YNEG_ZNEG_XPOS = CUT_YNEG | CUT_ZNEG | CUT_XPOS;
    private static final int TEST_YNEG_ZPOS_XNEG = CUT_YNEG | CUT_ZPOS | CUT_XNEG;
    private static final int TEST_YNEG_ZPOS_XPOS = CUT_YNEG | CUT_ZPOS | CUT_XPOS;
    private static final int TEST_YPOS_ZNEG_XNEG = CUT_YPOS | CUT_ZNEG | CUT_XNEG;
    private static final int TEST_YPOS_ZNEG_XPOS = CUT_YPOS | CUT_ZNEG | CUT_XPOS;
    private static final int TEST_YPOS_ZPOS_XNEG = CUT_YPOS | CUT_ZPOS | CUT_XNEG;
    private static final int TEST_YPOS_ZPOS_XPOS = CUT_YPOS | CUT_ZPOS | CUT_XPOS;

    public static void renderOctantExterior (Block block, double x, double y, double z, IIcon icon, RenderBlocks renderer, double unit, int connectedFlags, int cutFlags) {
        double xNeg = x - (int)x;
        double yNeg = y - (int)y;
        double zNeg = z - (int)z;
        double xPos = xNeg + .5;
        double yPos = yNeg + .5;
        double zPos = zNeg + .5;
        x = (int)x;
        y = (int)y;
        z = (int)z;

        if ((cutFlags & CUT_YNEG) != 0)
            connectedFlags |= CONNECT_YNEG;
        if ((cutFlags & CUT_YPOS) != 0)
            connectedFlags |= CONNECT_YPOS;
        if ((cutFlags & CUT_ZNEG) != 0)
            connectedFlags |= CONNECT_ZNEG;
        if ((cutFlags & CUT_ZPOS) != 0)
            connectedFlags |= CONNECT_ZPOS;
        if ((cutFlags & CUT_XNEG) != 0)
            connectedFlags |= CONNECT_XNEG;
        if ((cutFlags & CUT_XPOS) != 0)
            connectedFlags |= CONNECT_XPOS;

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);

        // Render solid faces
        if ((connectedFlags & CONNECT_XNEG) == 0)
            renderer.renderFaceXNeg(block, x, y, z, icon);
        if ((connectedFlags & CONNECT_XPOS) == 0)
            renderer.renderFaceXPos(block, x, y, z, icon);
        if ((connectedFlags & CONNECT_YNEG) == 0)
            renderer.renderFaceYNeg(block, x, y, z, icon);
        if ((connectedFlags & CONNECT_YPOS) == 0)
            renderer.renderFaceYPos(block, x, y, z, icon);
        if ((connectedFlags & CONNECT_ZNEG) == 0)
            renderer.renderFaceZNeg(block, x, y, z, icon);
        if ((connectedFlags & CONNECT_ZPOS) == 0)
            renderer.renderFaceZPos(block, x, y, z, icon);

        // Render edge faces
        if ((cutFlags & TEST_YNEG_ZNEG) != 0) {
            renderer.setRenderBounds(xNeg + unit, yNeg, zNeg, xPos - unit, yNeg + unit, zNeg + unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags & CONNECT_ZNEG) == 0)
                renderer.renderFaceYNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags & CONNECT_YNEG) == 0)
                renderer.renderFaceZNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YNEG_ZPOS) != 0) {
            renderer.setRenderBounds(xNeg + unit, yNeg, zPos - unit, xPos - unit, yNeg + unit, zPos);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags & CONNECT_ZPOS) == 0)
                renderer.renderFaceYNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags & CONNECT_YNEG) == 0)
                renderer.renderFaceZPos(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YNEG_XNEG) != 0) {
            renderer.setRenderBounds(xNeg, yNeg, zNeg + unit, xNeg + unit, yNeg + unit, zPos - unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags & CONNECT_XNEG) == 0)
                renderer.renderFaceYNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags & CONNECT_YNEG) == 0)
                renderer.renderFaceXNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YNEG_XPOS) != 0) {
            renderer.setRenderBounds(xPos - unit, yNeg, zNeg + unit, xPos, yNeg + unit, zPos - unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags & CONNECT_XPOS) == 0)
                renderer.renderFaceYNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags & CONNECT_YNEG) == 0)
                renderer.renderFaceXPos(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YPOS_ZNEG) != 0) {
            renderer.setRenderBounds(xNeg + unit, yPos - unit, zNeg, xPos - unit, yPos, zNeg + unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags & CONNECT_ZNEG) == 0)
                renderer.renderFaceYPos(block, x, y, z, icon);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags & CONNECT_YPOS) == 0)
                renderer.renderFaceZNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YPOS_ZPOS) != 0) {
            renderer.setRenderBounds(xNeg + unit, yPos - unit, zPos - unit, xPos - unit, yPos, zPos);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags & CONNECT_ZPOS) == 0)
                renderer.renderFaceYPos(block, x, y, z, icon);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags & CONNECT_YPOS) == 0)
                renderer.renderFaceZPos(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YPOS_XNEG) != 0) {
            renderer.setRenderBounds(xNeg, yPos - unit, zNeg + unit, xNeg + unit, yPos, zPos - unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags & CONNECT_XNEG) == 0)
                renderer.renderFaceYPos(block, x, y, z, icon);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags & CONNECT_YPOS) == 0)
                renderer.renderFaceXNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YPOS_XPOS) != 0) {
            renderer.setRenderBounds(xPos - unit, yPos - unit, zNeg + unit, xPos, yPos, zPos - unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags & CONNECT_XPOS) == 0)
                renderer.renderFaceYPos(block, x, y, z, icon);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags & CONNECT_YPOS) == 0)
                renderer.renderFaceXPos(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_ZNEG_XNEG) != 0) {
            renderer.setRenderBounds(xNeg, yNeg + unit, zNeg, xNeg + unit, yPos - unit, zNeg + unit);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags & CONNECT_XNEG) == 0)
                renderer.renderFaceZNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags & CONNECT_ZNEG) == 0)
                renderer.renderFaceXNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_ZNEG_XPOS) != 0) {
            renderer.setRenderBounds(xPos - unit, yNeg + unit, zNeg, xPos, yPos - unit, zNeg + unit);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags & CONNECT_XPOS) == 0)
                renderer.renderFaceZNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags & CONNECT_ZNEG) == 0)
                renderer.renderFaceXPos(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_ZPOS_XNEG) != 0) {
            renderer.setRenderBounds(xNeg, yNeg + unit, zPos - unit, xNeg + unit, yPos - unit, zPos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags & CONNECT_XNEG) == 0)
                renderer.renderFaceZPos(block, x, y, z, icon);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags & CONNECT_ZPOS) == 0)
                renderer.renderFaceXNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_ZPOS_XPOS) != 0) {
            renderer.setRenderBounds(xPos - unit, yNeg + unit, zPos - unit, xPos, yPos - unit, zPos);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags & CONNECT_XPOS) == 0)
                renderer.renderFaceZPos(block, x, y, z, icon);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags & CONNECT_ZPOS) == 0)
                renderer.renderFaceXPos(block, x, y, z, icon);
        }

        // Render corner faces
        if ((cutFlags & TEST_YNEG_ZNEG_XNEG) != 0) {
            renderer.setRenderBounds(xNeg, yNeg, zNeg, xNeg + unit, yNeg + unit, zNeg + unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XNEG | CONNECT_ZNEG_XNEG) != connectedFlags)
                renderer.renderFaceYNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XNEG | CONNECT_YNEG_XNEG) != connectedFlags)
                renderer.renderFaceZNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZNEG | CONNECT_YNEG_ZNEG) != connectedFlags)
                renderer.renderFaceXNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YNEG_ZNEG_XPOS) != 0) {
            renderer.setRenderBounds(xPos - unit, yNeg, zNeg, xPos, yNeg + unit, zNeg + unit);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XPOS | CONNECT_ZNEG_XPOS) != connectedFlags)
                renderer.renderFaceYNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XPOS | CONNECT_YNEG_XPOS) != connectedFlags)
                renderer.renderFaceZNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZNEG | CONNECT_YNEG_ZNEG) != connectedFlags)
                renderer.renderFaceXPos(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YNEG_ZPOS_XNEG) != 0) {
            renderer.setRenderBounds(xNeg, yNeg, zPos - unit, xNeg + unit, yNeg + unit, zPos);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XNEG | CONNECT_ZPOS_XNEG) != connectedFlags)
                renderer.renderFaceYNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XNEG | CONNECT_YNEG_XNEG) != connectedFlags)
                renderer.renderFaceZPos(block, x, y, z, icon);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZPOS | CONNECT_YNEG_ZPOS) != connectedFlags)
                renderer.renderFaceXNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YNEG_ZPOS_XPOS) != 0) {
            renderer.setRenderBounds(xPos - unit, yNeg, zPos - unit, xPos, yNeg + unit, zPos);
            if ((cutFlags & CUT_YNEG) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XPOS | CONNECT_ZPOS_XPOS) != connectedFlags)
                renderer.renderFaceYNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XPOS | CONNECT_YNEG_XPOS) != connectedFlags)
                renderer.renderFaceZPos(block, x, y, z, icon);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZPOS | CONNECT_YNEG_ZPOS) != connectedFlags)
                renderer.renderFaceXPos(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YPOS_ZNEG_XNEG) != 0) {
            renderer.setRenderBounds(xNeg, yPos - unit, zNeg, xNeg + unit, yPos, zNeg + unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XNEG | CONNECT_ZNEG_XNEG) != connectedFlags)
                renderer.renderFaceYPos(block, x, y, z, icon);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XNEG | CONNECT_YPOS_XNEG) != connectedFlags)
                renderer.renderFaceZNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZNEG | CONNECT_YPOS_ZNEG) != connectedFlags)
                renderer.renderFaceXNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YPOS_ZNEG_XPOS) != 0) {
            renderer.setRenderBounds(xPos - unit, yPos - unit, zNeg, xPos, yPos, zNeg + unit);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XPOS | CONNECT_ZNEG_XPOS) != connectedFlags)
                renderer.renderFaceYPos(block, x, y, z, icon);
            if ((cutFlags & CUT_ZNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XPOS | CONNECT_YPOS_XPOS) != connectedFlags)
                renderer.renderFaceZNeg(block, x, y, z, icon);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZNEG | CONNECT_YPOS_ZNEG) != connectedFlags)
                renderer.renderFaceXPos(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YPOS_ZPOS_XNEG) != 0) {
            renderer.setRenderBounds(xNeg, yPos - unit, zPos - unit, xNeg + unit, yPos, zPos);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XNEG | CONNECT_ZPOS_XNEG) != connectedFlags)
                renderer.renderFaceYPos(block, x, y, z, icon);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XNEG | CONNECT_YPOS_XNEG) != connectedFlags)
                renderer.renderFaceZPos(block, x, y, z, icon);
            if ((cutFlags & CUT_XNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZPOS | CONNECT_YPOS_ZPOS) != connectedFlags)
                renderer.renderFaceXNeg(block, x, y, z, icon);
        }
        if ((cutFlags & TEST_YPOS_ZPOS_XPOS) != 0) {
            renderer.setRenderBounds(xPos - unit, yPos - unit, zPos - unit, xPos, yPos, zPos);
            if ((cutFlags & CUT_YPOS) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XPOS | CONNECT_ZPOS_XPOS) != connectedFlags)
                renderer.renderFaceYPos(block, x, y, z, icon);
            if ((cutFlags & CUT_ZPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_XPOS | CONNECT_YPOS_XPOS) != connectedFlags)
                renderer.renderFaceZPos(block, x, y, z, icon);
            if ((cutFlags & CUT_XPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZPOS | CONNECT_YPOS_ZPOS) != connectedFlags)
                renderer.renderFaceXPos(block, x, y, z, icon);
        }
    }

    public static void renderOctantInterior (Block block, double x, double y, double z, IIcon icon, RenderBlocks renderer, double unit, int connectedFlags, int cutFlags) {
        double xNeg = x - (int) x;
        double yNeg = y - (int) y;
        double zNeg = z - (int) z;
        double xPos = xNeg + .5;
        double yPos = yNeg + .5;
        double zPos = zNeg + .5;
        x = (int)x;
        y = (int)y;
        z = (int)z;

        if ((cutFlags & CUT_YNEG) != 0)
            connectedFlags |= CONNECT_YNEG;
        if ((cutFlags & CUT_YPOS) != 0)
            connectedFlags |= CONNECT_YPOS;
        if ((cutFlags & CUT_ZNEG) != 0)
            connectedFlags |= CONNECT_ZNEG;
        if ((cutFlags & CUT_ZPOS) != 0)
            connectedFlags |= CONNECT_ZPOS;
        if ((cutFlags & CUT_XNEG) != 0)
            connectedFlags |= CONNECT_XNEG;
        if ((cutFlags & CUT_XPOS) != 0)
            connectedFlags |= CONNECT_XPOS;

        renderer.setRenderBounds(xNeg + unit, yNeg + unit, zNeg + unit, xPos - unit, yPos - unit, zPos - unit);

        // Render solid faces
        if ((connectedFlags & CONNECT_YNEG) == 0) {
            renderer.setRenderBounds(xNeg + unit, yNeg, zNeg + unit, xPos - unit, yNeg + unit, zPos - unit);
            renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if ((connectedFlags & CONNECT_YPOS) == 0) {
            renderer.setRenderBounds(xNeg + unit, yPos - unit, zNeg + unit, xPos - unit, yPos, zPos - unit);
            renderer.renderFaceYNeg(block, x, y, z, icon);
        }
        if ((connectedFlags & CONNECT_ZNEG) == 0) {
            renderer.setRenderBounds(xNeg + unit, yNeg + unit, zNeg, xPos - unit, yPos - unit, zNeg + unit);
            renderer.renderFaceZPos(block, x, y, z, icon);
        }
        if ((connectedFlags & CONNECT_ZPOS) == 0) {
            renderer.setRenderBounds(xNeg + unit, yNeg + unit, zPos - unit, xPos - unit, yPos - unit, zPos);
            renderer.renderFaceZNeg(block, x, y, z, icon);
        }
        if ((connectedFlags & CONNECT_XNEG) == 0) {
            renderer.setRenderBounds(xNeg, yNeg + unit, zNeg + unit, xNeg + unit, yPos - unit, zPos - unit);
            renderer.renderFaceXPos(block, x, y, z, icon);
        }
        if ((connectedFlags & CONNECT_XPOS) == 0) {
            renderer.setRenderBounds(xPos - unit, yNeg + unit, zNeg + unit, xPos, yPos - unit, zPos - unit);
            renderer.renderFaceXNeg(block, x, y, z, icon);
        }

        // Render edge faces
        if ((connectedFlags & TEST_YNEG_ZNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZNEG | CONNECT_YNEG_ZNEG) != connectedFlags) {
            renderer.setRenderBounds(xNeg + unit, yNeg, zNeg, xPos - unit, yNeg + unit, zNeg + unit);
            if ((connectedFlags & CONNECT_YNEG) != 0)
                renderer.renderFaceZPos(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_ZNEG) != 0)
                renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_YNEG_ZPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_ZPOS | CONNECT_YNEG_ZPOS) != connectedFlags) {
            renderer.setRenderBounds(xNeg + unit, yNeg, zPos - unit, xPos - unit, yNeg + unit, zPos);
            if ((connectedFlags & CONNECT_YNEG) != 0)
                renderer.renderFaceZNeg(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_ZPOS) != 0)
                renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_YNEG_XNEG) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XNEG | CONNECT_YNEG_XNEG) != connectedFlags) {
            renderer.setRenderBounds(xNeg, yNeg, zNeg + unit, xNeg + unit, yNeg + unit, zPos - unit);
            if ((connectedFlags & CONNECT_YNEG) != 0)
                renderer.renderFaceXPos(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_XNEG) != 0)
                renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_YNEG_XPOS) != 0 && (connectedFlags | CONNECT_YNEG | CONNECT_XPOS | CONNECT_YNEG_XPOS) != connectedFlags) {
            renderer.setRenderBounds(xPos - unit, yNeg, zNeg + unit, xPos, yNeg + unit, zPos - unit);
            if ((connectedFlags & CONNECT_YNEG) != 0)
                renderer.renderFaceXNeg(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_XPOS) != 0)
                renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_YPOS_ZNEG) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZNEG | CONNECT_YPOS_ZNEG) != connectedFlags) {
            renderer.setRenderBounds(xNeg + unit, yPos - unit, zNeg, xPos - unit, yPos, zNeg + unit);
            if ((connectedFlags & CONNECT_YPOS) != 0)
                renderer.renderFaceZPos(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_ZNEG) != 0)
                renderer.renderFaceYNeg(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_YPOS_ZPOS) != 0 && (connectedFlags | CONNECT_YPOS | CONNECT_ZPOS | CONNECT_YPOS_ZPOS) != connectedFlags) {
            renderer.setRenderBounds(xNeg + unit, yPos - unit, zPos - unit, xPos - unit, yPos, zPos);
            if ((connectedFlags & CONNECT_YPOS) != 0)
                renderer.renderFaceZNeg(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_ZPOS) != 0)
                renderer.renderFaceYNeg(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_YPOS_XNEG) != 0) {
            renderer.setRenderBounds(xNeg, yPos - unit, zNeg + unit, xNeg + unit, yPos, zPos - unit);
            if ((connectedFlags & CONNECT_YPOS) != 0 && (connectedFlags & CONNECT_XNEG) == 0)
                renderer.renderFaceXPos(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_XNEG) != 0)
                renderer.renderFaceYNeg(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_YPOS_XPOS) != 0) {
            renderer.setRenderBounds(xPos - unit, yPos - unit, zNeg + unit, xPos, yPos, zPos - unit);
            if ((connectedFlags & CONNECT_YPOS) != 0 && (connectedFlags & CONNECT_XPOS) == 0)
                renderer.renderFaceXNeg(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_XPOS) != 0)
                renderer.renderFaceYNeg(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_ZNEG_XNEG) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XNEG | CONNECT_ZNEG_XNEG) != connectedFlags) {
            renderer.setRenderBounds(xNeg, yNeg + unit, zNeg, xNeg + unit, yPos - unit, zNeg + unit);
            if ((connectedFlags & CONNECT_ZNEG) != 0)
                renderer.renderFaceXPos(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_XNEG) != 0)
                renderer.renderFaceZPos(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_ZNEG_XPOS) != 0 && (connectedFlags | CONNECT_ZNEG | CONNECT_XPOS | CONNECT_ZNEG_XPOS) != connectedFlags) {
            renderer.setRenderBounds(xPos - unit, yNeg + unit, zNeg, xPos, yPos - unit, zNeg + unit);
            if ((connectedFlags & CONNECT_ZNEG) != 0)
                renderer.renderFaceXNeg(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_XPOS) != 0)
                renderer.renderFaceZPos(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_ZPOS_XNEG) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XNEG | CONNECT_ZPOS_XNEG) != connectedFlags) {
            renderer.setRenderBounds(xNeg, yNeg + unit, zPos - unit, xNeg + unit, yPos - unit, zPos);
            if ((connectedFlags & CONNECT_ZPOS) != 0)
                renderer.renderFaceXPos(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_XNEG) != 0)
                renderer.renderFaceZNeg(block, x, y, z, icon);
        }
        if ((connectedFlags & TEST_ZPOS_XPOS) != 0 && (connectedFlags | CONNECT_ZPOS | CONNECT_XPOS | CONNECT_ZPOS_XPOS) != connectedFlags) {
            renderer.setRenderBounds(xPos - unit, yNeg + unit, zPos - unit, xPos, yPos - unit, zPos);
            if ((connectedFlags & CONNECT_ZPOS) != 0)
                renderer.renderFaceXNeg(block, x, y, z, icon);
            if ((connectedFlags & CONNECT_XPOS) != 0)
                renderer.renderFaceZNeg(block, x, y, z, icon);
        }
    }

    public static void renderOctantZNeg (Block block, double x, double y, double z, IIcon icon, RenderBlocks renderer, double unit, int connectFlags, int cutFlags) {
        double xNeg = x - (int)x;
        double yNeg = y - (int)y;
        double zNeg = z - (int)z;
        double xPos = xNeg + .5;
        double yPos = yNeg + .5;
        double zPos = zNeg + unit;

        if ((connectFlags & CONNECT_ZNEG) != 0) {
            if ((connectFlags & CONNECT_YNEG) == 0)
                yNeg += unit;
            if ((connectFlags & CONNECT_YPOS) == 0)
                yPos -= unit;

            if ((connectFlags & CONNECT_XNEG) != 0 && (connectFlags & CONNECT_ZNEG_XNEG) == 0) {
                renderer.setRenderBounds(xNeg, yNeg, zNeg, xNeg + unit, yPos, zPos);
                renderer.renderFaceZPos(block, x, y, z, icon);
            }
            if ((connectFlags & CONNECT_XPOS) != 0 && (connectFlags & CONNECT_ZNEG_XPOS) == 0) {
                renderer.setRenderBounds(xPos - unit, yNeg, zNeg, xPos, yPos, zPos);
                renderer.renderFaceZPos(block, x, y, z, icon);
            }
            return;
        }

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceZNeg(block, x, y, z, icon);

        if ((connectFlags & CONNECT_XNEG) == 0)
            xNeg += unit;
        if ((connectFlags & CONNECT_XPOS) == 0)
            xPos -= unit;
        if ((connectFlags & CONNECT_YNEG) == 0)
            yNeg += unit;
        if ((connectFlags & CONNECT_YPOS) == 0)
            yPos -= unit;

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceZPos(block, x, y, z, icon);
    }

    public static void renderOctantZPos (Block block, double x, double y, double z, IIcon icon, RenderBlocks renderer, double unit, int connectFlags) {
        if ((connectFlags & CONNECT_ZNEG) != 0)
            return;

        double xNeg = x - (int)x;
        double yNeg = y - (int)y;
        double zNeg = z - (int)z + .5 - unit;
        double xPos = xNeg + .5;
        double yPos = yNeg + .5;
        double zPos = zNeg + unit;

        if ((connectFlags & CONNECT_ZPOS) != 0) {
            if ((connectFlags & CONNECT_YNEG) == 0)
                yNeg += unit;
            if ((connectFlags & CONNECT_YPOS) == 0)
                yPos -= unit;

            if ((connectFlags & CONNECT_XNEG) != 0 && (connectFlags & CONNECT_ZPOS_XNEG) == 0) {
                renderer.setRenderBounds(xNeg, yNeg, zNeg, xNeg + unit, yPos, zPos);
                renderer.renderFaceZNeg(block, x, y, z, icon);
            }
            if ((connectFlags & CONNECT_XPOS) != 0 && (connectFlags & CONNECT_ZPOS_XPOS) == 0) {
                renderer.setRenderBounds(xPos - unit, yNeg, zNeg, xPos, yPos, zPos);
                renderer.renderFaceZNeg(block, x, y, z, icon);
            }
            return;
        }

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceZPos(block, x, y, z, icon);

        if ((connectFlags & CONNECT_XNEG) == 0)
            xNeg += unit;
        if ((connectFlags & CONNECT_XPOS) == 0)
            xPos -= unit;
        if ((connectFlags & CONNECT_YNEG) == 0)
            yNeg += unit;
        if ((connectFlags & CONNECT_YPOS) == 0)
            yPos -= unit;

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceZNeg(block, x, y, z, icon);
    }

    public static void renderOctantXNeg (Block block, double x, double y, double z, IIcon icon, RenderBlocks renderer, double unit, int connectFlags) {
        double xNeg = x - (int)x;
        double yNeg = y - (int)y;
        double zNeg = z - (int)z;
        double xPos = xNeg + unit;
        double yPos = yNeg + .5;
        double zPos = zNeg + .5;

        if ((connectFlags & CONNECT_XNEG) != 0) {
            if ((connectFlags & CONNECT_YNEG) == 0)
                yNeg += unit;
            if ((connectFlags & CONNECT_YPOS) == 0)
                yPos -= unit;

            if ((connectFlags & CONNECT_ZNEG) != 0 && (connectFlags & CONNECT_ZNEG_XNEG) == 0) {
                renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zNeg + unit);
                renderer.renderFaceXPos(block, x, y, z, icon);
            }
            if ((connectFlags & CONNECT_ZPOS) != 0 && (connectFlags & CONNECT_ZPOS_XNEG) == 0) {
                renderer.setRenderBounds(xNeg, yNeg, zPos - unit, xPos, yPos, zPos);
                renderer.renderFaceXPos(block, x, y, z, icon);
            }
            return;
        }

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceXNeg(block, x, y, z, icon);

        if ((connectFlags & CONNECT_ZNEG) == 0)
            zNeg += unit;
        if ((connectFlags & CONNECT_ZPOS) == 0)
            zPos -= unit;
        if ((connectFlags & CONNECT_YNEG) == 0)
            yNeg += unit;
        if ((connectFlags & CONNECT_YPOS) == 0)
            yPos -= unit;

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceZPos(block, x, y, z, icon);
    }

    public static void renderOctantXPos (Block block, double x, double y, double z, IIcon icon, RenderBlocks renderer, double unit, int connectFlags) {
        double xNeg = x - (int)x + .5 - unit;
        double yNeg = y - (int)y;
        double zNeg = z - (int)z;
        double xPos = xNeg + unit;
        double yPos = yNeg + .5;
        double zPos = zNeg + .5;

        if ((connectFlags & CONNECT_XPOS) != 0) {
            if ((connectFlags & CONNECT_YNEG) == 0)
                yNeg += unit;
            if ((connectFlags & CONNECT_YPOS) == 0)
                yPos -= unit;

            if ((connectFlags & CONNECT_ZNEG) != 0 && (connectFlags & CONNECT_ZNEG_XPOS) == 0) {
                renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zNeg + unit);
                renderer.renderFaceXNeg(block, x, y, z, icon);
            }
            if ((connectFlags & CONNECT_ZPOS) != 0 && (connectFlags & CONNECT_ZPOS_XPOS) == 0) {
                renderer.setRenderBounds(xNeg, yNeg, zPos - unit, xPos, yPos, zPos);
                renderer.renderFaceXNeg(block, x, y, z, icon);
            }
            return;
        }

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceZPos(block, x, y, z, icon);

        if ((connectFlags & CONNECT_ZNEG) == 0)
            zNeg += unit;
        if ((connectFlags & CONNECT_ZPOS) == 0)
            zPos -= unit;
        if ((connectFlags & CONNECT_YNEG) == 0)
            yNeg += unit;
        if ((connectFlags & CONNECT_YPOS) == 0)
            yPos -= unit;

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceZNeg(block, x, y, z, icon);
    }
}
