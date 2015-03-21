package com.jaquadro.minecraft.gardentrees.core.handlers;

import com.jaquadro.minecraft.gardencore.api.SaplingRegistry;
import com.jaquadro.minecraft.gardencore.api.event.EnrichedSoilEvent;
import com.jaquadro.minecraft.gardentrees.GardenTrees;
import com.jaquadro.minecraft.gardentrees.core.recipe.WoodPostRecipe;
import com.jaquadro.minecraft.gardentrees.item.ItemThinLog;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.feature.WorldGenerator;

public class ForgeEventHandler
{
    @SubscribeEvent
    public void applyEnrichedSoil (EnrichedSoilEvent event) {
        if (!GardenTrees.config.compostGrowsOrnamentalTrees)
            return;

        Item sapling = Item.getItemFromBlock(event.block);
        int saplingMeta = event.world.getBlockMetadata(event.x, event.y, event.z);
        if (sapling == null)
            return;

        WorldGenerator generator = (WorldGenerator) SaplingRegistry.instance().getExtendedData(sapling, saplingMeta, "sm_generator");
        if (generator == null)
            return;

        event.world.setBlockToAir(event.x, event.y, event.z);

        if (generator.generate(event.world, event.world.rand, event.x, event.y, event.z)) {
            event.setResult(Event.Result.ALLOW);
            return;
        }

        event.world.setBlock(event.x, event.y, event.z, event.block, saplingMeta, 0);
    }

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
        for (int i = 0, n = WoodPostRecipe.axeList.size(); i < n; i++) {
            if (item == WoodPostRecipe.axeList.get(i))
                return true;
        }

        return false;
    }
}
