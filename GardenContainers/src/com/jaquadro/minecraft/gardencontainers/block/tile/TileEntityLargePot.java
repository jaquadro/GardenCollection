package com.jaquadro.minecraft.gardencontainers.block.tile;

import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenConnected;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityLargePot extends TileEntityGardenConnected
{
    private int carving;

    public int getCarving () {
        return carving;
    }

    public void setCarving (int id) {
        carving = id;
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (carving != 0)
            tag.setShort("Carv", (short) carving);
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        carving = tag.hasKey("Carv") ? tag.getShort("Carv") : 0;
    }
}
