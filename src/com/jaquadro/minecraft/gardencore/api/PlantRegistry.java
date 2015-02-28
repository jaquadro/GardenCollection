package com.jaquadro.minecraft.gardencore.api;

import com.jaquadro.minecraft.gardencore.api.plant.*;
import com.jaquadro.minecraft.gardencore.client.renderer.plant.*;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.integration.VanillaMetaResolver;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaRegistry;
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
    private static final DefaultPlantInfo defaultPlantInfo = new DefaultPlantInfo();

    public static final IPlantRenderer CROPS_RENDERER = new CropsPlantRenderer();
    public static final IPlantRenderer CROSSED_SQUARES_RENDERER = new CrossedSquaresPlantRenderer();
    public static final IPlantRenderer DOUBLE_PLANT_RENDERER = new DoublePlantRenderer();
    public static final IPlantRenderer GROUND_COVER_RENDERER = new GroundCoverPlantRenderer();

    private UniqueMetaRegistry<IPlantRenderer> renderRegistry;
    private Map<Integer, IPlantRenderer> renderIdRegistry;
    private UniqueMetaRegistry<IPlantMetaResolver> metaResolverRegistry;
    private UniqueMetaRegistry<IPlantInfo> plantInfoRegistry;

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
        plantInfoRegistry = new UniqueMetaRegistry<IPlantInfo>();

        registerPlantRenderer(1, new CrossedSquaresPlantRenderer());
        registerPlantRenderer(40, new DoublePlantRenderer());
        registerPlantRenderer(Blocks.double_plant, 0, new SunflowerRenderer());

        VanillaMetaResolver vanillaResolver = new VanillaMetaResolver();
        registerPlantMetaResolver(Blocks.red_flower, vanillaResolver);
        registerPlantMetaResolver(Blocks.yellow_flower, vanillaResolver);
        registerPlantMetaResolver(Blocks.red_mushroom, vanillaResolver);
        registerPlantMetaResolver(Blocks.brown_mushroom, vanillaResolver);
        registerPlantMetaResolver(Blocks.tallgrass, vanillaResolver);
        registerPlantMetaResolver(Blocks.double_plant, vanillaResolver);

        for (int i : new int[] { 0, 2, 3, 4, 5, 6, 7, 8 })
            registerPlantInfo(Blocks.red_flower, i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
        for (int i : new int[] { 0 })
            registerPlantInfo(Blocks.yellow_flower, i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
        for (int i : new int[] { 0, 1, 3, 4, 5 })
            registerPlantInfo(Blocks.double_plant, i, new SimplePlantInfo(PlantType.GROUND, PlantSize.MEDIUM, 2, 2, new int[] { i, i | 8 }));
        for (int i : new int[] { 2 })
            registerPlantInfo(Blocks.double_plant, i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE, 2, 2, new int[] { i, i | 8 }));
        for (int i : new int[] { 2 })
            registerPlantInfo(Blocks.tallgrass, i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
        for (int i : new int[] { 1 })
            registerPlantInfo(Blocks.tallgrass, i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));

        registerPlantInfo(Blocks.red_mushroom, 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
        registerPlantInfo(Blocks.brown_mushroom, 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));
        registerPlantInfo(Blocks.waterlily, new SimplePlantInfo(PlantType.GROUND_COVER, PlantSize.FULL));

        registerPlantInfo(Blocks.wheat, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));
        registerPlantInfo(Blocks.carrots, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));
        registerPlantInfo(Blocks.potatoes, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));
        registerPlantInfo(Blocks.nether_wart, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));
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

    public void registerPlantRenderer (String modId, String block, IPlantRenderer renderer) {
        UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, block, OreDictionary.WILDCARD_VALUE);
        renderRegistry.register(id, renderer);
    }

    public void registerPlantRenderer (String modId, String block, int meta, IPlantRenderer renderer) {
        UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, block, meta);
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

    public void registerPlantMetaResolver (String modId, String block, IPlantMetaResolver resolver) {
        UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, block, OreDictionary.WILDCARD_VALUE);
        metaResolverRegistry.register(id, resolver);
    }

    public void registerPlantMetaResolver (String modId, String block, int meta, IPlantMetaResolver resolver) {
        UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, block, meta);
        metaResolverRegistry.register(id, resolver);
    }

    public IPlantMetaResolver getPlantMetaResolver (Block block, int meta) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (id == null)
            return null;

        return metaResolverRegistry.getEntry(id);
    }

    public void registerPlantInfo (Block block, IPlantInfo info) {
        registerPlantInfo(block, OreDictionary.WILDCARD_VALUE, info);
    }

    public void registerPlantInfo (Block block, int meta, IPlantInfo info) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (id != null) {
            plantInfoRegistry.register(id, info);
            metaResolverRegistry.register(id, info);
        }
    }

    public void registerPlantInfo (String modId, IPlantInfo info) {
        UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId);
        plantInfoRegistry.register(id, info);
        metaResolverRegistry.register(id, info);
    }

    public void registerPlantInfo (String modId, String block, IPlantInfo info) {
        UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, block);
        plantInfoRegistry.register(id, info);
        metaResolverRegistry.register(id, info);
    }

    public void registerPlantInfo (String modId, String block, int meta, IPlantInfo info) {
        UniqueMetaIdentifier id = new UniqueMetaIdentifier(modId, block, meta);
        plantInfoRegistry.register(id, info);
        metaResolverRegistry.register(id, info);
    }

    public IPlantInfo getPlantInfo (Block block, int meta) {
        UniqueMetaIdentifier id = ModBlocks.getUniqueMetaID(block, meta);
        if (id == null)
            return null;

        return plantInfoRegistry.getEntry(id);
    }

    public IPlantInfo getPlantInfo (IBlockAccess world, IPlantable plant) {
        Block block = plant.getPlant(world, 0, 0, 0);
        int meta = plant.getPlantMetadata(world, 0, 0, 0);

        return  getPlantInfo(block, meta);
    }

    public IPlantInfo getPlantInfoOrDefault (Block block, int meta) {
        IPlantInfo info = getPlantInfo(block, meta);
        if (info != null)
            return info;

        return defaultPlantInfo;
    }

    public boolean isValidPlantBlock (Block block) {
        if (block == null)
            return false;
        if (block instanceof IPlantable)
            return true;

        Item item = Item.getItemFromBlock(block);
        if (item instanceof IPlantable)
            return true;

        return false;
    }

    // API Stuff

    public static IPlantable getPlantable (ItemStack plantItemStack) {
        if (plantItemStack == null || plantItemStack.getItem() == null)
            return null;

        IPlantable plantable = null;
        Item item = plantItemStack.getItem();
        if (item instanceof IPlantable)
            plantable = (IPlantable) item;
        else if (item instanceof ItemBlock) {
            Block itemBlock = Block.getBlockFromItem(item);
            if (itemBlock instanceof IPlantable)
                plantable = (IPlantable) itemBlock;
        }

        return plantable;
    }


}
