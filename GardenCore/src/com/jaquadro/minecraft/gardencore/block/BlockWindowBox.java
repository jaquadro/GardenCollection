package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityWindowBox;
import com.jaquadro.minecraft.gardencore.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockWindowBox extends BlockGarden
{
    private static ItemStack substrate = new ItemStack(Blocks.dirt, 1);

    public BlockWindowBox (String blockName) {
        super(blockName, Material.wood);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(0.5f);
        setStepSound(Block.soundTypeWood);
        //setLightOpacity(255);
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
    public ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z) {
        return substrate;
    }

    @Override
    public float getPlantOffsetY (IBlockAccess world, int x, int y, int z, int slot) {
        TileEntityWindowBox te = getTileEntity(world, x, y, z);
        if (te == null)
            return 0;

        return te.isUpper() ? -.0625f : -.5f - .0625f;
    }

    @Override
    protected int getSlot (World world, int x, int y, int z, int side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        TileEntityWindowBox tileEntity = getTileEntity(world, x, y, z);

        if (hitX <= .5) {
            if (hitZ <= .5 && tileEntity.isSlotValid(TileEntityGarden.SLOT_NW))
                return TileEntityGarden.SLOT_NW;
            else if (tileEntity.isSlotValid(TileEntityGarden.SLOT_SW))
                return TileEntityGarden.SLOT_SW;
        }
        else {
            if (hitZ <= .5 && tileEntity.isSlotValid(TileEntityGarden.SLOT_NE))
                return TileEntityGarden.SLOT_NE;
            else if (tileEntity.isSlotValid(TileEntityGarden.SLOT_SE))
                return TileEntityGarden.SLOT_SE;
        }

        return TileEntityGarden.SLOT_INVALID;
    }

    @Override
    public TileEntityWindowBox createNewTileEntity (World var1, int var2) {
        return new TileEntityWindowBox();
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        TileEntityWindowBox te = getTileEntity(world, x, y, z);
        boolean validNE = te.isSlotValid(TileEntityGarden.SLOT_NE);
        boolean validNW = te.isSlotValid(TileEntityGarden.SLOT_NW);
        boolean validSE = te.isSlotValid(TileEntityGarden.SLOT_SE);
        boolean validSW = te.isSlotValid(TileEntityGarden.SLOT_SW);

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
        boolean validNE = te.isSlotValid(TileEntityGarden.SLOT_NE);
        boolean validNW = te.isSlotValid(TileEntityGarden.SLOT_NW);
        boolean validSE = te.isSlotValid(TileEntityGarden.SLOT_SE);
        boolean validSW = te.isSlotValid(TileEntityGarden.SLOT_SW);

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
    public IIcon getIcon (int side, int meta) {
        return Blocks.planks.getIcon(side, meta);
    }

    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side) {
        return Blocks.planks.getIcon(world, x, y, z, side);
    }

    public TileEntityWindowBox getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityWindowBox) ? (TileEntityWindowBox) te : null;
    }
}
