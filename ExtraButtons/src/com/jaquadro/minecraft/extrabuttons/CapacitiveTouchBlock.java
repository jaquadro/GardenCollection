package com.jaquadro.minecraft.extrabuttons;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class CapacitiveTouchBlock extends Block
{
    public CapacitiveTouchBlock (int id, int texture)
    {
        super(id, texture, Material.circuits);

        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public int tickRate()
    {
        return 20;
    }

    @Override
    public boolean isOpaqueCube ()
    {
        return false;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int data)
    {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        int data = world.getBlockMetadata(x, y, z);
        int isPressed = 8 - (data & 8);

        if (isPressed == 0)
        {
            return true;
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, isPressed);
            notifyNeighbors(world, x, y, z);
            world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
            return true;
        }
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int data)
    {
        if ((data & 8) > 0)
        {
            this.notifyNeighbors(world, x, y, z);
        }

        super.breakBlock(world, x, y, z, par5, data);
    }

    @Override
    public boolean isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return (world.getBlockMetadata(x, y, z) & 8) > 0;
    }

    @Override
    public boolean isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return (world.getBlockMetadata(x, y, z) & 8) > 0;
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (!world.isRemote)
        {
            int data = world.getBlockMetadata(x, y, z);

            if ((data & 8) != 0)
            {
                world.setBlockMetadataWithNotify(x, y, z, data & 7);
                notifyNeighbors(world, x, y, z);
            }
        }
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int data)
    {
        if ((data & 8) > 0)
            return this.blockIndexInTexture + 1;
        else
            return this.blockIndexInTexture;
    }

    private void notifyNeighbors (World world, int x, int y, int z)
    {
        world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
        world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
        world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
        world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, this.blockID);
    }

    @Override
    public String getTextureFile ()
    {
        return CommonProxy.BLOCK_PNG;
    }
}
