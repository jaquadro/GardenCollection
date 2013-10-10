package com.jaquadro.minecraft.extrabuttons.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDelayButton extends TileEntity
{
    public byte metadata = 0;
    public byte delayLevel = 0;
    public byte stateLevel = 0;
    public long chainUpdate = 0;

    @Override
    public void writeToNBT (NBTTagCompound tag)
    {
        super.writeToNBT(tag);

        tag.setByte("meta", this.metadata);
        tag.setByte("delay", this.delayLevel);
        tag.setByte("state", this.stateLevel);

        if (chainUpdate > 0)
            tag.setLong("update", this.chainUpdate);
    }

    @Override
    public void readFromNBT (NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        this.metadata = tag.getByte("meta");
        this.delayLevel = tag.getByte("delay");
        this.stateLevel = tag.getByte("state");

        if (tag.hasKey("update"))
            this.chainUpdate = tag.getLong("update");
    }

    public int getDirection ()
    {
        return metadata & 0x7;
    }

    public void setDirection (int direction)
    {
        metadata = (byte) ((metadata & ~0x7) | (direction & 0x7));
    }

    public boolean isLatched ()
    {
        return (metadata & 0x8) != 0;
    }

    public void setIsLatched (boolean latched)
    {
        metadata = (byte) ((metadata & ~0x8) | (latched ? 0x8 : 0));
    }

    public boolean isDepressed ()
    {
        return (metadata & 0x10) != 0;
    }

    public void setIsDepressed (boolean depressed)
    {
        metadata = (byte) ((metadata & ~0x10) | (depressed ? 0x10 : 0));
    }

    public boolean isShowingDelay ()
    {
        return (metadata & 0x20) != 0;
    }

    public void setShowingDelay (boolean showDelay)
    {
        metadata = (byte) ((metadata & ~0x20) | (showDelay ? 0x20 : 0));
    }

    public int getDelay ()
    {
        return delayLevel;
    }

    public void setDelay (int delay)
    {
        delayLevel = (byte)delay;
    }

    public int getState ()
    {
        return stateLevel;
    }

    public void setState (int state)
    {
        stateLevel = (byte)state;
    }

    public long getUpdateTime ()
    {
        return chainUpdate;
    }

    public void setUpdateTime (long time)
    {
        chainUpdate = time;
    }

    @Override
    public Packet getDescriptionPacket ()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    @Override
    public void onDataPacket (INetworkManager netManager, Packet132TileEntityData packet)
    {
        readFromNBT(packet.data);

        getWorldObj().markBlockForRenderUpdate(xCoord, yCoord, zCoord);
    }
}
