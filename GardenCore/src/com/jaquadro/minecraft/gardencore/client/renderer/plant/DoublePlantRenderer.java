package com.jaquadro.minecraft.gardencore.client.renderer.plant;

import com.jaquadro.minecraft.gardencore.api.IPlantMetaResolver;
import com.jaquadro.minecraft.gardencore.api.IPlantRenderer;
import com.jaquadro.minecraft.gardencore.api.PlantRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class DoublePlantRenderer implements IPlantRenderer
{
    @Override
    public void render (IBlockAccess world, int x, int y, int z, RenderBlocks renderer, Block block, int meta, int height) {
        if (!(block instanceof BlockDoublePlant))
            return;

        BlockDoublePlant doublePlant = (BlockDoublePlant)block;

        IPlantMetaResolver resolver = PlantRegistry.instance().getPlantMetaResolver(block, meta);
        if (resolver != null)
            meta = resolver.getPlantSectionMeta(block, meta, height);

        boolean isTopHalf = BlockDoublePlant.func_149887_c(meta);
        int baseMeta = BlockDoublePlant.func_149890_d(meta);

        IIcon iicon = doublePlant.func_149888_a(isTopHalf, baseMeta);
        renderer.drawCrossedSquares(iicon, x, y, z, 1.0F);
    }
}
