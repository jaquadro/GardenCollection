package com.jaquadro.minecraft.modularpots.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryPottery implements IInventory
{
    private IInventory parent;
    private Container container;

    public InventoryPottery (IInventory parentInventory, Container container) {
        parent = parentInventory;
        this.container = container;
    }

    @Override
    public int getSizeInventory () {
        return parent.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot (int var1) {
        return parent.getStackInSlot(var1);
    }

    @Override
    public ItemStack decrStackSize (int slot, int count) {
        ItemStack stack = parent.getStackInSlot(slot);
        if (stack == null)
            return null;

        int stackCount = stack.stackSize;
        ItemStack result = parent.decrStackSize(slot, count);

        ItemStack stackAfter = parent.getStackInSlot(slot);
        if (stack != stackAfter || stackCount != stackAfter.stackSize)
            container.onCraftMatrixChanged(this);

        return result;
    }

    @Override
    public ItemStack getStackInSlotOnClosing (int var1) {
        return parent.getStackInSlotOnClosing(var1);
    }

    @Override
    public void setInventorySlotContents (int var1, ItemStack var2) {
        parent.setInventorySlotContents(var1, var2);
        container.onCraftMatrixChanged(this);
    }

    @Override
    public String getInventoryName () {
        return parent.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName () {
        return parent.hasCustomInventoryName();
    }

    @Override
    public int getInventoryStackLimit () {
        return parent.getInventoryStackLimit();
    }

    @Override
    public void markDirty () {
        parent.markDirty();
    }

    @Override
    public boolean isUseableByPlayer (EntityPlayer var1) {
        return parent.isUseableByPlayer(var1);
    }

    @Override
    public void openInventory () {
        parent.openInventory();
    }

    @Override
    public void closeInventory () {
        parent.closeInventory();
    }

    @Override
    public boolean isItemValidForSlot (int var1, ItemStack var2) {
        return parent.isItemValidForSlot(var1, var2);
    }
}
