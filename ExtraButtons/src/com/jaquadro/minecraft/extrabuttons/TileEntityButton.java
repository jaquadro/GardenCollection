package com.jaquadro.minecraft.extrabuttons;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityButton extends TileEntity
{
    public byte colorIndex = 0;

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setByte("color", this.colorIndex);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        this.colorIndex = tag.getByte("color");

        if (this.colorIndex < 0)
            this.colorIndex = 0;

        if (this.colorIndex > 15)
            this.colorIndex = 15;
    }

    /*@Override
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
        NBTTagCompound tag = pkt.customParam1;
        this.readFromNBT(tag);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
    }*/
}
