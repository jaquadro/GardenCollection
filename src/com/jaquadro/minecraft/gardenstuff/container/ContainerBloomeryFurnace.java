package com.jaquadro.minecraft.gardenstuff.container;

import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityBloomeryFurnace;
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

public class ContainerBloomeryFurnace extends Container
{
    private static final int InventoryX = 8;
    private static final int InventoryY = 84;
    private static final int HotbarY = 142;

    private TileEntityBloomeryFurnace tileFurnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;

    private Slot primarySlot;
    private Slot secondarySlot;
    private Slot fuelSlot;
    private Slot outputSlot;
    private List<Slot> playerSlots;
    private List<Slot> hotbarSlots;

    public ContainerBloomeryFurnace (InventoryPlayer inventory, TileEntityBloomeryFurnace tile) {
        tileFurnace = tile;

        primarySlot = addSlotToContainer(new Slot(tile, 0, 56, 17));
        secondarySlot = addSlotToContainer(new Slot(tile, 1, 35, 17));
        fuelSlot = addSlotToContainer(new Slot(tile, 2, 56, 53));

        outputSlot = addSlotToContainer(new SlotBloomeryOutput(inventory.player, tile, 3, 116, 35));

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

        crafting.sendProgressBarUpdate(this, 0, tileFurnace.furnaceCookTime);
        crafting.sendProgressBarUpdate(this, 1, tileFurnace.furnaceBurnTime);
        crafting.sendProgressBarUpdate(this, 2, tileFurnace.currentItemBurnTime);
    }

    @Override
    public void detectAndSendChanges () {
        super.detectAndSendChanges();

        for (int i = 0; i < crafters.size(); i++) {
            ICrafting crafting = (ICrafting) crafters.get(i);
            if (lastCookTime != tileFurnace.furnaceCookTime)
                crafting.sendProgressBarUpdate(this, 0, tileFurnace.furnaceCookTime);
            if (lastBurnTime != tileFurnace.furnaceBurnTime)
                crafting.sendProgressBarUpdate(this, 1, tileFurnace.furnaceBurnTime);
            if (lastItemBurnTime != tileFurnace.currentItemBurnTime)
                crafting.sendProgressBarUpdate(this, 2, tileFurnace.currentItemBurnTime);
        }

        lastCookTime = tileFurnace.furnaceCookTime;
        lastBurnTime = tileFurnace.furnaceBurnTime;
        lastItemBurnTime = tileFurnace.currentItemBurnTime;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar (int id, int value) {
        switch (id) {
            case 0:
                tileFurnace.furnaceCookTime = value;
                break;
            case 1:
                tileFurnace.furnaceBurnTime = value;
                break;
            case 2:
                tileFurnace.currentItemBurnTime = value;
                break;
        }
    }

    @Override
    public boolean canInteractWith (EntityPlayer player) {
        return tileFurnace.isUseableByPlayer(player);
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
                boolean merged = false;
                if (TileEntityBloomeryFurnace.isItemFuel(slotStack))
                    merged = mergeItemStack(slotStack, fuelSlot.slotNumber, fuelSlot.slotNumber + 1, false);
                else if (TileEntityBloomeryFurnace.isItemPrimaryInput(slotStack))
                    merged = mergeItemStack(slotStack, primarySlot.slotNumber, primarySlot.slotNumber + 1, false);
                else if (TileEntityBloomeryFurnace.isItemSecondaryInput(slotStack))
                    merged = mergeItemStack(slotStack, secondarySlot.slotNumber, secondarySlot.slotNumber + 1, false);

                if (!merged) {
                    if (slotIndex >= inventoryStart && slotIndex < hotbarStart) {
                        if (!mergeItemStack(slotStack, hotbarStart, hotbarEnd, false))
                            return null;
                    } else if (slotIndex >= hotbarStart && slotIndex < hotbarEnd && !this.mergeItemStack(slotStack, inventoryStart, hotbarStart, false))
                        return null;
                }
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
