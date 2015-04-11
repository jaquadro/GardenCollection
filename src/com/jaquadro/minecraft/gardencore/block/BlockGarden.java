package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.api.IPlantProxy;
import com.jaquadro.minecraft.gardencore.api.block.IGardenBlock;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.block.garden.IConnectionProfile;
import com.jaquadro.minecraft.gardencore.api.block.garden.ISlotProfile;
import com.jaquadro.minecraft.gardencore.api.block.garden.ISlotShareProfile;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockGarden extends BlockContainer implements IGardenBlock
{
    public static final int SLOT_INVALID = -1;

    protected IConnectionProfile connectionProfile;
    protected ISlotProfile slotProfile;
    protected ISlotShareProfile slotShareProfile;

    protected BlockGarden (String blockName, Material material) {
        super(material);

        setBlockName(blockName);
    }

    @Override
    public boolean canSustainPlant (IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        return false;
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (side != ForgeDirection.UP.ordinal())
            return false;

        TileEntityGarden tileEntity = getTileEntity(world, x, y, z);
        if (tileEntity == null) {
            tileEntity = createNewTileEntity(world, 0);
            world.setTileEntity(x, y, z, tileEntity);
        }

        return applyItemToGarden(world, x, y, z, player, null, hitX, hitY, hitZ);
    }

    @Override
    public abstract TileEntityGarden createNewTileEntity (World world, int meta);

    @Override
    public void onBlockHarvested (World world, int x, int y, int z, int p_149681_5_, EntityPlayer player) {
        super.onBlockHarvested(world, x, y, z, p_149681_5_, player);

        if (player.capabilities.isCreativeMode) {
            TileEntityGarden te = getTileEntity(world, x, y, z);
            if (te != null)
                te.clearPlantedContents();
        }
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        TileEntityGarden te = getTileEntity(world, x, y, z);

        if (te != null) {
            for (ItemStack item : te.getReachableContents())
                dropBlockAsItem(world, x, y, z, item);
            te.clearReachableContents();
        }

        super.breakBlock(world, x, y, z, block, data);
    }

    @Override
    public int getDefaultSlot () {
        return SLOT_INVALID;
    }

    public IConnectionProfile getConnectionProfile () {
        return connectionProfile;
    }

    public ISlotProfile getSlotProfile () {
        return slotProfile;
    }

    public ISlotShareProfile getSlotShareProfile () {
        return slotShareProfile;
    }

    public void clearPlantedContents (IBlockAccess world, int x, int y, int z) {
        TileEntityGarden te = getTileEntity(world, x, y, z);
        if (te != null)
            te.clearReachableContents();

        validateBlockState(te);
    }

    public NBTTagCompound saveAndClearPlantedContents (IBlockAccess world, int x, int y, int z) {
        TileEntityGarden te = getTileEntity(world, x, y, z);
        if (te == null)
            return null;

        NBTTagCompound tag = new NBTTagCompound();
        te.writeToNBT(tag);

        clearPlantedContents(world, x, y, z);
        return tag;
    }

    public void restorePlantedContents (IBlockAccess world, int x, int y, int z, NBTTagCompound tag) {
        TileEntityGarden te = getTileEntity(world, x, y, z);
        if (te == null)
            return;

        te.readFromNBT(tag);
        validateBlockState(te);
    }

    public Block getPlantBlockFromSlot (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        TileEntityGarden te = getTileEntity(blockAccess, x, y, z);

        ItemStack stack = te.getPlantInSlot(slot);
        if (stack == null || stack.getItem() == null)
            return null;

        return Block.getBlockFromItem(stack.getItem());
    }

    public int getPlantMetaFromSlot (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        TileEntityGarden te = getTileEntity(blockAccess, x, y, z);

        ItemStack stack = te.getPlantInSlot(slot);
        if (stack == null || stack.getItem() == null)
            return 0;

        return stack.getItemDamage();
    }

    public static void validateBlockState (TileEntityGarden tileEntity) {
        Block baseBlock = tileEntity.getWorldObj().getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        if (!(baseBlock instanceof BlockGarden))
            return;

        BlockGarden garden = (BlockGarden) baseBlock;
        int plantHeight = garden.getAggregatePlantHeight(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

        World world = tileEntity.getWorldObj();
        int x = tileEntity.xCoord;
        int y = tileEntity.yCoord + 1;
        int z = tileEntity.zCoord;

        for (int yLimit = y + plantHeight; y < yLimit; y++) {
            Block block = world.getBlock(x, y, z);
            if (block.isAir(world, x, y, z))
                world.setBlock(x, y, z, ModBlocks.gardenProxy, 0, 3);

            world.func_147479_m(x, y, z); // markBlockForRenderUpdate
        }

        while (world.getBlock(x, y, z) instanceof BlockGardenProxy)
            world.setBlockToAir(x, y++, z);
    }

    public int getAggregatePlantHeight (IBlockAccess blockAccess, int x, int y, int z) {
        TileEntityGarden garden = getTileEntity(blockAccess, x, y, z);

        int height = 0;
        for (int slot : slotProfile.getPlantSlots()) {
            ItemStack item = garden.getStackInSlot(slot);
            PlantItem plant = PlantItem.getForItem(blockAccess, item);
            if (plant == null)
                continue;

            height = Math.max(height, plant.getPlantHeight());
        }

        return height;
    }

    public final boolean applyItemToGarden (World world, int x, int y, int z, EntityPlayer player, ItemStack itemStack, float hitX, float hitY, float hitZ) {
        return applyItemToGarden(world, x, y, z, player, itemStack, hitX, hitY, hitZ, true);
    }

    public final boolean applyItemToGarden (World world, int x, int y, int z, EntityPlayer player, ItemStack itemStack) {
        return applyItemToGarden(world, x, y, z, player, itemStack, 0, 0, 0, false);
    }

    protected boolean applyItemToGarden (World world, int x, int y, int z, EntityPlayer player, ItemStack itemStack, float hitX, float hitY, float hitZ, boolean hitValid) {
        ItemStack item = (itemStack == null) ? player.inventory.getCurrentItem() : itemStack;
        if (item == null)
            return false;

        int slot = getSlot(world, x, y, z, player, hitX, hitY, hitZ);

        if (applyTestKitToGarden(world, x, y, z, slot, item))
            return true;

        PlantItem plant = PlantItem.getForItem(world, item);

        if (plant != null) {
            int plantSlot = hitValid
                ? getEmptySlotForPlant(world, x, y, z, player, plant, hitX, hitY, hitZ)
                : getEmptySlotForPlant(world, x, y, z, player, plant);

            if (plantSlot == TileEntityGarden.SLOT_INVALID)
                return false;
            if (canSustainPlantIndependently(world, x, y, z, plant))
                return false;

            return applyPlantToGarden(world, x, y, z, (itemStack == null) ? player : null, plantSlot, plant);
        }

        return false;
    }

    public boolean canSustainPlantIndependently (IBlockAccess blockAccess, int x, int y, int z, PlantItem plant) {
        Item item = plant.getPlantSourceItem().getItem();
        if (item instanceof IPlantable)
            return canSustainPlant(blockAccess, x, y, z, ForgeDirection.UP, (IPlantable) item);

        return false;
    }

    public abstract ItemStack getGardenSubstrate (IBlockAccess blockAccess, int x, int y, int z, int slot);

    protected int getSlot (World world, int x, int y, int z, EntityPlayer player, float hitX, float hitY, float hitZ) {
        return TileEntityGarden.SLOT_INVALID;
    }

    protected int getEmptySlotForPlant (World world, int x, int y, int z, EntityPlayer player, PlantItem plant) {
        return TileEntityGarden.SLOT_INVALID;
    }

    protected int getEmptySlotForPlant (World world, int x, int y, int z, EntityPlayer player, PlantItem plant, float hitX, float hitY, float hitZ) {
        return getEmptySlotForPlant(world, x, y, z, player, plant);
    }

    protected boolean enoughSpaceAround (IBlockAccess blockAccess, int x, int y, int z, int slot, PlantItem plant) {
        int height = plant.getPlantHeight();

        boolean enough = true;
        for (int i = 0; i < height; i++)
            enough &= blockAccess.isAirBlock(x, y + 1 + i, z) || blockAccess.getBlock(x, y + 1 + i, z) instanceof IPlantProxy;

        return enough;
    }

    protected boolean isPlantValidForSubstrate (ItemStack substrate, PlantItem plant) {
        return true;
    }

    public boolean isPlantValidForSlot (World world, int x, int y, int z, int slot, PlantItem plant) {
        if (plant == null)
            return false;

        if (plant.getPlantBlock() instanceof BlockContainer)
            return false;

        if (!slotProfile.isPlantValidForSlot(world, x, y, z, slot, plant))
            return false;

        if (!enoughSpaceAround(world, x, y, z, slot, plant))
            return false;

        if (!isPlantValidForSubstrate(getGardenSubstrate(world, x, y, z, slot), plant))
            return false;

        if (canSustainPlantIndependently(world, x, y, z, plant))
            return false;

        return true;
    }

    public boolean applyHoe (World world, int x, int y, int z) {
        return false;
    }

    protected boolean applyPlantToGarden (World world, int x, int y, int z, EntityPlayer player, int slot, PlantItem plant) {
        if (!isPlantValidForSlot(world, x, y, z, slot, plant))
            return false;

        TileEntityGarden garden = getTileEntity(world, x, y, z);

        garden.setInventorySlotContents(slot, plant.getPlantSourceItem().copy());

        if (player != null && !player.capabilities.isCreativeMode) {
            ItemStack currentItem = player.inventory.getCurrentItem();
            if (--currentItem.stackSize <= 0)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }

        return true;
    }

    protected boolean applyTestKitToGarden (World world, int x, int y, int z, int slot, ItemStack testKit) {
        if (testKit == null || testKit.getItem() != ModItems.usedSoilTestKit)
            return false;

        ItemStack substrateStack = getGardenSubstrate(world, x, y, z, slot);
        if (substrateStack == null || substrateStack.getItem() == null)
            return false;

        TileEntityGarden tileEntity = getTileEntity(world, x, y, z);
        if (tileEntity == null)
            return false;

        tileEntity.setBiomeData(testKit.getItemDamage());
        world.markBlockForUpdate(x, y, z);

        for (int i = 0; i < 5; i++) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            world.spawnParticle("happyVillager", x + world.rand.nextFloat(), y + .5f + world.rand.nextFloat(), z + world.rand.nextFloat(), d0, d1, d2);
        }

        return true;
    }

    public TileEntityGarden getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityGarden) ? (TileEntityGarden) te : null;
    }
}
