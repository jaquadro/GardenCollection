package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardenapi.api.connect.IChainSingleAttachable;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLattice;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import com.jaquadro.minecraft.gardentrees.block.BlockThinLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockLattice extends BlockContainer implements IChainSingleAttachable
{
    public BlockLattice (String blockName, Material material) {
        super(material);

        setBlockName(blockName);
        setHardness(2.5f);
        setResistance(5);
        setBlockTextureName(GardenStuff.MOD_ID + ":lattice_iron");
        setCreativeTab(ModCreativeTabs.tabGardenCore);

        setBlockBoundsForItemRender();
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
        return ClientProxy.latticeRenderID;
    }

    @Override
    public int damageDropped (int meta) {
        return meta;
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z) {
        int connectFlags = calcConnectionFlags(world, x, y, z);

        float margin = .0625f * 6;
        float ys = (connectFlags & 1) != 0 ? 0 : margin;
        float ye = (connectFlags & 2) != 0 ? 1 : 1 - margin;
        float zs = (connectFlags & 4) != 0 ? 0 : margin;
        float ze = (connectFlags & 8) != 0 ? 1 : 1 - margin;
        float xs = (connectFlags & 16) != 0 ? 0 : margin;
        float xe = (connectFlags & 32) != 0 ? 1 : 1 - margin;

        setBlockBounds(xs, ys, zs, xe, ye, ze);
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
    }

    @Override
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
            {
                ret.add(new ItemStack(item, 1, damageDropped(metadata)));
            }
        }
        return ret;
    }

    public int calcConnectionFlags (IBlockAccess world, int x, int y, int z) {
        Block blockYNeg = world.getBlock(x, y - 1, z);
        Block blockYPos = world.getBlock(x, y + 1, z);
        Block blockZNeg = world.getBlock(x, y, z - 1);
        Block blockZPos = world.getBlock(x, y, z + 1);
        Block blockXNeg = world.getBlock(x - 1, y, z);
        Block blockXPos = world.getBlock(x + 1, y, z);

        boolean hardYNeg = isNeighborHardConnection(world, x, y - 1, z, blockYNeg, ForgeDirection.DOWN);
        boolean hardYPos = isNeighborHardConnection(world, x, y + 1, z, blockYPos, ForgeDirection.UP);
        boolean hardZNeg = isNeighborHardConnection(world, x, y, z - 1, blockZNeg, ForgeDirection.NORTH);
        boolean hardZPos = isNeighborHardConnection(world, x, y, z + 1, blockZPos, ForgeDirection.SOUTH);
        boolean hardXNeg = isNeighborHardConnection(world, x - 1, y, z, blockXNeg, ForgeDirection.WEST);
        boolean hardXPos = isNeighborHardConnection(world, x + 1, y, z, blockXPos, ForgeDirection.EAST);

        boolean extYNeg = isNeighborExtConnection(world, x, y - 1, z, blockYNeg, ForgeDirection.DOWN);
        boolean extYPos = isNeighborExtConnection(world, x, y + 1, z, blockYPos, ForgeDirection.UP);
        boolean extZNeg = isNeighborExtConnection(world, x, y, z - 1, blockZNeg, ForgeDirection.NORTH);
        boolean extZPos = isNeighborExtConnection(world, x, y, z + 1, blockZPos, ForgeDirection.SOUTH);
        boolean extXNeg = isNeighborExtConnection(world, x - 1, y, z, blockXNeg, ForgeDirection.WEST);
        boolean extXPos = isNeighborExtConnection(world, x + 1, y, z, blockXPos, ForgeDirection.EAST);

        return (hardYNeg ? 1 : 0) | (hardYPos ? 2 : 0) | (hardZNeg ? 4 : 0) | (hardZPos ? 8 : 0) | (hardXNeg ? 16 : 0) | (hardXPos ? 32 : 0)
            | (extYNeg ? 64 : 0) | (extYPos ? 128 : 0) | (extZNeg ? 256 : 0) | (extZPos ? 512 : 0) | (extXNeg ? 1024 : 0) | (extXPos ? 2048 : 0);
    }

    private boolean isNeighborHardConnection (IBlockAccess world, int x, int y, int z, Block block, ForgeDirection side) {
        if (block.getMaterial().isOpaque() && block.renderAsNormalBlock())
            return true;

        if (block.isSideSolid(world, x, y, z, side.getOpposite()))
            return true;

        if (block == this)
            return true;

        if (side == ForgeDirection.DOWN || side == ForgeDirection.UP) {
            if (block instanceof BlockFence || block instanceof net.minecraft.block.BlockFence)
                return true;
        }

        return false;
    }

    private boolean isNeighborExtConnection (IBlockAccess world, int x, int y, int z, Block block, ForgeDirection side) {
        if (block instanceof BlockThinLog)
            return true;

        return false;
    }

    protected TileEntityLattice getTileEntity (IBlockAccess blockAccess, int x, int y, int z) {
        TileEntity te = blockAccess.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityLattice)
            return (TileEntityLattice) te;

        return null;
    }

    private final Vec3[] attachPoints = new Vec3[] {
        Vec3.createVectorHelper(.5, .5, .5),
        Vec3.createVectorHelper(.5, .5, .5),
        Vec3.createVectorHelper(.5, .5, .5),
        Vec3.createVectorHelper(.5, .5, .5),
        Vec3.createVectorHelper(.5, .5, .5),
        Vec3.createVectorHelper(.5, .5, .5),
    };

    @Override
    public Vec3 getChainAttachPoint (IBlockAccess blockAccess, int x, int y, int z, int side) {
        int connectFlags = calcConnectionFlags(blockAccess, x, y, z);

        switch (side) {
            case 0: return (connectFlags & 1) == 0 ? attachPoints[0] : null;
            case 1: return (connectFlags & 2) == 0 ? attachPoints[1] : null;
            case 2: return (connectFlags & 4) == 0 ? attachPoints[2] : null;
            case 3: return (connectFlags & 8) == 0 ? attachPoints[3] : null;
            case 4: return (connectFlags & 16) == 0 ? attachPoints[4] : null;
            case 5: return (connectFlags & 32) == 0 ? attachPoints[5] : null;
        }

        return null;
    }
}
