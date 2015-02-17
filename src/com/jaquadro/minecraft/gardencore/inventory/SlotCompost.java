package com.jaquadro.minecraft.gardencore.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCompost extends Slot
{
    public SlotCompost (IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition) {
        super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
    }

    @Override
    public boolean isItemValid (ItemStack itemStack) {
        return inventory.isItemValidForSlot(getSlotIndex(), itemStack);
    }
}
