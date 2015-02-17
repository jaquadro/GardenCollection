package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.api.IPlantProxy;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLightChain extends Block implements IPlantProxy
{
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
}
