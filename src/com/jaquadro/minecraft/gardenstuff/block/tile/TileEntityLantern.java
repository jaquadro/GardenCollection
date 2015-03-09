package com.jaquadro.minecraft.gardenstuff.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLantern extends TileEntity
{
    public enum LightSource {
        NONE,
        TORCH,
        REDSTONE_TORCH,
        GLOWSTONE,
        CANDLE,
        FIREFLY;
    }

    private boolean hasGlass;
    private LightSource source = LightSource.NONE;

    public void setHasGlass (boolean hasGlass) {
        this.hasGlass = hasGlass;
    }

    public boolean hasGlass () {
        return hasGlass;
    }

    public void setLightSource (LightSource source) {
        this.source = source;
    }

    public LightSource getLightSource () {
        return source;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        hasGlass = false;
        source = LightSource.NONE;

        if (tag.hasKey("Glas"))
            hasGlass = tag.getBoolean("Glas");
        if (tag.hasKey("Src")) {
            LightSource[] values = LightSource.values();
            int index = tag.getByte("Src");

            if (index >= 0 && index < values.length)
                source = values[index];
        }
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (hasGlass)
            tag.setBoolean("Glas", true);
        if (source != null && source != LightSource.NONE)
            tag.setByte("Src", (byte)source.ordinal());
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
