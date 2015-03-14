package com.jaquadro.minecraft.gardenstuff.renderer;

import com.jaquadro.minecraft.gardencore.client.renderer.support.ModularBoxRenderer;
import com.jaquadro.minecraft.gardenstuff.block.BlockFence;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class FenceRenderer implements ISimpleBlockRenderingHandler
{
    private static final float UN4 = .0625f * -4;
    private static final float U1 = .0625f * 1 - .001f;
    private static final float U7 = .0625f * 7 + .001f;
    private static final float U8 = .0625f * 8;
    private static final float U9 = .0625f * 9 - .001f;
    private static final float U15 = .0625f * 15 + .001f;
    private static final float U20 = .0625f * 20;

    private ModularBoxRenderer boxRenderer = new ModularBoxRenderer();

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockFence))
            return false;

        return renderWorldBlock(world, x, y, z, (BlockFence) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockFence block, int modelId, RenderBlocks renderer) {
        int meta = world.getBlockMetadata(x, y, z);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        boxRenderer.setUnit(0);
        boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);
        boxRenderer.setIcon(block.getIcon(meta));
        boxRenderer.flipOpposite = false;

        int connectFlags = block.calcConnectionFlags(world, x, y, z);

        boolean connectYNeg = (connectFlags & 1) != 0;
        boolean connectYPos = (connectFlags & 2) != 0;
        boolean connectZNeg = (connectFlags & 4) != 0;
        boolean connectZPos = (connectFlags & 8) != 0;
        boolean connectXNeg = (connectFlags & 16) != 0;
        boolean connectXPos = (connectFlags & 32) != 0;

        boxRenderer.renderSolidBox(renderer, block, x, y, z, U7, 0, U7, U9, 1, U9);

        if (block.getPostInterval(meta) == 8) {
            if (connectZNeg)
                boxRenderer.renderSolidBox(renderer, block, x, y, z, U7, 0, 0, U9, 1, U1);
            if (connectZPos)
                boxRenderer.renderSolidBox(renderer, block, x, y, z, U7, 0, U15, U9, 1, 1);
            if (connectXNeg)
                boxRenderer.renderSolidBox(renderer, block, x, y, z, 0, 0, U7, U1, 1, U9);
            if (connectXPos)
                boxRenderer.renderSolidBox(renderer, block, x, y, z, U15, 0, U7, 1, 1, U9);
        }

        boxRenderer.flipOpposite = true;

        boolean fenceBelow = block.isFenceBelow(world, x, y, z);
        boolean abNN = connectYNeg && !fenceBelow && !connectYPos;
        boolean abYY = connectYNeg && fenceBelow && connectYPos;

        if (abNN)
            boxRenderer.setIcon(block.getIconTB(meta));

        float yN = 0;
        float yP = 1;

        if (!(abNN || abYY)) {
            if (connectYPos)
                yN = U8;
            else if (connectYNeg)
                yP = U8;
        }

        if (connectZNeg)
            boxRenderer.renderSolidBox(renderer, block, x, y, z, U8, yN, 0, U8, yP, U8);
        if (connectZPos)
            boxRenderer.renderSolidBox(renderer, block, x, y, z, U8, yN, U8, U8, yP, 1);
        if (connectXNeg)
            boxRenderer.renderSolidBox(renderer, block, x, y, z, 0, yN, U8, U8, yP, U8);
        if (connectXPos)
            boxRenderer.renderSolidBox(renderer, block, x, y, z, U8, yN, U8, 1, yP, U8);

        if (!(abNN || abYY)) {
            if (connectYPos) {
                yN = 0;
                yP = U8;
            }
            else if (connectYNeg) {
                yN = U8;
                yP = 1;
            }

            boxRenderer.setIcon(block.getIconTB(meta));

            if (connectZNeg)
                boxRenderer.renderSolidBox(renderer, block, x, y, z, U8, yN, 0, U8, yP, U8);
            if (connectZPos)
                boxRenderer.renderSolidBox(renderer, block, x, y, z, U8, yN, U8, U8, yP, 1);
            if (connectXNeg)
                boxRenderer.renderSolidBox(renderer, block, x, y, z, 0, yN, U8, U8, yP, U8);
            if (connectXPos)
                boxRenderer.renderSolidBox(renderer, block, x, y, z, U8, yN, U8, 1, yP, U8);
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.fenceRenderID;
    }
}
