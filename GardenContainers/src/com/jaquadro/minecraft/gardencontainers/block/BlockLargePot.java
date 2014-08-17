package com.jaquadro.minecraft.gardencontainers.block;

import com.jaquadro.minecraft.gardencontainers.GardenContainers;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityLargePot;
import com.jaquadro.minecraft.gardencontainers.config.PatternConfig;
import com.jaquadro.minecraft.gardencontainers.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.BlockGardenContainer;
import com.jaquadro.minecraft.gardencore.block.support.*;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockLargePot extends BlockGardenContainer
{
    private class LocalSlotProfile extends Slot14Profile
    {
        public LocalSlotProfile (Slot[] slots) {
            super(slots);
        }

        @Override
        public float getPlantOffsetY (IBlockAccess blockAccess, int x, int y, int z, int slot) {
            return -.0625f;
        }

        @Override
        public Object openPlantGUI (InventoryPlayer playerInventory, TileEntityGarden gardenTile, boolean client) {
            if (gardenTile.getSubstrate() == null)
                return null;

            return super.openPlantGUI(playerInventory, gardenTile, client);
        }
    }

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
        slotShareProfile = new SlotShare8Profile(Slot14Profile.SLOT_TOP_LEFT, Slot14Profile.SLOT_TOP,
            Slot14Profile.SLOT_TOP_RIGHT, Slot14Profile.SLOT_RIGHT, Slot14Profile.SLOT_BOTTOM_RIGHT,
            Slot14Profile.SLOT_BOTTOM, Slot14Profile.SLOT_BOTTOM_LEFT, Slot14Profile.SLOT_LEFT);

        PlantType[] commonType = new PlantType[] { PlantType.GROUND, PlantType.AQUATIC_COVER, PlantType.AQUATIC_SURFACE};

        PlantSize[] smallSize = new PlantSize[] { PlantSize.SMALL };
        PlantSize[] commonSize = new PlantSize[] { PlantSize.LARGE, PlantSize.MEDIUM, PlantSize.SMALL };
        PlantSize[] allSize = new PlantSize[] { PlantSize.FULL, PlantSize.LARGE, PlantSize.MEDIUM, PlantSize.SMALL };

        slotProfile = new LocalSlotProfile(new LocalSlotProfile.Slot[] {
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_CENTER, commonType, allSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_COVER, new PlantType[] { PlantType.GROUND_COVER}, allSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_NW, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_NE, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_SW, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_SE, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_TOP_LEFT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_TOP, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_TOP_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_BOTTOM_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_BOTTOM, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_BOTTOM_LEFT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_LEFT, commonType, commonSize),
        });
    }

    public abstract String[] getSubTypes ();

    @Override
    protected int getSlot (World world, int x, int y, int z, EntityPlayer player, float hitX, float hitY, float hitZ) {
        return Slot14Profile.SLOT_CENTER;
    }

    @Override
    protected int getEmptySlotForPlant (World world, int x, int y, int z, EntityPlayer player, PlantItem plant) {
        TileEntityGarden garden = getTileEntity(world, x, y, z);

        if (plant.getPlantTypeClass() == PlantType.GROUND_COVER)
            return garden.getStackInSlot(Slot14Profile.SLOT_COVER) == null ? Slot14Profile.SLOT_COVER : SLOT_INVALID;

        if (plant.getPlantSizeClass() == PlantSize.FULL)
            return garden.getStackInSlot(Slot14Profile.SLOT_CENTER) == null ? Slot14Profile.SLOT_CENTER : SLOT_INVALID;

        if (garden.getStackInSlot(Slot14Profile.SLOT_CENTER) == null)
            return Slot14Profile.SLOT_CENTER;

        if (plant.getPlantSizeClass() == PlantSize.SMALL) {
            for (int slot : new int[] { Slot14Profile.SLOT_NE, Slot14Profile.SLOT_SW, Slot14Profile.SLOT_NW, Slot14Profile.SLOT_SE }) {
                if (garden.getStackInSlot(slot) == null)
                    return slot;
            }
        }

        for (int slot : new int[] { Slot14Profile.SLOT_LEFT, Slot14Profile.SLOT_RIGHT, Slot14Profile.SLOT_TOP,
            Slot14Profile.SLOT_BOTTOM, Slot14Profile.SLOT_TOP_LEFT, Slot14Profile.SLOT_BOTTOM_RIGHT,
            Slot14Profile.SLOT_TOP_RIGHT, Slot14Profile.SLOT_BOTTOM_LEFT }) {
            if (!garden.isSlotValid(slot))
                continue;
            if (garden.getStackInSlot(slot) == null)
                return slot;
        }

        return SLOT_INVALID;
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
    public boolean canSustainPlant (IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        TileEntityGarden gardenTile = getTileEntity(world, x, y, z);
        EnumPlantType plantType = plantable.getPlantType(world, x, y, z);

        if (plantType == EnumPlantType.Crop)
            return substrateSupportsCrops(gardenTile.getSubstrate());

        return false;
    }

    protected boolean substrateSupportsCrops (ItemStack substrate) {
        if (substrate.getItem() == null)
            return false;

        if (Block.getBlockFromItem(substrate.getItem()) == ModBlocks.gardenFarmland)
            return true;
        if (Block.getBlockFromItem(substrate.getItem()) == Blocks.farmland)
            return true;

        return false;
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
        ItemStack item = (itemStack == null) ? player.inventory.getCurrentItem() : itemStack;
        if (item == null)
            return false;

        TileEntityGarden garden = getTileEntity(world, x, y, z);

        if (garden.getSubstrate() != null) {
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
        if (substrate == Blocks.dirt || substrate == Blocks.grass)
            tile.setSubstrate(new ItemStack(Blocks.farmland, 1, 1), new ItemStack(Blocks.dirt, 1, tile.getSubstrate().getItemDamage()));
        else if (substrate == ModBlocks.gardenSoil)
            tile.setSubstrate(new ItemStack(ModBlocks.gardenFarmland), new ItemStack(ModBlocks.gardenSoil));
        else
            return;

        tile.markDirty();

        world.markBlockForUpdate(x, y, z);
        world.playSoundEffect(x + .5f, y + .5f, z + .5f, Blocks.farmland.stepSound.getStepResourcePath(),
            (Blocks.farmland.stepSound.getVolume() + 1) / 2f, Blocks.farmland.stepSound.getPitch() * .8f);
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
