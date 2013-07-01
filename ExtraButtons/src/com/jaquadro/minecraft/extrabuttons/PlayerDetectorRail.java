package com.jaquadro.minecraft.extrabuttons;

import net.minecraft.block.BlockDetectorRail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class PlayerDetectorRail extends BlockDetectorRail
{
    public PlayerDetectorRail(int id, int texture)
    {
        super(id, texture);
        this.setTickRandomly(true);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (!world.isRemote)
        {
            int data = world.getBlockMetadata(x, y, z);

            if ((data & 8) == 0)
            {
                this.setStateIfMinecartInteractsWithRail(world, x, y, z, data);
            }
        }
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand)
    {
        if (!world.isRemote)
        {
            int data = world.getBlockMetadata(x, y, z);

            if ((data & 8) != 0)
            {
                this.setStateIfMinecartInteractsWithRail(world, x, y, z, data);
            }
        }
    }

    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int data)
    {
        if ((data & 8) != 0)
            return this.blockIndexInTexture + 1;
        else
            return this.blockIndexInTexture;
    }

    private void setStateIfMinecartInteractsWithRail(World world, int x, int y, int z, int data)
    {
        boolean isPowerBitSet = (data & 8) != 0;
        boolean isValidTarget = false;
        float boundAdjust = 0.125F;
        List entities = world.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)((float)x + boundAdjust), (double)y, (double)((float)z + boundAdjust), (double)((float)(x + 1) - boundAdjust), (double)((float)(y + 1) - boundAdjust), (double)((float)(z + 1) - boundAdjust)));

        if (!entities.isEmpty())
        {
            for (Object item : entities) {
                EntityMinecart minecart = (EntityMinecart)item;
                if (minecart.riddenByEntity != null)
                    isValidTarget = true;
            }
        }

        if (isValidTarget && !isPowerBitSet)
        {
            world.setBlockMetadataWithNotify(x, y, z, data | 8);
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }

        if (!isValidTarget && isPowerBitSet)
        {
            world.setBlockMetadataWithNotify(x, y, z, data & 7);
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }

        if (isValidTarget)
        {
            world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
        }
    }

    @Override
    public String getTextureFile ()
    {
        return CommonProxy.BLOCK_PNG;
    }
}
