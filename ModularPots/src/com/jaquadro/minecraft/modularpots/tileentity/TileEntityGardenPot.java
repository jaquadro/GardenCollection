package com.jaquadro.minecraft.modularpots.tileentity;

// Slot lookup:
// base + 0: Pot Substrate

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityGardenPot extends TileEntityGarden
{
    private static final int DEFAULT_BIOME_DATA = 65407;

    private ItemStack substrate;
    private ItemStack substrateOrig;
    private int carving;

    private boolean hasBiomeOverride;
    private int biomeData = DEFAULT_BIOME_DATA;

    public ItemStack getSubstrate () {
        return substrate;
    }

    public ItemStack getSubstrateOrig () {
        return substrateOrig;
    }

    public int getCarving () {
        return carving;
    }

    public boolean isHasBiomeOverride () {
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

    public void setSubstrate (Item item, int itemData) {
        if (substrate == null || substrate.getItem() != item)
            substrate = new ItemStack(item, 1, itemData);
        else
            substrate.setItemDamage(itemData);
    }

    public void setSubstrate (Item item, int itemData, int origData) {
        setSubstrate(item, itemData);
        if (substrateOrig == null || substrateOrig.getItem() != item)
            substrateOrig = new ItemStack(item, 1, origData);
        else
            substrateOrig.setItemDamage(origData);
    }

    public void setBiomeData (int data) {
        biomeData = data;
        hasBiomeOverride = true;
    }

    public void setCarving (int id) {
        carving = id;
    }

    @Override
    protected int containerSlotCount () {
        return super.containerSlotCount() + 1;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        substrate = null;
        substrateOrig = null;

        if (tag.hasKey("Subs")) {
            Item substrateItem = null;
            int substrateData = tag.hasKey("SubsD") ? tag.getShort("SubsD") : 0;
            int substrateOrigData = tag.hasKey("SubsO") ? tag.getShort("SubsO") : substrateData;

            String substrateString = tag.getString("Subs");
            if (substrateString != null && !substrateString.equals(""))
                substrateItem = (Item)Item.itemRegistry.getObject(substrateString);

            if (substrateItem != null) {
                substrate = new ItemStack(substrateItem, 1, substrateData);
                substrateOrig = new ItemStack(substrateItem, 1, substrateOrigData);
            }
        }

        hasBiomeOverride = tag.hasKey("Biom");
        biomeData = tag.hasKey("Biom") ? tag.getInteger("Biom") : DEFAULT_BIOME_DATA;

        carving = tag.hasKey("Carv") ? tag.getShort("Carv") : 0;
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (substrate != null) {
            tag.setString("Subs", Item.itemRegistry.getNameForObject(substrate.getItem()));
            if (substrate.getItemDamage() != 0)
                tag.setShort("SubsD", (short) substrate.getItemDamage());

            if (substrateOrig != null) {
                if (substrateOrig.getItemDamage() != substrate.getItemDamage())
                    tag.setShort("SubsO", (short) substrateOrig.getItemDamage());
            }
        }

        if (hasBiomeOverride || biomeData != DEFAULT_BIOME_DATA)
            tag.setInteger("Biom", biomeData);
        if (carving != 0)
            tag.setShort("Carv", (short) carving);
    }
}
