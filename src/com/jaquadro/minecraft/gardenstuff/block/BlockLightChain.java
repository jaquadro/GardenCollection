package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.api.IPlantProxy;
import com.jaquadro.minecraft.gardencore.api.block.IChainAttachable;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLightChain extends Block implements IPlantProxy
{
    private static final Vec3[] defaultAttachPoints = new Vec3[] {
        Vec3.createVectorHelper(.03125, 1, .03125), Vec3.createVectorHelper(.03125, 1, 1 - .03125),
        Vec3.createVectorHelper(1 - .03125, 1, .03125), Vec3.createVectorHelper(1 - .03125, 1, 1 - .03125),
    };
    private static final Vec3[] singleAttachPoint = new Vec3[] {
        Vec3.createVectorHelper(.5, 1, .5),
    };

    public BlockLightChain (String blockName) {
        super(Material.iron);

        setBlockName(blockName);
        setHardness(2.5f);
        setResistance(5f);
        setStepSound(Block.soundTypeMetal);
        setBlockBounds(.5f - .0625f, 0, .5f - .0625f, .5f + .0625f, 1, .5f + .0625f);
        setBlockTextureName(GardenStuff.MOD_ID + ":chain_light");
        setCreativeTab(ModCreativeTabs.tabGardenCore);
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
        return ClientProxy.lightChainRenderID; // Crossed Squares
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean applyBonemeal (World world, int x, int y, int z) {
        return ModBlocks.gardenProxy.applyBonemeal(world, x, y, z);
    }

    @Override
    public TileEntityGarden getGardenEntity (IBlockAccess blockAccess, int x, int y, int z) {
        return ModBlocks.gardenProxy.getGardenEntity(blockAccess, x, y, z);
    }

    public int findMinY (IBlockAccess world, int x, int y, int z) {
        while (y > 0) {
            if (world.getBlock(x, --y, z) != this)
                return y + 1;
        }

        return y;
    }

    public int findMaxY (IBlockAccess world, int x, int y, int z) {
        while (y < world.getHeight() - 1) {
            if (world.getBlock(x, ++y, z) != this)
                return y - 1;
        }

        return y;
    }

    public Vec3[] getAttachPoints (IBlockAccess world, int x, int y, int z) {
        int yMin = findMinY(world, x, y, z);
        Block bottomBlock = world.getBlock(x, yMin - 1, z);

        Vec3[] attachPoints = singleAttachPoint;
        if (bottomBlock instanceof IChainAttachable)
            attachPoints = ((IChainAttachable) bottomBlock).getChainAttachPoints();
        else if (bottomBlock.isSideSolid(world, x, y, z, ForgeDirection.UP))
            attachPoints = defaultAttachPoints;

        return attachPoints;
    }
}
