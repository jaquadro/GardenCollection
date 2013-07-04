package com.jaquadro.minecraft.extrabuttons.block;

import net.minecraft.block.BlockButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class PanelButton extends BlockButton
{
    public PanelButton (int id, int texture, boolean sensing)
    {
        super(id, texture, sensing);
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z)
    {
        int data = world.getBlockMetadata(x, y, z);
        this.updateBlockBoundsWithState(data);
    }

    private void updateBlockBoundsWithState (int data)
    {
        int direction = data & 7;
        boolean isPressed = (data & 8) > 0;
        float minY = 0.075F;
        float maxY = 0.925F;
        float halfWidth = 0.4375F;
        float depth = 0.125F;

        if (isPressed) {
            depth = 0.0625F;
        }

        if (direction == 1) {
            this.setBlockBounds(0.0F, minY, 0.5F - halfWidth, depth, maxY, 0.5F + halfWidth);
        }
        else if (direction == 2) {
            this.setBlockBounds(1.0F - depth, minY, 0.5F - halfWidth, 1.0F, maxY, 0.5F + halfWidth);
        }
        else if (direction == 3) {
            this.setBlockBounds(0.5F - halfWidth, minY, 0.0F, 0.5F + halfWidth, maxY, depth);
        }
        else if (direction == 4) {
            this.setBlockBounds(0.5F - halfWidth, minY, 1.0F - depth, 0.5F + halfWidth, maxY, 1.0F);
        }
    }

    @Override
    public void setBlockBoundsForItemRender ()
    {
        float x = 0.4375F;
        float y = 0.4375F;
        float z = 0.125F;
        this.setBlockBounds(0.5F - x, 0.5F - y, 0.5F - z, 0.5F + x, 0.5F + y, 0.5F + z);
    }

    @Override
    protected void func_82535_o(World par1World, int par2, int par3, int par4)
    {
        int var5 = par1World.getBlockMetadata(par2, par3, par4);
        int var6 = var5 & 7;
        boolean var7 = (var5 & 8) != 0;
        this.updateBlockBoundsWithState(var5);
        List var9 = par1World.getEntitiesWithinAABB(EntityArrow.class, AxisAlignedBB.getAABBPool().addOrModifyAABBInPool((double)par2 + this.minX, (double)par3 + this.minY, (double)par4 + this.minZ, (double)par2 + this.maxX, (double)par3 + this.maxY, (double)par4 + this.maxZ));
        boolean var8 = !var9.isEmpty();

        if (var8 && !var7)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 | 8);
            this.func_82536_d(par1World, par2, par3, par4, var6);
            par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!var8 && var7)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, var6);
            this.func_82536_d(par1World, par2, par3, par4, var6);
            par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (var8)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, this.blockID, this.tickRate());
        }
    }

    private void func_82536_d(World par1World, int par2, int par3, int par4, int par5)
    {
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4, this.blockID);

        if (par5 == 1)
        {
            par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, this.blockID);
        }
        else if (par5 == 2)
        {
            par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, this.blockID);
        }
        else if (par5 == 3)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, this.blockID);
        }
        else if (par5 == 4)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, this.blockID);
        }
        else
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, this.blockID);
        }
    }
}
