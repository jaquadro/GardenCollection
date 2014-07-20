package com.jaquadro.minecraft.gardencontainers.block.tile;

import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.support.Slot5Profile;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWindowBox extends TileEntityGarden
{
    @Override
    protected int containerSlotCount () {
        return 6;
    }

    private int direction;

    public int getDirection () {
        return direction & 7;
    }

    public boolean isUpper () {
        return (direction & 8) != 0;
    }

    public void setDirection (int direction) {
        this.direction = (this.direction & 8) | (direction & 7);
        //invalidate();
    }

    public void setUpper (boolean isUpper) {
        direction = direction & 7;
        if (isUpper)
            direction |= 8;
        //invalidate();
    }

    @Override
    public boolean isSlotValid (int slot) {
        int dir = getDirection();

        BlockGarden garden = getGardenBlock();
        if (garden == null)
            return false;

        // Outer corner check
        if (garden.getConnectionProfile().isAttachedNeighbor(worldObj, xCoord, yCoord, zCoord, dir)) {
            int facingDir = getNeighborDirection(dir);
            switch (slot) {
                case Slot5Profile.SLOT_NW: // Z- X-
                    if (dir == 2 && facingDir == 5)
                        return false;
                    if (dir == 4 && facingDir == 3)
                        return false;
                    break;
                case Slot5Profile.SLOT_NE: // Z- X+
                    if (dir == 2 && facingDir == 4)
                        return false;
                    if (dir == 5 && facingDir == 3)
                        return false;
                    break;
                case Slot5Profile.SLOT_SW: // Z+ X-
                    if (dir == 3 && facingDir == 5)
                        return false;
                    if (dir == 4 && facingDir == 2)
                        return false;
                    break;
                case Slot5Profile.SLOT_SE: // Z+ X+
                    if (dir == 3 && facingDir == 4)
                        return false;
                    if (dir == 5 && facingDir == 2)
                        return false;
                    break;
            }
        }

        int rdir = (dir % 2 == 0) ? dir + 1 : dir - 1;

        // Inner corner check
        if (garden.getConnectionProfile().isAttachedNeighbor(worldObj, xCoord, yCoord, zCoord, rdir)) {
            int facingDir = getNeighborDirection(rdir);
            switch (slot) {
                case Slot5Profile.SLOT_NW: // Z- X-
                    if (dir == 3 && facingDir == 4)
                        return true;
                    if (dir == 5 && facingDir == 2)
                        return true;
                    break;
                case Slot5Profile.SLOT_NE: // Z- X+
                    if (dir == 3 && facingDir == 5)
                        return true;
                    if (dir == 4 && facingDir == 2)
                        return true;
                    break;
                case Slot5Profile.SLOT_SW: // Z+ X-
                    if (dir == 2 && facingDir == 4)
                        return true;
                    if (dir == 5 && facingDir == 3)
                        return true;
                    break;
                case Slot5Profile.SLOT_SE: // Z+ X+
                    if (dir == 2 && facingDir == 5)
                        return true;
                    if (dir == 4 && facingDir == 3)
                        return true;
                    break;
            }
        }

        switch (slot) {
            case Slot5Profile.SLOT_COVER:
                return true;
            case Slot5Profile.SLOT_NW: // Z- X-
                if (dir == 2 || dir == 4)
                    return true;
                break;
            case Slot5Profile.SLOT_NE: // Z- X+
                if (dir == 2 || dir == 5)
                    return true;
                break;
            case Slot5Profile.SLOT_SW: // Z+ X-
                if (dir == 3 || dir == 4)
                    return true;
                break;
            case Slot5Profile.SLOT_SE: // Z+ X+
                if (dir == 3 || dir == 5)
                    return  true;
                break;
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
