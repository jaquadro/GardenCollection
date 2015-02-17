package com.jaquadro.minecraft.gardencontainers.block.tile;

import com.jaquadro.minecraft.gardencontainers.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLargePot extends TileEntityGarden
{
    private int carving;

    @Override
    protected int containerSlotCount () {
        return ModBlocks.largePot.getSlotProfile().getPlantSlots().length;
    }

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
