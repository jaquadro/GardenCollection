package com.jaquadro.minecraft.gardencontainers.client.renderer;

import com.jaquadro.minecraft.gardencontainers.block.BlockWindowBox;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityWindowBox;
import com.jaquadro.minecraft.gardencontainers.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.block.support.Slot5Profile;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.client.renderer.support.ModularBoxRenderer;
import com.jaquadro.minecraft.gardencore.util.RenderUtil;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

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
        if (!(block instanceof BlockWindowBox))
            return;

        renderInventoryBlock((BlockWindowBox) block, metadata, modelId, renderer);
    }

    private void renderInventoryBlock (BlockWindowBox block, int metadata, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 1, metadata);

        float unit = .0625f;
        boxRenderer.setIcon(icon);
        boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);

        GL11.glRotatef(90, 0, 1, 0);
        GL11.glTranslatef(-.5f, -.5f, -.5f);

        boxRenderer.renderBox(renderer, block, 0, 0, 0, 0 * unit, 4 * unit, 4 * unit, 16 * unit, 12 * unit, 12 * unit, 0, ModularBoxRenderer.CUT_YPOS);

        GL11.glTranslatef(.5f, .5f, .5f);
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
        boolean validNE = te.isSlotValid(Slot5Profile.SLOT_NE);
        boolean validNW = te.isSlotValid(Slot5Profile.SLOT_NW);
        boolean validSE = te.isSlotValid(Slot5Profile.SLOT_SE);
        boolean validSW = te.isSlotValid(Slot5Profile.SLOT_SW);

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

        ItemStack substrateItem = block.getGardenSubstrate(world, x, y, z, TileEntityGarden.SLOT_INVALID);
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
        return true;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.windowBoxRenderID;
    }
}
