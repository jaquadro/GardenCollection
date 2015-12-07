package com.jaquadro.minecraft.gardenstuff.renderer;

import com.jaquadro.minecraft.gardencore.client.renderer.support.ModularBoxRenderer;
import com.jaquadro.minecraft.gardenstuff.block.BlockHoop;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class HoopRenderer implements ISimpleBlockRenderingHandler
{
    private ModularBoxRenderer boxrender = new ModularBoxRenderer();

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockHoop))
            return;

        renderInventoryBlock((BlockHoop) block, metadata, modelId, renderer);
    }

    private void renderInventoryBlock (BlockHoop block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glRotatef(90, 0, 1, 0);
        GL11.glTranslatef(-.5f, -.5f, -.5f);

        boxrender.setUnit(0.0625f);
        boxrender.setIcon(block.getIcon(0, 0));
        boxrender.setColor(ModularBoxRenderer.COLOR_WHITE);

        boxrender.renderBox(null, block, 0, 0, 0, 0, .0625f, 0, 1, .375f, 1, 0, ModularBoxRenderer.CUT_YNEG | ModularBoxRenderer.CUT_YPOS);

        GL11.glTranslatef(.5f, .5f, .5f);
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockHoop))
            return false;

        return renderWorldBlock(world, x, y, z, (BlockHoop) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockHoop block, int modelId, RenderBlocks renderer) {
        boxrender.setUnit(0.0625f);
        boxrender.setIcon(block.getIcon(0, 0));
        boxrender.setColor(ModularBoxRenderer.COLOR_WHITE);

        boxrender.renderBox(world, block, x, y, z, 0, .0625f, 0, 1, .375f, 1, 0, ModularBoxRenderer.CUT_YNEG | ModularBoxRenderer.CUT_YPOS);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return true;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.hoopRenderID;
    }
}
