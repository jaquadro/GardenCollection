package com.jaquadro.minecraft.modularpots.inventory;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

public class SlotPottery extends Slot
{
    private final IInventory inputInventory;
    private EntityPlayer player;
    private int amountCrafted;

    public SlotPottery (EntityPlayer player, IInventory inputInventory, IInventory inventory, int par2, int par3, int par4) {
        super(inventory, par2, par3, par4);

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
        FMLCommonHandler.instance().firePlayerCraftingEvent(player, itemStack, inputInventory);
        onCrafting(itemStack);

        ItemStack itemTarget = inputInventory.getStackInSlot(1);
        if (itemTarget != null) {
            inputInventory.decrStackSize(1, 1);

            if (itemTarget.getItem().hasContainerItem(itemTarget)) {
                ItemStack itemContainer = itemTarget.getItem().getContainerItem(itemTarget);
                if (itemContainer != null && itemContainer.isItemStackDamageable() && itemContainer.getItemDamage() > itemContainer.getMaxDamage()) {
                    MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(this.player, itemContainer));
                    return;
                }

                if (!itemTarget.getItem().doesContainerItemLeaveCraftingGrid(itemTarget) || !this.player.inventory.addItemStackToInventory(itemContainer)) {
                    if (inputInventory.getStackInSlot(1) == null)
                        inputInventory.setInventorySlotContents(1, itemContainer);
                    else
                        this.player.dropPlayerItemWithRandomChoice(itemContainer, false);
                }
            }
        }

        ItemStack itemPattern = inputInventory.getStackInSlot(0);
        if (itemPattern != null && itemPattern.getItem() == Items.dye) {
            inputInventory.decrStackSize(0, 1);
        }
    }
}
