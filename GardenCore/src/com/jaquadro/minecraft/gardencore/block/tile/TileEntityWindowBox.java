package com.jaquadro.minecraft.gardencore.block.tile;

import net.minecraft.nbt.NBTTagCompound;
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
    }

    public void setUpper (boolean isUpper) {
        direction = direction & 7;
        if (isUpper)
            direction |= 8;
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
