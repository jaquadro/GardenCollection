package com.jaquadro.minecraft.gardencore.api;

import com.jaquadro.minecraft.gardencore.client.renderer.CrossedSquaresPlantRenderer;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
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

        registerPlantRenderer(1, new CrossedSquaresPlantRenderer());
        registerPlantRenderer(40, new CrossedSquaresPlantRenderer());
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
}
