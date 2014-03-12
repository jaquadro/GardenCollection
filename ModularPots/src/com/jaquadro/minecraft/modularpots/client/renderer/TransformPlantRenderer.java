package com.jaquadro.minecraft.modularpots.client.renderer;

import com.jaquadro.minecraft.modularpots.block.LargePotPlantProxy;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;

public class TransformPlantRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof LargePotPlantProxy))
            return false;

        return renderWorldBlock(world, x, y, z, (LargePotPlantProxy) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, LargePotPlantProxy block, int modelId, RenderBlocks renderer) {
        //Item item = block.getItemBlock(world, x, y, z);
        //Block itemBlock = (item instanceof IPlantable) ? ((IPlantable)item).getPlant(world, x, y, z) : Block.getBlockFromItem(item);
        Block itemBlock = block.getItemBlock(world, x, y, z);
        if (itemBlock == null || itemBlock.isAir(world, x, y, z))
            return true;

        int itemRenderType = itemBlock.getRenderType();

        Tessellator tessellator = Tessellator.instance;
        tessellator.addTranslation(0, -.0625f, 0);

        int color = block.colorMultiplier(world, x, y, z);
        if (color != 16777215) {
            float r = (color >> 16 & 255) / 255f;
            float g = (color >> 8 & 255) / 255f;
            float b = (color & 255) / 255f;
            tessellator.setColorOpaque_F(r, g, b);
        }

        TileEntityLargePot potData = block.getAttachedPotEntity(world, x, y, z);
        if (potData == null)
            potData = new TileEntityLargePot();

        if (itemRenderType == 1)
            renderCrossedSquares(world, renderer, itemBlock, x, y, z, potData);
        else if (itemRenderType == 40 && itemBlock instanceof BlockDoublePlant)
            renderBlockDoublePlant(world, renderer, (BlockDoublePlant) itemBlock, x, y, z, potData);
        else
            renderer.renderBlockByRenderType(itemBlock, x, y, z);

        tessellator.addTranslation(0, +.0625f, 0);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.transformPlantRenderID;
    }

    private boolean renderCrossedSquares(IBlockAccess world, RenderBlocks renderer, Block block, int x, int y, int z, TileEntityLargePot potData)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        int l = block.colorMultiplier(renderer.blockAccess, x, y, z);
        if (l == world.getBiomeGenForCoords(x, z).getBiomeGrassColor(x, y, z))
            l = ColorizerGrass.getGrassColor(potData.getBiomeTemperature(), potData.getBiomeHumidity());

        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        double d1 = (double)x;
        double d2 = (double)y;
        double d0 = (double)z;
        long i1;

        IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, renderer.blockAccess.getBlockMetadata(x, y, z));
        renderer.drawCrossedSquares(iicon, d1, d2, d0, 1.0F);
        return true;
    }

    private boolean renderBlockDoublePlant(IBlockAccess world, RenderBlocks renderer, BlockDoublePlant block, int x, int y, int z, TileEntityLargePot potData)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        int l = block.colorMultiplier(renderer.blockAccess, x, y, z);
        if (l == world.getBiomeGenForCoords(x, z).getBiomeGrassColor(x, y, z))
            l = ColorizerGrass.getGrassColor(potData.getBiomeTemperature(), potData.getBiomeHumidity());

        float f = (float)(l >> 16 & 255) / 255.0F;
        float f1 = (float)(l >> 8 & 255) / 255.0F;
        float f2 = (float)(l & 255) / 255.0F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        tessellator.setColorOpaque_F(f, f1, f2);
        long j1 = (long)(x * 3129871) ^ (long)z * 116129781L;
        j1 = j1 * j1 * 42317861L + j1 * 11L;
        double d19 = (double)x;
        double d0 = (double)y;
        double d1 = (double)z;
        //d19 += ((double)((float)(j1 >> 16 & 15L) / 15.0F) - 0.5D) * 0.3D;
        //d1 += ((double)((float)(j1 >> 24 & 15L) / 15.0F) - 0.5D) * 0.3D;
        int i1 = renderer.blockAccess.getBlockMetadata(x, y, z);
        boolean flag = false;
        boolean flag1 = BlockDoublePlant.func_149887_c(i1);
        int k1;

        if (flag1)
        {
            k1 = BlockDoublePlant.func_149890_d(renderer.blockAccess.getBlockMetadata(x, y - 1, z));
        }
        else
        {
            k1 = BlockDoublePlant.func_149890_d(i1);
        }

        IIcon iicon = block.func_149888_a(flag1, k1);
        renderer.drawCrossedSquares(iicon, d19, d0, d1, 1.0F);

        if (flag1 && k1 == 0)
        {
            IIcon iicon1 = block.sunflowerIcons[0];
            double d2 = Math.cos((double)j1 * 0.8D) * Math.PI * 0.1D;
            double d3 = Math.cos(d2);
            double d4 = Math.sin(d2);
            double d5 = (double)iicon1.getMinU();
            double d6 = (double)iicon1.getMinV();
            double d7 = (double)iicon1.getMaxU();
            double d8 = (double)iicon1.getMaxV();
            double d9 = 0.3D;
            double d10 = -0.05D;
            double d11 = 0.5D + 0.3D * d3 - 0.5D * d4;
            double d12 = 0.5D + 0.5D * d3 + 0.3D * d4;
            double d13 = 0.5D + 0.3D * d3 + 0.5D * d4;
            double d14 = 0.5D + -0.5D * d3 + 0.3D * d4;
            double d15 = 0.5D + -0.05D * d3 + 0.5D * d4;
            double d16 = 0.5D + -0.5D * d3 + -0.05D * d4;
            double d17 = 0.5D + -0.05D * d3 - 0.5D * d4;
            double d18 = 0.5D + 0.5D * d3 + -0.05D * d4;
            tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d5, d8);
            tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d7, d8);
            tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d7, d6);
            tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d5, d6);
            IIcon iicon2 = block.sunflowerIcons[1];
            d5 = (double)iicon2.getMinU();
            d6 = (double)iicon2.getMinV();
            d7 = (double)iicon2.getMaxU();
            d8 = (double)iicon2.getMaxV();
            tessellator.addVertexWithUV(d19 + d17, d0 + 1.0D, d1 + d18, d5, d8);
            tessellator.addVertexWithUV(d19 + d15, d0 + 1.0D, d1 + d16, d7, d8);
            tessellator.addVertexWithUV(d19 + d13, d0 + 0.0D, d1 + d14, d7, d6);
            tessellator.addVertexWithUV(d19 + d11, d0 + 0.0D, d1 + d12, d5, d6);
        }

        return true;
    }
}
