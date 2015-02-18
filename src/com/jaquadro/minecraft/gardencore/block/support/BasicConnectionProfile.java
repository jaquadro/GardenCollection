package com.jaquadro.minecraft.gardencore.block.support;

import com.jaquadro.minecraft.gardencore.api.block.garden.IConnectionProfile;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class BasicConnectionProfile implements IConnectionProfile
{
    @Override
    public boolean isAttachedNeighbor (IBlockAccess blockAccess, int x, int y, int z, int side) {
        switch (side) {
            case 0: return isAttachedNeighbor(blockAccess, x, y, z, x, y - 1, z);
            case 1: return isAttachedNeighbor(blockAccess, x, y, z, x, y + 1, z);
            case 2: return isAttachedNeighbor(blockAccess, x, y, z, x, y, z - 1);
            case 3: return isAttachedNeighbor(blockAccess, x, y, z, x, y, z + 1);
            case 4: return isAttachedNeighbor(blockAccess, x, y, z, x - 1, y, z);
            case 5: return isAttachedNeighbor(blockAccess, x, y, z, x + 1, y, z);
        }

        return false;
    }

    @Override
    public boolean isAttachedNeighbor (IBlockAccess blockAccess, int x, int y, int z, int nx, int ny, int nz) {
        if (y != ny || Math.abs(nx - x) > 1 || Math.abs(nz - z) > 1)
            return false;

        Block sBlock = blockAccess.getBlock(x, y, z);
        Block nBlock = blockAccess.getBlock(nx, ny, nz);
        if (sBlock != nBlock)
            return false;

        int sData = blockAccess.getBlockMetadata(x, y, z);
        int nData = blockAccess.getBlockMetadata(nx, ny, nz);
        if (sData != nData)
            return false;

        TileEntity sEntity = blockAccess.getTileEntity(x, y, z);
        TileEntity nEntity = blockAccess.getTileEntity(nx, ny, nz);
        if (sEntity == null || nEntity == null || sEntity.getClass() != nEntity.getClass())
            return false;

        if (!(sEntity instanceof TileEntityGarden))
            return false;

        /*TileEntityGarden nGarden = (TileEntityGarden) nEntity;
        if ((substrate == null || nGarden.substrate == null) && substrate != nGarden.substrate)
            return false;

        if (substrate != null) {
            if (substrate.getItem() != nGarden.substrate.getItem() || substrate.getItemDamage() != nGarden.substrate.getItemDamage())
                return false;
        }*/

        return true;
    }
}
