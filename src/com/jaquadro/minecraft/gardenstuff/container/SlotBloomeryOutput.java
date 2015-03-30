package com.jaquadro.minecraft.gardenstuff.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBloomeryOutput extends Slot
{
    private final IInventory inputInventory;
    private EntityPlayer player;
    private int amountCrafted;

    public SlotBloomeryOutput (EntityPlayer player, IInventory inputInventory, int par2, int par3, int par4) {
        super(inputInventory, par2, par3, par4);

        this.player = player;
        this.inputInventory = inputInventory;
    }

    @Override
    public boolean isItemValid (ItemStack itemStack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize (int count) {
        if (getHasStack())
            amountCrafted += Math.min(count, getStack().stackSize);

        return super.decrStackSize(count);
    }

    @Override
    protected void onCrafting (ItemStack itemStack, int count) {
        amountCrafted += count;
        super.onCrafting(itemStack, count);
    }

    @Override
    protected void onCrafting (ItemStack itemStack) {
        itemStack.onCrafting(player.worldObj, player, amountCrafted);
        amountCrafted = 0;
    }

    @Override
    public void onPickupFromSlot (EntityPlayer player, ItemStack itemStack) {
        onCrafting(itemStack);
        super.onPickupFromSlot(player, itemStack);
    }
}
