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
import org.lwjgl.opengl.GL11;

public class ThinLogRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof ThinLog))
            return false;

        return renderWorldBlock(world, x, y, z, (ThinLog) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, ThinLog block, int modelId, RenderBlocks renderer) {
        float margin = block.getMargin();
        int connectFlags = block.calcConnectionFlags(world, x, y, z);

        boolean connectYNeg = (connectFlags & 1) != 0;
        boolean connectYPos = (connectFlags & 2) != 0;
        boolean connectZNeg = (connectFlags & 4) != 0;
        boolean connectZPos = (connectFlags & 8) != 0;
        boolean connectXNeg = (connectFlags & 16) != 0;
        boolean connectXPos = (connectFlags & 32) != 0;

        if (connectYNeg || connectYPos) {
            if (connectYNeg && connectYPos)
                renderer.setRenderBounds(margin, 0, margin, 1 - margin, 1, 1 - margin);
            else if (connectYNeg)
                renderer.setRenderBounds(margin, 0, margin, 1 - margin, 1 - margin, 1 - margin);
            else if (connectYPos)
                renderer.setRenderBounds(margin, margin, margin, 1 - margin, 1, 1 - margin);
            renderer.renderStandardBlock(block, x, y, z);
        }

        if (connectZNeg || connectZPos) {
            if (connectZNeg && connectZPos)
                renderer.setRenderBounds(margin, margin, 0, 1 - margin, 1 - margin, 1);
            else if (connectZNeg)
                renderer.setRenderBounds(margin, margin, 0, 1 - margin, 1 - margin, 1 - margin);
            else if (connectZPos)
                renderer.setRenderBounds(margin, margin, margin, 1 - margin, 1 - margin, 1);
            renderer.renderStandardBlock(block, x, y, z);
        }

        if (connectXNeg || connectXPos) {
            if (connectXNeg && connectXPos)
                renderer.setRenderBounds(0, margin, margin, 1, 1 - margin, 1 - margin);
            else if (connectXNeg)
                renderer.setRenderBounds(0, margin, margin, 1 - margin, 1 - margin, 1 - margin);
            else if (connectXPos)
                renderer.setRenderBounds(margin, margin, margin, 1, 1 - margin, 1 - margin);
            renderer.renderStandardBlock(block, x, y, z);
        }

        //renderer.renderStandardBlock(block, x, y, z);

        Block blockUnder = world.getBlock(x, y - 1, z);
        if (blockUnder instanceof LargePot) {
            //Tessellator tessellator = Tessellator.instance;
            //tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            //tessellator.setColorOpaque_F(1f, 1f, 1f);

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
        return true;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.thinLogRenderID;
    }
}
