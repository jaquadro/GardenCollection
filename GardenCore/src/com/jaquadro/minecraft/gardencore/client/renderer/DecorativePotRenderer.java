package com.jaquadro.minecraft.gardencore.client.renderer;

import com.jaquadro.minecraft.gardencore.block.BlockDecorativePot;
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

public class DecorativePotRenderer implements ISimpleBlockRenderingHandler
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
        RenderUtil.scaleColor(activeRimColor, baseColor, .8f);
        RenderUtil.scaleColor(activeInWallColor, baseColor, .7f);
        RenderUtil.scaleColor(activeBottomColor, baseColor, .6f);

        boxRenderer.setIcon(icon);
        boxRenderer.setExteriorColor(baseColor);
        boxRenderer.setInteriorColor(activeInWallColor);
        boxRenderer.setInteriorColor(activeBottomColor, ModularBoxRenderer.FACE_YNEG);
        boxRenderer.setCutColor(activeRimColor);

        RenderUtil.calculateBaseColor(baseColor, block.colorMultiplier(world, x, y, z));
        RenderUtil.scaleColor(activeRimColor, baseColor, .8f);

        float unit = .0625f;

        boxRenderer.renderBox(renderer, block, x, y, z, 0, 14 * unit, 0, 1, 1, 1, 0, ModularBoxRenderer.CUT_YNEG | ModularBoxRenderer.CUT_YPOS);
        boxRenderer.renderBox(renderer, block, x, y, z, 1 * unit, 8 * unit, 1 * unit, 15 * unit, 16 * unit, 15 * unit, 0, ModularBoxRenderer.CUT_YPOS);

        //renderer.setRenderBounds(0, 1f - .125f, 0, 1, 1, 1);
        //renderer.renderStandardBlock(block, x, y, z);

        //renderer.setRenderBounds(.0625f, .5f, .0625f, 1f - .0625f, 1f - .125f, 1f - .0625f);
        //renderer.renderStandardBlock(block, x, y, z);

        renderer.setRenderBounds(.1875f, .375f, .1875f, 1f - .1875f, .5f, 1f - .1875f);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.setRenderBounds(.3125f, .1875f, .3125f, 1f - .3125f, .375f, 1f - .3125f);
        renderer.renderStandardBlock(block, x, y, z);

        renderer.setRenderBounds(.125f, 0, .125f, 1f - .125f, .1875f, 1f - .125f);
        renderer.renderStandardBlock(block, x, y, z);

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

    private void renderRim (RenderBlocks renderer, Block block, int x, int y, int z, IIcon icon, float unit, float startX, float stopX, float startZ, float stopZ, float yLevel) {
        RenderUtil.setTessellatorColor(Tessellator.instance, activeRimColor);

        renderer.setRenderBounds(startX, 0, startZ, startX + unit, yLevel, stopZ);
        renderer.renderFaceYPos(block, x, y, z, icon);

        renderer.setRenderBounds(stopX - unit, 0, startZ, stopX, yLevel, stopZ);
        renderer.renderFaceYPos(block, x, y, z, icon);

        renderer.setRenderBounds(startX, 0, startZ, stopX, yLevel, startZ + unit);
        renderer.renderFaceYPos(block, x, y, z, icon);

        renderer.setRenderBounds(startX, 0, stopZ - unit, stopX, yLevel, stopZ);
        renderer.renderFaceYPos(block, x, y, z, icon);
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return 0;
    }
}
