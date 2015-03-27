package com.jaquadro.minecraft.gardencore.client.renderer.plant;

import com.jaquadro.minecraft.gardencore.api.IPlantRenderer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class SunflowerRenderer implements IPlantRenderer
{
    private DoublePlantRenderer plantRender = new DoublePlantRenderer();

    @Override
    public void render (IBlockAccess world, int x, int y, int z, RenderBlocks renderer, Block block, int meta, int height, AxisAlignedBB[] bounds) {
        if (!(block instanceof BlockDoublePlant))
            return;

        plantRender.render(world, x, y, z, renderer, block, meta, height, bounds);
        if (height != 2)
            return;

        Tessellator tessellator = Tessellator.instance;

        double orientation = Math.cos((double)0 * 0.8D) * Math.PI * 0.1D;
        double aCos = Math.cos(orientation);
        double aSin = Math.sin(orientation);

        double xTR = 0.5D + 0.3D * aCos - 0.5D * aSin;
        double zTR = 0.5D + 0.5D * aCos + 0.3D * aSin;
        double xTL = 0.5D + 0.3D * aCos + 0.5D * aSin;
        double ZTL = 0.5D + -0.5D * aCos + 0.3D * aSin;
        double xBL = 0.5D + -0.05D * aCos + 0.5D * aSin;
        double zBL = 0.5D + -0.5D * aCos + -0.05D * aSin;
        double xBR = 0.5D + -0.05D * aCos - 0.5D * aSin;
        double zBR = 0.5D + 0.5D * aCos + -0.05D * aSin;

        IIcon icon = Blocks.double_plant.sunflowerIcons[0];
        double iconMinU = icon.getMinU();
        double iconMinV = icon.getMinV();
        double iconMaxU = icon.getMaxU();
        double iconMaxV = icon.getMaxV();

        tessellator.addVertexWithUV(x + xBL, y + 1.0D, z + zBL, iconMinU, iconMaxV);
        tessellator.addVertexWithUV(x + xBR, y + 1.0D, z + zBR, iconMaxU, iconMaxV);
        tessellator.addVertexWithUV(x + xTR, y + 0.0D, z + zTR, iconMaxU, iconMinV);
        tessellator.addVertexWithUV(x + xTL, y + 0.0D, z + ZTL, iconMinU, iconMinV);

        icon = Blocks.double_plant.sunflowerIcons[1];
        iconMinU = icon.getMinU();
        iconMinV = icon.getMinV();
        iconMaxU = icon.getMaxU();
        iconMaxV = icon.getMaxV();

        tessellator.addVertexWithUV(x + xBR, y + 1.0D, z + zBR, iconMinU, iconMaxV);
        tessellator.addVertexWithUV(x + xBL, y + 1.0D, z + zBL, iconMaxU, iconMaxV);
        tessellator.addVertexWithUV(x + xTL, y + 0.0D, z + ZTL, iconMaxU, iconMinV);
        tessellator.addVertexWithUV(x + xTR, y + 0.0D, z + zTR, iconMinU, iconMinV);
    }
}
