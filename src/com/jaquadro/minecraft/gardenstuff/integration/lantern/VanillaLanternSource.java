package com.jaquadro.minecraft.gardenstuff.integration.lantern;

import com.jaquadro.minecraft.gardenapi.api.component.IRedstoneSource;
import com.jaquadro.minecraft.gardenapi.api.component.StandardLanternSource;
import com.jaquadro.minecraft.gardencore.util.RenderHelper;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLantern;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import java.util.Random;

public class VanillaLanternSource
{
    public static class TorchLanternSource extends StandardLanternSource
    {
        public TorchLanternSource () {
            super(new LanternSourceInfo("torch", Item.getItemFromBlock(Blocks.torch), Blocks.torch.getLightValue()));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void render (RenderBlocks renderer, int x, int y, int z, int meta, int pass) {
            renderer.renderBlockAllFaces(Blocks.torch, x, y, z);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void renderItem (RenderBlocks renderer, IItemRenderer.ItemRenderType renderType, int meta) {
            RenderHelper renderHelper = RenderHelper.instance;

            renderer.setRenderBounds(0, 0, 0.4375, 1, 1, 0.5625);
            renderHelper.renderFace(RenderHelper.ZNEG, renderer, Blocks.torch, Blocks.torch.getIcon(2, 0), meta);
            renderHelper.renderFace(RenderHelper.ZPOS, renderer, Blocks.torch, Blocks.torch.getIcon(3, 0), meta);
            renderer.setRenderBounds(0.4375, 0, 0, 0.5625, 1, 1);
            renderHelper.renderFace(RenderHelper.XNEG, renderer, Blocks.torch, Blocks.torch.getIcon(4, 0), meta);
            renderHelper.renderFace(RenderHelper.XPOS, renderer, Blocks.torch, Blocks.torch.getIcon(5, 0), meta);
            renderer.setRenderBounds(0.4375, 0, 0.4375, 0.5625, 0.625, 0.5625);
            renderHelper.renderFace(RenderHelper.YPOS, renderer, Blocks.torch, Blocks.torch.getIcon(1, 0), meta);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void renderParticle (World world, int x, int y, int z, Random rand, int meta) {
            double px = x + 0.5F;
            double py = y + 0.7F;
            double pz = z + 0.5F;

            TileEntityLantern tile = (TileEntityLantern)world.getTileEntity(x, y, z);
            if (tile == null || !tile.hasGlass())
                world.spawnParticle("smoke", px, py, pz, 0.0D, 0.0D, 0.0D);

            world.spawnParticle("flame", px, py, pz, 0.0D, 0.0D, 0.0D);
        }
    }

    public static class RedstoneTorchSource extends StandardLanternSource implements IRedstoneSource
    {
        public RedstoneTorchSource () {
            super(new LanternSourceInfo("redstoneTorch", Item.getItemFromBlock(Blocks.redstone_torch), Blocks.redstone_torch.getLightValue()));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void render (RenderBlocks renderer, int x, int y, int z, int meta, int pass) {
            renderer.renderBlockAllFaces(Blocks.redstone_torch, x, y, z);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void renderItem (RenderBlocks renderer, IItemRenderer.ItemRenderType renderType, int meta) {
            RenderHelper renderHelper = RenderHelper.instance;

            renderer.setRenderBounds(0, 0, 0.4375, 1, 1, 0.5625);
            renderHelper.renderFace(RenderHelper.ZNEG, renderer, Blocks.redstone_torch, Blocks.redstone_torch.getIcon(2, 0), meta);
            renderHelper.renderFace(RenderHelper.ZPOS, renderer, Blocks.redstone_torch, Blocks.redstone_torch.getIcon(3, 0), meta);
            renderer.setRenderBounds(0.4375, 0, 0, 0.5625, 1, 1);
            renderHelper.renderFace(RenderHelper.XNEG, renderer, Blocks.redstone_torch, Blocks.redstone_torch.getIcon(4, 0), meta);
            renderHelper.renderFace(RenderHelper.XPOS, renderer, Blocks.redstone_torch, Blocks.redstone_torch.getIcon(5, 0), meta);
            renderer.setRenderBounds(0.4375, 0, 0.4375, 0.5625, 0.625, 0.5625);
            renderHelper.renderFace(RenderHelper.YPOS, renderer, Blocks.redstone_torch, Blocks.redstone_torch.getIcon(1, 0), meta);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void renderParticle (World world, int x, int y, int z, Random rand, int meta) {
            double px = x + .5f + (rand.nextFloat() - 0.5F) * 0.2D;
            double py = y + .6f + (rand.nextFloat() - 0.5F) * 0.2D + 0.1F;
            double pz = z + .5f + (rand.nextFloat() - 0.5F) * 0.2D;

            world.spawnParticle("reddust", px, py, pz, 0.0D, 0.0D, 0.0D);
        }

        @Override
        public int strongPowerValue (int meta) {
            return 15;
        }

        @Override
        public int weakPowerValue (int meta) {
            return 15;
        }
    }

    public static class GlowstoneSource extends StandardLanternSource
    {
        public GlowstoneSource () {
            super(new LanternSourceInfo("glowstone", Items.glowstone_dust, Blocks.glowstone.getLightValue()));
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void render (RenderBlocks renderer, int x, int y, int z, int meta, int pass) {
            renderer.setRenderBounds(.3, 0, .3, .7, .4, .7);
            renderer.renderStandardBlock(Blocks.glowstone, x, y, z);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void renderItem (RenderBlocks renderer, IItemRenderer.ItemRenderType renderType, int meta) {
            renderer.setRenderBounds(.3, 0, .3, .7, .4, .7);
            RenderHelper.instance.renderBlock(renderer, Blocks.glowstone, 0);
        }
    }
}
