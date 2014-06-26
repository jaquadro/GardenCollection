package com.jaquadro.minecraft.gardencore.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityWindowBox extends TileEntityGarden
{
    private static final int[] PLANT_SLOTS = new int[] {
        1, 2, 3, 4, 5
    };

    private static final SlotMapping[][] SLOT_MAP = new SlotMapping[][] {
        null, null, null, null, null
    };

    private int direction;

    public int getDirection () {
        return direction & 7;
    }

    public boolean isUpper () {
        return (direction & 8) != 0;
    }

    public void setDirection (int direction) {
        this.direction |= direction & 7;
        //invalidate();
    }

    public void setUpper (boolean isUpper) {
        direction = direction & 7;
        if (isUpper)
            direction |= 8;
        //invalidate();
    }

    @Override
    protected int containerSlotCount () {
        return 14;
    }

    @Override
    public int[] getPlantSlots () {
        return PLANT_SLOTS;
    }

    @Override
    protected SlotMapping[] getNeighborMappingsForSlot (int slot) {
        return null;
    }

    @Override
    public boolean isSlotValid (int slot) {
        int dir = getDirection();
        if (isAttachedNeighbor(dir)) {
            int facingDir = getNeighborDirection(dir);
            switch (slot) {
                case 2: // Z- X-
                    if (dir == 2 && facingDir == 5)
                        return false;
                    if (dir == 4 && facingDir == 3)
                        return false;
                    break;
                case 3: // Z- X+
                    if (dir == 2 && facingDir == 4)
                        return false;
                    if (dir == 5 && facingDir == 3)
                        return false;
                    break;
                case 4: // Z+ X-
                    if (dir == 3 && facingDir == 5)
                        return false;
                    if (dir == 4 && facingDir == 2)
                        return false;
                    break;
                case 5: // Z+ X+
                    if (dir == 3 && facingDir == 4)
                        return false;
                    if (dir == 5 && facingDir == 2)
                        return false;
                    break;
            }
        }

        switch (slot) {
            case 1:
                return true;
            case 2: // Z- X-
                if (dir == 2 || dir == 4)
                    return true;
            case 3: // Z- X+
                if (dir == 2 || dir == 5)
                    return true;
            case 4: // Z+ X-
                if (dir == 3 || dir == 4)
                    return true;
            case 5: // Z+ X+
                if (dir == 3 || dir == 5)
                    return  true;
        }

        return false;
    }

    private int getNeighborDirection (int direction) {
        TileEntity te = null;
        if (direction == 2)
            te = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
        else if (direction == 3)
            te = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
        else if (direction == 4)
            te = worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
        else if (direction == 5)
            te = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);

        if (te instanceof TileEntityWindowBox) {
            TileEntityWindowBox wb = (TileEntityWindowBox) te;
            return wb.getDirection();
        }

        return -1;
    }

    private int neighborToDirection (int x, int z) {
        if (z < zCoord)
            return 2;
        if (z > zCoord)
            return 3;
        if (x < xCoord)
            return 4;
        if (x > xCoord)
            return 5;

        return -1;
    }

    private boolean isAttachedNeighbor (int direction) {
        switch (direction) {
            case 2:
                return isAttachedNeighbor(xCoord, yCoord, zCoord - 1);
            case 3:
                return isAttachedNeighbor(xCoord, yCoord, zCoord + 1);
            case 4:
                return isAttachedNeighbor(xCoord - 1, yCoord, zCoord);
            case 5:
                return isAttachedNeighbor(xCoord + 1, yCoord, zCoord);
            default:
                return false;
        }
    }

    @Override
    public boolean isAttachedNeighbor (int x, int y, int z) {
        if (!super.isAttachedNeighbor(x, y, z))
            return false;

        TileEntityWindowBox nte = (TileEntityWindowBox)worldObj.getTileEntity(x, y, z);
        if (nte == null)
            return false;

        if (isUpper() != nte.isUpper())
            return false;

        int dir = getDirection();
        int ndir = nte.getDirection();

        if (dir == neighborToDirection(x, z)) {
            switch (dir) {
                case 2:
                case 3:
                    return ndir == 4 || ndir == 5;
                case 4:
                case 5:
                    return ndir == 2 || ndir == 3;
            }
        }
        else {
            TileEntity te = null;
            if (dir == 2)
                te = worldObj.getTileEntity(x, y, z - 1);
            else if (dir == 3)
                te = worldObj.getTileEntity(x, y, z + 1);
            else if (dir == 4)
                te = worldObj.getTileEntity(x - 1, y, z);
            else if (dir == 5)
                te = worldObj.getTileEntity(x + 1, y, z);

            if (te instanceof TileEntityWindowBox) {
                TileEntityWindowBox dte = (TileEntityWindowBox) te;
                int ddir = dte.getDirection();

                if ((dir == 2 || dir == 3) && (ddir == 4 || ddir == 5))
                    return ddir == neighborToDirection(x, z);
                if ((dir == 4 || dir == 5) && (ddir == 2 || ddir == 3))
                    return ddir == neighborToDirection(x, z);
            }
        }

        switch (dir) {
            case 2:
            case 3:
                if (x - xCoord != 0 && ndir == dir)
                    break;
                if (x - xCoord < 0 && ndir == 4)
                    break;
                if (x - xCoord > 0 && ndir == 5)
                    break;
                return false;
            case 4:
            case 5:
                if (z - zCoord != 0 && ndir == dir)
                    break;
                if (z - zCoord < 0 && ndir == 2)
                    break;
                if (z - zCoord > 0 && ndir == 3)
                    break;
                return false;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        direction = 0;
        if (tag.hasKey("Dir"))
            direction = tag.getByte("Dir");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setByte("Dir", (byte)direction);
    }
}
