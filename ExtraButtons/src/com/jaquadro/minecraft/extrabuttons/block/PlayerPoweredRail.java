package com.jaquadro.minecraft.extrabuttons.block;

import com.jaquadro.minecraft.extrabuttons.block.PlayerDetectorRail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class PlayerPoweredRail extends PlayerDetectorRail
{
    public PlayerPoweredRail (int id, int texture)
    {
        super(id, texture);
        this.setTickRandomly(true);
    }

    @Override
    public void onEntityCollidedWithBlock (World world, int x, int y, int z, Entity entity)
    {
        if (!world.isRemote) {
            int data = world.getBlockMetadata(x, y, z);

            if ((data & 8) == 0) {
                this.setStateIfMinecartInteractsWithRail(world, x, y, z, data);
            }
        }
    }

    @Override
    public void updateTick (World world, int x, int y, int z, Random rand)
    {
        if (!world.isRemote) {
            int data = world.getBlockMetadata(x, y, z);

            if ((data & 8) != 0) {
                this.setStateIfMinecartInteractsWithRail(world, x, y, z, data);
            }
        }
    }

    private void setStateIfMinecartInteractsWithRail (World world, int x, int y, int z, int data)
    {
        boolean isPowerBitSet = (data & 8) != 0;
        boolean isValidTarget = false;
        float boundAdjust = 0.125F;
        List entities = world.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double) ((float) x + boundAdjust), (double) y, (double) ((float) z + boundAdjust), (double) ((float) (x + 1) - boundAdjust), (double) ((float) (y + 1) - boundAdjust), (double) ((float) (z + 1) - boundAdjust)));

        if (!entities.isEmpty()) {
            for (Object item : entities) {
                EntityMinecart minecart = (EntityMinecart) item;
                if (minecart.riddenByEntity != null)
                    isValidTarget = true;
            }
        }

        if (isValidTarget && !isPowerBitSet) {
            world.setBlockMetadataWithNotify(x, y, z, data | 8);
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }

        if (!isValidTarget && isPowerBitSet) {
            world.setBlockMetadataWithNotify(x, y, z, data & 7);
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }

        if (isValidTarget) {
            world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate());
        }

        data = world.getBlockMetadata(x, y, z);

        for (Object item : entities) {
            EntityMinecart minecart = (EntityMinecart) item;
            if (minecart != null)
                updateMinecart(minecart, data);
        }
    }

    private void updateMinecart (EntityMinecart entity, int railData)
    {
        int direction = railData &= 7;
        boolean isPoweredBitSet = (railData & 8) != 0;

        if (entity.shouldDoRailFunctions()) {
            int posX = MathHelper.floor_double(entity.posX);
            int posY = MathHelper.floor_double(entity.posY);
            int posZ = MathHelper.floor_double(entity.posZ);

            double motion = Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);

            if (isPoweredBitSet) {
                if (motion > 0.01D) {
                    double boost = 0.06D;
                    entity.motionX += entity.motionX / motion * boost;
                    entity.motionZ += entity.motionZ / motion * boost;
                }
                else if (direction == 1) {
                    if (entity.worldObj.isBlockNormalCube(posX - 1, posY, posZ)) {
                        entity.motionX = 0.02D;
                    }
                    else if (entity.worldObj.isBlockNormalCube(posX + 1, posY, posZ)) {
                        entity.motionX = -0.02D;
                    }
                }
                else if (direction == 0) {
                    if (entity.worldObj.isBlockNormalCube(posX, posY, posZ - 1)) {
                        entity.motionZ = 0.02D;
                    }
                    else if (entity.worldObj.isBlockNormalCube(posX, posY, posZ + 1)) {
                        entity.motionZ = -0.02D;
                    }
                }
            }
            else {
                if (motion < 0.03D) {
                    entity.motionX *= 0.0D;
                    entity.motionY *= 0.0D;
                    entity.motionZ *= 0.0D;
                }
                else {
                    entity.motionX *= 0.5D;
                    entity.motionY *= 0.0D;
                    entity.motionZ *= 0.5D;
                }
            }
        }
    }
}
