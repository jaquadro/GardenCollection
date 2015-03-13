package com.jaquadro.minecraft.gardencontainers.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPotteryTable extends TileEntity implements IInventory
{
    private ItemStack[] tableItemStacks = new ItemStack[3 + 12];

    private String customName;

    @Override
    public int getSizeInventory () {
        return tableItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot (int slot) {
        return tableItemStacks[slot];
    }

    @Override
    public ItemStack decrStackSize (int slot, int count) {
        if (tableItemStacks[slot] != null) {
            if (tableItemStacks[slot].stackSize <= count) {
                ItemStack stack = tableItemStacks[slot];
                tableItemStacks[slot] = null;
                markDirty();
                return stack;
            }
            else {
                ItemStack stack = tableItemStacks[slot].splitStack(count);
                if (tableItemStacks[slot].stackSize == 0)
                    tableItemStacks[slot] = null;

                markDirty();
                return stack;
            }
        }
        else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing (int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents (int slot, ItemStack itemStack) {
        tableItemStacks[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            itemStack.stackSize = getInventoryStackLimit();

        markDirty();
    }

    @Override
    public String getInventoryName () {
        return hasCustomInventoryName() ? customName : "container.gardencontainers.potteryTable";
    }

    @Override
    public boolean hasCustomInventoryName () {
        return customName != null && customName.length() > 0;
    }

    public void setCustomName (String name) {
        customName = name;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList itemList = tag.getTagList("Items", 10);
        tableItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound item = itemList.getCompoundTagAt(i);
            byte slot = item.getByte("Slot");

            if (slot >= 0 && slot < tableItemStacks.length)
                tableItemStacks[slot] = ItemStack.loadItemStackFromNBT(item);
        }

        if (tag.hasKey("CustomName", 8))
            customName = tag.getString("CustomName");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < tableItemStacks.length; i++) {
            if (tableItemStacks[i] != null) {
                NBTTagCompound item = new NBTTagCompound();
                item.setByte("Slot", (byte)i);
                tableItemStacks[i].writeToNBT(item);
                itemList.appendTag(item);
            }
        }

        tag.setTag("Items", itemList);

        if (hasCustomInventoryName())
            tag.setString("CustomName", customName);
    }

    @Override
    public int getInventoryStackLimit () {
        return 64;
    }

    public static boolean isItemValidTool (ItemStack itemStack) {
        return false;
    }

    public static boolean isItemValidPot (ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isUseableByPlayer (EntityPlayer player) {
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
            return false;

        return player.getDistanceSq(xCoord + .5, yCoord + .5, zCoord + .5) <= 64;
    }

    @Override
    public void openInventory () { }

    @Override
    public void closeInventory () { }

    @Override
    public boolean isItemValidForSlot (int slot, ItemStack itemStack) {
        if (slot == 2)
            return false;
        if (slot == 0)
            return isItemValidTool(itemStack);
        if (slot == 1)
            return isItemValidPot(itemStack);

        return true;
    }
}
