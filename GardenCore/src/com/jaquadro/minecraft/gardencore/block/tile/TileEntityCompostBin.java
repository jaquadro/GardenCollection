package com.jaquadro.minecraft.gardencore.block.tile;

import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.IPlantable;

public class TileEntityCompostBin extends TileEntity implements IInventory
{
    private ItemStack[] compostItemStacks = new ItemStack[10];

    // The number of ticks remaining to decompose the current item
    private int binDecomposeTime;

    // The slot actively being decomposed
    private int currentItemSlot;

    // The number of ticks that a fresh copy of the currently-decomposing item would decompose for
    private int currentItemDecomposeTime;

    private String customName;

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Items", 10);
        compostItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
            byte slot = itemTag.getByte("Slot");

            if (slot >= 0 && slot < compostItemStacks.length)
                compostItemStacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }

        binDecomposeTime = tag.getShort("DecompTime");
        currentItemSlot = tag.getByte("DecompSlot");
        currentItemDecomposeTime = getItemDecomposeTime(compostItemStacks[currentItemSlot]);

        if (tag.hasKey("CustomName", 8))
            customName = tag.getString("CustomName");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setShort("DecompTime", (short)binDecomposeTime);
        tag.setByte("DecompSlot", (byte)currentItemSlot);

        NBTTagList tagList = new NBTTagList();
        for (int i = 0; i < compostItemStacks.length; i++) {
            if (compostItemStacks[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte)i);
                compostItemStacks[i].writeToNBT(itemTag);
                tagList.appendTag(itemTag);
            }
        }

        tag.setTag("Items", tagList);

        if (hasCustomInventoryName())
            tag.setString("CustomName", customName);
    }

    public boolean isDecomposing () {
        return binDecomposeTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public int getDecomposeTimeRemainingScaled (int scale) {
        if (currentItemDecomposeTime == 0)
            currentItemDecomposeTime = 200;

        return binDecomposeTime * scale / currentItemDecomposeTime;
    }

    @Override
    public void updateEntity () {
        boolean isDecomposing = binDecomposeTime > 0;
        boolean shouldUpdate = false;

        if (binDecomposeTime > 0)
            --binDecomposeTime;

        if (!worldObj.isRemote) {
            int filledSlotCount = 0;
            for (int i = 0; i < 9; i++)
                filledSlotCount += (compostItemStacks[i] != null) ? 1 : 0;

            if (binDecomposeTime != 0 || filledSlotCount > 0) {
                if (binDecomposeTime == 0) {
                    if (compostItemStacks[currentItemSlot] != null) {
                        --compostItemStacks[currentItemSlot].stackSize;
                        shouldUpdate = true;
                    }

                    if (compostItemStacks[currentItemSlot].stackSize == 0) {
                        compostItemStacks[currentItemSlot] = compostItemStacks[currentItemSlot].getItem().getContainerItem(compostItemStacks[currentItemSlot]);
                        shouldUpdate = true;
                    }

                    currentItemSlot = selectRandomFilledSlot();
                    currentItemDecomposeTime = getItemDecomposeTime(compostItemStacks[currentItemSlot]);
                    binDecomposeTime = currentItemDecomposeTime;

                    if (binDecomposeTime > 0)
                        shouldUpdate = true;
                }
            }

            if (isDecomposing != binDecomposeTime > 0) {
                shouldUpdate = true;
                // Update block state
            }
        }

        if (shouldUpdate)
            markDirty();
    }

    private int selectRandomFilledSlot () {
        int filledSlotCount = 0;
        for (int i = 0; i < 9; i++)
            filledSlotCount += (compostItemStacks[i] != null) ? 1 : 0;

        int index = worldObj.rand.nextInt(filledSlotCount);
        for (int i = 0, c = 0; i < 9; i++) {
            if (c == index)
                return i;
            if (compostItemStacks[i] != null)
                c++;
        }

        return -1;
    }

    public static int getItemDecomposeTime (ItemStack itemStack) {
        if (itemStack == null)
            return 0;

        Item item = itemStack.getItem();
        if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
            Block block = Block.getBlockFromItem(item);

            if (WoodRegistry.instance().contains(block, itemStack.getItemDamage()))
                return 300;
            if (block instanceof IPlantable)
                return 100;
            if (block instanceof IGrowable)
                return 100;
        }

        if (item == Items.stick)
            return 150;
        if (item instanceof IPlantable)
            return 100;

        return 0;
    }

    public static boolean isItemDecomposable (ItemStack itemStack) {
        return getItemDecomposeTime(itemStack) > 0;
    }

    @Override
    public int getSizeInventory () {
        return compostItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot (int slot) {
        return compostItemStacks[slot];
    }

    @Override
    public ItemStack decrStackSize (int slot, int count) {
        if (compostItemStacks[slot] != null) {
            if (compostItemStacks[slot].stackSize <= count) {
                ItemStack itemStack = compostItemStacks[slot];
                compostItemStacks[slot] = null;
                return itemStack;
            }
            else {
                ItemStack itemStack = compostItemStacks[slot].splitStack(count);
                if (compostItemStacks[slot].stackSize == 0)
                    compostItemStacks[slot] = null;
                return itemStack;
            }
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing (int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents (int slot, ItemStack itemStack) {
        compostItemStacks[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            itemStack.stackSize = getInventoryStackLimit();
    }

    @Override
    public String getInventoryName () {
        return hasCustomInventoryName() ? customName : "container.compost_bin";
    }

    @Override
    public boolean hasCustomInventoryName () {
        return customName != null && customName.length() > 0;
    }

    @Override
    public int getInventoryStackLimit () {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer (EntityPlayer player) {
        if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
            return false;

        return player.getDistanceSq(xCoord + .5, yCoord + .5, zCoord + .5) <= 64;
    }

    @Override
    public void openInventory () { }

    @Override
    public void closeInventory () { }

    @Override
    public boolean isItemValidForSlot (int slot, ItemStack item) {
        if (slot >= 0 && slot < 9)
            return isItemDecomposable(item);

        return false;
    }
}
