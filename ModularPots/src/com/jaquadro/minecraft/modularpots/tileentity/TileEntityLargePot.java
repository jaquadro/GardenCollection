package com.jaquadro.minecraft.modularpots.tileentity;

import com.jaquadro.minecraft.modularpots.block.LargePotPlantProxy;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityLargePot extends TileEntity
{
    private Item flowerPotItem;
    private int flowerPotData;
    private Item substrate;
    private int substrateData;
    private int substrateOrigData;

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
        while (getWorldObj().getBlock(xCoord, ++y, zCoord) instanceof LargePotPlantProxy)
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
}
