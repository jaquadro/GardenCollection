package com.jaquadro.minecraft.gardencore.client.renderer;

import com.jaquadro.minecraft.gardencore.util.RenderUtil;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class WindowBoxRenderer implements ISimpleBlockRenderingHandler
{
    private float[] baseColor = new float[3];

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int data = world.getBlockMetadata(x, y, z);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 1, data);

        RenderUtil.calculateBaseColor(baseColor, block.colorMultiplier(world, x, y, z));
        RenderUtil.setTessellatorColor(tessellator, baseColor);

        RenderUtil.renderOctantExterior(block, x, y, z, icon, renderer, .0625, RenderUtil.CONNECT_XPOS, RenderUtil.CUT_YPOS);
        RenderUtil.renderOctantExterior(block, x + .5, y, z, icon, renderer, .0625, RenderUtil.CONNECT_XNEG, RenderUtil.CUT_YPOS);

        RenderUtil.renderOctantInterior(block, x, y, z, icon, renderer, .0625, RenderUtil.CONNECT_XPOS, RenderUtil.CUT_YPOS);
        RenderUtil.renderOctantInterior(block, x + .5, y, z, icon, renderer, .0625, RenderUtil.CONNECT_XNEG, RenderUtil.CUT_YPOS);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return 0;
    }

    /*private void renderOctZNeg (Block block, double x, double y, double z, IIcon icon, RenderBlocks renderer, int connect) {
        if ((connect & CONNECT_ZNEG) != 0)
            return;

        double xNeg = x - (int)x;
        double yNeg = y - (int)y;
        double zNeg = z - (int)z;
        double xPos = xNeg + .5;
        double yPos = yNeg + .5;
        double zPos = zNeg + UNIT;

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceZNeg(block, x, y, z, icon);

        if ((connect & CONNECT_XNEG) != 0)
            xNeg += UNIT;
        if ((connect & CONNECT_XPOS) != 0)
            xPos -= UNIT;

        renderer.setRenderBounds(xNeg, yNeg, zNeg, xPos, yPos, zPos);
        renderer.renderFaceZPos(block, x, y, z, icon);
    }

    private void renderZNeg (Block block, int x, int y, int z, IIcon icon, RenderBlocks renderer, int xNegEdge, int xPosEdge, int yLevel) {
        float unit = 0.0625f;

        double yNeg = (yLevel == Y_LOWER) ? 0 : .5;
        double yPos = (yLevel == Y_LOWER) ? .5 : 1;

        switch (xNegEdge) {
            case EDGE_FLUSH:
                renderer.setRenderBounds(0, yNeg, 0, .5, yPos, unit);
                renderer.renderFaceXNeg(block, x, y, z, icon);
        }

        if (xNegEdge != EDGE_NONE) {
            double xNeg = 0;
            double xPos = .5;
            renderer.setRenderBounds();
        }
    }*/
}
