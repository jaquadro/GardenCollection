package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityCompostBin;
import com.jaquadro.minecraft.gardencore.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCompostBin extends BlockContainer
{
    private static final int ICON_SIDE = 0;
    private static final int ICON_TOP = 1;
    private static final int ICON_BOTTOM = 2;
    private static final int ICON_INNER = 3;

    @SideOnly(Side.CLIENT)
    IIcon[] icons;

    public BlockCompostBin () {
        super(Material.wood);

        setBlockName("compostBin");
        setCreativeTab(ModCreativeTabs.tabGardenCore);
    }

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.compostBinRenderID;
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta) {
        switch (side) {
            case 0:
                return icons[ICON_BOTTOM];
            case 1:
                return icons[ICON_TOP];
            default:
                return icons[ICON_SIDE];
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getInnerIcon () {
        return icons[ICON_INNER];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[4];

        icons[ICON_SIDE] = register.registerIcon(GardenCore.MOD_ID + ":compost_bin_side");
        icons[ICON_TOP] = register.registerIcon(GardenCore.MOD_ID + ":compost_bin_top");
        icons[ICON_BOTTOM] = register.registerIcon(GardenCore.MOD_ID + ":compost_bin_bottom");
        icons[ICON_INNER] = register.registerIcon(GardenCore.MOD_ID + ":compost_bin_inner");
    }

    @Override
    public TileEntity createNewTileEntity (World world, int p_149915_2_) {
        return new TileEntityCompostBin();
    }
}
