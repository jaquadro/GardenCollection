package com.jaquadro.minecraft.modularpots.tileentity;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.block.LargePotPlantProxy;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLargePot extends TileEntity
{
    private static final int DEFAULT_BIOME_DATA = 65407;

    private Item flowerPotItem;
    private int flowerPotData;
    private Item substrate;
    private int substrateData;
    private int substrateOrigData;

    private boolean hasBiomeOverride;
    private int biomeData = DEFAULT_BIOME_DATA;

    public TileEntityLargePot () { }

    public TileEntityLargePot (Item item, int itemData) {
        this.flowerPotItem = item;
        this.flowerPotData = itemData;
    }

    public Item getFlowerPotItem () {
        return flowerPotItem;
    }

    public int getFlowerPotData () {
        return flowerPotData;
    }

    public Item getSubstrate () {
        return substrate;
    }

    public int getSubstrateData () {
        return substrateData;
    }

    public int getSubstrateOriginalData () {
        return substrateOrigData;
    }

    public boolean hasBiomeDataOverride () {
        return hasBiomeOverride;
    }

    public int getBiomeData () {
        return biomeData;
    }

    public float getBiomeTemperature () {
        return (biomeData & 255) / 255f;
    }

    public float getBiomeHumidity () {
        return ((biomeData >> 8) & 255) / 255f;
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (flowerPotItem != null)
            tag.setInteger("Item", flowerPotItem.itemID);
        if (flowerPotData != 0)
            tag.setShort("ItemD", (short) flowerPotData);
        if (substrate != null)
            tag.setInteger("Subs", substrate.itemID);
        if (substrateData != 0)
            tag.setShort("SubsD", (short) substrateData);
        if (substrateOrigData != 0)
            tag.setShort("SubsO", (short) substrateOrigData);
        if (hasBiomeOverride || biomeData != DEFAULT_BIOME_DATA)
            tag.setInteger("Biom", biomeData);
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        flowerPotItem = null;
        flowerPotData = 0;
        substrate = null;
        substrateData = 0;

        if (tag.hasKey("Item")) {
            int id = tag.getInteger("Item");
            if (id <= 0)
                flowerPotItem = null;
            else
                flowerPotItem = Item.itemsList[id];

            flowerPotData = tag.hasKey("ItemD") ? tag.getShort("ItemD") : 0;
        }

        if (tag.hasKey("Subs")) {
            int id = tag.getInteger("Subs");
            if (id <= 0)
                substrate = null;
            else
                substrate = Item.itemsList[id];

            substrateData = tag.hasKey("SubsD") ? tag.getShort("SubsD") : 0;
            substrateOrigData = tag.hasKey("SubsO") ? tag.getShort("SubsO") : 0;
        }

        hasBiomeOverride = tag.hasKey("Biom");
        biomeData = tag.hasKey("Biom") ? tag.getInteger("Biom") : DEFAULT_BIOME_DATA;
    }

    @Override
    public Packet getDescriptionPacket () {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void onDataPacket (INetworkManager net, Packet132TileEntityData pkt) {
        readFromNBT(pkt.data);
        getWorldObj().markBlockForRenderUpdate(xCoord, yCoord, zCoord);

        int y = yCoord;
        while (getWorldObj().getBlockId(xCoord, ++y, zCoord) == ModularPots.largePotPlantProxyId)
            getWorldObj().markBlockForRenderUpdate(xCoord, y, zCoord);
    }

    public void setItem (Item item, int itemData) {
        this.flowerPotItem = item;
        this.flowerPotData = itemData;
    }

    public void setSubstrate (Item item, int itemData) {
        this.substrate = item;
        this.substrateData = itemData;
    }
      
    public void setSubstrate (Item item, int itemData, int origData) {
        this.substrate = item;
        this.substrateData = itemData;
        this.substrateOrigData = origData;
    }

    public void setBiomeData (int data) {
        this.biomeData = data;
        this.hasBiomeOverride = true;
    }

    /*public void markDirty()
    {
        if (this.worldObj != null)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, this);

            if (this.getBlockType().blockID != 0)
            {
                this.worldObj.func_147453_f(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            }
        }
    }*/
}
