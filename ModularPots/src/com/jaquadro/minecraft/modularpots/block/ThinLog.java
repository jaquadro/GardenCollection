package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class ThinLog extends Block
{
    public ThinLog () {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabDecorations);

        setBlockBoundsForItemRender();
    }

    public float getMargin () {
        return 0.25f;
    }

    @Override
    public void setBlockBoundsForItemRender () {
        float margin = getMargin();
        setBlockBounds(margin, 0, margin, 1 - margin, 1, 1 - margin);
    }

    @Override
    public int quantityDropped (Random random) {
        return 1;
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
        return ClientProxy.thinLogRenderID;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int meta) {
        byte range = 4;
        int height = range + 1;

        if (world.checkChunksExist(x - height, y - height, z - height, x + height, y + height, z + height)) {
            for (int dx = -range; dx <= range; dx++) {
                for (int dy = -range; dy <= range; dy++) {
                    for (int dz = -range; dz <= range; dz++) {
                        Block leaf = world.getBlock(x + dx, y + dy, z + dz);
                        if (leaf.isLeaves(world, x + dx, y + dy, z + dz))
                            leaf.beginLeavesDecay(world, x + dx, y + dy, z + dz);
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int meta) {
        return getIconSource(meta).getIcon(side, meta % 4);
    }

    private Block getIconSource (int meta) {
        switch (meta / 4) {
            case 0:
                return Blocks.log;
            case 1:
                return Blocks.log2;
            default:
                return Blocks.log;
        }
    }

    @Override
    public boolean canSustainLeaves (IBlockAccess world, int x, int y, int z) {
        return true;
    }
}
