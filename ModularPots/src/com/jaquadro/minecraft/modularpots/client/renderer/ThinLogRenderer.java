package com.jaquadro.minecraft.modularpots.client.renderer;

import com.jaquadro.minecraft.modularpots.block.LargePot;
import com.jaquadro.minecraft.modularpots.block.ThinLog;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class ThinLogRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof ThinLog))
            return false;

        return renderWorldBlock(world, x, y, z, (ThinLog) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, ThinLog block, int modelId, RenderBlocks renderer) {
        renderer.renderStandardBlock(block, x, y, z);

        Block blockUnder = world.getBlock(x, y - 1, z);
        if (blockUnder instanceof LargePot) {
            //Tessellator tessellator = Tessellator.instance;
            //tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            //tessellator.setColorOpaque_F(1f, 1f, 1f);

            float margin = block.getMargin();
//            block.setBlockBounds(margin, -.0625f, margin, 1 - margin, 0, 1 - margin);
            renderer.setRenderBounds(margin, 1 - .0625f, margin, 1 - margin, 1, 1 - margin);
            renderer.renderStandardBlock(block, x, y - 1, z);

            //IIcon sideIcon = block.getIcon(2, world.getBlockMetadata(x, y, z));
            //renderer.renderFaceXNeg(block, x, y - 1, z, sideIcon);
            //renderer.renderFaceXPos(block, x, y - 1, z, sideIcon);
            //renderer.renderFaceZNeg(block, x, y - 1, z, sideIcon);
            //renderer.renderFaceZPos(block, x, y - 1, z, sideIcon);
//            block.setBlockBoundsForItemRender();
            renderer.setRenderBoundsFromBlock(block);
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.thinLogRenderID;
    }
}
