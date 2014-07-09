package com.jaquadro.minecraft.gardencore.block.tile;

import com.jaquadro.minecraft.gardencore.api.IPlantMetaResolver;
import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.BlockGardenProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;

public class TileEntityGarden extends TileEntity implements IInventory
{
    protected static class SlotMapping {
        public int slot;
        public int mappedSlot;
        public int mappedX;
        public int mappedZ;

        public SlotMapping (int slot, int mappedSlot, int mappedX, int mappedY) {
            this.slot = slot;
            this.mappedSlot = mappedSlot;
            this.mappedX = mappedX;
            this.mappedZ = mappedY;
        }
    }

    private static final int DEFAULT_BIOME_DATA = 65407;

    public static final int SLOT_INVALID = -1;
    public static final int SLOT_CENTER = 0;
    public static final int SLOT_COVER = 1;
    public static final int SLOT_NW = 2;
    public static final int SLOT_NE = 3;
    public static final int SLOT_SW = 4;
    public static final int SLOT_SE = 5;
    public static final int SLOT_TOP_LEFT = 6;
    public static final int SLOT_TOP = 7;
    public static final int SLOT_TOP_RIGHT = 8;
    public static final int SLOT_RIGHT = 9;
    public static final int SLOT_BOTTOM_RIGHT = 10;
    public static final int SLOT_BOTTOM = 11;
    public static final int SLOT_BOTTOM_LEFT = 12;
    public static final int SLOT_LEFT = 13;

    private static final int[] PLANT_SLOTS = new int[] {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13
    };

    private static final SlotMapping[][] SLOT_MAP = new SlotMapping[][] {
        null, null, null, null, null, null,
        new SlotMapping[] {
            new SlotMapping(SLOT_TOP_LEFT, SLOT_TOP_RIGHT, -1, 0),
            new SlotMapping(SLOT_TOP_LEFT, SLOT_BOTTOM_LEFT, 0, -1),
            new SlotMapping(SLOT_TOP_LEFT, SLOT_BOTTOM_RIGHT, -1, -1)
        },
        new SlotMapping[] { new SlotMapping(SLOT_TOP, SLOT_BOTTOM, 0, -1) },
        new SlotMapping[] {
            new SlotMapping(SLOT_TOP_RIGHT, SLOT_TOP_LEFT, 1, 0),
            new SlotMapping(SLOT_TOP_RIGHT, SLOT_BOTTOM_RIGHT, 0, -1),
            new SlotMapping(SLOT_TOP_RIGHT, SLOT_BOTTOM_LEFT, 1, -1)
        },
        new SlotMapping[] { new SlotMapping(SLOT_RIGHT, SLOT_LEFT, 1, 0) },
        new SlotMapping[] {
            new SlotMapping(SLOT_BOTTOM_RIGHT, SLOT_BOTTOM_LEFT, 1, 0),
            new SlotMapping(SLOT_BOTTOM_RIGHT, SLOT_TOP_RIGHT, 0, 1),
            new SlotMapping(SLOT_BOTTOM_RIGHT, SLOT_TOP_LEFT, 1, 1)
        },
        new SlotMapping[] { new SlotMapping(SLOT_BOTTOM, SLOT_TOP, 0, 1) },
        new SlotMapping[] {
            new SlotMapping(SLOT_BOTTOM_LEFT, SLOT_BOTTOM_RIGHT, -1, 0),
            new SlotMapping(SLOT_BOTTOM_LEFT, SLOT_TOP_LEFT, 0, 1),
            new SlotMapping(SLOT_BOTTOM_LEFT, SLOT_TOP_RIGHT, -1, 1)
        },
        new SlotMapping[] { new SlotMapping(SLOT_LEFT, SLOT_RIGHT, -1, 0) },
    };

    private ItemStack[] containerStacks;
    private String customName;

    private ItemStack substrate;
    private ItemStack substrateSource;

    private boolean hasBiomeOverride;
    private int biomeData = DEFAULT_BIOME_DATA;

    public TileEntityGarden () {
        containerStacks = new ItemStack[containerSlotCount()];
    }

    protected int containerSlotCount () {
        return 14;
    }

    public int[] getPlantSlots () {
        return PLANT_SLOTS;
    }

    protected SlotMapping[] getNeighborMappingsForSlot (int slot) {
        if (slot >= SLOT_MAP.length)
            return null;

        return SLOT_MAP[slot];
    }

    public ItemStack getPlantInSlot (int slot) {
        return getStackInSlot(slot);
    }

    public void clearPlantedContents () {
        clearReachableContents(0, containerSlotCount());
    }

