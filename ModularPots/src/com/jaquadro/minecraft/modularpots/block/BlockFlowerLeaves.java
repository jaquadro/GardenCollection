package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.sun.tracing.dtrace.ModuleAttributes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFlowerLeaves extends BlockOldLeaf
{
    private IIcon[] flowersTop;
    private IIcon[] flowersTopSide;
    private IIcon[] flowersSide;

    public  BlockFlowerLeaves (String blockName) {
        setBlockName(blockName);
        setBlockTextureName("leaves");
    }

    /*@Override
    public IIcon getIcon (int var1, int var2) {
        return null;
    }*/

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide (World p_149707_1_, int p_149707_2_, int p_149707_3_, int p_149707_4_, int p_149707_5_) {
        return true;
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess p_149646_1_, int p_149646_2_, int p_149646_3_, int p_149646_4_, int p_149646_5_) {
        return true;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.flowerLeafRenderID;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getFlowerIcon (IBlockAccess world, int x, int y, int z, int meta, int side) {
        boolean clear1 = world.getBlock(x, y + 1, z) != this;
        boolean clear2 = world.getBlock(x, y, z - 1) != this;
        boolean clear3 = world.getBlock(x, y, z + 1) != this;
        boolean clear4 = world.getBlock(x - 1, y, z) != this;
        boolean clear5 = world.getBlock(x + 1, y, z) != this;

        if (side == 1 && clear1)
            return flowersTop[meta & 3];
        else if ((side == 2 && clear2) || (side == 3 && clear3) || (side == 4 && clear4) || (side == 5 && clear5))
            return clear1 ? flowersTopSide[meta & 3] : flowersSide[meta & 3];

        return null;
    }

    /*@Override
    public String[] func_150125_e () {
        return new String[0];
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister register) {
        super.registerBlockIcons(register);

        flowersTop = new IIcon[4];
        flowersTopSide = new IIcon[4];
        flowersSide = new IIcon[4];

        for (int i = 0; i < 4; i++) {
            flowersTop[i] = register.registerIcon(ModularPots.MOD_ID + ":leaves_flowers_white");
            flowersTopSide[i] = register.registerIcon(ModularPots.MOD_ID + ":leaves_flowers_white_half");
            flowersSide[i] = register.registerIcon(ModularPots.MOD_ID + ":leaves_flowers_white_sparse");
        }
    }
}
