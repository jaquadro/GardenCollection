package com.jaquadro.minecraft.gardencore.inventory;

import com.jaquadro.minecraft.gardencore.block.tile.TileEntityCompostBin;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ContainerCompostBin extends Container
{
    private static final int InventoryX = 8;
    private static final int InventoryY = 84;
    private static final int HotbarY = 142;

    private TileEntityCompostBin tileCompost;
    private int lastDecompTime;
    private int lastItemDecompTime;
    private int lastDecompCount;

    private List<Slot> playerSlots;
    private List<Slot> hotbarSlots;

    public ContainerCompostBin (InventoryPlayer inventory, TileEntityCompostBin tileEntity) {
        tileCompost = tileEntity;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++)
                addSlotToContainer(new SlotCompost(tileEntity, x + y * 3, 30 + x * 18, 17 + y * 18));
        }

        addSlotToContainer(new SlotCompostOutput(inventory.player, tileEntity, 9, 123, 34));

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
    public void addCraftingToCrafters (ICrafting crafting) {
        super.addCraftingToCrafters(crafting);

        crafting.sendProgressBarUpdate(this, 0, tileCompost.getDecompTime());
        crafting.sendProgressBarUpdate(this, 1, tileCompost.getCurrentItemDecompTime());
        crafting.sendProgressBarUpdate(this, 2, tileCompost.itemDecomposeCount);
    }

    @Override
    public void detectAndSendChanges () {
        super.detectAndSendChanges();

        for (int i = 0; i < crafters.size(); i++) {
            ICrafting crafting = (ICrafting) crafters.get(i);
            if (lastDecompTime != tileCompost.getDecompTime())
                crafting.sendProgressBarUpdate(this, 0, tileCompost.getDecompTime());
            if (lastItemDecompTime != tileCompost.getCurrentItemDecompTime())
                crafting.sendProgressBarUpdate(this, 1, tileCompost.getCurrentItemDecompTime());
            if (lastDecompCount != tileCompost.itemDecomposeCount)
                crafting.sendProgressBarUpdate(this, 2, tileCompost.itemDecomposeCount);
        }

        lastDecompTime = tileCompost.getDecompTime();
        lastItemDecompTime = tileCompost.getCurrentItemDecompTime();
        lastDecompCount = tileCompost.itemDecomposeCount;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar (int id, int value) {
        if (id == 0)
            tileCompost.binDecomposeTime = value;
        if (id == 1)
            tileCompost.currentItemDecomposeTime = value;
        if (id == 2)
            tileCompost.itemDecomposeCount = value;
    }

    @Override
    public boolean canInteractWith (EntityPlayer player) {
        return tileCompost.isUseableByPlayer(player);
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
                } else if (slotIndex >= hotbarStart && slotIndex < hotbarEnd && !this.mergeItemStack(slotStack, inventoryStart, hotbarStart, false))
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
