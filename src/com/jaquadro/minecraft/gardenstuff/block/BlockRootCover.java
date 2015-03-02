package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockRootCover extends Block
{
    @SideOnly(Side.CLIENT)
    private static IIcon[][] icons;

    public BlockRootCover (String name) {
        super(Material.wood);

        setBlockName(name);
        setHardness(1.5f);
        setResistance(2.5f);
        setStepSound(Block.soundTypeWood);
        setBlockTextureName(GardenStuff.MOD_ID + ":root_cover");
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
        return 1;
    }

    @Override
    public IIcon getIcon (IBlockAccess blockAccess, int x, int y, int z, int side) {
        if (side == 0 || side == 1)
            return icons[mod2(x)][mod2(z)];
        else if (side == 2 || side == 3)
            return icons[mod2(x)][mod2(y)];
        else
            return icons[mod2(z)][mod2(y)];
    }

    private int mod2 (int x) {
        return ((x % 2) + 2) % 2;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return icons[0][0];
    }

    @Override
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[2][2];
        icons[0][0] = register.registerIcon(getTextureName() + "_00");
        icons[0][1] = register.registerIcon(getTextureName() + "_01");
        icons[1][0] = register.registerIcon(getTextureName() + "_10");
        icons[1][1] = register.registerIcon(getTextureName() + "_10");
    }
}
