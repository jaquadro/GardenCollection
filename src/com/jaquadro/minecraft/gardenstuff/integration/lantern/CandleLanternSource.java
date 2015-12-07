package com.jaquadro.minecraft.gardenstuff.integration.lantern;

import com.jaquadro.minecraft.gardenapi.api.component.StandardLanternSource;
import com.jaquadro.minecraft.gardencore.client.renderer.support.ModularBoxRenderer;
import com.jaquadro.minecraft.gardencore.util.RenderHelper;
import com.jaquadro.minecraft.gardenstuff.block.BlockCandelabra;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import com.jaquadro.minecraft.gardenstuff.core.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import java.util.Random;

public class CandleLanternSource extends StandardLanternSource
{
    private ModularBoxRenderer boxrender = new ModularBoxRenderer();

    public CandleLanternSource () {
        super(new LanternSourceInfo("candle", ModItems.candle, 14));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render (RenderBlocks renderer, int x, int y, int z, int meta, int pass) {
        float unit = 0.0625f;
        BlockCandelabra block = ModBlocks.candelabra;

        RenderHelper.instance.state.setRenderOffset(0, -.375f, 0);
        RenderHelper.instance.state.setColorMult(1, .9f, .8f, .5f);

        boxrender.setUnit(0);
        boxrender.setColor(ModularBoxRenderer.COLOR_WHITE);
        boxrender.setIcon(block.getIconCandleTop(), 1);
        for (int i = 2; i < 6; i++)
            boxrender.setIcon(block.getIconCandleSide(), i);

        // Candle
        boxrender.renderExterior(renderer.blockAccess, block, x, y, z, unit * 6.5f, unit * 7, unit * 6.5f, unit * 9.5f, unit * 13, unit * 9.5f, 0, ModularBoxRenderer.CUT_YNEG);

        RenderHelper.instance.state.resetColorMult();

        RenderHelper.instance.setRenderBounds(0, 0, 0, 1, 1, 1);
        RenderHelper.instance.renderCrossedSquares(renderer.blockAccess, block, x, y, z, block.getIconCandleSide());

        RenderHelper.instance.state.clearRenderOffset();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderItem (RenderBlocks renderer, IItemRenderer.ItemRenderType renderType, int meta) {
        float unit = 0.0625f;
        BlockCandelabra block = ModBlocks.candelabra;

        RenderHelper.instance.state.setRenderOffset(0, -.375f, 0);
        RenderHelper.instance.state.setColorMult(1, .9f, .8f, .5f);

        boxrender.setUnit(0);
        boxrender.setColor(ModularBoxRenderer.COLOR_WHITE);
        boxrender.setIcon(block.getIconCandleTop(), 1);
        for (int i = 2; i < 6; i++)
            boxrender.setIcon(block.getIconCandleSide(), i);

        int x = 0;
        int y = 0;
        int z = 0;

        // Candle
        boxrender.renderExterior(null, block, x, y, z, unit * 6.5f, unit * 7, unit * 6.5f, unit * 9.5f, unit * 13, unit * 9.5f, 0, ModularBoxRenderer.CUT_YNEG);

        RenderHelper.instance.state.resetColorMult();

        RenderHelper.instance.setRenderBounds(0, 0, 0, 1, 1, 1);
        RenderHelper.instance.renderCrossedSquares(block, meta, block.getIconCandleSide());

        RenderHelper.instance.state.clearRenderOffset();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderParticle (World world, int x, int y, int z, Random rand, int meta) {
        double px = x + 0.5F;
        double py = y + 0.625F;
        double pz = z + 0.5F;

        world.spawnParticle("flame", px, py, pz, 0.0D, 0.0D, 0.0D);
    }
}
