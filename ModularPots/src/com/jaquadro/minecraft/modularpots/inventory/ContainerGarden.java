package com.jaquadro.minecraft.modularpots.inventory;

import com.jaquadro.minecraft.modularpots.tileentity.TileEntityGarden;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ContainerGarden extends Container
{
    private static final int InventoryX = 8;
    private static final int InventoryY = 122;
    private static final int HotbarY = 180;

    private static final int StorageX = 44;
    private static final int StorageY = 18;
    private static final int SubstrateX = 17;
    private static final int SubstrateY = 86;
    private static final int CoverX = 143;
    private static final int CoverY = SubstrateY;

    private static final int[] storageSlotIndex = new int[] {
        0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13
    };
    private static final int[] storageXIndex = new int[] {
        2, 1, 3, 1, 3, 0, 2, 4, 4, 4, 2, 0, 0
    };
    private static final int[] storageYIndex = new int[] {
        2, 1, 1, 3, 3, 0, 0, 0, 2, 4, 4, 4, 2
    };

    private IInventory gardenInventory;

    private Slot substrateSlot;
    private Slot coverSlot;
    private List<Slot> storageSlots;
    private List<Slot> playerSlots;
    private List<Slot> hotbarSlots;

    public ContainerGarden (InventoryPlayer inventory, TileEntityGarden tileEntity) {
        gardenInventory = new InventoryGarden(tileEntity, this);

        coverSlot = addSlotToContainer(new Slot(gardenInventory, 1, CoverX, CoverY));

        storageSlots = new ArrayList<Slot>();
        for (int i = 0; i < storageSlotIndex.length; i++)
            storageSlots.add(addSlotToContainer(new Slot(gardenInventory, storageSlotIndex[i], StorageX + 18 * storageXIndex[i], StorageY + 18 * storageYIndex[i])));

        playerSlots = new ArrayList<Slot>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++)
                playerSlots.add(addSlotToContainer(new Slot(inventory, j + i * 9 + 9, InventoryX + j * 18, InventoryY + i * 18)));
        }

        hotbarSlots = new ArrayList<Slot>();
        for (int i = 0; i < 9; i++)
            hotbarSlots.add(addSlotToContainer(new Slot(inventory, i, InventoryX + i * 18, HotbarY)));
    }

    @Override
    public boolean canInteractWith (EntityPlayer player) {
        return gardenInventory.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot (EntityPlayer player, int slotIndex) {
        ItemStack itemStack = null;
        Slot slot = (Slot) inventorySlots.get(slotIndex);

        // Assume inventory and hotbar slot IDs are contiguous
        int inventoryStart = playerSlots.get(0).slotNumber;
        int hotbarStart = hotbarSlots.get(0).slotNumber;
        int hotbarEnd = hotbarSlots.get(hotbarSlots.size() - 1).slotNumber + 1;

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            // Try merge stacks within inventory and hotbar spaces
            if (slotIndex >= inventoryStart && slotIndex < hotbarEnd) {
                if (slotIndex >= inventoryStart && slotIndex < hotbarStart) {
                    if (!mergeItemStack(slotStack, hotbarStart, hotbarEnd, false))
                        return null;
                }
                else if (slotIndex >= hotbarStart && slotIndex < hotbarEnd && !this.mergeItemStack(slotStack, inventoryStart, hotbarStart, false))
                    return null;
            }
            // Try merge stack into inventory
            else if (!mergeItemStack(slotStack, inventoryStart, hotbarEnd, false))
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
