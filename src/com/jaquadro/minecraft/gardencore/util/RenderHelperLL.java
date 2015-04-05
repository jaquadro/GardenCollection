package com.jaquadro.minecraft.gardencore.util;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class RenderHelperLL
{
    private static final int TL = 0;
    private static final int BL = 1;
    private static final int BR = 2;
    private static final int TR = 3;

    private static final int MINX = 0;
    private static final int MAXX = 1;
    private static final int MINY = 2;
    private static final int MAXY = 3;
    private static final int MINZ = 4;
    private static final int MAXZ = 5;

    private static final int xyzuvMap[][][] = {
        {       // Y-NEG
            { 0, 2, 5, 0, 3 },
            { 0, 2, 4, 0, 2 },
            { 1, 2, 4, 1, 2 },
            { 1, 2, 5, 1, 3 }
        }, {    // Y-POS
            { 1, 3, 5, 1, 3 },
            { 1, 3, 4, 1, 2 },
            { 0, 3, 4, 0, 2 },
            { 0, 3, 5, 0, 3 }
        }, {    // Z-NEG
            { 0, 3, 4, 1, 2 },
            { 1, 3, 4, 0, 2 },
            { 1, 2, 4, 0, 3 },
            { 0, 2, 4, 1, 3 }
        }, {    // Z-POS
            { 0, 3, 5, 0, 2 },
            { 0, 2, 5, 0, 3 },
            { 1, 2, 5, 1, 3 },
            { 1, 3, 5, 1, 2 }
        }, {    // X-NEG
            { 0, 3, 5, 1, 2 },
            { 0, 3, 4, 0, 2 },
            { 0, 2, 4, 0, 3 },
            { 0, 2, 5, 1, 3 }
        }, {    // X-POS
            { 1, 2, 5, 0, 3 },
            { 1, 2, 4, 1, 3 },
            { 1, 3, 4, 1, 2 },
            { 1, 3, 5, 0, 2 }
        },
    };

    public boolean enableAO = true;

    public double uShift;
    public double vShift;

    public double renderMinX;
    public double renderMinY;
    public double renderMinZ;
    public double renderMaxX;
    public double renderMaxY;
    public double renderMaxZ;

    public float[] colorTopLeft = new float[3];
    public float[] colorTopRight = new float[3];
    public float[] colorBottomLeft = new float[3];
    public float[] colorBottomRight = new float[3];

    public int brightnessTopLeft;
    public int brightnessTopRight;
    public int brightnessBottomLeft;
    public int brightnessBottomRight;

    private double[] minUDiv = new double[24];
    private double[] maxUDiv = new double[24];
    private double[] minVDiv = new double[24];
    private double[] maxVDiv = new double[24];

    // u-min, u-max, v-min, v-max
    private double[] uv = new double[4];

    // x-min, x-max, y-min, y-max, z-min, z-max
    private double[] xyz = new double[6];

    public void drawFaceYNeg (double x, double y, double z, IIcon icon) {
        int rangeX = (int)(Math.ceil(renderMaxX + uShift) - Math.floor(renderMinX + uShift));
        int rangeZ = (int)(Math.ceil(renderMaxZ + vShift) - Math.floor(renderMinZ + vShift));

        if (rangeX == 1 && rangeZ == 1) {
            setXYZ(x, y, z);
            setUV(icon, renderMaxX + uShift, renderMaxZ + vShift, renderMinX + uShift, renderMinZ + vShift);

            if (enableAO)
                renderXYZUVAO(xyzuvMap[0]);
            else
                renderXYZUV(xyzuvMap[0]);
            return;
        }

        double uStart = (renderMinX + uShift + rangeX) % 1.0;
        double uStop = (renderMaxX + uShift + rangeX) % 1.0;
        double vStart = (renderMinZ + vShift + rangeZ) % 1.0;
        double vStop = (renderMaxZ + vShift + rangeZ) % 1.0;

        setupUVPoints(uStart, vStart, uStop, vStop, rangeX, rangeZ, icon);
        setXYZ(x, y, z);

        for (int ix = 0; ix < rangeX; ix++) {
            xyz[MAXX] = xyz[MINX] + maxUDiv[ix] - minUDiv[ix];
            xyz[MINZ] = z + renderMinZ;

            for (int iz = 0; iz < rangeZ; iz++) {
                xyz[MAXZ] = xyz[MINZ] + maxVDiv[iz] - minVDiv[iz];

                setUV(minUDiv[ix], minVDiv[iz], maxUDiv[ix], maxVDiv[iz]);
                renderXYZUV(xyzuvMap[0]);

                xyz[MINZ] = xyz[MAXZ];
            }
            xyz[MINX] = xyz[MAXX];
        }
    }

    public void drawFaceY (int face, double x, double y, double z, IIcon icon) {
        int rangeX = (int)(Math.ceil(renderMaxX + uShift) - Math.floor(renderMinX + uShift));
        int rangeZ = (int)(Math.ceil(renderMaxZ + vShift) - Math.floor(renderMinZ + vShift));

        if (rangeX == 1 && rangeZ == 1) {
            setXYZ(x, y, z);
            setUV(icon, renderMinX + uShift, renderMinZ + vShift, renderMaxX + uShift, renderMaxZ + vShift);

            if (enableAO)
                renderXYZUVAO(xyzuvMap[face]);
            else
                renderXYZUV(xyzuvMap[face]);
            return;
        }

        double uStart = (renderMinX + uShift + rangeX) % 1.0;
        double uStop = (renderMaxX + uShift + rangeX) % 1.0;
        double vStart = (renderMinZ + vShift + rangeZ) % 1.0;
        double vStop = (renderMaxZ + vShift + rangeZ) % 1.0;

        setupUVPoints(uStart, vStart, uStop, vStop, rangeX, rangeZ, icon);
        setXYZ(x, y, z);

        for (int ix = 0; ix < rangeX; ix++) {
            xyz[MAXX] = xyz[MINX] + maxUDiv[ix] - minUDiv[ix];
            xyz[MINZ] = z + renderMinZ;

            for (int iz = 0; iz < rangeZ; iz++) {
                xyz[MAXZ] = xyz[MINZ] + maxVDiv[iz] - minVDiv[iz];

                setUV(minUDiv[ix], minVDiv[iz], maxUDiv[ix], maxVDiv[iz]);
                renderXYZUV(xyzuvMap[face]);

                xyz[MINZ] = xyz[MAXZ];
            }
            xyz[MINX] = xyz[MAXX];
        }
    }

    public void drawFaceZ (int face, double x, double y, double z, IIcon icon) {
        int rangeX = (int)(Math.ceil(renderMaxX + uShift) - Math.floor(renderMinX + uShift));
        int rangeY = (int)(Math.ceil(renderMaxY + vShift) - Math.floor(renderMinY + vShift));

        if (rangeX == 1 && rangeY == 1) {
            setXYZ(x, y, z);
            setUV(icon, renderMinX + uShift, 1 - renderMaxY + vShift, renderMaxX + uShift, 1 - renderMinY + vShift);

            if (enableAO)
                renderXYZUVAO(xyzuvMap[face]);
            else
                renderXYZUV(xyzuvMap[face]);
            return;
        }

        double uStart = (renderMinX + uShift + rangeX) % 1.0;
        double uStop = (renderMaxX + uShift + rangeX) % 1.0;
        double vStart = (renderMinY + vShift + rangeY) % 1.0;
        double vStop = (renderMaxY + vShift + rangeY) % 1.0;

        setupUVPoints(uStart, vStart, uStop, vStop, rangeX, rangeY, icon);
        setXYZ(x, y, z);

        for (int ix = 0; ix < rangeX; ix++) {
            xyz[MAXX] = xyz[MINX] + maxUDiv[ix] - minUDiv[ix];
            xyz[MINY] = y + renderMinY;

            for (int iy = 0; iy < rangeY; iy++) {
                xyz[MAXY] = xyz[MINY] + maxVDiv[iy] - minVDiv[iy];

                setUV(icon, minUDiv[ix], minVDiv[iy], maxUDiv[ix], maxVDiv[iy]);
                renderXYZUV(xyzuvMap[face]);

                xyz[MINY] = xyz[MAXY];
            }
            xyz[MINX] = xyz[MAXX];
        }
    }

    public void drawFaceX (int face, double x, double y, double z, IIcon icon) {
        int rangeZ = (int)(Math.ceil(renderMaxZ + uShift) - Math.floor(renderMinZ + uShift));
        int rangeY = (int)(Math.ceil(renderMaxY + vShift) - Math.floor(renderMinY + vShift));

        if (rangeZ == 1 && rangeY == 1) {
            setXYZ(x, y, z);
            setUV(icon, renderMinZ + uShift, 1 - renderMaxY + vShift, renderMaxZ + uShift, 1 - renderMinY + vShift);

            if (enableAO)
                renderXYZUVAO(xyzuvMap[face]);
            else
                renderXYZUV(xyzuvMap[face]);
            return;
        }

        double uStart = (renderMinZ + uShift + rangeZ) % 1.0;
        double uStop = (renderMaxZ + uShift + rangeZ) % 1.0;
        double vStart = (renderMinY + vShift + rangeY) % 1.0;
        double vStop = (renderMaxY + vShift + rangeY) % 1.0;

        setupUVPoints(uStart, vStart, uStop, vStop, rangeZ, rangeY, icon);
        setXYZ(x, y, z);

        for (int iz = 0; iz < rangeZ; iz++) {
            xyz[MAXZ] = xyz[MINZ] + maxUDiv[iz] - minUDiv[iz];
            xyz[MINY] = y + renderMinY;

            for (int iy = 0; iy < rangeY; iy++) {
                xyz[MAXY] = xyz[MINY] + maxVDiv[iy] - minVDiv[iy];

                setUV(minUDiv[iz], minVDiv[iy], maxUDiv[iz], maxVDiv[iy]);
                renderXYZUV(xyzuvMap[face]);

                xyz[MINY] = xyz[MAXY];
            }
            xyz[MINZ] = xyz[MAXZ];
        }
    }

    private void setupUVPoints (double uStart, double vStart, double uStop, double vStop, int rangeU, int rangeV, IIcon icon) {
        if (rangeU <= 1) {
            minUDiv[0] = uStart;
            maxUDiv[0] = uStop;
        }
        else {
            minUDiv[0] = uStart;
            maxUDiv[0] = 1;
            for (int i = 1; i < rangeU - 1; i++) {
                minUDiv[i] = 0;
                maxUDiv[i] = 1;
            }
            minUDiv[rangeU - 1] = 0;
            maxUDiv[rangeU - 1] = uStop;
        }

        if (rangeV <= 1) {
            minVDiv[0] = vStart;
            maxVDiv[0] = vStop;
        }
        else {
            minVDiv[0] = vStart;
            maxVDiv[0] = 1;
            for (int i = 1; i < rangeV - 1; i++) {
                minVDiv[i] = 0;
                maxVDiv[i] = 1;
            }
            minVDiv[rangeV - 1] = 0;
            maxVDiv[rangeV - 1] = vStop;
        }
    }

    private void setUV (IIcon icon, double uMin, double vMin, double uMax, double vMax) {
        uv[0] = icon.getInterpolatedU(uMin * 16);
        uv[1] = icon.getInterpolatedU(uMax * 16);
        uv[2] = icon.getInterpolatedV(vMin * 16);
        uv[3] = icon.getInterpolatedV(vMax * 16);
    }

    private void setUV (double uMin, double vMin, double uMax, double vMax) {
        uv[0] = uMin;
        uv[1] = uMax;
        uv[2] = vMin;
        uv[3] = vMax;
    }

    private void setXYZ (double x, double y, double z) {
        xyz[0] = x + renderMinX;
        xyz[1] = x + renderMaxX;
        xyz[2] = y + renderMinY;
        xyz[3] = y + renderMaxY;
        xyz[4] = z + renderMinZ;
        xyz[5] = z + renderMaxZ;
    }

    private void renderXYZUV (int[][] index) {
        Tessellator tessellator = Tessellator.instance;

        int[] tl = index[TL];
        int[] bl = index[BL];
        int[] br = index[BR];
        int[] tr = index[TR];

        tessellator.addVertexWithUV(xyz[tl[0]], xyz[tl[1]], xyz[tl[2]], uv[tl[3]], uv[tl[4]]);
        tessellator.addVertexWithUV(xyz[bl[0]], xyz[bl[1]], xyz[bl[2]], uv[bl[3]], uv[bl[4]]);
        tessellator.addVertexWithUV(xyz[br[0]], xyz[br[1]], xyz[br[2]], uv[br[3]], uv[br[4]]);
        tessellator.addVertexWithUV(xyz[tr[0]], xyz[tr[1]], xyz[tr[2]], uv[tr[3]], uv[tr[4]]);
    }

    private void renderXYZUVAO (int[][] index) {
        Tessellator tessellator = Tessellator.instance;

        int[] tl = index[TL];
        int[] bl = index[BL];
        int[] br = index[BR];
        int[] tr = index[TR];

        tessellator.setColorOpaque_F(colorTopLeft[0], colorTopLeft[1], colorTopLeft[2]);
        tessellator.setBrightness(brightnessTopLeft);
        tessellator.addVertexWithUV(xyz[tl[0]], xyz[tl[1]], xyz[tl[2]], uv[tl[3]], uv[tl[4]]);

        tessellator.setColorOpaque_F(colorBottomLeft[0], colorBottomLeft[1], colorBottomLeft[2]);
        tessellator.setBrightness(brightnessBottomLeft);
        tessellator.addVertexWithUV(xyz[bl[0]], xyz[bl[1]], xyz[bl[2]], uv[bl[3]], uv[bl[4]]);

        tessellator.setColorOpaque_F(colorBottomRight[0], colorBottomRight[1], colorBottomRight[2]);
        tessellator.setBrightness(brightnessBottomRight);
        tessellator.addVertexWithUV(xyz[br[0]], xyz[br[1]], xyz[br[2]], uv[br[3]], uv[br[4]]);

        tessellator.setColorOpaque_F(colorTopRight[0], colorTopRight[1], colorTopRight[2]);
        tessellator.setBrightness(brightnessTopRight);
        tessellator.addVertexWithUV(xyz[tr[0]], xyz[tr[1]], xyz[tr[2]], uv[tr[3]], uv[tr[4]]);
    }
}
