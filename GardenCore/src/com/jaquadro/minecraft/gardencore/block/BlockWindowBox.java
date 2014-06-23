package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.core.CommonProxy;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockWindowBox extends BlockGarden
{
    private static ItemStack substrate = new ItemStack(Blocks.dirt, 1);

    public BlockWindowBox (String blockName) {
        super(blockName, Material.wood);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(0.5f);
        setStepSound(Block.soundTypeWood);
    }

    @Override
    public int getRenderType () {
        return ClientProxy.windowBoxRenderID;
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    protected ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z) {
        return substrate;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return Blocks.planks.getIcon(side, meta);
    }

    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side) {
        return Blocks.planks.getIcon(world, x, y, z, side);
    }
}
