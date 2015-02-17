package com.jaquadro.minecraft.gardentrees.client.renderer;

import com.jaquadro.minecraft.gardentrees.core.ClientProxy;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class IvyRenderer implements ISimpleBlockRenderingHandler
{
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        int color = block.colorMultiplier(world, x, y, z);
        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tessellator.setColorOpaque_F(r, g, b);

        double d = .05;
        double[] uv = new double[4];

        int meta = world.getBlockMetadata(x, y, z);

        if ((meta & 2) != 0) {
            IIcon icon = block.getIcon(world, x, y, z, 5);
            getIconUV(icon, uv);

            tessellator.addVertexWithUV(x + d, y + 1, z + 1, uv[0], uv[1]);
            tessellator.addVertexWithUV(x + d, y + 0, z + 1, uv[0], uv[3]);
            tessellator.addVertexWithUV(x + d, y + 0, z + 0, uv[2], uv[3]);
            tessellator.addVertexWithUV(x + d, y + 1, z + 0, uv[2], uv[1]);
            tessellator.addVertexWithUV(x + d, y + 1, z + 0, uv[2], uv[1]);
            tessellator.addVertexWithUV(x + d, y + 0, z + 0, uv[2], uv[3]);
            tessellator.addVertexWithUV(x + d, y + 0, z + 1, uv[0], uv[3]);
            tessellator.addVertexWithUV(x + d, y + 1, z + 1, uv[0], uv[1]);
        }

        if ((meta & 8) != 0) {
            IIcon icon = block.getIcon(world, x, y, z, 4);
            getIconUV(icon, uv);

            tessellator.addVertexWithUV(x + 1 - d, y + 0, z + 1, uv[2], uv[3]);
            tessellator.addVertexWithUV(x + 1 - d, y + 1, z + 1, uv[2], uv[1]);
            tessellator.addVertexWithUV(x + 1 - d, y + 1, z + 0, uv[0], uv[1]);
            tessellator.addVertexWithUV(x + 1 - d, y + 0, z + 0, uv[0], uv[3]);
            tessellator.addVertexWithUV(x + 1 - d, y + 0, z + 0, uv[0], uv[3]);
            tessellator.addVertexWithUV(x + 1 - d, y + 1, z + 0, uv[0], uv[1]);
            tessellator.addVertexWithUV(x + 1 - d, y + 1, z + 1, uv[2], uv[1]);
            tessellator.addVertexWithUV(x + 1 - d, y + 0, z + 1, uv[2], uv[3]);
        }

        if ((meta & 4) != 0) {
            IIcon icon = block.getIcon(world, x, y, z, 3);
            getIconUV(icon, uv);

            tessellator.addVertexWithUV(x + 1, y + 0, z + d, uv[2], uv[3]);
            tessellator.addVertexWithUV(x + 1, y + 1, z + d, uv[2], uv[1]);
            tessellator.addVertexWithUV(x + 0, y + 1, z + d, uv[0], uv[1]);
            tessellator.addVertexWithUV(x + 0, y + 0, z + d, uv[0], uv[3]);
            tessellator.addVertexWithUV(x + 0, y + 0, z + d, uv[0], uv[3]);
            tessellator.addVertexWithUV(x + 0, y + 1, z + d, uv[0], uv[1]);
            tessellator.addVertexWithUV(x + 1, y + 1, z + d, uv[2], uv[1]);
            tessellator.addVertexWithUV(x + 1, y + 0, z + d, uv[2], uv[3]);
        }

        if ((meta & 1) != 0) {
            IIcon icon = block.getIcon(world, x, y, z, 2);
            getIconUV(icon, uv);

            tessellator.addVertexWithUV(x + 1, y + 1, z + 1 - d, uv[0], uv[1]);
            tessellator.addVertexWithUV(x + 1, y + 0, z + 1 - d, uv[0], uv[3]);
            tessellator.addVertexWithUV(x + 0, y + 0, z + 1 - d, uv[2], uv[3]);
            tessellator.addVertexWithUV(x + 0, y + 1, z + 1 - d, uv[2], uv[1]);
            tessellator.addVertexWithUV(x + 0, y + 1, z + 1 - d, uv[2], uv[1]);
            tessellator.addVertexWithUV(x + 0, y + 0, z + 1 - d, uv[2], uv[3]);
            tessellator.addVertexWithUV(x + 1, y + 0, z + 1 - d, uv[0], uv[3]);
            tessellator.addVertexWithUV(x + 1, y + 1, z + 1 - d, uv[0], uv[1]);
        }

        return true;
    }

    private void getIconUV (IIcon icon, double[] uv) {
        uv[0] = icon.getMinU();
        uv[1] = icon.getMinV();
        uv[2] = icon.getMaxU();
        uv[3] = icon.getMaxV();
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.ivyRenderID;
    }
}
