package com.jaquadro.minecraft.gardenapi.api.connect;

import net.minecraft.world.IBlockAccess;

public class StandardAttachable implements IAttachable
{
    boolean[] valid = new boolean[6];
    double[] faceDepths = new double[6];

    @Override
    public boolean isAttachable (IBlockAccess blockAccess, int x, int y, int z, int side) {
        return valid[side];
    }

    @Override
    public double getAttachDepth (IBlockAccess blockAccess, int x, int y, int z, int side) {
        return faceDepths[side];
    }

    public static StandardAttachable createBottom (double bottom) {
        StandardAttachable attachable = new StandardAttachable();
        attachable.faceDepths[0] = bottom;
        attachable.valid[0] = true;
        return attachable;
    }

    public static StandardAttachable createTop (double top) {
        StandardAttachable attachable = new StandardAttachable();
        attachable.faceDepths[1] = top;
        attachable.valid[1] = true;
        return attachable;
    }

    public static StandardAttachable createSide (double side) {
        StandardAttachable attachable = new StandardAttachable();
        for (int i = 2; i < 6; i++) {
            attachable.faceDepths[i] = side;
            attachable.valid[i] = true;
        }
        return attachable;
    }

    public static StandardAttachable create (double bottom, double top, double north, double south, double west, double east) {
        StandardAttachable attachable = new StandardAttachable();
        attachable.faceDepths[0] = bottom;
        attachable.faceDepths[1] = top;
        attachable.faceDepths[2] = north;
        attachable.faceDepths[3] = south;
        attachable.faceDepths[4] = west;
        attachable.faceDepths[5] = east;

        for (int i = 0; i < 6; i++)
            attachable.valid[i] = true;

        return attachable;
    }
}
