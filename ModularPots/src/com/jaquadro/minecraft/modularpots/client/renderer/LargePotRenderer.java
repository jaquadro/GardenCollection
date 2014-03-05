package com.jaquadro.minecraft.modularpots.client.renderer;

import com.jaquadro.minecraft.modularpots.block.LargePot;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import static com.jaquadro.minecraft.modularpots.block.LargePot.Direction;
import static com.jaquadro.minecraft.modularpots.block.LargePot.Direction.*;

public class LargePotRenderer implements ISimpleBlockRenderingHandler
{
    private float[] baseColor = new float[3];
    private float[] activeRimColor = new float[3];
    private float[] activeInWallColor = new float[3];
    private float[] activeBottomColor = new float[3];
    private float[] activeSubstrateColor = new float[3];

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (!(block instanceof LargePot))
            return;

        renderInventoryBlock((LargePot) block, metadata, modelId, renderer);
    }

    private void renderInventoryBlock (LargePot block, int metadata, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);

        GL11.glRotatef(90, 0, 1, 0);
        GL11.glTranslatef(-.5f, -.5f, -.5f);

        // Bottom
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, -1, 0);
        renderer.renderFaceYNeg(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        // Sides
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, -1);
        renderer.renderFaceZNeg(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, 1);
        renderer.renderFaceZPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1, 0, 0);
        renderer.renderFaceXNeg(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1, 0, 0);
        renderer.renderFaceXPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();

        // Top Lip
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 1, 0);
        for (int i = 1; i <= 4; i++) {
            block.setRenderStep(i);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceYPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        }
        tessellator.draw();

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);

        // Interior Sides
        float dim = .0625f;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, -1);
        renderer.renderFaceZNeg(block, 0, 0, 1 - dim, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, 1);
        renderer.renderFaceZPos(block, 0, 0, dim - 1, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1, 0, 0);
        renderer.renderFaceXNeg(block, 1 - dim, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1, 0, 0);
        renderer.renderFaceXPos(block, dim - 1, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();

        // Interior Bottom
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 1, 0);
        renderer.renderFaceYPos(block, 0, dim - 1, 0 - dim, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        GL11.glTranslatef(.5f, .5f, .5f);
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof LargePot))
            return false;

        return renderWorldBlock(world, x, y, z, (LargePot) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, LargePot block, int modelId, RenderBlocks renderer) {
        renderer.renderStandardBlock(block, x, y, z);

        int data = world.getBlockMetadata(x, y, z);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 1, data);

        TileEntityLargePot tileEntity = block.getTileEntity(world, x, y, z);
        int connected = (tileEntity != null) ? tileEntity.getConnectedFlags() : 0;

        calculateBaseColor(baseColor, block.colorMultiplier(world, x, y, z));
        scaleColor(activeRimColor, baseColor, .8f);
        scaleColor(activeInWallColor, baseColor, .7f);
        scaleColor(activeBottomColor, baseColor, .6f);

        setTessellatorColor(tessellator, activeRimColor);

        float unit = 0.0625f;
        if (!Direction.isSet(connected, Direction.North)) {
            block.setRenderStep(3);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if (!Direction.isSet(connected, Direction.South)) {
            block.setRenderStep(4);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if (!Direction.isSet(connected, Direction.West)) {
            block.setRenderStep(1);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if (!Direction.isSet(connected, Direction.East)) {
            block.setRenderStep(2);
            renderer.setRenderBoundsFromBlock(block);
            renderer.renderFaceYPos(block, x, y, z, icon);
        }

        // Render interior corners
        if (Direction.isSet(connected, North) && Direction.isSet(connected, East) && !Direction.isSet(connected, NorthEast))
            renderCorner(block, renderer, NorthEast, x, y, z, icon);
        if (Direction.isSet(connected, North) && Direction.isSet(connected, West) && !Direction.isSet(connected, NorthWest))
            renderCorner(block, renderer, NorthWest, x, y, z, icon);
        if (Direction.isSet(connected, South) && Direction.isSet(connected, East) && !Direction.isSet(connected, SouthEast))
            renderCorner(block, renderer, SouthEast, x, y, z, icon);
        if (Direction.isSet(connected, South) && Direction.isSet(connected, West) && !Direction.isSet(connected, SouthWest))
            renderCorner(block, renderer, SouthWest, x, y, z, icon);

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);

        setTessellatorColor(tessellator, activeInWallColor);

        if (!Direction.isSet(connected, Direction.West))
            renderer.renderFaceXPos(block, x - 1 + unit, y, z, icon);
        if (!Direction.isSet(connected, Direction.East))
            renderer.renderFaceXNeg(block, x + 1 - unit, y, z, icon);
        if (!Direction.isSet(connected, Direction.North))
            renderer.renderFaceZPos(block, x, y, z - 1 + unit, icon);
        if (!Direction.isSet(connected, Direction.South))
            renderer.renderFaceZNeg(block, x, y, z + 1 - unit, icon);

        if (tileEntity != null && tileEntity.getSubstrate() instanceof ItemBlock) {
            Block substrate = Block.getBlockFromItem(tileEntity.getSubstrate());
            int substrateData = tileEntity.getSubstrateData();

            calculateBaseColor(activeSubstrateColor, substrate.getBlockColor());
            scaleColor(activeSubstrateColor, activeSubstrateColor, .8f);
            setTessellatorColor(tessellator, activeSubstrateColor);

            IIcon substrateIcon = renderer.getBlockIconFromSideAndMetadata(substrate, 1, substrateData);
            renderer.renderFaceYPos(block, x, y - unit, z, substrateIcon);
        }
        else {
            setTessellatorColor(tessellator, activeBottomColor);
            renderer.renderFaceYPos(block, x, y - 1 + unit, z, renderer.getBlockIcon(Blocks.hardened_clay));
        }

        return true;
    }

    private void renderCorner (LargePot block, RenderBlocks renderer, Direction direction, int x, int y, int z, IIcon icon) {
        Tessellator tessellator = Tessellator.instance;
        float dim = .0625f;

        switch (direction) {
            case NorthEast:
                block.setBlockBounds(1 - dim, 0, 0, 1, 1, dim);
                renderer.setRenderBoundsFromBlock(block);
                setTessellatorColor(tessellator, activeRimColor);
                renderer.renderFaceYPos(block, x, y, z, icon);
                setTessellatorColor(tessellator, activeInWallColor);
                renderer.renderFaceXNeg(block, x, y, z, icon);
                renderer.renderFaceZPos(block, x, y, z, icon);
                break;
            case NorthWest:
                block.setBlockBounds(0, 0, 0, dim, 1, dim);
                renderer.setRenderBoundsFromBlock(block);
                setTessellatorColor(tessellator, activeRimColor);
                renderer.renderFaceYPos(block, x, y, z, icon);
                setTessellatorColor(tessellator, activeInWallColor);
                renderer.renderFaceXPos(block, x, y, z, icon);
                renderer.renderFaceZPos(block, x, y, z, icon);
                break;
            case SouthEast:
                block.setBlockBounds(1 - dim, 0, 1 - dim, 1, 1, 1);
                renderer.setRenderBoundsFromBlock(block);
                setTessellatorColor(tessellator, activeRimColor);
                renderer.renderFaceYPos(block, x, y, z, icon);
                setTessellatorColor(tessellator, activeInWallColor);
                renderer.renderFaceXNeg(block, x, y, z, icon);
                renderer.renderFaceZNeg(block, x, y, z, icon);
                break;
            case SouthWest:
                block.setBlockBounds(0, 0, 1 - dim, dim, 1, 1);
                renderer.setRenderBoundsFromBlock(block);
                setTessellatorColor(tessellator, activeRimColor);
                renderer.renderFaceYPos(block, x, y, z, icon);
                setTessellatorColor(tessellator, activeInWallColor);
                renderer.renderFaceXPos(block, x, y, z, icon);
                renderer.renderFaceZNeg(block, x, y, z, icon);
                break;
        }

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
    }

    private void calculateBaseColor (float[] target, int color) {
        float r = (float)(color >> 16 & 255) / 255f;
        float g = (float)(color >> 8 & 255) / 255f;
        float b = (float)(color & 255) / 255f;

        if (EntityRenderer.anaglyphEnable) {
            float gray = (r * 30f + g * 59f + b * 11f) / 100f;
            float rg = (r * 30f + g * 70f) / 100f;
            float rb = (r * 30f + b * 70f) / 100f;

            r = gray;
            g = rg;
            b = rb;
        }

        target[0] = r;
        target[1] = g;
        target[2] = b;
    }

    private void scaleColor (float[] target, float[] source, float scale) {
        target[0] = source[0] * scale;
        target[1] = source[1] * scale;
        target[2] = source[2] * scale;
    }

    private void setTessellatorColor (Tessellator tessellator, float[] color) {
        tessellator.setColorOpaque_F(color[0], color[1], color[2]);
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return true;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.largePotRenderID;
    }
}
