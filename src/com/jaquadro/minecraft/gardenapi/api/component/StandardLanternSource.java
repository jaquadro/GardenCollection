package com.jaquadro.minecraft.gardenapi.api.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import java.util.Random;

public abstract class StandardLanternSource implements ILanternSource
{
    public static class LanternSourceInfo
    {
        public String sourceID;
        public Item item;
        public int lightLevel;

        public LanternSourceInfo (String sourceID, Item item, int lightLevel) {
            this.sourceID = sourceID;
            this.item = item;
            this.lightLevel = lightLevel;
        }
    }

    private LanternSourceInfo info;

    public StandardLanternSource (LanternSourceInfo info) {
        this.info = info;
    }

    @Override
    public String getSourceID () {
        return info.sourceID;
    }

    @Override
    public int getSourceMeta (ItemStack item) {
        return (item != null) ? item.getItemDamage() : 0;
    }

    @Override
    public boolean isValidSourceItem (ItemStack item) {
        return item != null && item.getItem() == info.item;
    }

    @Override
    public ItemStack getRemovedItem (int meta) {
        return new ItemStack(info.item, 1, meta);
    }

    @Override
    public int getLightLevel (int meta) {
        return info.lightLevel;
    }

    @Override
    public String getLanguageKey (int meta) {
        return "gardenstuff.lanternSource." + info.sourceID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderParticle (World world, int x, int y, int z, Random rand, int meta) { }

    @Override
    @SideOnly(Side.CLIENT)
    public void render (RenderBlocks renderer, int x, int y, int z, int meta, int pass) { }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderItem (RenderBlocks renderer, IItemRenderer.ItemRenderType renderType, int meta) { }

    @Override
    public boolean renderInPass (int pass) {
        return pass == 0;
    }
}
