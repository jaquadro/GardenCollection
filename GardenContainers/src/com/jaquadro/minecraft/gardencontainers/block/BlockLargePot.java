package com.jaquadro.minecraft.gardencontainers.block;

import com.jaquadro.minecraft.gardencontainers.GardenContainers;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityLargePot;
import com.jaquadro.minecraft.gardencontainers.config.PatternConfig;
import com.jaquadro.minecraft.gardencontainers.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import com.jaquadro.minecraft.gardencore.api.plant.IPlantInfo;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenConnected;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockLargePot extends BlockGarden
{
    @SideOnly(Side.CLIENT)
    private IIcon[] iconOverlayArray;

    private int scratchDropMetadata;

    public BlockLargePot (String blockName) {
        super(blockName, Material.clay);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(.5f);
        setResistance(5f);
        setStepSound(Block.soundTypeStone);
    }

    public abstract String[] getSubTypes ();

    @Override
    public ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z) {
        TileEntityGarden te = getTileEntity(world, x, y, z);
        return (te != null) ? te.getSubstrate() : null;
    }

    @Override
    public float getPlantOffsetY (IBlockAccess world, int x, int y, int z, int slot) {
        return -.0625f;
    }

    @Override
    protected int getSlot (World world, int x, int y, int z, int side, EntityPlayer player, float hitX, float hitY, float hitZ, IPlantable plant) {
        Block block = plant.getPlant(world, 0, 0, 0);
        int meta = plant.getPlantMetadata(world, 0, 0, 0);

        IPlantInfo info = PlantRegistry.instance().getPlantInfo(block, meta);
        if (info.getPlantTypeClass(block, meta) == PlantType.GROUND_COVER)
            return TileEntityGardenConnected.SLOT_COVER;

        return TileEntityGardenConnected.SLOT_CENTER;
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        float dim = .0625f;

        TileEntityLargePot te = getTileEntity(world, x, y, z);
        if (te == null || te.getSubstrate() == null || !isSubstrateSolid(te.getSubstrate().getItem()))
            setBlockBounds(0, 0, 0, 1, dim, 1);
        else
            setBlockBounds(0, 0, 0, 1, 1 - dim, 1);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);

        if (!te.isAttachedNeighbor(x - 1, y, z)) {
            setBlockBounds(0, 0, 0, dim, 1, 1);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        if (!te.isAttachedNeighbor(x, y, z - 1)) {
            setBlockBounds(0, 0, 0, 1, 1, dim);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        if (!te.isAttachedNeighbor(x + 1, y, z)) {
            setBlockBounds(1 - dim, 0, 0, 1, 1, 1);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        if (!te.isAttachedNeighbor(x, y, z + 1)) {
            setBlockBounds(0, 0, 1 - dim, 1, 1, 1);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }

        setBlockBoundsForItemRender();
    }

    @Override
    public void setBlockBoundsForItemRender () {
        setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.largePotRenderID;
    }

    @Override
    public boolean canRenderInPass (int pass) {
        ClientProxy.renderPass = pass;
        return true;
    }

    @Override
    public boolean isSideSolid (IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return side != ForgeDirection.UP;
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess blockAccess, int x, int y, int z, int side) {
        int nx = x;
        int nz = z;

        switch (side) {
            case 0:
                y++;
                break;
            case 1:
                y--;
                break;
            case 2:
                z++;
                break;
            case 3:
                z--;
                break;
            case 4:
                x++;
                break;
            case 5:
                x--;
                break;
        }

        if (side >= 2 && side < 6) {
            TileEntityGarden te = getTileEntity(blockAccess, x, y, z);
            if (te != null)
                return !te.isAttachedNeighbor(nx, y, nz);
        }

        return side != 1;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        TileEntityLargePot te = getTileEntity(world, x, y, z);

        if (te != null)
            scratchDropMetadata = te.getCarving() << 8;

        super.breakBlock(world, x, y, z, block, data);
    }

    @Override
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for (int i = 0; i < count; i++) {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
                items.add(new ItemStack(item, 1, metadata | scratchDropMetadata));
        }

        return items;
    }

    @Override
    protected boolean isValidSubstrate (ItemStack itemStack) {
        if (itemStack.getItem() == Items.water_bucket)
            return true;

        Block block = Block.getBlockFromItem(itemStack.getItem());
        if (block == null)
            return false;

        if (block == Blocks.water)
            return true;

        return super.isValidSubstrate(itemStack);
    }

    private boolean isSubstrateSolid (Item item) {
        Block block = Block.getBlockFromItem(item);
        return block != Blocks.water;
    }

    @Override
    protected void applySubstrate (World world, int x, int y, int z, TileEntityGarden tileEntity, EntityPlayer player) {
        ItemStack substrate = player.inventory.getCurrentItem();

        if (substrate.getItem() == Items.water_bucket) {
            tileEntity.setSubstrate(new ItemStack(Blocks.water));
            tileEntity.markDirty();

            if (!player.capabilities.isCreativeMode)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));

            world.markBlockForUpdate(x, y, z);
            return;
        }

        super.applySubstrate(world, x, y, z, tileEntity, player);
    }

    @Override
    protected boolean canApplyItemToSubstrate (TileEntityGarden tileEntity, ItemStack itemStack) {
        return itemStack.getItem() == Items.water_bucket
            //|| itemStack.getItem() == ModItems.usedSoilTestKit
            || itemStack.getItem() == Items.bucket
            || itemStack.getItem() instanceof ItemHoe
            || itemStack.getItem() instanceof ItemTool
            || super.canApplyItemToSubstrate(tileEntity, itemStack);
    }

    @Override
    protected void applyItemToSubstrate (World world, int x, int y, int z, TileEntityGarden tileEntity, EntityPlayer player) {
        if (tileEntity.getSubstrate() == null)
            return;

        ItemStack item = player.inventory.getCurrentItem();
        if (item.getItem() == Items.bucket) {
            if (Block.getBlockFromItem(tileEntity.getSubstrate().getItem()) == Blocks.water) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.water_bucket));
                tileEntity.setSubstrate(null);
                tileEntity.markDirty();
                world.markBlockForUpdate(x, y, z);
            }
            return;
        }

        /*if (item.getItem() instanceof ItemTool && item.getItem().getToolClasses(item).contains("shovel")) {
            if (Block.getBlockFromItem(tileEntity.getSubstrate()) != Blocks.water) {
                ItemStack drop = new ItemStack(tileEntity.getSubstrate(), 1, tileEntity.getSubstrateOriginalData());
                dropBlockAsItem(world, x, y, z, drop);

                if (tile.getFlowerPotItem() != null) {
                    drop = new ItemStack(tile.getFlowerPotItem(), 1, tile.getFlowerPotData());
                    dropBlockAsItem(world, x, y, z, drop);
                }

                tile.setSubstrate(null, 0);
                tile.setItem(null, 0);
                tile.markDirty();
                world.markBlockForUpdate(x, y, z);

                world.playSoundEffect(x + .5f, y + .5f, z + .5f, Blocks.dirt.stepSound.getStepResourcePath(),
                    (Blocks.dirt.stepSound.getVolume() + 1) / 2f, Blocks.dirt.stepSound.getPitch() * .8f);

                if (world.getBlock(x, y + 1, z) == ModBlocks.largePotPlantProxy)
                    world.setBlockToAir(x, y + 1, z);
            }
            return;
        }*/

        if (item.getItem() == Items.water_bucket) {
            applyWaterToSubstrate(world, x, y, z, tileEntity, player);
            return;
        }
        else if (item.getItem() instanceof ItemHoe) {
            applyHoeToSubstrate(world, x, y, z, tileEntity, player);
            return;
        }
        //else if (item.getItem() == ModItems.usedSoilTestKit)
        //    applyTestKit(world, x, y, z, item);

        super.applyItemToSubstrate(world, x, y, z, tileEntity, player);
    }

    protected void applyWaterToSubstrate (World world, int x, int y, int z, TileEntityGarden tile, EntityPlayer player) {
        if (Block.getBlockFromItem(tile.getSubstrate().getItem()) == Blocks.dirt) {
            tile.setSubstrate(new ItemStack(Blocks.farmland, 1, 1), new ItemStack(Blocks.dirt, 1, tile.getSubstrate().getItemDamage()));
            tile.markDirty();

            world.markBlockForUpdate(x, y, z);
        }
    }

    protected void applyHoeToSubstrate (World world, int x, int y, int z, TileEntityGarden tile, EntityPlayer player) {
        Block substrate = Block.getBlockFromItem(tile.getSubstrate().getItem());
        if (substrate == Blocks.dirt || substrate == Blocks.grass) {
            tile.setSubstrate(new ItemStack(Blocks.farmland, 1, 1), new ItemStack(Blocks.dirt, 1, tile.getSubstrate().getItemDamage()));
            tile.markDirty();

            world.markBlockForUpdate(x, y, z);
            world.playSoundEffect(x + .5f, y + .5f, z + .5f, Blocks.farmland.stepSound.getStepResourcePath(),
                (Blocks.farmland.stepSound.getVolume() + 1) / 2f, Blocks.farmland.stepSound.getPitch() * .8f);
        }
    }

    @Override
    public TileEntityLargePot getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityLargePot) ? (TileEntityLargePot) te : null;
    }

    @Override
    public TileEntityLargePot createNewTileEntity (World world, int data) {
        return new TileEntityLargePot();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getOverlayIcon (int data) {
        if (iconOverlayArray[data] != null)
            return iconOverlayArray[data];

        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        iconOverlayArray = new IIcon[256];
        for (int i = 1; i < iconOverlayArray.length; i++) {
            PatternConfig pattern = GardenContainers.config.getPattern(i);
            if (pattern != null && pattern.getOverlay() != null)
                iconOverlayArray[i] = iconRegister.registerIcon(GardenContainers.MOD_ID + ":" + pattern.getOverlay());
        }
    }
}
