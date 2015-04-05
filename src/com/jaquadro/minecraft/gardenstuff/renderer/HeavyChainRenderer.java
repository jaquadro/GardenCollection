package com.jaquadro.minecraft.gardenstuff.renderer;

import com.jaquadro.minecraft.gardencore.api.block.IChainSingleAttachable;
import com.jaquadro.minecraft.gardenstuff.block.BlockHeavyChain;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

public class HeavyChainRenderer implements ISimpleBlockRenderingHandler
{
    private static final Vec3[] defaultAttachPoint = new Vec3[] {
        Vec3.createVectorHelper(.5, 0, .5),
        Vec3.createVectorHelper(.5, 1, .5),
        Vec3.createVectorHelper(.5, .5, 0),
        Vec3.createVectorHelper(.5, .5, 1),
        Vec3.createVectorHelper(0, .5, .5),
        Vec3.createVectorHelper(1, .5, .5),
    };
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (block instanceof BlockHeavyChain)
            return renderWorldBlock(world, x, y, z, (BlockHeavyChain)block, modelId, renderer);

        return false;
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockHeavyChain block, int modelId, RenderBlocks renderer) {
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderCrossedSquares(renderer, block, x, y, z);

        Block upperBlock = world.getBlock(x, y + 1, z);
        if (upperBlock instanceof IChainSingleAttachable) {
            Vec3 attach = ((IChainSingleAttachable) upperBlock).getChainAttachPoint(world, x, y + 1, z, 0);
            if (attach != null && attach != defaultAttachPoint[0]) {
                renderer.setRenderBounds(0, 0, 0, 1, attach.yCoord, 1);
                renderer.setOverrideBlockTexture(block.getIcon(world, x, y, z, 0));
                renderCrossedSquares(renderer, block, x, y + 1, z);
                renderer.setOverrideBlockTexture(null);
            }
        }

        Block lowerBlock = world.getBlock(x, y - 1, z);
        if (lowerBlock instanceof IChainSingleAttachable) {
            Vec3 attach = ((IChainSingleAttachable) lowerBlock).getChainAttachPoint(world, x, y - 1, z, 0);
            if (attach != null && attach != defaultAttachPoint[1]) {
                renderer.setRenderBounds(0, attach.yCoord, 0, 1, 1, 1);
                renderer.setOverrideBlockTexture(block.getIcon(world, x, y, z, 0));
                renderCrossedSquares(renderer, block, x, y - 1, z);
                renderer.setOverrideBlockTexture(null);
            }
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.heavyChainRenderID;
    }

    private void renderCrossedSquares (RenderBlocks renderer, Block block, int x, int y, int z)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        int l = block.colorMultiplier(renderer.blockAccess, x, y, z);
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

        IIcon iicon = renderer.getBlockIconFromSideAndMetadata(block, 0, renderer.blockAccess.getBlockMetadata(x, y, z));
        drawCrossedSquares(renderer, iicon, x, y, z, 1.0F);
    }

    private void drawCrossedSquares(RenderBlocks renderer, IIcon icon, double x, double y, double z, float scale)
    {
        Tessellator tessellator = Tessellator.instance;
        if (renderer.hasOverrideBlockTexture())
            icon = renderer.overrideBlockTexture;

        double uMin = icon.getInterpolatedU(renderer.renderMinX * 16.0D);
        double uMax = icon.getInterpolatedU(renderer.renderMaxX * 16.0D);
        double vMin = icon.getInterpolatedV(16 - renderer.renderMaxY * 16.0D);
        double vMax = icon.getInterpolatedV(16 - renderer.renderMinY * 16.0D);

        double d7 = 0.45D * (double)scale;
        double xMin = x + 0.5D - d7;
        double xMax = x + 0.5D + d7;
        double yMin = y + renderer.renderMinY * scale;
        double yMax = y + renderer.renderMaxY * scale;
        double zMin = z + 0.5D - d7;
        double zMax = z + 0.5D + d7;

        tessellator.addVertexWithUV(xMin, yMax, zMin, uMin, vMin);
        tessellator.addVertexWithUV(xMin, yMin, zMin, uMin, vMax);
        tessellator.addVertexWithUV(xMax, yMin, zMax, uMax, vMax);
        tessellator.addVertexWithUV(xMax, yMax, zMax, uMax, vMin);
        tessellator.addVertexWithUV(xMax, yMax, zMax, uMin, vMin);
        tessellator.addVertexWithUV(xMax, yMin, zMax, uMin, vMax);
        tessellator.addVertexWithUV(xMin, yMin, zMin, uMax, vMax);
        tessellator.addVertexWithUV(xMin, yMax, zMin, uMax, vMin);

        tessellator.addVertexWithUV(xMin, yMax, zMax, uMin, vMin);
        tessellator.addVertexWithUV(xMin, yMin, zMax, uMin, vMax);
        tessellator.addVertexWithUV(xMax, yMin, zMin, uMax, vMax);
        tessellator.addVertexWithUV(xMax, yMax, zMin, uMax, vMin);
        tessellator.addVertexWithUV(xMax, yMax, zMin, uMin, vMin);
        tessellator.addVertexWithUV(xMax, yMin, zMin, uMin, vMax);
        tessellator.addVertexWithUV(xMin, yMin, zMax, uMax, vMax);
        tessellator.addVertexWithUV(xMin, yMax, zMax, uMax, vMin);
    }
}
