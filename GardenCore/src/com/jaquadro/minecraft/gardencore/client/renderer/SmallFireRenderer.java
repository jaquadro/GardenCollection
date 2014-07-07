package com.jaquadro.minecraft.gardencore.client.renderer;

import com.jaquadro.minecraft.gardencore.block.BlockSmallFire;
import com.jaquadro.minecraft.gardencore.core.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class SmallFireRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (block instanceof BlockSmallFire)
            return renderWorldBlock(world, x, y, z, (BlockSmallFire) block, modelId, renderer);

        return false;
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockSmallFire block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        IIcon icon0 = block.getFireIcon(0);
        IIcon icon1 = block.getFireIcon(1);
        IIcon icon2 = icon0;

        if (renderer.hasOverrideBlockTexture())
            icon2 = renderer.overrideBlockTexture;

        tessellator.setColorOpaque_F(1, 1, 1);
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        double uMin = icon2.getMinU();
        double vMin = icon2.getMinV();
        double uMax = icon2.getMaxU();
        double vMax = icon2.getMaxV();

        double y0 = y - .0625;
        double y1 = y + 1;

        double x0 = x + .5 + .2;
        double x1 = x + .5 - .2;
        double x2 = x + .5 - .3;
        double x3 = x + .5 + .3;
        double z0 = z + .5 + .2;
        double z1 = z + .5 - .2;
        double z2 = z + .5 - .3;
        double z3 = z + .5 + .3;

        tessellator.addVertexWithUV(x2, y1, z + 1 - .0625f, uMax, vMin);
        tessellator.addVertexWithUV(x0, y0, z + 1 - .0625f, uMax, vMax);
        tessellator.addVertexWithUV(x0, y0, z + 0 + .0625f, uMin, vMax);
        tessellator.addVertexWithUV(x2, y1, z + 0 + .0625f, uMin, vMin);

        tessellator.addVertexWithUV(x3, y1, z + 0 + .0625f, uMax, vMin);
        tessellator.addVertexWithUV(x1, y0, z + 0 + .0625f, uMax, vMax);
        tessellator.addVertexWithUV(x1, y0, z + 1 - .0625f, uMin, vMax);
        tessellator.addVertexWithUV(x3, y1, z + 1 - .0625f, uMin, vMin);

        uMin = icon1.getMinU();
        vMin = icon1.getMinV();
        uMax = icon1.getMaxU();
        vMax = icon1.getMaxV();

        tessellator.addVertexWithUV(x + 1 - .0625f, y1, z3, uMax, vMin);
        tessellator.addVertexWithUV(x + 1 - .0625f, y0, z1, uMax, vMax);
        tessellator.addVertexWithUV(x + 0 + .0625f, y0, z1, uMin, vMax);
        tessellator.addVertexWithUV(x + 0 + .0625f, y1, z3, uMin, vMin);

        tessellator.addVertexWithUV(x + 0 + .0625f, y1, z2, uMax, vMin);
        tessellator.addVertexWithUV(x + 0 + .0625f, y0, z0, uMax, vMax);
        tessellator.addVertexWithUV(x + 1 - .0625f, y0, z0, uMin, vMax);
        tessellator.addVertexWithUV(x + 1 - .0625f, y1, z2, uMin, vMin);

        x0 = x + .5 - .5 + .125f;
        x1 = x + .5 + .5 - .125f;
        x2 = x + .5 - .4 + .125f;
        x3 = x + .5 + .4 - .125f;
        z0 = z + .5 - .5 + .125f;
        z1 = z + .5 + .5 - .125f;
        z2 = z + .5 - .4 + .125f;
        z3 = z + .5 + .4 - .125f;

        tessellator.addVertexWithUV(x2, y1, z + 0, uMax, vMin);
        tessellator.addVertexWithUV(x0, y0, z + 0, uMax, vMax);
        tessellator.addVertexWithUV(x0, y0, z + 1, uMin, vMax);
        tessellator.addVertexWithUV(x2, y1, z + 1, uMin, vMin);

        tessellator.addVertexWithUV(x3, y1, z + 1, uMax, vMin);
        tessellator.addVertexWithUV(x1, y0, z + 1, uMax, vMax);
        tessellator.addVertexWithUV(x1, y0, z + 0, uMin, vMax);
        tessellator.addVertexWithUV(x3, y1, z + 0, uMin, vMin);

        uMin = icon0.getMinU();
        vMin = icon0.getMinV();
        uMax = icon0.getMaxU();
        vMax = icon0.getMaxV();

        tessellator.addVertexWithUV(x + 0, y1, z3, uMax, vMin);
        tessellator.addVertexWithUV(x + 0, y0, z1, uMax, vMax);
        tessellator.addVertexWithUV(x + 1, y0, z1, uMin, vMax);
        tessellator.addVertexWithUV(x + 1, y1, z3, uMin, vMin);

        tessellator.addVertexWithUV(x + 1, y1, z2, uMax, vMin);
        tessellator.addVertexWithUV(x + 1, y0, z0, uMax, vMax);
        tessellator.addVertexWithUV(x + 0, y0, z0, uMin, vMax);
        tessellator.addVertexWithUV(x + 0, y1, z2, uMin, vMin);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.smallFireRenderID;
    }
}
