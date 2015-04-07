package com.jaquadro.minecraft.gardenstuff.block.tile;

import com.jaquadro.minecraft.gardenstuff.block.BlockBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import com.jaquadro.minecraft.gardenstuff.core.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileEntityBloomeryFurnace extends TileEntity implements ISidedInventory
{
    private static final int SLOT_PRIMARY = 0;
    private static final int SLOT_SECONDARY = 1;
    private static final int SLOT_FUEL = 2;
    private static final int SLOT_OUTPUT = 3;

    private static final int[] slots = new int[] { 0, 1, 2, 3 };

    private ItemStack[] furnaceItemStacks = new ItemStack[4];
    private String customName;

    public int furnaceBurnTime;
    public int currentItemBurnTime;
    public int furnaceCookTime;

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList list = tag.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        furnaceItemStacks = new ItemStack[getSizeInventory()];

        for (int i = 0, n = list.tagCount(); i < n; i++) {
            NBTTagCompound itemTag = list.getCompoundTagAt(i);
            byte slot = itemTag.getByte("Slot");

            if (slot >= 0 && slot < furnaceItemStacks.length)
                furnaceItemStacks[slot] = ItemStack.loadItemStackFromNBT(itemTag);
        }

        furnaceBurnTime = tag.getShort("BurnTime");
        furnaceCookTime = tag.getShort("CookTime");
        currentItemBurnTime = getItemBurnTime(furnaceItemStacks[SLOT_FUEL]);

        if (tag.hasKey("CustomName", Constants.NBT.TAG_STRING))
            customName = tag.getString("CustomName");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setShort("BurnTime", (short)furnaceBurnTime);
        tag.setShort("CookTime", (short)furnaceCookTime);

        NBTTagList list = new NBTTagList();
        for (int i = 0, n = furnaceItemStacks.length; i < n; i++) {
            if (furnaceItemStacks[i] != null) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte)i);
                furnaceItemStacks[i].writeToNBT(itemTag);
                list.appendTag(itemTag);
            }
        }

        tag.setTag("Items", list);

        if (hasCustomInventoryName())
            tag.setString("CustomName", customName);
    }

    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled (int ceiling) {
        return ceiling * furnaceCookTime / 200;
    }

    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled (int ceiling) {
        if (currentItemBurnTime == 0)
            currentItemBurnTime = 200;

        return ceiling * furnaceBurnTime / currentItemBurnTime;
    }

    public boolean isBurning () {
        return furnaceBurnTime > 0;
    }

    public void setCustomName (String name) {
        customName = name;
    }

    @Override
    public void updateEntity () {
        boolean hasPrevBurnTime = furnaceBurnTime > 0;
        boolean isDirty = false;

        if (furnaceBurnTime > 0)
            furnaceBurnTime--;

        if (!worldObj.isRemote) {
            if (furnaceBurnTime != 0 || (furnaceItemStacks[SLOT_FUEL] != null && furnaceItemStacks[SLOT_PRIMARY] != null && furnaceItemStacks[SLOT_SECONDARY] != null)) {
                if (furnaceBurnTime == 0 && canSmelt()) {
                    currentItemBurnTime = furnaceBurnTime = getItemBurnTime(furnaceItemStacks[SLOT_FUEL]);

                    if (furnaceBurnTime > 0) {
                        isDirty = true;

                        if (furnaceItemStacks[SLOT_FUEL] != null) {
                            furnaceItemStacks[SLOT_FUEL].stackSize--;
                            if (furnaceItemStacks[SLOT_FUEL].stackSize == 0)
                                furnaceItemStacks[SLOT_FUEL] = furnaceItemStacks[SLOT_FUEL].getItem().getContainerItem(furnaceItemStacks[SLOT_FUEL]);
                        }
                    }
                }

                if (isBurning() && canSmelt()) {
                    furnaceCookTime++;

                    if (furnaceCookTime == 200) {
                        furnaceCookTime = 0;
                        smeltItem();
                        isDirty = true;
                    }
                }
                else
                    furnaceCookTime = 0;
            }

            if (hasPrevBurnTime != furnaceBurnTime > 0) {
                isDirty = true;
                BlockBloomeryFurnace.updateFurnaceBlockState(worldObj, xCoord, yCoord, zCoord, furnaceBurnTime > 0);
            }
        }

        if (isDirty)
            markDirty();
    }

    private boolean canSmelt () {
        if (furnaceItemStacks[SLOT_PRIMARY] == null || furnaceItemStacks[SLOT_SECONDARY] == null)
            return false;

        if (furnaceItemStacks[SLOT_PRIMARY].getItem() != Items.iron_ingot && furnaceItemStacks[SLOT_PRIMARY].getItem() != Item.getItemFromBlock(Blocks.iron_ore))
            return false;
        if (furnaceItemStacks[SLOT_SECONDARY].getItem() != Item.getItemFromBlock(Blocks.sand))
            return false;

        ItemStack itemOutput = new ItemStack(ModItems.wroughtIronIngot);
        if (furnaceItemStacks[SLOT_OUTPUT] == null)
            return true;
        if (!furnaceItemStacks[SLOT_OUTPUT].isItemEqual(itemOutput))
            return false;

        int result = furnaceItemStacks[SLOT_OUTPUT].stackSize + itemOutput.stackSize;
        return result <= getInventoryStackLimit() && result <= furnaceItemStacks[SLOT_OUTPUT].getMaxStackSize();
    }

    public void smeltItem () {
        if (!canSmelt())
            return;

        ItemStack itemOutput = new ItemStack(ModItems.wroughtIronIngot);

        if (furnaceItemStacks[SLOT_OUTPUT] == null)
            furnaceItemStacks[SLOT_OUTPUT] = itemOutput.copy();
        else if (furnaceItemStacks[SLOT_OUTPUT].getItem() == itemOutput.getItem())
            furnaceItemStacks[SLOT_OUTPUT].stackSize += itemOutput.stackSize;

        furnaceItemStacks[SLOT_PRIMARY].stackSize--;
        furnaceItemStacks[SLOT_SECONDARY].stackSize--;

        if (furnaceItemStacks[SLOT_PRIMARY].stackSize <= 0)
            furnaceItemStacks[SLOT_PRIMARY] = null;
        if (furnaceItemStacks[SLOT_SECONDARY].stackSize <= 0)
            furnaceItemStacks[SLOT_SECONDARY] = null;
    }

    public static int getItemBurnTime (ItemStack stack) {
        if (stack == null)
            return 0;

        Item item = stack.getItem();

        if (item instanceof ItemBlock) {
            Block block = Block.getBlockFromItem(item);
            if (block == ModBlocks.stoneBlock && stack.getItemDamage() == 0)
                return ModBlocks.stoneBlock.getBurnTime(stack);
        }

        if (item == Items.coal && stack.getItemDamage() == 1)
            return 1600;

        return 0;
    }

    public static boolean isItemFuel (ItemStack stack) {
        return getItemBurnTime(stack) > 0;
    }

    public static boolean isItemPrimaryInput (ItemStack stack) {
        if (stack == null)
            return false;

        if (stack.getItem() == Items.iron_ingot)
            return true;
        if (stack.getItem() == Item.getItemFromBlock(Blocks.iron_ore))
            return true;

        return false;
    }

    public static boolean isItemSecondaryInput (ItemStack stack) {
        if (stack == null)
            return false;

        if (stack.getItem() == Item.getItemFromBlock(Blocks.sand))
            return true;

        return false;
    }

    @Override
    public int[] getAccessibleSlotsFromSide (int side) {
        return slots;
    }

    @Override
    public boolean canInsertItem (int slot, ItemStack stack, int side) {
        return isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem (int slot, ItemStack stack, int side) {
        return slot == SLOT_OUTPUT;
    }

    @Override
    public int getSizeInventory () {
        return furnaceItemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot (int slot) {
        return furnaceItemStacks[slot];
    }

    @Override
    public ItemStack decrStackSize (int slot, int count) {
        if (furnaceItemStacks[slot] != null) {
            if (furnaceItemStacks[slot].stackSize <= count) {
                ItemStack stack = furnaceItemStacks[slot];
                furnaceItemStacks[slot] = null;
                return stack;
            }
            else {
                ItemStack stack = furnaceItemStacks[slot].splitStack(count);
                if (furnaceItemStacks[slot].stackSize == 0)
                    furnaceItemStacks[slot] = null;
                return stack;
            }
        }

        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing (int slot) {
        return null;
    }

    @Override
    public void setInventorySlotContents (int slot, ItemStack stack) {
        furnaceItemStacks[slot] = stack;
        if (stack != null && stack.stackSize > getInventoryStackLimit())
            stack.stackSize = getInventoryStackLimit();
    }

    @Override
    public String getInventoryName () {
        return hasCustomInventoryName() ? customName : "container.gardenstuff.bloomeryFurnace";
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
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + .5, yCoord + .5, zCoord + .5) <= 64;
    }

    @Override
    public void openInventory () { }

    @Override
    public void closeInventory () { }

    @Override
    public boolean isItemValidForSlot (int slot, ItemStack stack) {
        if (slot == SLOT_OUTPUT)
            return false;
        if (slot == SLOT_FUEL)
            return isItemFuel(stack);

        return true;
    }
}
