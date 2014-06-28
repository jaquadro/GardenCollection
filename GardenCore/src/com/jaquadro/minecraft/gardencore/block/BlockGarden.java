package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
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

    public float getPlantOffsetY () {
        return 0;
    }

    public abstract ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z);

    protected boolean canApplyItemToSubstrate (TileEntityGarden tileEntity, ItemStack itemStack) {
        return false;
    }

    protected void applyItemToSubstrate (World world, int x, int y, int z, TileEntityGarden tileEntity, EntityPlayer player) { }

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
                return substrate == Blocks.grass || substrate == Blocks.dirt;
            case Beach:
                return substrate == Blocks.grass || substrate == Blocks.dirt || substrate == Blocks.sand;
            case Water:
                return substrate == Blocks.water;
            default:
                return false;
        }
    }

    protected void applyPlantable (World world, int x, int y, int z, TileEntityGarden tileEntity, EntityPlayer player, IPlantable plantable) {
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

        /*world.setBlock(x, y + 1, z, ModBlocks.gardenProxy, itemMeta, 3);
        if (itemBlock instanceof BlockDoublePlant || itemBlock.getRenderType() == 40)
            world.setBlock(x, y + 2, z, ModBlocks.gardenProxy, itemMeta | 8, 3);*/

        tileEntity.setCenterPlant(itemStack.getItem(), itemMeta);

        if (!player.capabilities.isCreativeMode && --itemStack.stackSize <= 0)
            player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
    }

    private boolean enoughAirAbove (IBlockAccess world, int x, int y, int z, IPlantable plant) {
        Block plantBlock = plant.getPlant(world, x, y + 1, z);
        int height = (plantBlock instanceof BlockDoublePlant) ? 2 : 1;

        boolean enough = true;
        for (int i = 0; i < height; i++)
            enough &= world.isAirBlock(x, y + 1 + i, z);

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
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
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

        if (canApplyItemToSubstrate(tileEntity, itemStack)) {
            applyItemToSubstrate(world, x, y, z, tileEntity, player);
            return true;
        }
        else if (plantable != null && canSustainPlantable(world, x, y, z, plantable)) {
            if (!enoughAirAbove(world, x, y, z, plantable))
                return false;

            applyPlantable(world, x, y, z, tileEntity, player, plantable);
            return true;
        }

        return false;
    }

    public TileEntityGarden getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityGarden) ? (TileEntityGarden) te : null;
    }
}
