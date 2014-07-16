package com.jaquadro.minecraft.gardencontainers.block;

import com.jaquadro.minecraft.gardencontainers.GardenContainers;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityLargePot;
import com.jaquadro.minecraft.gardencontainers.config.PatternConfig;
import com.jaquadro.minecraft.gardencontainers.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.BlockGardenContainer;
import com.jaquadro.minecraft.gardencore.block.support.BasicSlotProfile;
import com.jaquadro.minecraft.gardencore.block.support.ContainerConnectionProfile;
import com.jaquadro.minecraft.gardencore.block.support.SlotShare8Profile;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
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
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockLargePot extends BlockGardenContainer
{
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

    @SideOnly(Side.CLIENT)
    private IIcon[] iconOverlayArray;

    private int scratchDropMetadata;

    public BlockLargePot (String blockName) {
        super(blockName, Material.clay);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(.5f);
        setResistance(5f);
        setStepSound(Block.soundTypeStone);

        connectionProfile = new ContainerConnectionProfile();
        slotShareProfile = new SlotShare8Profile(SLOT_TOP_LEFT, SLOT_TOP, SLOT_TOP_RIGHT, SLOT_RIGHT, SLOT_BOTTOM_RIGHT, SLOT_BOTTOM, SLOT_BOTTOM_LEFT, SLOT_LEFT);

        PlantType[] commonType = new PlantType[] { PlantType.GROUND, PlantType.AQUATIC_COVER, PlantType.AQUATIC_SURFACE};

        PlantSize[] smallSize = new PlantSize[] { PlantSize.SMALL };
        PlantSize[] commonSize = new PlantSize[] { PlantSize.LARGE, PlantSize.SMALL };
        PlantSize[] allSize = new PlantSize[] { PlantSize.FULL, PlantSize.LARGE, PlantSize.SMALL };

        slotProfile = new BasicSlotProfile(new BasicSlotProfile.Slot[] {
            new BasicSlotProfile.Slot(SLOT_CENTER, commonType, allSize),
            new BasicSlotProfile.Slot(SLOT_COVER, new PlantType[] { PlantType.GROUND_COVER}, allSize),
            new BasicSlotProfile.Slot(SLOT_NW, commonType, smallSize),
            new BasicSlotProfile.Slot(SLOT_NE, commonType, smallSize),
            new BasicSlotProfile.Slot(SLOT_SW, commonType, smallSize),
            new BasicSlotProfile.Slot(SLOT_SE, commonType, smallSize),
            new BasicSlotProfile.Slot(SLOT_TOP_LEFT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_TOP, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_TOP_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_BOTTOM_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_BOTTOM, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_BOTTOM_LEFT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_LEFT, commonType, commonSize),
        });
    }

    public abstract String[] getSubTypes ();

    @Override
    public float getPlantOffsetY (IBlockAccess world, int x, int y, int z, int slot) {
        return -.0625f;
    }

    @Override
    protected int getSlot (World world, int x, int y, int z, EntityPlayer player, float hitX, float hitY, float hitZ) {
        return SLOT_CENTER;
    }

    @Override
    protected int getEmptySlotForPlant (World world, int x, int y, int z, EntityPlayer player, PlantItem plant) {
        if (plant.getPlantTypeClass() == PlantType.GROUND_COVER)
            return SLOT_COVER;

        return SLOT_CENTER;
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
    public int getRenderBlockPass () {
        return 1;
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

    private boolean isSubstrateSolid (Item item) {
        Block block = Block.getBlockFromItem(item);
        return block != Blocks.water;
    }

    @Override
    protected boolean applySubstrateToGarden (World world, int x, int y, int z, EntityPlayer player, int slot, ItemStack itemStack) {
        if (getGardenSubstrate(world, x, y, z, slot) != null)
            return false;

        if (itemStack.getItem() == Items.water_bucket) {
            TileEntityGarden garden = getTileEntity(world, x, y, z);
            garden.setSubstrate(new ItemStack(Blocks.water));
            garden.markDirty();

            if (player != null && !player.capabilities.isCreativeMode)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));

            world.markBlockForUpdate(x, y, z);
            return true;
        }

        return super.applySubstrateToGarden(world, x, y, z, player, slot, itemStack);
    }

    @Override
    protected boolean applyItemToGarden (World world, int x, int y, int z, EntityPlayer player, ItemStack itemStack, float hitX, float hitY, float hitZ, boolean hitValid) {
        TileEntityGarden garden = getTileEntity(world, x, y, z);

        if (garden.getSubstrate() != null) {
            ItemStack item = (itemStack == null) ? player.inventory.getCurrentItem() : itemStack;
            if (item.getItem() == Items.bucket) {
                if (Block.getBlockFromItem(garden.getSubstrate().getItem()) == Blocks.water) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.water_bucket));
                    garden.setSubstrate(null);
                    garden.markDirty();
                    world.markBlockForUpdate(x, y, z);
                }
                return true;
            }

            if (item.getItem() == Items.water_bucket) {
                applyWaterToSubstrate(world, x, y, z, garden, player);
                return true;
            }
            else if (item.getItem() instanceof ItemHoe) {
                applyHoeToSubstrate(world, x, y, z, garden, player);
                return true;
            }
        }

        return super.applyItemToGarden(world, x, y, z, player, itemStack, hitX, hitY, hitZ, hitValid);
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
