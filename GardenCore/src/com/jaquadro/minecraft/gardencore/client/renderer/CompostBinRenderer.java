package com.jaquadro.minecraft.gardencore.client.renderer;

import com.jaquadro.minecraft.gardencore.block.BlockCompostBin;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityCompostBin;
import com.jaquadro.minecraft.gardencore.client.renderer.support.ModularBoxRenderer;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

public class CompostBinRenderer implements ISimpleBlockRenderingHandler
{
    private ModularBoxRenderer boxRenderer = new ModularBoxRenderer();

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockCompostBin))
            return false;

        return renderWorldBlock(world, x, y, z, (BlockCompostBin) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, BlockCompostBin block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));

        boxRenderer.setColor(ModularBoxRenderer.COLOR_WHITE);
        boxRenderer.setCutIcon(block.getInnerIcon());
        for (int side = 0; side < 6; side++) {
            boxRenderer.setExteriorIcon(block.getIcon(world, x, y, z, side), side);
            boxRenderer.setInteriorIcon(block.getIcon(world, x, y, z, side), side);
        }

        boxRenderer.renderBox(renderer, block, x, y, z, 0, 0, 0, 1, 1, 1, 0, ModularBoxRenderer.CUT_YPOS);

        TileEntityCompostBin te = (TileEntityCompostBin) world.getTileEntity(x, y, z);
        if (te != null) {
            if (te.hasInputItems()) {
                boxRenderer.setExteriorIcon(Blocks.dirt.getIcon(1, 2));
                boxRenderer.renderSolidBox(renderer, block, x, y, z, .0625, .0625, .0625, 1 - .0625, 1 - .0625, 1 - .0625);
            }
            else if (te.hasOutputItems()) {
                boxRenderer.setExteriorIcon(ModBlocks.gardenSoil.getIcon(1, 0));
                boxRenderer.renderSolidBox(renderer, block, x, y, z, .0625, .0625, .0625, 1 - .0625, 1 - .0625, 1 - .0625);
            }
        }

        //renderer.setRenderBoundsFromBlock(block);
        //RenderUtil.renderBlock(renderer, block, x, y, z);

        return true;
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
