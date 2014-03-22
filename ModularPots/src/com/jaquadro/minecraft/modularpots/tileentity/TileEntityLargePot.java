package com.jaquadro.minecraft.modularpots.tileentity;

import com.jaquadro.minecraft.modularpots.block.BlockLargePotPlantProxy;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLargePot extends TileEntity
{
    private static final int DEFAULT_BIOME_DATA = 65407;

    private Item flowerPotItem;
    private int flowerPotData;
    private Item substrate;
    private int substrateData;
    private int substrateOrigData;
    private int carving;

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

    public int getCarving () {
        return carving;
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
            tag.setString("Item", Item.itemRegistry.getNameForObject(flowerPotItem));
        if (flowerPotData != 0)
            tag.setShort("ItemD", (short) flowerPotData);
        if (substrate != null)
            tag.setString("Subs", Item.itemRegistry.getNameForObject(substrate));
        if (substrateData != 0)
            tag.setShort("SubsD", (short) substrateData);
        if (substrateOrigData != 0)
            tag.setShort("SubsO", (short) substrateOrigData);
        if (hasBiomeOverride || biomeData != DEFAULT_BIOME_DATA)
            tag.setInteger("Biom", biomeData);
        if (carving != 0)
            tag.setShort("Carv", (short) carving);
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        flowerPotItem = null;
        flowerPotData = 0;
        substrate = null;
        substrateData = 0;

        if (tag.hasKey("Item")) {
            String itemString = tag.getString("Item");
            if (itemString == null || itemString.equals(""))
                flowerPotItem = null;
            else
                flowerPotItem = (Item)Item.itemRegistry.getObject(itemString);

            flowerPotData = tag.hasKey("ItemD") ? tag.getShort("ItemD") : 0;
        }

        if (tag.hasKey("Subs")) {
            String substrateString = tag.getString("Subs");
            if (substrateString == null || substrateString.equals(""))
                substrate = null;
            else
                substrate = (Item)Item.itemRegistry.getObject(substrateString);

            substrateData = tag.hasKey("SubsD") ? tag.getShort("SubsD") : 0;
            substrateOrigData = tag.hasKey("SubsO") ? tag.getShort("SubsO") : 0;
        }

        hasBiomeOverride = tag.hasKey("Biom");
        biomeData = tag.hasKey("Biom") ? tag.getInteger("Biom") : DEFAULT_BIOME_DATA;

        carving = tag.hasKey("Carv") ? tag.getShort("Carv") : 0;
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

        int y = yCoord;
        while (getWorldObj().getBlock(xCoord, ++y, zCoord) instanceof BlockLargePotPlantProxy)
            getWorldObj().func_147479_m(xCoord, y, zCoord);
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

    public void setCarving (int id) {
        carving = id;
    }
}
