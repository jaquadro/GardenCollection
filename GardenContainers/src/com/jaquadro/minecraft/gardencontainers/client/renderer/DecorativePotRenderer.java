package com.jaquadro.minecraft.gardencontainers.client.renderer;

import com.jaquadro.minecraft.gardencontainers.block.BlockDecorativePot;
import com.jaquadro.minecraft.gardencontainers.core.ClientProxy;
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

public class DecorativePotRenderer implements ISimpleBlockRenderingHandler
{
    private float[] baseColor = new float[3];
    private float[] activeSubstrateColor = new float[3];

    private ModularBoxRenderer boxRenderer = new ModularBoxRenderer();

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockDecorativePot))
            return;

        renderInventoryBlock((BlockDecorativePot) block, metadata, modelId, renderer);
    }

    private void renderInventoryBlock (BlockDecorativePot block, int metadata, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 1, metadata);

        float unit = .0625f;
        boxRenderer.setIcon(icon);
        boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);

        GL11.glRotatef(90, 0, 1, 0);
        GL11.glTranslatef(-.5f, -.5f, -.5f);

        boxRenderer.renderBox(renderer, block, 0, 0, 0, 0, 14 * unit, 0, 1, 1, 1, 0, ModularBoxRenderer.CUT_YNEG | ModularBoxRenderer.CUT_YPOS);
        boxRenderer.renderBox(renderer, block, 0, 0, 0, 1 * unit, 8 * unit, 1 * unit, 15 * unit, 16 * unit, 15 * unit, 0, ModularBoxRenderer.CUT_YPOS);

        boxRenderer.renderSolidBox(renderer, block, 0, 0, 0, 3 * unit, 6 * unit, 3 * unit, 13 * unit, 8 * unit, 13 * unit);
        boxRenderer.renderSolidBox(renderer, block, 0, 0, 0, 5 * unit, 3 * unit, 5 * unit, 11 * unit, 6 * unit, 11 * unit);
        boxRenderer.renderSolidBox(renderer, block, 0, 0, 0, 2 * unit, 0 * unit, 2 * unit, 14 * unit, 3 * unit, 14 * unit);

        GL11.glTranslatef(.5f, .5f, .5f);
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockDecorativePot))
            return false;

        return renderWorldBlock(world, x, y, z, (BlockDecorativePot) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockDecorativePot block, int modelId, RenderBlocks renderer) {
        int data = world.getBlockMetadata(x, y, z);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 1, data);

        RenderUtil.calculateBaseColor(baseColor, block.colorMultiplier(world, x, y, z));

        float unit = .0625f;

        boxRenderer.setIcon(icon);
        boxRenderer.setColor(baseColor);
        boxRenderer.renderBox(renderer, block, x, y, z, 0, 14 * unit, 0, 1, 1, 1, 0, ModularBoxRenderer.CUT_YNEG | ModularBoxRenderer.CUT_YPOS);
        boxRenderer.setScaledColor(baseColor, .9375f);
        boxRenderer.renderBox(renderer, block, x, y, z, 1 * unit, 8 * unit, 1 * unit, 15 * unit, 16 * unit, 15 * unit, 0, ModularBoxRenderer.CUT_YPOS);

        boxRenderer.setScaledExteriorColor(baseColor, .875f);
        boxRenderer.renderSolidBox(renderer, block, x, y, z, 3 * unit, 6 * unit, 3 * unit, 13 * unit, 8 * unit, 13 * unit);
        boxRenderer.setScaledExteriorColor(baseColor, .8125f);
        boxRenderer.renderSolidBox(renderer, block, x, y, z, 5 * unit, 3 * unit, 5 * unit, 11 * unit, 6 * unit, 11 * unit);
        boxRenderer.setScaledExteriorColor(baseColor, .9375f);
        boxRenderer.setScaledExteriorColor(baseColor, .75f, 1);
        boxRenderer.renderSolidBox(renderer, block, x, y, z, 2 * unit, 0 * unit, 2 * unit, 14 * unit, 3 * unit, 14 * unit);

        ItemStack substrateItem = block.getGardenSubstrate(world, x, y, z);
        if (substrateItem != null && substrateItem.getItem() instanceof ItemBlock) {
            Block substrate = Block.getBlockFromItem(substrateItem.getItem());
            IIcon substrateIcon = renderer.getBlockIconFromSideAndMetadata(substrate, 1, substrateItem.getItemDamage());

            RenderUtil.calculateBaseColor(activeSubstrateColor, substrate.getBlockColor());
            RenderUtil.scaleColor(activeSubstrateColor, activeSubstrateColor, .8f);
            RenderUtil.setTessellatorColor(tessellator, activeSubstrateColor);

            renderer.setRenderBounds(.0625f, 0, .0625f, 1f - .0625f, 1f - .0625f, 1f - .0625f);
            renderer.renderFaceYPos(block, x, y, z, substrateIcon);
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return true;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.decorativePotRenderID;
    }
}
