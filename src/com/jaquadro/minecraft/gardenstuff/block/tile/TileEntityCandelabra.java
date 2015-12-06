package com.jaquadro.minecraft.gardenstuff.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

public class TileEntityCandelabra extends TileEntity
{
    private int direction;
    private int level;

    public boolean isDirectionInitialized () {
        return direction > 0;
    }

    public int getDirection () {
        return MathHelper.clamp_int(direction & 7, 2, 5);
    }

    public void setDirection (int direction) {
        this.direction = direction & 7;
    }

    public boolean isSconce () {
        return (direction & 8) != 0;
    }

    public void setSconce (boolean isSconce) {
        direction = direction & 7;
        if (isSconce)
            direction |= 8;
    }

    public int getLevel () {
        return level;
    }

    public void setLevel (int level) {
        this.level = level;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        direction = 0;
        if (tag.hasKey("Dir"))
            direction = tag.getByte("Dir");

        level = tag.getByte("Lev");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setByte("Dir", (byte)direction);
        tag.setByte("Lev", (byte)level);
    }

    @Override
    public Packet getDescriptionPacket () {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 5, tag);
    }

    @Override
    public void onDataPacket (NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
        getWorldObj().func_147479_m(xCoord, yCoord, zCoord); // markBlockForRenderUpdate
    }
}
