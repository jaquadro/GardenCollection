package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModItems;
import com.jaquadro.minecraft.gardencore.core.handlers.GuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockGarden extends BlockContainer
{
    protected BlockGarden (String blockName, Material material) {
        super(material);

        setBlockName(blockName);
    }

    public float getPlantOffsetY (IBlockAccess world, int x, int y, int z, int slot) {
        return 0;
    }

    public abstract ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z);

    protected void applySubstrate (World world, int x, int y, int z, TileEntityGarden tileEntity, EntityPlayer player) {
        ItemStack substrate = player.inventory.getCurrentItem();

        tileEntity.setSubstrate(substrate);
        if (Block.getBlockFromItem(substrate.getItem()) == Blocks.farmland)
            tileEntity.setSubstrate(substrate, new ItemStack(Blocks.farmland, 1, 1));

        tileEntity.markDirty();
        if (!player.capabilities.isCreativeMode && --substrate.stackSize <= 0)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

        world.markBlockForUpdate(x, y, z);
    }

    protected boolean canApplyItemToSubstrate (TileEntityGarden tileEntity, ItemStack itemStack) {
        return itemStack.getItem() == ModItems.usedSoilTestKit;
    }

    protected void applyItemToSubstrate (World world, int x, int y, int z, TileEntityGarden tileEntity, EntityPlayer player) {
        ItemStack item = player.inventory.getCurrentItem();

        if (item.getItem() == ModItems.usedSoilTestKit)
            applyTestKit(world, x, y, z, item);
    }

    public boolean applyTestKit (World world, int x, int y, int z, ItemStack testKit) {
        if (testKit.getItem() != ModItems.usedSoilTestKit)
            return false;

        ItemStack substrateStack = getGardenSubstrate(world, x, y, z);
        if (substrateStack == null || substrateStack.getItem() == null)
            return false;

        Block substrate = Block.getBlockFromItem(substrateStack.getItem());
        if (substrate != Blocks.dirt && substrate != Blocks.grass && substrate != Blocks.farmland)
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

    protected int getSlot (World world, int x, int y, int z, int side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        return TileEntityGarden.SLOT_CENTER;
    }

    protected boolean isValidSubstrate (ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null)
            return false;

        Block block = Block.getBlockFromItem(itemStack.getItem());
        if (block == null)
            return false;

        return block == Blocks.dirt
            || block == Blocks.sand
            || block == Blocks.gravel
            || block == Blocks.soul_sand
            || block == Blocks.grass
            || block == Blocks.water
            || block == Blocks.farmland;
    }

    protected boolean canSustainPlantable (IBlockAccess world, int x, int y, int z, IPlantable plantable) {
        ItemStack substrateStack = getGardenSubstrate(world, x, y, z);
        if (substrateStack == null)
            return false;

        Block substrate = Block.getBlockFromItem(substrateStack.getItem());
        if (substrate == null)
            return false;

        EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
        Block plant = plantable.getPlant(world, x, y + 1, z);

        if (plant == null && plantable instanceof Block)
            plant = (Block) plantable;

        if (plant != null) {
            ItemStack plantItem = new ItemStack(plant, 1, plantable.getPlantMetadata(world, x, y, z));
            if (PlantRegistry.instance().isPlantBlacklisted(plantItem))
                return false;
        }

        if (plantType == null)
            plantType = EnumPlantType.Plains;

        if (plant == Blocks.cactus)
            return false;

        switch (plantType) {
            case Desert:
                return substrate == Blocks.sand;
            case Nether:
                return substrate == Blocks.soul_sand;
            //case Crop:
            //    return substrate == Blocks.dirt || substrate == Blocks.farmland;
            case Cave:
                return true;
            case Plains:
                return substrate == Blocks.grass || substrate == Blocks.dirt || substrate == Blocks.farmland;
            case Beach:
                return substrate == Blocks.grass || substrate == Blocks.dirt || substrate == Blocks.sand;
            case Water:
                return substrate == Blocks.water;
            default:
                return false;
        }
    }

    protected void applyPlantable (World world, int x, int y, int z, TileEntityGarden tileEntity, EntityPlayer player, IPlantable plantable, int slot) {
        ItemStack itemStack = player.inventory.getCurrentItem();

        Block itemBlock = plantable.getPlant(world, x, y, z);
        int itemMeta = itemStack.getItemDamage();
        if (itemBlock == null && plantable instanceof Block) {
            itemBlock = (Block) plantable;
        }
        else {
            int plantMeta = plantable.getPlantMetadata(world, x, y, z);
            if (plantMeta > 0)
                itemMeta = plantMeta;
        }

        tileEntity.setInventorySlotContents(slot, new ItemStack(itemBlock, 1, itemMeta));

        if (!player.capabilities.isCreativeMode && --itemStack.stackSize <= 0)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
    }

    private boolean enoughAirAbove (IBlockAccess world, int x, int y, int z, IPlantable plant) {
        Block plantBlock = plant.getPlant(world, x, y + 1, z);
        int height = (plantBlock instanceof BlockDoublePlant) ? 2 : 1;

        boolean enough = true;
        for (int i = 0; i < height; i++)
            enough &= world.isAirBlock(x, y + 1 + i, z) || world.getBlock(x, y + 1 + i, z) instanceof BlockGardenProxy;

        return enough;
    }

    public static void validateBlockState (TileEntityGarden tileEntity) {
        int plantHeight = tileEntity.getPlantHeight();

        World world = tileEntity.getWorldObj();
        //world.markBlockForUpdate(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

        int x = tileEntity.xCoord;
        int y = tileEntity.yCoord + 1;
        int z = tileEntity.zCoord;

        for (int yLimit = y + plantHeight; y < yLimit; y++) {
            Block block = world.getBlock(x, y, z);
            if (block.isAir(world, x, y, z))
                world.setBlock(x, y, z, ModBlocks.gardenProxy, 0, 3);
            //world.markBlockForUpdate(x, y, z);
            world.func_147479_m(x, y, z); // markBlockForRenderUpdate
        }

        while (world.getBlock(x, y, z) instanceof BlockGardenProxy) {
            world.setBlockToAir(x, y++, z);
        }
    }

    @Override
    public TileEntityGarden createNewTileEntity (World var1, int var2) {
        return new TileEntityGarden();
    }

    @Override
    public boolean canSustainPlant (IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        ItemStack substrateStack = getGardenSubstrate(world, x, y, z);
        if (substrateStack == null)
            return false;

        EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);
        Block plant = plantable.getPlant(world, x, y + 1, z);
        Block substrate = Block.getBlockFromItem(substrateStack.getItem());

        ItemStack plantItem = new ItemStack(plant, 1, plantable.getPlantMetadata(world, x, y, z));
        if (PlantRegistry.instance().isPlantBlacklisted(plantItem))
            return false;

        if (plant == Blocks.cactus)
            return substrate == Blocks.sand;

        return plantType == EnumPlantType.Crop && substrate == Blocks.farmland;
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block block) {
        if (world.isRemote)
            return;

        if (y >= world.getHeight() - 1)
            return;

        /*Block overBlock = world.getBlock(x, y + 1, z);
        if (overBlock.isAir(world, x, y, z)) {
            TileEntityGarden tileEntity = getTileEntity(world, x, y, z);
            if (tileEntity != null)
                tileEntity.clearPlantedContents();
        }*/
    }

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
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (side != ForgeDirection.UP.ordinal())
            return false;

        ItemStack itemStack = player.inventory.getCurrentItem();

        TileEntityGarden tileEntity = getTileEntity(world, x, y, z);
        if (tileEntity == null) {
            tileEntity = createNewTileEntity(world, 0);
            world.setTileEntity(x, y, z, tileEntity);
        }

        IPlantable plantable = null;
        Item item = itemStack.getItem();
        if (item instanceof IPlantable)
            plantable = (IPlantable) item;
        else if (item instanceof ItemBlock) {
            Block itemBlock = Block.getBlockFromItem(item);
            if (itemBlock instanceof IPlantable)
                plantable = (IPlantable) itemBlock;
        }

        if (getGardenSubstrate(world, x, y, z) == null && isValidSubstrate(itemStack)) {
            applySubstrate(world, x, y, z, tileEntity, player);
            return true;
        }
        if (canApplyItemToSubstrate(tileEntity, itemStack)) {
            applyItemToSubstrate(world, x, y, z, tileEntity, player);
            return true;
        }
        else if (plantable != null && canSustainPlantable(world, x, y, z, plantable)) {
            int slot = getSlot(world, x, y, z, side, player, hitX, hitY, hitZ);
            if (slot == TileEntityGarden.SLOT_INVALID)
                return false;
            if (tileEntity.getStackInSlot(slot) != null)
                return false;

            if (!enoughAirAbove(world, x, y, z, plantable))
                return false;

            applyPlantable(world, x, y, z, tileEntity, player, plantable, slot);
            return true;
        }

        return false;
    }

    public TileEntityGarden getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityGarden) ? (TileEntityGarden) te : null;
    }
}
