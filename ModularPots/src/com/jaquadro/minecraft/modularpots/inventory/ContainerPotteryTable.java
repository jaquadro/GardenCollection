package com.jaquadro.minecraft.modularpots.inventory;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.item.crafting.PotteryManager;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityPotteryTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerPotteryTable extends Container
{
    private static final int InventoryX = 10;
    private static final int InventoryY = 140;
    private static final int HotbarY = 198;

    private static final int StorageX1 = 26;
    private static final int StorageY1 = 18;
    private static final int StorageX2 = 138;
    private static final int StorageY2 = StorageY1;

    private IInventory tableInventory;
    private IInventory craftResult = new InventoryCraftResult();

    public ContainerPotteryTable (InventoryPlayer inventory, TileEntityPotteryTable tileEntity) {
        tableInventory = new InventoryPottery(tileEntity, this);

        addSlotToContainer(new Slot(tableInventory, 0, 50, 44));
        addSlotToContainer(new Slot(tableInventory, 1, 50, 80));
        addSlotToContainer(new SlotPottery(inventory.player, tableInventory, craftResult, 2, 110, 62));

        for (int i = 0; i < 6; i++)
            addSlotToContainer(new Slot(tableInventory, 3 + i, StorageX1, StorageY1 + i * 18));

        for (int i = 0; i < 6; i++)
            addSlotToContainer(new Slot(tableInventory, 9 + i, StorageX2, StorageY2 + i * 18));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new Slot(inventory, j + i * 9 + 9, InventoryX + j * 18, InventoryY + i * 18));
        }

        for (int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(inventory, i, InventoryX + i * 18, HotbarY));
    }

    @Override
    public boolean canInteractWith (EntityPlayer player) {
        return tableInventory.isUseableByPlayer(player);
    }

    @Override
    public void onCraftMatrixChanged (IInventory inventory) {
        ItemStack pattern = tableInventory.getStackInSlot(0);
        ItemStack target = tableInventory.getStackInSlot(1);

        if (pattern == null && target != null && target.getItem() == Item.getItemFromBlock(Blocks.clay)) {
            craftResult.setInventorySlotContents(0, new ItemStack(ModularPots.largePot, 1, 1));
            return;
        }

        craftResult.setInventorySlotContents(0, PotteryManager.instance().getStampResult(pattern, target));
    }

    @Override
    public ItemStack transferStackInSlot (EntityPlayer player, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (slotIndex == 2) {
                if (!mergeItemStack(slotStack, 3, 39, true))
                    return null;
                slot.onSlotChange(slotStack, itemStack);
            }
            else if (slotIndex > 2) {
                if (slotIndex >= 3 && slotIndex < 30) {
                    if (!mergeItemStack(slotStack, 30, 39, false))
                        return null;
                }
                else if (slotIndex >= 30 && slotIndex < 39 && !this.mergeItemStack(slotStack, 3, 30, false))
                    return null;
            }
            else if (!mergeItemStack(slotStack, 3, 39, false))
                return null;

            if (slotStack.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();

            if (slotStack.stackSize == itemStack.stackSize)
                return null;

            slot.onPickupFromSlot(player, slotStack);
        }

        return itemStack;
    }
}
