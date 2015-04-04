package com.jaquadro.minecraft.gardenstuff.block.tile;

import com.jaquadro.minecraft.gardenapi.api.component.ILanternSourceRegistry;
import com.jaquadro.minecraft.gardenapi.internal.Api;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityLantern extends TileEntity
{
    public enum LightSource {
        NONE,
        TORCH,
        REDSTONE_TORCH,
        GLOWSTONE,
        CANDLE,
        FIREFLY,
    }

    private boolean hasGlass;
    private String source;
    private int sourceMeta = 0;

    public void setHasGlass (boolean hasGlass) {
        this.hasGlass = hasGlass;
    }

    public boolean hasGlass () {
        return hasGlass;
    }

    public void setLightSource (String source) {
        this.source = source;
    }

    public String getLightSource () {
        return source;
    }

    public void setLightSourceMeta (int meta) {
        sourceMeta = meta;
    }

    public int getLightSourceMeta () {
        return sourceMeta;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        hasGlass = false;
        source = null;

        if (tag.hasKey("Glas"))
            hasGlass = tag.getBoolean("Glas");

        if (tag.hasKey("Src", Constants.NBT.TAG_BYTE)) {
            LightSource[] values = LightSource.values();
            int index = tag.getByte("Src");

            LightSource legacySource = LightSource.NONE;
            if (index >= 0 && index < values.length)
                legacySource = values[index];

            switch (legacySource) {
                case TORCH: source = "torch"; break;
                case REDSTONE_TORCH: source = "redstoneTorch"; break;
                case GLOWSTONE: source = "glowstone"; break;
                case FIREFLY: source = "firefly"; break;
            }
        }
        else if (tag.hasKey("Src", Constants.NBT.TAG_STRING))
            source = tag.getString("Src");

        if (tag.hasKey("SrcMeta"))
            sourceMeta = tag.getShort("SrcMeta");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (hasGlass)
            tag.setBoolean("Glas", true);

        if (source != null)
            tag.setString("Src", source);

        if (sourceMeta != 0)
            tag.setShort("SrcMeta", (short)sourceMeta);
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
