package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenSingle;
import com.jaquadro.minecraft.gardencore.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockDecorativePot extends BlockGarden
{
    private static ItemStack substrate = new ItemStack(Blocks.dirt, 1);

    public BlockDecorativePot (String blockName) {
        super(blockName, Material.rock);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(.5f);
        setStepSound(Block.soundTypeStone);
    }

    @Override
    public ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z) {
        return substrate;
    }

    @Override
    public float getPlantOffsetY (IBlockAccess world, int x, int y, int z, int slot) {
        return -.0625f;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.decorativePotRenderID;
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
    public boolean shouldSideBeRendered (IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public TileEntityGardenSingle createNewTileEntity (World var1, int var2) {
        return new TileEntityGardenSingle();
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        setBlockBounds(0, .5f, 0, 1, 1, 1);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);

        setBlockBounds(.1875f, 0, .1875f, 1f - .1875f, .5f, 1f - .1875f);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);

        setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return Blocks.quartz_block.getIcon(side, 0);
    }

    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side) {
        return Blocks.quartz_block.getIcon(world, x, y, z, side);
    }
}
