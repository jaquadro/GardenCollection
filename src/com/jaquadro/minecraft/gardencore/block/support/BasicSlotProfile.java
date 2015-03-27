package com.jaquadro.minecraft.gardencore.block.support;

import com.jaquadro.minecraft.gardencore.api.block.garden.ISlotProfile;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;

public class BasicSlotProfile implements ISlotProfile
{
    public static class Slot {
        public int slot;
        public List<PlantType> validTypeClasses = new ArrayList<PlantType>();
        public List<PlantSize> validSizeClasses = new ArrayList<PlantSize>();

        public Slot (int slot, PlantType[] typeClasses, PlantSize[] sizeClasses) {
            this.slot = slot;

            for (int i = 0; i < typeClasses.length; i++)
                validTypeClasses.add(typeClasses[i]);
            for (int i = 0; i < sizeClasses.length; i++)
                validSizeClasses.add(sizeClasses[i]);
        }
    }

    private static AxisAlignedBB[] defaultClippingBounds = new AxisAlignedBB[] { AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 1, 1) };

    protected Slot[] slots;
    protected int[] slotIndexes;

    protected BasicSlotProfile () { }

    public BasicSlotProfile (Slot[] slots) {
        this.slots = slots;

        slotIndexes = new int[slots.length];
        for (int i = 0; i < slots.length; i++)
            slotIndexes[i] = slots[i].slot;
    }

    @Override
    public int[] getPlantSlots () {
        return slotIndexes;
    }

    @Override
    public boolean isPlantValidForSlot (IBlockAccess blockAccess, int x, int y, int z, int slot, PlantItem plant) {
        if (slots == null || slot < 0 || slot >= slots.length)
            return false;
        if (slots[slot] == null)
            return false;

        if (!slots[slot].validTypeClasses.contains(plant.getPlantTypeClass()))
            return false;
        if (!slots[slot].validSizeClasses.contains(plant.getPlantSizeClass()))
            return false;

        return true;
    }

    @Override
    public float getPlantOffsetX (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        return 0;
    }

    @Override
    public float getPlantOffsetY (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        return 0;
    }

    @Override
    public float getPlantOffsetZ (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        return 0;
    }

    @Override
    public Object openPlantGUI (InventoryPlayer playerInventory, TileEntity gardenTile, boolean client) {
        return null;
    }

    @Override
    public AxisAlignedBB[] getClippingBounds (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        return defaultClippingBounds;
    }

    /*@Override
    public int getNextAvailableSlot (IBlockAccess blockAccess, int x, int y, int z, int slot, PlantItem plant) {
        return -1;
    }

    @Override
    public SlotMapping[] getSharedSlotMap (int slot) {
        return null;
    }*/
}
