package com.jaquadro.minecraft.gardenstuff.renderer.item;

import com.jaquadro.minecraft.gardenapi.api.component.ILanternSource;
import com.jaquadro.minecraft.gardenapi.internal.Api;
import com.jaquadro.minecraft.gardencore.util.RenderHelper;
import com.jaquadro.minecraft.gardenstuff.block.BlockLantern;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

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
            renderHelper.renderFace(RenderHelper.YPOS, renderer, block, block.getIcon(0, item.getItemDamage()), 0);
        }

        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderer.overrideBlockTexture = block.getIconTopCross();
        renderHelper.renderCrossedSquares(renderer, block, item.getItemDamage());
        renderer.overrideBlockTexture = null;

        ILanternSource lanternSource = Api.instance.registries().lanternSources().getLanternSource(block.getLightSource(item));
        if (lanternSource != null)
            lanternSource.renderItem(renderer, renderType, block.getLightSourceMeta(item));

        if (block.isGlass(item)) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_NORMALIZE);

            RenderHelper.calculateBaseColor(colorScratch, block.getBlockColor());
            RenderHelper.setTessellatorColor(Tessellator.instance, colorScratch);
            Tessellator.instance.setBrightness(RenderHelper.FULL_BRIGHTNESS);

            IIcon glass = block.getIconStainedGlass(item.getItemDamage());

            renderer.setRenderBoundsFromBlock(block);
            renderer.renderMinX += .01;
            renderer.renderMinZ += .01;
            renderer.renderMaxX -= .01;
            renderer.renderMaxZ -= .01;
            renderer.renderMaxY -= .01;

            renderHelper.renderFace(RenderHelper.XNEG, renderer, block, glass, 0);
            renderHelper.renderFace(RenderHelper.XPOS, renderer, block, glass, 0);
            renderHelper.renderFace(RenderHelper.ZNEG, renderer, block, glass, 0);
            renderHelper.renderFace(RenderHelper.ZPOS, renderer, block, glass, 0);
            renderHelper.renderFace(RenderHelper.YPOS, renderer, block, glass, 0);

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    private RenderBlocks getRenderer (Object[] data) {
        for (Object obj : data) {
            if (obj instanceof RenderBlocks)
                return (RenderBlocks)obj;
        }

        return null;
    }
}
