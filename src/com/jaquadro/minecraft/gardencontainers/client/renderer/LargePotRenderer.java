package com.jaquadro.minecraft.gardencontainers.client.renderer;

import com.jaquadro.minecraft.gardencontainers.block.BlockLargePot;
import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityLargePot;
import com.jaquadro.minecraft.gardencontainers.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.client.renderer.support.ModularBoxRenderer;
import com.jaquadro.minecraft.gardencore.util.RenderHelper;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class LargePotRenderer implements ISimpleBlockRenderingHandler
{
    private ModularBoxRenderer boxRenderer = new ModularBoxRenderer();
    private float[] colorScratch = new float[3];

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockLargePot))
            return;

        renderInventoryBlock((BlockLargePot) block, metadata, modelId, renderer);
    }

    private void renderInventoryBlock (BlockLargePot block, int metadata, int modelId, RenderBlocks renderer) {
        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 1, metadata & 15);

        boolean blendEnabled = GL11.glIsEnabled(GL11.GL_BLEND);
        if (blendEnabled)
            GL11.glDisable(GL11.GL_BLEND);

        GL11.glDepthMask(true);

        boxRenderer.setUnit(.0625);
        boxRenderer.setIcon(icon);
        boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);

        GL11.glRotatef(90, 0, 1, 0);
        GL11.glTranslatef(-.5f, -.5f, -.5f);

        boxRenderer.renderBox(null, block, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, ModularBoxRenderer.CUT_YPOS);

        GL11.glEnable(GL11.GL_BLEND);

        if ((metadata & 0xFF00) != 0) {
            int cutFlags = ModularBoxRenderer.CUT_YPOS | ModularBoxRenderer.CUT_YNEG;

            boxRenderer.setUnit(0);
            boxRenderer.setIcon(block.getOverlayIcon((metadata >> 8) & 255));

            boxRenderer.renderExterior(null, block, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, cutFlags);
        }

        if (!blendEnabled)
            GL11.glDisable(GL11.GL_BLEND);

        GL11.glTranslatef(.5f, .5f, .5f);
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockLargePot))
            return false;

        try {
            if (ClientProxy.renderPass == 0)
                return renderWorldBlockPass0(world, x, y, z, (BlockLargePot) block, modelId, renderer);
            else if (ClientProxy.renderPass == 1)
                return renderWorldBlockPass1(world, x, y, z, (BlockLargePot) block, modelId, renderer);
        }
        catch (Exception e) { }

        return false;
    }

    private boolean renderWorldBlockPass0 (IBlockAccess world, int x, int y, int z, BlockLargePot block, int modelId, RenderBlocks renderer) {
        int metadata = world.getBlockMetadata(x, y, z);

        boxRenderer.setUnit(.0625);
        boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);
        for (int i = 0; i < 6; i++)
            boxRenderer.setIcon(renderer.getBlockIconFromSideAndMetadata(block, i, metadata), i);

        TileEntityLargePot te = block.getTileEntity(world, x, y, z);

        int connectFlags = 0;
        connectFlags |= te.isAttachedNeighbor(x - 1, y, z - 1) ? ModularBoxRenderer.CONNECT_ZNEG_XNEG : 0;
        connectFlags |= te.isAttachedNeighbor(x, y, z - 1) ? ModularBoxRenderer.CONNECT_ZNEG : 0;
        connectFlags |= te.isAttachedNeighbor(x + 1, y, z - 1) ? ModularBoxRenderer.CONNECT_ZNEG_XPOS : 0;
        connectFlags |= te.isAttachedNeighbor(x - 1, y, z) ? ModularBoxRenderer.CONNECT_XNEG : 0;
        connectFlags |= te.isAttachedNeighbor(x + 1, y, z) ? ModularBoxRenderer.CONNECT_XPOS : 0;
        connectFlags |= te.isAttachedNeighbor(x - 1, y, z + 1) ? ModularBoxRenderer.CONNECT_ZPOS_XNEG : 0;
        connectFlags |= te.isAttachedNeighbor(x, y, z + 1) ? ModularBoxRenderer.CONNECT_ZPOS : 0;
        connectFlags |= te.isAttachedNeighbor(x + 1, y, z + 1) ? ModularBoxRenderer.CONNECT_ZPOS_XPOS : 0;

        boxRenderer.renderBox(world, block, x, y, z, 0, 0, 0, 1, 1, 1, connectFlags, ModularBoxRenderer.CUT_YPOS);

        if (te != null && te.getSubstrate() != null && te.getSubstrate().getItem() instanceof ItemBlock) {
            Block substrate = Block.getBlockFromItem(te.getSubstrate().getItem());
            int substrateData = te.getSubstrate().getItemDamage();

            if (substrate != Blocks.water) {
                IIcon substrateIcon = renderer.getBlockIconFromSideAndMetadata(substrate, 1, substrateData);

                int color = substrate.colorMultiplier(world, x, y, z);
                if (color == Blocks.grass.colorMultiplier(world, x, y, z))
                    color = ColorizerGrass.getGrassColor(te.getBiomeTemperature(), te.getBiomeHumidity());

                RenderHelper.calculateBaseColor(colorScratch, color);

                RenderHelper.instance.setRenderBounds(0, 0, 0, 1, 1 - .0625, 1);
                RenderHelper.instance.renderFace(RenderHelper.YPOS, world, block, x, y, z, substrateIcon, colorScratch[0], colorScratch[1], colorScratch[2]);
            }
        }

        return true;
    }

    private boolean renderWorldBlockPass1 (IBlockAccess world, int x, int y, int z, BlockLargePot block, int modelId, RenderBlocks renderer) {
        TileEntityLargePot tileEntity = block.getTileEntity(world, x, y, z);
        if (tileEntity == null) {
            RenderHelper.instance.renderEmptyPlane(x, y, z);
            return true;
        }

        boolean didRender = false;

        IIcon icon = block.getOverlayIcon(tileEntity.getCarving());
        if (icon != null) {
            int connectFlags = 0;
            int cutFlags = ModularBoxRenderer.CUT_YPOS | ModularBoxRenderer.CUT_YNEG;

            connectFlags |= tileEntity.isAttachedNeighbor(x, y, z - 1) ? ModularBoxRenderer.CONNECT_ZNEG : 0;
            connectFlags |= tileEntity.isAttachedNeighbor(x - 1, y, z) ? ModularBoxRenderer.CONNECT_XNEG : 0;
            connectFlags |= tileEntity.isAttachedNeighbor(x + 1, y, z) ? ModularBoxRenderer.CONNECT_XPOS : 0;
            connectFlags |= tileEntity.isAttachedNeighbor(x, y, z + 1) ? ModularBoxRenderer.CONNECT_ZPOS : 0;

            if (connectFlags != (ModularBoxRenderer.CONNECT_XNEG | ModularBoxRenderer.CONNECT_XPOS | ModularBoxRenderer.CONNECT_ZNEG | ModularBoxRenderer.CONNECT_ZPOS)) {
                boxRenderer.setUnit(0);
                boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);
                boxRenderer.setExteriorIcon(icon);

                boxRenderer.renderExterior(world, block, x, y, z, 0, 0, 0, 1, 1, 1, connectFlags, cutFlags);

                didRender = true;
            }
        }

        if (tileEntity.getSubstrate() != null && tileEntity.getSubstrate().getItem() instanceof ItemBlock) {
            Block substrate = Block.getBlockFromItem(tileEntity.getSubstrate().getItem());
            int substrateData = tileEntity.getSubstrate().getItemDamage();

            if (substrate == Blocks.water) {
                IIcon substrateIcon = renderer.getBlockIconFromSideAndMetadata(substrate, 1, substrateData);

                RenderHelper.instance.state.setRenderBounds(0, 0, 0, 1, 1 - .0625, 1);
                RenderHelper.instance.renderFace(RenderHelper.YPOS, world, block, x, y, z, substrateIcon);

                didRender = true;
            }
        }

        if (!didRender)
            RenderHelper.instance.renderEmptyPlane(x, y, z);

        return true;
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
