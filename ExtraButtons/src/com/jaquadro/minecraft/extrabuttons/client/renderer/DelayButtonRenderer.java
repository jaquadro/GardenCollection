package com.jaquadro.minecraft.extrabuttons.client.renderer;

import com.jaquadro.minecraft.extrabuttons.block.DelayButton;
import com.jaquadro.minecraft.extrabuttons.client.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class DelayButtonRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        DelayButton dbBlock = (DelayButton)block;
        if (dbBlock == null)
            return;

        GL11.glPushMatrix();
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-.5f, -0.5625f, -.0625f);

        dbBlock.setBlockForPanelRender(DelayButton.defaultTileEntity);
        renderer.setRenderBoundsFromBlock(block);
        renderStandaloneBlock(renderer, block);

        dbBlock.setBlockForButtonRender(DelayButton.defaultTileEntity);
        renderer.setRenderBoundsFromBlock(block);
        renderStandaloneBlock(renderer, block);

        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        DelayButton dbBlock = (DelayButton)block;
        if (dbBlock == null)
            return false;

        TileEntity te = world.getBlockTileEntity(x, y, z);

        dbBlock.setBlockForPanelRender(te);
        renderer.setRenderBoundsFromBlock(dbBlock);
        renderer.renderStandardBlock(block, x, y, z);

        dbBlock.setBlockForButtonRender(te);
        renderer.setRenderBoundsFromBlock(block);
        renderer.renderStandardBlock(block, x, y, z);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory ()
    {
        return true;
    }

    @Override
    public int getRenderId ()
    {
        return ClientProxy.delayButtonRenderID;
    }

    private void renderStandaloneBlock (RenderBlocks renderBlocks, Block block)
    {
        renderStandaloneBlock(renderBlocks, block, 1f, 1f, 1f);
    }

    private void renderStandaloneBlock (RenderBlocks renderBlocks, Block block, float r, float g, float b)
    {
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(.5f * r, .5f * g, .5f * b);
        tessellator.setNormal(0f, -1f, 0f);
        renderBlocks.renderFaceYNeg(block, 0, 0, 0, block.getBlockTextureFromSide(0));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(1f * r, 1f * g, 1f * b);
        tessellator.setNormal(0f, 1f, 0f);
        renderBlocks.renderFaceYPos(block, 0, 0, 0, block.getBlockTextureFromSide(1));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(.8f * r, .8f * g, .8f * b);
        tessellator.setNormal(0f, 0f, -1f);
        renderBlocks.renderFaceZNeg(block, 0, 0, 0, block.getBlockTextureFromSide(2));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(.8f * r, .8f * g, .8f * b);
        tessellator.setNormal(0f, 0f, 1f);
        renderBlocks.renderFaceZPos(block, 0, 0, 0, block.getBlockTextureFromSide(3));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(.6f * r, .6f * g, .6f * b);
        tessellator.setNormal(-1f, 0f, 0f);
        renderBlocks.renderFaceXNeg(block, 0, 0, 0, block.getBlockTextureFromSide(4));
        tessellator.draw();

        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_F(.6f * r, .6f * g, .6f * b);
        tessellator.setNormal(1f, 0f, 0f);
        renderBlocks.renderFaceXPos(block, 0, 0, 0, block.getBlockTextureFromSide(5));
        tessellator.draw();
    }
}
