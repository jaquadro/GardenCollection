package com.jaquadro.minecraft.gardencontainers.block;

import com.jaquadro.minecraft.gardencontainers.block.support.WindowBoxConnectionProfile;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityWindowBox;
import com.jaquadro.minecraft.gardencontainers.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.api.WoodRegistry;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.support.BasicSlotProfile;
import com.jaquadro.minecraft.gardencore.block.support.Slot5Profile;
import com.jaquadro.minecraft.gardencore.block.support.SlotShare0Profile;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.List;

public class BlockWindowBox extends BlockGarden
{
    public  static final String[] subTypes = new String[] { "oak", "spruce", "birch", "jungle", "acacia", "big_oak" };

    private static ItemStack substrate = new ItemStack(Blocks.dirt, 1);

    private class LocalSlotProfile extends Slot5Profile
    {
        public LocalSlotProfile (Slot[] slots) {
            super(slots);
        }

        @Override
        public float getPlantOffsetY (IBlockAccess blockAccess, int x, int y, int z, int slot) {
            TileEntityWindowBox garden = getTileEntity(blockAccess, x, y, z);
            if (garden == null || garden.isUpper())
                return -.0625f;
            else
                return -.5f - .0625f;
        }
    }

    public BlockWindowBox (String blockName, Material material) {
        super(blockName, material);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(0.5f);
        setStepSound(Block.soundTypeWood);
        //setLightOpacity(255);

        connectionProfile = new WindowBoxConnectionProfile();
        slotShareProfile = new SlotShare0Profile();

        PlantType[] commonType = new PlantType[] { PlantType.GROUND };
        PlantSize[] smallSize = new PlantSize[] { PlantSize.SMALL };
        PlantSize[] allSize = new PlantSize[] { PlantSize.FULL, PlantSize.LARGE, PlantSize.MEDIUM, PlantSize.SMALL };

        slotProfile = new LocalSlotProfile(new BasicSlotProfile.Slot[]{
            new BasicSlotProfile.Slot(Slot5Profile.SLOT_COVER, new PlantType[] { PlantType.GROUND_COVER }, allSize),
            new BasicSlotProfile.Slot(Slot5Profile.SLOT_NW, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot5Profile.SLOT_NE, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot5Profile.SLOT_SW, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot5Profile.SLOT_SE, commonType, smallSize),
        });
    }

    @Override
    public int getRenderType () {
        return ClientProxy.windowBoxRenderID;
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
    public ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z, int slot) {
        return substrate;
    }

    @Override
    protected int getSlot (World world, int x, int y, int z, EntityPlayer player, float hitX, float hitY, float hitZ) {
        TileEntityWindowBox tileEntity = getTileEntity(world, x, y, z);

        if (hitX <= .5) {
            if (hitZ <= .5 && tileEntity.isSlotValid(Slot5Profile.SLOT_NW))
                return Slot5Profile.SLOT_NW;
            else if (tileEntity.isSlotValid(Slot5Profile.SLOT_SW))
                return Slot5Profile.SLOT_SW;
        }
        else {
            if (hitZ <= .5 && tileEntity.isSlotValid(Slot5Profile.SLOT_NE))
                return Slot5Profile.SLOT_NE;
            else if (tileEntity.isSlotValid(Slot5Profile.SLOT_SE))
                return Slot5Profile.SLOT_SE;
        }

        return TileEntityGarden.SLOT_INVALID;
    }

    @Override
    protected int getEmptySlotForPlant (World world, int x, int y, int z, EntityPlayer player, PlantItem plant, float hitX, float hitY, float hitZ) {
        return getSlot(world, x, y, z, player, hitX, hitY, hitZ);
    }

    @Override
    public TileEntityWindowBox createNewTileEntity (World var1, int var2) {
        return new TileEntityWindowBox();
    }

    @Override
    public int damageDropped (int meta) {
        return meta;
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        TileEntityWindowBox te = getTileEntity(world, x, y, z);
        boolean validNE = te.isSlotValid(Slot5Profile.SLOT_NE);
        boolean validNW = te.isSlotValid(Slot5Profile.SLOT_NW);
        boolean validSE = te.isSlotValid(Slot5Profile.SLOT_SE);
        boolean validSW = te.isSlotValid(Slot5Profile.SLOT_SW);

        float yMin = te.isUpper() ? .5f : 0;
        float yMax = te.isUpper() ? 1 : .5f;

        if (validNW) {
            setBlockBounds(0, yMin, 0, .5f, yMax, .5f);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        if (validNE) {
            setBlockBounds(.5f, yMin, 0, 1, yMax, .5f);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        if (validSW) {
            setBlockBounds(0, yMin, .5f, .5f, yMax, 1);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        if (validSE) {
            setBlockBounds(.5f, yMin, .5f, 1, yMax, 1);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }

        setBlockBounds(world, x, y, z, validNW, validNE, validSW, validSE);
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z) {
        TileEntityWindowBox te = getTileEntity(world, x, y, z);
        boolean validNE = te.isSlotValid(Slot5Profile.SLOT_NE);
        boolean validNW = te.isSlotValid(Slot5Profile.SLOT_NW);
        boolean validSE = te.isSlotValid(Slot5Profile.SLOT_SE);
        boolean validSW = te.isSlotValid(Slot5Profile.SLOT_SW);

        setBlockBounds(world, x, y, z, validNW, validNE, validSW, validSE);
    }

    private void setBlockBounds (IBlockAccess world, int x, int y, int z, boolean validNW, boolean validNE, boolean validSW, boolean validSE) {
        TileEntityWindowBox te = getTileEntity(world, x, y, z);

        float yMin = te.isUpper() ? .5f : 0;
        float yMax = te.isUpper() ? 1 : .5f;
        float xMin = (validNW || validSW) ? 0 : .5f;
        float xMax = (validNE || validSE) ? 1 : .5f;
        float zMin = (validNW || validNE) ? 0 : .5f;
        float zMax = (validSW || validSE) ? 1 : .5f;

        setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    @Override
    public void onBlockPlacedBy (World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        TileEntityWindowBox te = (TileEntityWindowBox) world.getTileEntity(x, y, z);
        if (te == null || te.getDirection() != 0)
            return;

        int quadrant = MathHelper.floor_double((entity.rotationYaw * 4f / 360f) + .5) & 3;
        switch (quadrant) {
            case 0:
                te.setDirection(3);
                break;
            case 1:
                te.setDirection(4);
                break;
            case 2:
                te.setDirection(2);
                break;
            case 3:
                te.setDirection(5);
                break;
        }

        if (world.isRemote) {
            te.invalidate();
            world.markBlockForUpdate(x, y, z);
        }
    }

    @Override
    public boolean canHarvestBlock (EntityPlayer player, int meta) {
        return true;
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List blockList) {
        for (int i = 0; i < 6; i++)
            blockList.add(new ItemStack(item, 1, i));

    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return Blocks.planks.getIcon(side, meta);
    }

    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side) {
        return Blocks.planks.getIcon(world, x, y, z, side);
    }

    public String[] getSubTypes () {
        return subTypes;
    }

    public TileEntityWindowBox getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityWindowBox) ? (TileEntityWindowBox) te : null;
    }
}
