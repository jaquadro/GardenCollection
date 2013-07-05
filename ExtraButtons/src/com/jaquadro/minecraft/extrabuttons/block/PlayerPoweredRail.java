package com.jaquadro.minecraft.extrabuttons.block;

import com.jaquadro.minecraft.extrabuttons.ExtraButtons;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class PlayerPoweredRail extends PlayerDetectorRail
{
    @SideOnly(Side.CLIENT)
    private Icon[] iconArray;

    public PlayerPoweredRail (int id)
    {
        super(id);
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
        List entities = world.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getAABBPool().getAABB((double) ((float) x + boundAdjust), (double) y, (double) ((float) z + boundAdjust), (double) ((float) (x + 1) - boundAdjust), (double) ((float) (y + 1) - boundAdjust), (double) ((float) (z + 1) - boundAdjust)));

        if (!entities.isEmpty()) {
            for (Object item : entities) {
                EntityMinecart minecart = (EntityMinecart) item;
                if (minecart.riddenByEntity != null)
                    isValidTarget = true;
            }
        }

        if (isValidTarget && !isPowerBitSet) {
            world.setBlockMetadataWithNotify(x, y, z, data | 8, 3);
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }

        if (!isValidTarget && isPowerBitSet) {
            world.setBlockMetadataWithNotify(x, y, z, data & 7, 3);
            world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }

        if (isValidTarget) {
            world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
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
        int direction = railData & 7;
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
                        entity.motionX = 0.04D;
                    }
                    else if (entity.worldObj.isBlockNormalCube(posX + 1, posY, posZ)) {
                        entity.motionX = -0.04D;
                    }
                }
                else if (direction == 0) {
                    if (entity.worldObj.isBlockNormalCube(posX, posY, posZ - 1)) {
                        entity.motionZ = 0.04D;
                    }
                    else if (entity.worldObj.isBlockNormalCube(posX, posY, posZ + 1)) {
                        entity.motionZ = -0.04D;
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

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon (int side, int data)
    {
        if ((data & 8) != 0)
            return iconArray[1];
        else
            return iconArray[0];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister) {
        iconArray = new Icon[] {
                iconRegister.registerIcon(ExtraButtons.MOD_ID + ":player_powered_rail_off"),
                iconRegister.registerIcon(ExtraButtons.MOD_ID + ":player_powered_rail_on"),
        };
    }
}
