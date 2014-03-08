package com.jaquadro.minecraft.modularpots.client.renderer;

import com.jaquadro.minecraft.modularpots.block.FlowerLeaves;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class FlowerLeafRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof FlowerLeaves))
            return false;

        return renderWorldBlock(world, x, y, z, (FlowerLeaves) block, modelId, renderer);
    }

    private boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, FlowerLeaves block, int modelId, RenderBlocks renderer) {
        renderer.renderStandardBlock(block, x, y, z);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(1f, 1f, 1f);

        int meta = world.getBlockMetadata(x, y, z);
        float offset = .03125f;
        IIcon icon = block.getFlowerIcon(world, x, y, z, meta, 1);
        if (icon != null)
            renderer.renderFaceYPos(block, x, y + offset, z, icon);

        icon = block.getFlowerIcon(world, x, y, z, meta, 2);
        if (icon != null)
            renderer.renderFaceZNeg(block, x, y, z - offset, icon);

        icon = block.getFlowerIcon(world, x, y, z, meta, 3);
        if (icon != null)
            renderer.renderFaceZPos(block, x, y, z + offset, icon);

        icon = block.getFlowerIcon(world, x, y, z, meta, 4);
        if (icon != null)
            renderer.renderFaceXNeg(block, x - offset, y, z, icon);

        icon = block.getFlowerIcon(world, x, y, z, meta, 5);
        if (icon != null)
            renderer.renderFaceXPos(block, x + offset, y, z, icon);

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return true;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.flowerLeafRenderID;
    }
}
