package com.jaquadro.minecraft.modularpots.inventory;

import com.jaquadro.minecraft.modularpots.core.ModBlocks;
import com.jaquadro.minecraft.modularpots.item.crafting.PotteryManager;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityPotteryTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

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

    private Slot inputSlot1;
    private Slot inputSlot2;
    private Slot outputSlot;
    private List<Slot> storageSlots;
    private List<Slot> playerSlots;
    private List<Slot> hotbarSlots;

    public ContainerPotteryTable (InventoryPlayer inventory, TileEntityPotteryTable tileEntity) {
        tableInventory = new InventoryPottery(tileEntity, this);

        inputSlot1 = addSlotToContainer(new Slot(tableInventory, 0, 50, 44));
        inputSlot2 = addSlotToContainer(new Slot(tableInventory, 1, 50, 80));
        outputSlot = addSlotToContainer(new SlotPottery(inventory.player, tableInventory, craftResult, 2, 110, 62));

        storageSlots = new ArrayList<Slot>();
        for (int i = 0; i < 6; i++)
            storageSlots.add(addSlotToContainer(new Slot(tableInventory, 3 + i, StorageX1, StorageY1 + i * 18)));

        for (int i = 0; i < 6; i++)
            storageSlots.add(addSlotToContainer(new Slot(tableInventory, 9 + i, StorageX2, StorageY2 + i * 18)));

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
        return tableInventory.isUseableByPlayer(player);
    }

    @Override
    public void onCraftMatrixChanged (IInventory inventory) {
        ItemStack pattern = tableInventory.getStackInSlot(0);
        ItemStack target = tableInventory.getStackInSlot(1);

        if (target != null && target.getItem() == Item.getItemFromBlock(Blocks.clay)) {
            if (pattern == null) {
                craftResult.setInventorySlotContents(0, new ItemStack(ModBlocks.largePot, 1, 1));
                return;
            }
            else if (PotteryManager.instance().isRegisteredPattern(pattern))
                target = new ItemStack(ModBlocks.largePot, target.stackSize, 1);
        }

        if (pattern != null && pattern.getItem() == Items.dye) {
            if (target != null && target.getItem() == Item.getItemFromBlock(ModBlocks.largePot) && (target.getItemDamage() & 15) == 0) {
                craftResult.setInventorySlotContents(0, new ItemStack(ModBlocks.largePotColored, 1, target.getItemDamage() | pattern.getItemDamage()));
                return;
            }
        }

        craftResult.setInventorySlotContents(0, PotteryManager.instance().getStampResult(pattern, target));
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

            // Try merge output into inventory and signal change
            if (slotIndex == outputSlot.slotNumber) {
                if (!mergeItemStack(slotStack, inventoryStart, hotbarEnd, true))
                    return null;
                slot.onSlotChange(slotStack, itemStack);
            }
            // Try merge stacks within inventory and hotbar spaces
            else if (slotIndex >= inventoryStart && slotIndex < hotbarEnd) {
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
