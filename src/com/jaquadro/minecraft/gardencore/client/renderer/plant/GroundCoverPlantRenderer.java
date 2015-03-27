package com.jaquadro.minecraft.gardencore.client.renderer.plant;

import com.jaquadro.minecraft.gardencore.api.IPlantMetaResolver;
import com.jaquadro.minecraft.gardencore.api.IPlantRenderer;
import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class GroundCoverPlantRenderer implements IPlantRenderer
{
    @Override
    public void render (IBlockAccess world, int x, int y, int z, RenderBlocks renderer, Block block, int meta, int height, AxisAlignedBB[] bounds) {
        IPlantMetaResolver resolver = PlantRegistry.instance().getPlantMetaResolver(block, meta);
        if (resolver != null)
            meta = resolver.getPlantSectionMeta(block, meta, height);

        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 0, meta);

        renderer.setRenderBounds(0, 0, 0, 1, .03125, 1);
        renderer.renderFaceYPos(block, x, y, z, icon);

        /*float f = 0.1F;
        double d0 = (double)icon.getMinU();
        double d1 = (double)icon.getMinV();
        double d2 = (double)icon.getMaxU();
        double d3 = (double)icon.getMaxV();

        float f1 = (float)x + 0.5F;
        float f2 = (float)z + 0.5F;
        float f3 = (float)(i1 & 1) * 0.5F * (float)(1 - i1 / 2 % 2 * 2);
        float f4 = (float)(i1 + 1 & 1) * 0.5F * (float)(1 - (i1 + 1) / 2 % 2 * 2);*/
    }
}
