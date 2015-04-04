package com.jaquadro.minecraft.gardenapi.api.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import java.util.Random;

public interface ILanternSource
{
    String getSourceID ();

    int getSourceMeta (ItemStack item);

    boolean isValidSourceItem (ItemStack item);

    ItemStack getRemovedItem (int meta);

    int getLightLevel (int meta);

    String getLanguageKey (int meta);

    @SideOnly(Side.CLIENT)
    void renderParticle (World world, int x, int y, int z, Random rand, int meta);

    @SideOnly(Side.CLIENT)
    void render (RenderBlocks renderer, int x, int y, int z, int meta, int pass);

    @SideOnly(Side.CLIENT)
    void renderItem (RenderBlocks renderer, IItemRenderer.ItemRenderType renderType, int meta);

    @SideOnly(Side.CLIENT)
    boolean renderInPass (int pass);
}
