package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModularPots;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockGardenSoil extends BlockGarden
{
    private static ItemStack substrate = new ItemStack(Blocks.dirt, 1);

    public BlockGardenSoil (String blockName) {
        super(blockName, Material.ground);

        setCreativeTab(ModularPots.tabModularPots);
        setHardness(0.5f);
        setStepSound(Block.soundTypeGravel);
    }

    @Override
    protected ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z) {
        return substrate;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return blockIcon;
    }

    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side) {
        return blockIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(ModularPots.MOD_ID + ":garden_dirt");
    }
}
