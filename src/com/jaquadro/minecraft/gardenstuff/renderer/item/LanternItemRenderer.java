package com.jaquadro.minecraft.gardenstuff.renderer.item;

import com.jaquadro.minecraft.gardencore.client.renderer.support.ModularBoxRenderer;
import com.jaquadro.minecraft.gardencore.util.RenderHelper;
import com.jaquadro.minecraft.gardencore.util.RenderUtil;
import com.jaquadro.minecraft.gardenstuff.block.BlockLantern;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLantern;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class LanternItemRenderer implements IItemRenderer
{
    private RenderHelper renderHelper = new RenderHelper();
    private float[] colorScratch = new float[3];

    @Override
    public boolean handleRenderType (ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper (ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem (ItemRenderType type, ItemStack item, Object... data) {
        RenderBlocks renderer = getRenderer(data);
        if (renderer == null)
            return;

        Block block = Block.getBlockFromItem(item.getItem());
        if (!(block instanceof BlockLantern))
            return;

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        renderLantern((BlockLantern)block, item, renderer, type);
    }

    private void renderLantern (BlockLantern block, ItemStack item, RenderBlocks renderer, ItemRenderType renderType) {
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        if (renderType == ItemRenderType.INVENTORY) {
            GL11.glScalef(1.2f, 1.2f, 1.2f);
        }

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        renderHelper.renderBlock(renderer, block, item.getItemDamage());

        if (renderType != ItemRenderType.INVENTORY) {
            renderer.renderFromInside = true;
            renderer.renderMinY = .005f;
            renderHelper.renderBlock(renderer, block, item.getItemDamage());
            renderer.renderFromInside = false;
        }
        else {
            renderer.renderMaxY = .005f;
            renderHelper.renderFace(RenderHelper.YPOS, renderer, block, block.getIcon(0, item.getItemDamage()));
        }

        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderer.overrideBlockTexture = block.getIconTopCross();
        renderHelper.renderCrossedSquares(renderer, block, item.getItemDamage());
        renderer.overrideBlockTexture = null;

        switch (block.getLightSource(item)) {
            case TORCH:
                renderTorchSource(renderer);
                break;
            case REDSTONE_TORCH:
                renderRedstoneTorchSource(renderer);
                break;
            case GLOWSTONE:
                renderGlowstoneSource(renderer);
                break;
            case CANDLE:
                renderCandleSource(renderer, block);
                break;
        }

        if (block.isGlass(item)) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_NORMALIZE);

            renderHelper.calculateBaseColor(colorScratch, block.getBlockColor());
            RenderUtil.setTessellatorColor(Tessellator.instance, colorScratch);
            Tessellator.instance.setBrightness(RenderHelper.FULL_BRIGHTNESS);

            IIcon glass = block.getIconStainedGlass(item.getItemDamage());

            renderer.setRenderBoundsFromBlock(block);
            renderer.renderMinX += .01;
            renderer.renderMinZ += .01;
            renderer.renderMaxX -= .01;
            renderer.renderMaxZ -= .01;
            renderer.renderMaxY -= .01;

            renderHelper.renderFace(RenderHelper.XNEG, renderer, block, glass);
            renderHelper.renderFace(RenderHelper.XPOS, renderer, block, glass);
            renderHelper.renderFace(RenderHelper.ZNEG, renderer, block, glass);
            renderHelper.renderFace(RenderHelper.ZPOS, renderer, block, glass);
            renderHelper.renderFace(RenderHelper.YPOS, renderer, block, glass);

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    private void renderGlowstoneSource (RenderBlocks renderer) {
        renderer.setRenderBounds(.3, 0, .3, .7, .4, .7);
        renderHelper.renderBlock(renderer, Blocks.glowstone, 0);
    }

    private void renderCandleSource (RenderBlocks renderer, BlockLantern block) {
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderer.overrideBlockTexture = block.getIconCandle();
        renderHelper.renderCrossedSquares(renderer, block, 0);
        renderer.overrideBlockTexture = null;
    }

    private void renderTorchSource (RenderBlocks renderer) {
        renderTorchBlock(renderer, Blocks.torch);
    }

    private void renderRedstoneTorchSource (RenderBlocks renderer) {
        renderTorchBlock(renderer, Blocks.redstone_torch);
    }

    private void renderTorchBlock (RenderBlocks renderer, Block torchBlock) {
        renderer.setRenderBounds(0, 0, 0.4375, 1, 1, 0.5625);
        renderHelper.renderFace(RenderHelper.ZNEG, renderer, torchBlock, torchBlock.getIcon(2, 0));
        renderHelper.renderFace(RenderHelper.ZPOS, renderer, torchBlock, torchBlock.getIcon(3, 0));
        renderer.setRenderBounds(0.4375, 0, 0, 0.5625, 1, 1);
        renderHelper.renderFace(RenderHelper.XNEG, renderer, torchBlock, torchBlock.getIcon(4, 0));
        renderHelper.renderFace(RenderHelper.XPOS, renderer, torchBlock, torchBlock.getIcon(5, 0));
        renderer.setRenderBounds(0.4375, 0, 0.4375, 0.5625, 0.625, 0.5625);
        renderHelper.renderFace(RenderHelper.YPOS, renderer, torchBlock, torchBlock.getIcon(1, 0));
    }

    private RenderBlocks getRenderer (Object[] data) {
        for (Object obj : data) {
            if (obj instanceof RenderBlocks)
                return (RenderBlocks)obj;
        }

        return null;
    }
}
