package com.jaquadro.minecraft.gardencore.block.tile;

import com.jaquadro.minecraft.gardencore.api.IPlantProxy;
import com.jaquadro.minecraft.gardencore.api.block.garden.ISlotMapping;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TileEntityGarden extends TileEntity implements IInventory
{
    private static final int DEFAULT_BIOME_DATA = 65407;

    public static final int SLOT_INVALID = -1;

    private ItemStack[] containerStacks;
    private PlantItem[] containerPlants;

    private String customName;

    private ItemStack substrate;
    private ItemStack substrateSource;

    private boolean hasBiomeOverride;
    private int biomeData = DEFAULT_BIOME_DATA;

    public TileEntityGarden () {
        containerStacks = new ItemStack[containerSlotCount()];
    }

    protected int containerSlotCount () {
        return 0;
    }

    public ItemStack getPlantInSlot (int slot) {
        return getStackInSlot(slot);
    }

    public void clearPlantedContents () {
        BlockGarden block = getGardenBlock();
        if (block != null)
            clearReachableContents(0, containerSlotCount());
    }

    public void clearReachableContents () {
        clearReachableContents(0, containerStacks.length);
    }

    public boolean isEmpty () {
        for (int i = 0; i < containerStacks.length; i++) {
            if (getPlantInSlot(i) != null)
                return false;
        }

        return true;
    }

    private void clearReachableContents (int start, int length) {
        for (int i = start; i < length; i++)
            setInventorySlotContents(i, null, false);

        BlockGarden.validateBlockState(this);
        if (!worldObj.isRemote)
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public List<ItemStack> getReachableContents () {
        List<ItemStack> contents = new ArrayList<ItemStack>();

        for (int i = 0; i < containerStacks.length; i++) {
            ItemStack item = getStackInSlot(i);
            if (item != null)
                contents.add(item);
        }

        return contents;
    }

    public boolean isSharedSlot (int slot) {
        BlockGarden block = getGardenBlock();
        if (block == null)
            return false;

        ISlotMapping[] nmap = block.getSlotShareProfile().getNeighborsForSlot(slot);
        return nmap != null && nmap.length > 0;
    }

    public boolean isSlotValid (int slot) {
        if (!isSharedSlot(slot))
            return true;

        BlockGarden block = getGardenBlock();
        for (ISlotMapping mapping : block.getSlotShareProfile().getNeighborsForSlot(slot)) {
            if (!block.getConnectionProfile().isAttachedNeighbor(worldObj, xCoord, yCoord, zCoord, xCoord + mapping.getMappedX(), yCoord, zCoord + mapping.getMappedZ()))
                return false;
        }

        return true;
    }

    public boolean isAttachedNeighbor (int nx, int ny, int nz) {
        BlockGarden garden = getGardenBlock();
        if (garden == null)
            return false;

        return garden.getConnectionProfile().isAttachedNeighbor(worldObj, xCoord, yCoord, zCoord, nx, ny, nz);
    }

    public BlockGarden getGardenBlock () {
        Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
        if (block instanceof BlockGarden)
            return (BlockGarden) block;

        return null;
    }

    public ItemStack getSubstrate () {
        return substrate;
    }

    public ItemStack getSubstrateSource () {
        return (substrateSource != null) ? substrateSource : substrate;
    }

    public void setSubstrate (ItemStack substrate) {
        this.substrate = (substrate != null) ? substrate.copy() : null;
        this.substrateSource = null;
    }

    public void setSubstrate (ItemStack substrate, ItemStack substrateSource) {
        this.substrate = (substrate != null) ? substrate.copy() : null;
        this.substrateSource = (getSubstrateSource() != null) ? substrateSource.copy() : null;
    }

    public boolean hasBiomeDataOverride () {
        return hasBiomeOverride;
    }

    public int getBiomeData () {
        return biomeData;
    }

    public float getBiomeTemperature () {
        return (biomeData & 127) / 127f;
    }

    public float getBiomeHumidity () {
        return ((biomeData >> 7) & 127) / 127f;
    }

    public void setBiomeData (int data) {
        this.biomeData = data;
        this.hasBiomeOverride = true;
    }

    private ItemStack getStackLocal (int slot) {
        if (containerPlants == null && hasWorldObj())
            initializeContainerPlants();

        if (containerPlants != null)
            return containerPlants[slot].getPlantSourceItem();
        else
            return containerStacks[slot];
    }

    private void setStackLocal (int slot, ItemStack itemStack) {
        if (containerPlants == null && hasWorldObj())
            initializeContainerPlants();

        if (containerPlants != null && hasWorldObj())
            containerPlants[slot] = PlantItem.getForItem(worldObj, itemStack);

        containerStacks[slot] = itemStack;
    }

    private void initializeContainerPlants () {
        containerPlants = new PlantItem[containerStacks.length];
        for (int i = 0; i < containerStacks.length; i++) {
            if (containerStacks[i] != null)
                containerPlants[i] = PlantItem.getForItem(worldObj, containerStacks[i]);
        }
    }

    @Override
    public void setWorldObj (World world) {
        super.setWorldObj(world);

        if (containerPlants == null && world != null)
            initializeContainerPlants();
        else if (world == null)
            containerPlants = null;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList itemList = tag.getTagList("Items", 10);
        containerStacks = new ItemStack[getSizeInventory()];
        containerPlants = null;

        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound item = itemList.getCompoundTagAt(i);
            if (!item.hasKey("Slot") || !item.hasKey("Item") || !item.hasKey("Data"))
                continue;

            byte slot = item.getByte("Slot");
            if (slot < 0 || slot >= containerStacks.length)
                continue;

            Item itemObj = Item.getItemById(item.getShort("Item"));
            if (itemObj == null)
                continue;

            short itemData = item.getShort("Data");

            setStackLocal(slot, new ItemStack(itemObj, 1, itemData));
        }

        customName = null;
        if (tag.hasKey("CustomName"))
            customName = tag.getString("CustomName");

        substrate = null;
        substrateSource = null;
        if (tag.hasKey("SubId")) {
            substrate = new ItemStack(Item.getItemById(tag.getShort("SubId")));
            if (tag.hasKey("SubDa"))
                substrate.setItemDamage(tag.getShort("SubDa"));

            if (tag.hasKey("SubSrcId")) {
                substrateSource = new ItemStack(Item.getItemById(tag.getShort("SubSrcId")));
                if (tag.hasKey("SubSrcDa"))
                    substrateSource.setItemDamage(tag.getShort("SubSrcDa"));
                if (tag.hasKey("SubSrcTag", 10))
                    substrateSource.stackTagCompound = tag.getCompoundTag("SubSrcTag");
            }
        }

        hasBiomeOverride = tag.hasKey("Biom");
        biomeData = tag.hasKey("Biom") ? tag.getInteger("Biom") : DEFAULT_BIOME_DATA;
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < containerStacks.length; i++) {
            if (containerStacks[i] == null || containerStacks[i].getItem() == null)
                continue;

            NBTTagCompound item = new NBTTagCompound();
            item.setByte("Slot", (byte)i);
            item.setShort("Item", (short) Item.getIdFromItem(containerStacks[i].getItem()));
            item.setShort("Data", (short) containerStacks[i].getItemDamage());

            itemList.appendTag(item);
        }

        tag.setTag("Items", itemList);

        if (hasCustomInventoryName())
            tag.setString("CustomName", customName);

        if (substrate != null) {
            tag.setShort("SubId", (short)Item.getIdFromItem(substrate.getItem()));
            if (substrate.getItemDamage() != 0)
                tag.setShort("SubDa", (short)substrate.getItemDamage());

            if (substrateSource != null) {
                tag.setShort("SubSrcId", (short)Item.getIdFromItem(substrateSource.getItem()));
                if (substrateSource.getItemDamage() != 0)
                    tag.setShort("SubSrcDa", (short)substrateSource.getItemDamage());
                if (substrateSource.stackTagCompound != null)
                    tag.setTag("SubSrcTag", substrateSource.stackTagCompound);
            }
        }

        if (hasBiomeOverride || biomeData != DEFAULT_BIOME_DATA)
            tag.setInteger("Biom", biomeData);
    }

    @Override
    public Packet getDescriptionPacket () {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 5, tag);
    }

    @Override
    public void onDataPacket (NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
        getWorldObj().func_147479_m(xCoord, yCoord, zCoord); // markBlockForRenderUpdate

        int y = yCoord;
        while (getWorldObj().getBlock(xCoord, ++y, zCoord) instanceof IPlantProxy)
            getWorldObj().func_147479_m(xCoord, y, zCoord);
    }

    @Override
    public int getSizeInventory () {
        return containerStacks.length;
    }

    @Override
    public ItemStack getStackInSlot (int slot) {
        if (!isSharedSlot(slot))
            return getStackInSlotIsolated(slot);

        if (!isSlotValid(slot))
            return null;

        ItemStack stack = getStackInSlotIsolated(slot);
        if (stack != null)
            return stack;

        BlockGarden block = getGardenBlock();
        for (ISlotMapping mapping : block.getSlotShareProfile().getNeighborsForSlot(slot)) {
            TileEntity te = worldObj.getTileEntity(xCoord + mapping.getMappedX(), yCoord, zCoord + mapping.getMappedZ());
            if (te == null || !(te instanceof TileEntityGarden))
                continue;

            TileEntityGarden nGarden = (TileEntityGarden)te;
            stack = nGarden.getStackInSlotIsolated(mapping.getMappedSlot());
            if (stack != null)
                return stack;
        }

        return null;
    }

    protected ItemStack getStackInSlotIsolated (int slot) {
        return containerStacks[slot];
    }

    @Override
    public ItemStack decrStackSize (int slot, int count) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= count) {
                setInventorySlotContents(slot, null);
                return stack;
            }
            else {
                ItemStack split = stack.splitStack(count);
                if (stack.stackSize == 0)
                    setInventorySlotContents(slot, null);

                markDirty();
                return split;
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
        setInventorySlotContents(slot, itemStack, true);
    }

    public void setInventorySlotContents (int slot, ItemStack itemStack, boolean notify) {
        if (!isSharedSlot(slot)) {
            setInventorySlotContentsIsolated(slot, itemStack, notify);
            return;
        }

        BlockGarden block = getGardenBlock();
        for (ISlotMapping mapping : block.getSlotShareProfile().getNeighborsForSlot(slot)) {
            if (!block.getConnectionProfile().isAttachedNeighbor(worldObj, xCoord, yCoord, zCoord, xCoord + mapping.getMappedX(), yCoord, zCoord + mapping.getMappedZ()))
                continue;

            TileEntity te = worldObj.getTileEntity(xCoord + mapping.getMappedX(), yCoord, zCoord + mapping.getMappedZ());
            if (te == null || !(te instanceof TileEntityGarden))
                continue;

            TileEntityGarden nGarden = (TileEntityGarden)te;
            nGarden.setInventorySlotContentsIsolated(mapping.getMappedSlot(), null, notify);
        }

        setInventorySlotContentsIsolated(slot, itemStack, notify);
    }

    protected void setInventorySlotContentsIsolated (int slot, ItemStack itemStack, boolean notify) {
        containerStacks[slot] = itemStack;

        if (itemStack != null && itemStack.stackSize > getInventoryStackLimit())
            itemStack.stackSize = getInventoryStackLimit();

        markDirty();

        if (notify)
            BlockGarden.validateBlockState(this);
    }

    @Override
    public String getInventoryName () {
        return hasCustomInventoryName() ? customName : "container.garden";
    }

    @Override
    public boolean hasCustomInventoryName () {
        return customName != null && customName.length() > 0;
    }

    @Override
    public int getInventoryStackLimit () {
        return 1;
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
    public boolean isItemValidForSlot (int slot, ItemStack itemStack) {
        if (!isSlotValid(slot))
            return false;

        PlantItem plant = PlantItem.getForItem(worldObj, itemStack);
        if (plant == null)
            return false;

        BlockGarden block = getGardenBlock();
        if (block == null)
            return false;

        if (!block.isPlantValidForSlot(worldObj, xCoord, yCoord, zCoord, slot, plant))
            return false;

        return true;
    }
}
