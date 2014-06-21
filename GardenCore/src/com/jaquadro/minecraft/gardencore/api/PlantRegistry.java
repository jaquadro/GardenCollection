package com.jaquadro.minecraft.gardencore.api;

import com.jaquadro.minecraft.gardencore.client.renderer.CrossedSquaresPlantRenderer;
import com.jaquadro.minecraft.gardencore.client.renderer.DoublePlantRenderer;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.integration.VanillaMetaResolver;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public final class PlantRegistry
{
    private UniqueMetaRegistry<IPlantRenderer> renderRegistry;
    private Map<Integer, IPlantRenderer> renderIdRegistry;
    private UniqueMetaRegistry<IPlantMetaResolver> metaResolverRegistry;

    private static PlantRegistry instance;
    static {
        instance = new PlantRegistry();
    }

    public static PlantRegistry instance () {
        return instance;
    }

    public boolean isPlantBlacklisted (ItemStack plant) {
        return false;
    }

    public boolean plantRespondsToBonemeal (ItemStack plant) {
        return false;
    }

    private PlantRegistry () {
        renderRegistry = new UniqueMetaRegistry<IPlantRenderer>();
        renderIdRegistry = new HashMap<Integer, IPlantRenderer>();
        metaResolverRegistry = new UniqueMetaRegistry<IPlantMetaResolver>();

        registerPlantRenderer(1, new CrossedSquaresPlantRenderer());
        registerPlantRenderer(40, new DoublePlantRenderer());

        VanillaMetaResolver vanillaResolver = new VanillaMetaResolver();
        registerPlantMetaResolver(Blocks.red_flower, vanillaResolver);
        registerPlantMetaResolver(Blocks.yellow_flower, vanillaResolver);
        registerPlantMetaResolver(Blocks.red_mushroom, vanillaResolver);
        registerPlantMetaResolver(Blocks.brown_mushroom, vanillaResolver);
        registerPlantMetaResolver(Blocks.tallgrass, vanillaResolver);
        registerPlantMetaResolver(Blocks.double_plant, vanillaResolver);
    }

    public void registerPlantRenderer (int renderId, IPlantRenderer renderer) {
        if (renderIdRegistry.containsKey(renderId))
            return;

        renderIdRegistry.put(renderId, renderer);
    }

    public void registerPlantRenderer (Block block, IPlantRenderer renderer) {
        registerPlantRenderer(block, OreDictionary.WILDCARD_VALUE, renderer);
    }

    public void registerPlantRenderer (Block block, int meta, IPlantRenderer renderer) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (id != null)
            renderRegistry.register(id, renderer);
    }

    public IPlantRenderer getPlantRenderer (Block block, int meta) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (id == null)
            return null;

        IPlantRenderer renderer = renderRegistry.getEntry(id);
        if (renderer != null)
            return renderer;

        return renderIdRegistry.get(block.getRenderType());
    }

    public void registerPlantMetaResolver (Block block, IPlantMetaResolver resolver) {
        registerPlantMetaResolver(block, OreDictionary.WILDCARD_VALUE, resolver);
    }

    public void registerPlantMetaResolver (Block block, int meta, IPlantMetaResolver resolver) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (id != null)
            metaResolverRegistry.register(id, resolver);
    }

    public IPlantMetaResolver getPlantMetaResolver (Block block, int meta) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (id == null)
            return null;

        return metaResolverRegistry.getEntry(id);
    }
}