    public void clearReachableContents () {
        clearReachableContents(0, containerStacks.length);
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

    public int getPlantCount () {
        int count = 0;
        for (int slot : getPlantSlots()) {
            if (getPlantInSlot(slot) != null)
                count++;
        }
        return count;
    }

    public int getPlantHeight () {
        int height = 0;
        for (int slot : getPlantSlots()) {
            IPlantable plant = getPlantable(getPlantInSlot(slot));
            if (plant == null)
                continue;

            Block plantBlock = plant.getPlant(worldObj, xCoord, yCoord + 1, zCoord);
            int plantMeta = getPlantInSlot(slot).getItemDamage();

            IPlantMetaResolver resolver = PlantRegistry.instance().getPlantMetaResolver(plantBlock, plantMeta);
            if (resolver != null)
                return resolver.getPlantHeight(plantBlock, plantMeta);

            if (plantBlock instanceof BlockDoublePlant)
                height = Math.max(height, 2);
            else
                height = Math.max(height, 1);
        }

        return height;
    }

    public static IPlantable getPlantable (ItemStack plant) {
        if (plant == null || plant.getItem() == null)
            return null;

        IPlantable plantable = null;
        Item item = plant.getItem();
        if (item instanceof IPlantable)
            plantable = (IPlantable) item;
        else if (item instanceof ItemBlock) {
            Block itemBlock = Block.getBlockFromItem(item);
            if (itemBlock instanceof IPlantable)
                plantable = (IPlantable) itemBlock;
        }

        return plantable;
    }

    public boolean isSharedSlot (int slot) {
        SlotMapping[] nmap = getNeighborMappingsForSlot(slot);
        return nmap != null && nmap.length > 0;
    }

    public boolean isSlotValid (int slot) {
        if (!isSharedSlot(slot))
            return true;

        for (SlotMapping mapping : getNeighborMappingsForSlot(slot)) {
            if (!isAttachedNeighbor(xCoord + mapping.mappedX, yCoord, zCoord + mapping.mappedZ))
                return false;
        }

        return true;
    }

    public boolean isAttachedNeighbor (int x, int y, int z) {
        if (yCoord != y || Math.abs(x - xCoord) > 1 || Math.abs(z - zCoord) > 1)
            return false;

        Block sBlock = worldObj.getBlock(xCoord, yCoord, zCoord);
        Block nBlock = worldObj.getBlock(x, y, z);
        if (sBlock != nBlock)
            return false;

        int sData = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        int nData = worldObj.getBlockMetadata(x, y, z);
        if (sData != nData)
            return false;

        TileEntity nEntity = worldObj.getTileEntity(x, y, z);
        if (nEntity == null || getClass() != nEntity.getClass())
            return false;

        TileEntityGarden nGarden = (TileEntityGarden) nEntity;
        if ((substrate == null || nGarden.substrate == null) && substrate != nGarden.substrate)
            return false;

        if (substrate != null) {
            if (substrate.getItem() != nGarden.substrate.getItem() || substrate.getItemDamage() != nGarden.substrate.getItemDamage())
                return false;
        }

        return true;
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
        return (biomeData & 255) / 255f;
    }

    public float getBiomeHumidity () {
        return ((biomeData >> 8) & 255) / 255f;
    }

    public void setBiomeData (int data) {
        this.biomeData = data;
        this.hasBiomeOverride = true;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        NBTTagList itemList = tag.getTagList("Items", 10);
        containerStacks = new ItemStack[getSizeInventory()];

        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound item = itemList.getCompoundTagAt(i);
            if (!item.hasKey("Slot") || !item.hasKey("Item") || !item.hasKey("Data"))
                continue;

            byte slot = item.getByte("Slot");
            if (slot < 0 || slot >= containerStacks.length)
                continue;

            String itemString = item.getString("Item");
            Item itemObj = (Item) Item.itemRegistry.getObject(itemString);
            if (itemObj == null)
                continue;

            short itemData = item.getShort("Data");

            containerStacks[slot] = new ItemStack(itemObj, 1, itemData);
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
            if (containerStacks[i] == null)
                continue;

            NBTTagCompound item = new NBTTagCompound();
            item.setByte("Slot", (byte)i);
            item.setString("Item", Item.itemRegistry.getNameForObject(containerStacks[i].getItem()));
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
        while (getWorldObj().getBlock(xCoord, ++y, zCoord) instanceof BlockGardenProxy)
            getWorldObj().func_147479_m(xCoord, y, zCoord);
    }

    /*@Override
    public void updateEntity () {
        if (!worldObj.isRemote) {
            BlockGarden.validateBlockState(this);
        }
    }*/

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

        for (SlotMapping mapping : getNeighborMappingsForSlot(slot)) {
            TileEntity te = worldObj.getTileEntity(xCoord + mapping.mappedX, yCoord, zCoord + mapping.mappedZ);
            if (te == null || !(te instanceof TileEntityGarden))
                continue;

            TileEntityGarden nGarden = (TileEntityGarden)te;
            stack = nGarden.getStackInSlotIsolated(mapping.mappedSlot);
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

        for (SlotMapping mapping : getNeighborMappingsForSlot(slot)) {
            if (!isAttachedNeighbor(xCoord + mapping.mappedX, yCoord, zCoord + mapping.mappedZ))
                continue;

            TileEntity te = worldObj.getTileEntity(xCoord + mapping.mappedX, yCoord, zCoord + mapping.mappedZ);
            if (te == null || !(te instanceof TileEntityGarden))
                continue;

            TileEntityGarden nGarden = (TileEntityGarden)te;
            nGarden.setInventorySlotContentsIsolated(mapping.mappedSlot, null, notify);
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
            return  false;

        if (itemStack != null && itemStack.getItem() instanceof IPlantable)
            return isSlotValid(slot);
        return false;
    }
}
