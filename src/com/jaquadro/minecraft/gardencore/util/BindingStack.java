package com.jaquadro.minecraft.gardencore.util;

import net.minecraft.world.World;
import scala.Array;

public class BindingStack
{
    private int[] slotStack = new int[16];
    private int[] dataStack = new int[16];
    int index = -1;
    int defaultMeta = 0;

    public void setDefaultMeta (int defaultMeta) {
        this.defaultMeta = defaultMeta;
    }

    public void bind (World world, int x, int y, int z, int slot, int data) {
        if (++index >= slotStack.length)
            growStack();

        slotStack[index] = slot;
        dataStack[index] = data;

        world.setBlockMetadataWithNotify(x, y, z, data, 0);
    }

    public void unbind (World world, int x, int y, int z) {
        if (index >= 0)
            index--;

        world.setBlockMetadataWithNotify(x, y, z, getData(), 0);
    }

    public void softUnbind () {
        if (index >= 0)
            index--;
    }

    public void refreshWorld (World world, int x, int y, int z) {
        if (index >= 0)
            world.setBlockMetadataWithNotify(x, y, z, getData(), 0);
    }

    public int getSlot () {
        return (index >= 0) ? slotStack[index] : -1;
    }

    public int getData () {
        return (index >= 0) ? dataStack[index] : defaultMeta;
    }

    private void growStack () {
        int[] newSlotStack = new int[slotStack.length * 2];
        int[] newDataStack = new int[dataStack.length * 2];

        Array.copy(slotStack, 0, newSlotStack, 0, slotStack.length);
        Array.copy(dataStack, 0, newDataStack, 0, dataStack.length);

        slotStack = newSlotStack;
        dataStack = newDataStack;
    }
}
