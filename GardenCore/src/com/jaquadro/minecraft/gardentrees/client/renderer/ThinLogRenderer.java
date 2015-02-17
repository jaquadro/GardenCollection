package com.jaquadro.minecraft.gardentrees.client.renderer;

import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardentrees.block.BlockThinLog;
import com.jaquadro.minecraft.gardentrees.core.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
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
        if (!(block instanceof BlockThinLog))
            return false;

        return renderWorldBlock(world, x, y, z, (BlockThinLog) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockThinLog block, int modelId, RenderBlocks renderer) {
        float margin = block.getMargin();
        int connectFlags = block.calcConnectionFlags(world, x, y, z);

        boolean connectYNeg = (connectFlags & 1) != 0;
        boolean connectYPos = (connectFlags & 2) != 0;
        boolean connectZNeg = (connectFlags & 4) != 0;
        boolean connectZPos = (connectFlags & 8) != 0;
        boolean connectXNeg = (connectFlags & 16) != 0;
        boolean connectXPos = (connectFlags & 32) != 0;

        boolean connectY = connectYNeg | connectYPos;
        boolean connectZ = connectZNeg | connectZPos;
        boolean connectX = connectXNeg | connectXPos;

        if (!(connectYNeg && connectYPos) && !(connectZNeg && connectZPos) && !(connectXNeg && connectXPos)) {
            if (connectY && !connectX && !connectZ)
                block.setOrientation(0);
            else if (connectZ && !connectY && !connectX)
                block.setOrientation(1);
            else if (connectX && !connectY && !connectZ)
                block.setOrientation(2);
            else
                block.setOrientation(3);

            renderer.setRenderBounds(margin, margin, margin, 1 - margin, 1 - margin, 1 - margin);
            renderer.renderStandardBlock(block, x, y, z);
        }

        if (connectY) {
            block.setOrientation(0);
            if (connectYNeg && connectYPos)
                renderer.setRenderBounds(margin, 0, margin, 1 - margin, 1, 1 - margin);
            else if (connectYNeg)
                renderer.setRenderBounds(margin, 0, margin, 1 - margin, margin, 1 - margin);
            else if (connectYPos)
                renderer.setRenderBounds(margin, 1 - margin, margin, 1 - margin, 1, 1 - margin);
            renderer.renderStandardBlock(block, x, y, z);
        }

        if (connectZ) {
            block.setOrientation(1);
            if (connectZNeg && connectZPos)
                renderer.setRenderBounds(margin, margin, 0, 1 - margin, 1 - margin, 1);
            else if (connectZNeg)
                renderer.setRenderBounds(margin, margin, 0, 1 - margin, 1 - margin, margin);
            else if (connectZPos)
                renderer.setRenderBounds(margin, margin, 1 - margin, 1 - margin, 1 - margin, 1);
            renderer.renderStandardBlock(block, x, y, z);
        }

        if (connectX) {
            block.setOrientation(2);
            if (connectXNeg && connectXPos)
                renderer.setRenderBounds(0, margin, margin, 1, 1 - margin, 1 - margin);
            else if (connectXNeg)
                renderer.setRenderBounds(0, margin, margin, margin, 1 - margin, 1 - margin);
            else if (connectXPos)
                renderer.setRenderBounds(1 - margin, margin, margin, 1, 1 - margin, 1 - margin);
            renderer.renderStandardBlock(block, x, y, z);
        }

        block.setOrientation(0);

        //renderer.renderStandardBlock(block, x, y, z);

        Block blockUnder = world.getBlock(x, y - 1, z);
        if (blockUnder instanceof BlockGarden) {
            float yDiff = ((BlockGarden) blockUnder).getSlotProfile().getPlantOffsetY(world, x, y, z, 0);
            if (yDiff >= (1 - .0625f) * 2) {
                //Tessellator tessellator = Tessellator.instance;
                //tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
                //tessellator.setColorOpaque_F(1f, 1f, 1f);

//            block.setBlockBounds(margin, -.0625f, margin, 1 - margin, 0, 1 - margin);
                renderer.setRenderBounds(margin, yDiff, margin, 1 - margin, 1, 1 - margin);
                renderer.renderStandardBlock(block, x, y - 1, z);

                //IIcon sideIcon = block.getIcon(2, world.getBlockMetadata(x, y, z));
                //renderer.renderFaceXNeg(block, x, y - 1, z, sideIcon);
                //renderer.renderFaceXPos(block, x, y - 1, z, sideIcon);
                //renderer.renderFaceZNeg(block, x, y - 1, z, sideIcon);
                //renderer.renderFaceZPos(block, x, y - 1, z, sideIcon);
//            block.setBlockBoundsForItemRender();
                renderer.setRenderBoundsFromBlock(block);
            }
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
