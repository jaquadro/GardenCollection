package com.jaquadro.minecraft.gardenstuff.renderer;

import com.jaquadro.minecraft.gardencore.util.RenderUtil;
import com.jaquadro.minecraft.gardenstuff.block.BlockLantern;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLantern;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class LanternRenderer implements ISimpleBlockRenderingHandler
{
    public int renderPass = 0;
    private float[] colorScratch = new float[3];

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockLantern))
            return false;

        return renderWorldBlock(world, x, y, z, (BlockLantern) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockLantern block, int modelId, RenderBlocks renderer) {
        if (renderPass == 0) {
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderStandardBlock(block, x, y, z);

            renderer.renderFromInside = true;
            renderer.renderMinY = .005f;
            renderer.renderStandardBlock(block, x, y, z);
            renderer.renderFromInside = false;

            renderer.overrideBlockTexture = block.getIconTopCross();
            renderer.renderCrossedSquares(block, x, y, z);
            renderer.overrideBlockTexture = null;

            TileEntityLantern tile = block.getTileEntity(world, x, y, z);
            if (tile != null) {
                block.binding.setDefaultMeta(world.getBlockMetadata(x, y, z));
                block.binding.bind(tile.getWorldObj(), x, y, z, 0, 0);
                Tessellator.instance.addTranslation(0, .001f, 0);

                switch (tile.getLightSource()) {
                    case TORCH:
                        renderTorchSource(renderer, block, x, y, z);
                        break;
                    case REDSTONE_TORCH:
                        renderRedstoneTorchSource(renderer, block, x, y, z);
                        break;
                    case GLOWSTONE:
                        renderGlowstoneSource(renderer, block, x, y, z);
                        break;
                    case CANDLE:
                        renderCandleSource(renderer, block, x, y, z);
                        break;
                }

                Tessellator.instance.addTranslation(0, -.001f, 0);
                block.binding.unbind(tile.getWorldObj(), x, y, z);
            }
        }
        else if (renderPass == 1) {
            TileEntityLantern tile = block.getTileEntity(world, x, y, z);
            if (tile != null && tile.hasGlass()) {
                IIcon glass = block.getIconStainedGlass(world.getBlockMetadata(x, y, z));

                RenderUtil.calculateBaseColor(colorScratch, block.getBlockColor());
                RenderUtil.setTessellatorColor(Tessellator.instance, colorScratch);
                Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

                renderer.setRenderBoundsFromBlock(block);
                renderer.renderMinX += .01;
                renderer.renderMinZ += .01;
                renderer.renderMaxX -= .01;
                renderer.renderMaxZ -= .01;

                renderer.renderFaceXNeg(block, x, y, z, glass);
                renderer.renderFaceXPos(block, x, y, z, glass);
                renderer.renderFaceZNeg(block, x, y, z, glass);
                renderer.renderFaceZPos(block, x, y, z, glass);

                renderer.renderMaxY -= .01;
                renderer.renderFaceYPos(block, x, y, z, glass);
            }
            else
                RenderUtil.renderEmptyPlane(block, x, y, z, renderer);
        }

        return true;
    }

    private void renderGlowstoneSource (RenderBlocks renderer, BlockLantern block, int x, int y, int z) {
        renderer.setRenderBounds(.3, 0, .3, .7, .4, .7);
        renderer.renderStandardBlock(Blocks.glowstone, x, y, z);
    }

    private void renderCandleSource (RenderBlocks renderer, BlockLantern block, int x, int y, int z) {
        renderer.overrideBlockTexture = block.getIconCandle();
        renderer.renderCrossedSquares(block, x, y, z);
        renderer.overrideBlockTexture = null;
    }

    private void renderTorchSource (RenderBlocks renderer, BlockLantern block, int x, int y, int z) {
        renderer.renderBlockAllFaces(Blocks.torch, x, y, z);
    }

    private void renderRedstoneTorchSource (RenderBlocks renderer, BlockLantern block, int x, int y, int z) {
        renderer.renderBlockAllFaces(Blocks.redstone_torch, x, y, z);
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.lanternRenderID;
    }
}
