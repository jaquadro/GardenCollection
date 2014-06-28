package com.jaquadro.minecraft.gardencore.client.renderer;

import com.jaquadro.minecraft.gardencore.block.BlockWindowBox;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityWindowBox;
import com.jaquadro.minecraft.gardencore.client.renderer.support.ModularBoxRenderer;
import com.jaquadro.minecraft.gardencore.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.util.RenderUtil;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class WindowBoxRenderer implements ISimpleBlockRenderingHandler
{
    private float[] baseColor = new float[3];
    private float[] activeRimColor = new float[3];
    private float[] activeInWallColor = new float[3];
    private float[] activeBottomColor = new float[3];
    private float[] activeSubstrateColor = new float[3];

    private ModularBoxRenderer boxRenderer = new ModularBoxRenderer();

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockWindowBox))
            return false;

        return renderWorldBlock(world, x, y, z, (BlockWindowBox) block, modelId, renderer);
    }

    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockWindowBox block, int modelId, RenderBlocks renderer) {
        int data = world.getBlockMetadata(x, y, z);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 1, data);

        RenderUtil.calculateBaseColor(baseColor, block.colorMultiplier(world, x, y, z));
        RenderUtil.scaleColor(activeRimColor, baseColor, .8f);
        RenderUtil.scaleColor(activeInWallColor, baseColor, .7f);
        RenderUtil.scaleColor(activeBottomColor, baseColor, .6f);

        boxRenderer.setIcon(icon);
        boxRenderer.setExteriorColor(baseColor);
        boxRenderer.setInteriorColor(activeInWallColor);
        boxRenderer.setInteriorColor(activeBottomColor, ModularBoxRenderer.FACE_YNEG);
        boxRenderer.setCutColor(activeRimColor);

        TileEntityWindowBox te = block.getTileEntity(world, x, y, z);
        boolean validNE = te.isSlotValid(TileEntityGarden.SLOT_NE);
        boolean validNW = te.isSlotValid(TileEntityGarden.SLOT_NW);
        boolean validSE = te.isSlotValid(TileEntityGarden.SLOT_SE);
        boolean validSW = te.isSlotValid(TileEntityGarden.SLOT_SW);

        if (validNW) {
            int connect = 0 | (validNE ? ModularBoxRenderer.CONNECT_XPOS : 0) | (validSW ? ModularBoxRenderer.CONNECT_ZPOS : 0);
            boxRenderer.renderOctant(renderer, block, x, y + (te.isUpper() ? .5 : 0), z, connect, ModularBoxRenderer.CUT_YPOS);
        }
        if (validNE) {
            int connect = 0 | (validNW ? ModularBoxRenderer.CONNECT_XNEG : 0) | (validSE ? ModularBoxRenderer.CONNECT_ZPOS : 0);
            boxRenderer.renderOctant(renderer, block, x + .5, y + (te.isUpper() ? .5 : 0), z, connect, ModularBoxRenderer.CUT_YPOS);
        }
        if (validSW) {
            int connect = 0 | (validSE ? ModularBoxRenderer.CONNECT_XPOS : 0) | (validNW ? ModularBoxRenderer.CONNECT_ZNEG : 0);
            boxRenderer.renderOctant(renderer, block, x, y + (te.isUpper() ? .5 : 0), z + .5, connect, ModularBoxRenderer.CUT_YPOS);
        }
        if (validSE) {
            int connect = 0 | (validSW ? ModularBoxRenderer.CONNECT_XNEG : 0) | (validNE ? ModularBoxRenderer.CONNECT_ZNEG : 0);
            boxRenderer.renderOctant(renderer, block, x + .5, y + (te.isUpper() ? .5 : 0), z + .5, connect, ModularBoxRenderer.CUT_YPOS);
        }

        ItemStack substrateItem = block.getGardenSubstrate(world, x, y, z);
        if (substrateItem != null && substrateItem.getItem() instanceof ItemBlock) {
            Block substrate = Block.getBlockFromItem(substrateItem.getItem());
            IIcon substrateIcon = renderer.getBlockIconFromSideAndMetadata(substrate, 1, substrateItem.getItemDamage());

            RenderUtil.calculateBaseColor(activeSubstrateColor, substrate.getBlockColor());
            RenderUtil.scaleColor(activeSubstrateColor, activeSubstrateColor, .8f);
            RenderUtil.setTessellatorColor(tessellator, activeSubstrateColor);

            double ySubstrate = (te.isUpper() ? 1 : .5) - .0625;
            if (validNW) {
                renderer.setRenderBounds(0, 0, 0, .5, ySubstrate, .5);
                renderer.renderFaceYPos(block, x, y, z, substrateIcon);
            }
            if (validNE) {
                renderer.setRenderBounds(.5, 0, 0, 1, ySubstrate, .5);
                renderer.renderFaceYPos(block, x, y, z, substrateIcon);
            }
            if (validSW) {
                renderer.setRenderBounds(0, 0, .5, .5, ySubstrate, 1);
                renderer.renderFaceYPos(block, x, y, z, substrateIcon);
            }
            if (validSE) {
                renderer.setRenderBounds(.5, 0, .5, 1, ySubstrate, 1);
                renderer.renderFaceYPos(block, x, y, z, substrateIcon);
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
        return ClientProxy.windowBoxRenderID;
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
