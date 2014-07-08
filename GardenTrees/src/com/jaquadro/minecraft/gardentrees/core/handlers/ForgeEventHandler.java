package com.jaquadro.minecraft.gardentrees.core.handlers;

import com.jaquadro.minecraft.gardentrees.item.ItemThinLog;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ForgeEventHandler
{
    @SubscribeEvent
    public void handleCrafting (PlayerEvent.ItemCraftedEvent event) {
        if (event.crafting.getItem() instanceof ItemThinLog) {
            for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
                ItemStack item = event.craftMatrix.getStackInSlot(i);
                if (item != null && isValidAxe(item) && item.getItemDamage() < item.getMaxDamage()) {
                    ItemStack modifiedAxe = item.copy();
                    modifiedAxe.setItemDamage(modifiedAxe.getItemDamage() + 1);
                    modifiedAxe.stackSize += modifiedAxe.stackSize;

                    event.craftMatrix.setInventorySlotContents(i, modifiedAxe);
                }
            }
        }
    }

    private boolean isValidAxe (ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item == Items.wooden_axe
            || item == Items.stone_axe
            || item == Items.iron_axe
            || item == Items.golden_axe
            || item == Items.diamond_axe;
    }
}
