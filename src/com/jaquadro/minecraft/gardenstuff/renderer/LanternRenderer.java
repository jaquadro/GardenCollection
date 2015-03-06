package com.jaquadro.minecraft.gardenstuff.renderer;

import com.jaquadro.minecraft.gardenstuff.block.BlockLantern;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class LanternRenderer implements ISimpleBlockRenderingHandler
{
    public int renderPass = 0;

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

            renderer.overrideBlockTexture = block.getIconCandle();
            renderer.renderCrossedSquares(block, x, y, z);
            renderer.overrideBlockTexture = block.getIconTopCross();
            renderer.renderCrossedSquares(block, x, y, z);
            renderer.overrideBlockTexture = null;
        }
        else if (renderPass == 1) {
            IIcon glass = block.getIconGlass();

            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceXNeg(block, x, y, z, glass);
            renderer.renderFaceXPos(block, x, y, z, glass);
            renderer.renderFaceZNeg(block, x, y, z, glass);
            renderer.renderFaceZPos(block, x, y, z, glass);
        }

        return true;
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
