package com.jaquadro.minecraft.gardencommon.integration.mods;

import com.jaquadro.minecraft.gardencommon.integration.IntegrationModule;
import com.jaquadro.minecraft.gardencommon.integration.SmallTreeRegistryHelper;
import com.jaquadro.minecraft.gardencore.api.*;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.api.plant.SimplePlantInfo;
import com.jaquadro.minecraft.gardencore.client.renderer.plant.CrossedSquaresPlantRenderer;
import com.jaquadro.minecraft.gardencore.util.UniqueMetaIdentifier;
import com.jaquadro.minecraft.gardentrees.GardenTrees;
import com.jaquadro.minecraft.gardentrees.world.gen.OrnamentalTreeFactory;
import com.jaquadro.minecraft.gardentrees.world.gen.OrnamentalTreeRegistry;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.HashMap;
import java.util.Map;

public class BiomesOPlenty extends IntegrationModule
{
    private static final String MOD_ID = "BiomesOPlenty";
    private static BOPMetaResolver metaResolver = new BOPMetaResolver();

    @Override
    public String getModID () {
        return MOD_ID;
    }

    @Override
    public void init () throws Throwable {
        initWood();

        PlantRegistry plantReg = PlantRegistry.instance();
        plantReg.registerPlantMetaResolver(MOD_ID, "foliage", metaResolver);

        // Default: spectralfern, thorns
        // deadgrass, desertgrass, desertsprouts, dunegrass
        for (int i : new int[] { 0, 1, 2, 3 })
            plantReg.registerPlantInfo(MOD_ID, "plants", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));

        plantReg.registerPlantInfo(MOD_ID, "plants", 6, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));  // Barley
        plantReg.registerPlantInfo(MOD_ID, "plants", 7, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL, 1, 2));  // Cattail Stage 1 (cattail)
        plantReg.registerPlantInfo(MOD_ID, "plants", 8, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));  // Rivercane
        plantReg.registerPlantInfo(MOD_ID, "plants", 10, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL, 2, 2, new int[] { 10, 9 }));  // Cattail Stage 2 (cattalbottom)
        plantReg.registerPlantInfo(MOD_ID, "plants", 11, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));  // Wild carrot
        plantReg.registerPlantInfo(MOD_ID, "plants", 12, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));  // Cactus
        plantReg.registerPlantInfo(MOD_ID, "plants", 13, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));  // Witherwart
        plantReg.registerPlantInfo(MOD_ID, "plants", 14, new SimplePlantInfo(PlantType.AQUATIC_EMERGENT, PlantSize.FULL, 2, 2));  // Reed
        plantReg.registerPlantInfo(MOD_ID, "plants", 15, new SimplePlantInfo(PlantType.HANGING, PlantSize.LARGE));  // Root

        plantReg.registerPlantRenderer(MOD_ID, "plants", PlantRegistry.CROSSED_SQUARES_RENDERER);
        for (int i : new int[] { 6, 7, 8, 9, 10, 11, 13, 14})
            plantReg.registerPlantRenderer(MOD_ID, "plants", i, PlantRegistry.CROPS_RENDERER);

        // Default: shortgrass, mediumgrass, bush, poisonivy, berrybush
        // sprout, shrub, wheatgrass, dampgrass, koru
        for (int i : new int[] { 5, 9, 10, 12 })
            plantReg.registerPlantInfo(MOD_ID, "foliage", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));

        plantReg.registerPlantInfo(MOD_ID, "foliage", 0, new SimplePlantInfo(PlantType.AQUATIC_COVER, PlantSize.FULL)); // duckweed
        plantReg.registerPlantInfo(MOD_ID, "foliage", 3, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE, 2, 2, new int[] { 3, 6 })); // hedgebottom
        plantReg.registerPlantInfo(MOD_ID, "foliage", 13, new SimplePlantInfo(PlantType.GROUND_COVER, PlantSize.FULL)); // cloverpatch
        plantReg.registerPlantInfo(MOD_ID, "foliage", 14, new SimplePlantInfo(PlantType.GROUND_COVER, PlantSize.FULL)); // leafpile
        plantReg.registerPlantInfo(MOD_ID, "foliage", 15, new SimplePlantInfo(PlantType.GROUND_COVER, PlantSize.FULL)); // deadleafpile

        plantReg.registerPlantRenderer(MOD_ID, "foliage", PlantRegistry.CROSSED_SQUARES_RENDERER);
        plantReg.registerPlantRenderer(MOD_ID, "foliage", 3, new BOPShrubRenderer());
        plantReg.registerPlantRenderer(MOD_ID, "foliage", 6, new BOPShrubRenderer());
        for (int i : new int[] { 0, 13, 14, 15 })
            plantReg.registerPlantRenderer(MOD_ID, "foliage", i, PlantRegistry.GROUND_COVER_RENDERER);

        // Default: hydrangia, wildflower, anemone
        // swampflower, violet, enderlotus, bromeliad
        for (int i : new int[] { 1, 11, 12 })
            plantReg.registerPlantInfo(MOD_ID, "flowers", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));

        plantReg.registerPlantInfo(MOD_ID, "flowers", 0, new SimplePlantInfo(PlantType.GROUND_COVER, PlantSize.FULL)); // cloverpatch
        plantReg.registerPlantInfo(MOD_ID, "flowers", 2, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL)); // deadbloom
        plantReg.registerPlantInfo(MOD_ID, "flowers", 3, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL)); // glowflower
        plantReg.registerPlantInfo(MOD_ID, "flowers", 5, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL)); // cosmos
        plantReg.registerPlantInfo(MOD_ID, "flowers", 6, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL)); // daffodil
        plantReg.registerPlantInfo(MOD_ID, "flowers", 10, new SimplePlantInfo(PlantType.AQUATIC_SURFACE, PlantSize.SMALL)); // lilyflower
        plantReg.registerPlantInfo(MOD_ID, "flowers", 13, new SimplePlantInfo(PlantType.GROUND, PlantSize.MEDIUM, 2, 2, new int[] { 13, 14 })); // eyebulbbottom
        plantReg.registerPlantInfo(MOD_ID, "flowers", 15, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL)); // dandelion

        plantReg.registerPlantRenderer(MOD_ID, "flowers", PlantRegistry.CROSSED_SQUARES_RENDERER);
        plantReg.registerPlantRenderer(MOD_ID, "flowers", 0, PlantRegistry.GROUND_COVER_RENDERER);
        plantReg.registerPlantRenderer(MOD_ID, "flowers", 10, PlantRegistry.GROUND_COVER_RENDERER);

        // Default: hibiscus, lavender, minersdelight, icyiris
        // lilyofthevalley, burningblossom, bluebells
        for (int i : new int[] { 1, 2, 5 })
            plantReg.registerPlantInfo(MOD_ID, "flowers2", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));
        // goldenrod, rose
        for (int i : new int[] { 4, 8 })
            plantReg.registerPlantInfo(MOD_ID, "flowers2", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));

        plantReg.registerPlantRenderer(MOD_ID, "flowers2", PlantRegistry.CROSSED_SQUARES_RENDERER);

        // TODO: Probably can't use SimplePlantInfo for these.  Kelp needs extra special treatment.
        plantReg.registerPlantInfo(MOD_ID, "coral1", 8, new SimplePlantInfo(PlantType.AQUATIC, PlantSize.LARGE, 2, 2, new int[] { 8, 10 })); // kelpbottom
        plantReg.registerPlantInfo(MOD_ID, "coral1", 11, new SimplePlantInfo(PlantType.AQUATIC, PlantSize.LARGE, 1, 2)); // kelpsingle
        plantReg.registerPlantInfo(MOD_ID, "coral1", 12, new SimplePlantInfo(PlantType.AQUATIC, PlantSize.LARGE)); // pinkcoral
        plantReg.registerPlantInfo(MOD_ID, "coral1", 13, new SimplePlantInfo(PlantType.AQUATIC, PlantSize.LARGE)); // orangecoral
        plantReg.registerPlantInfo(MOD_ID, "coral1", 14, new SimplePlantInfo(PlantType.AQUATIC, PlantSize.LARGE)); // bluecoral
        plantReg.registerPlantInfo(MOD_ID, "coral1", 15, new SimplePlantInfo(PlantType.AQUATIC, PlantSize.LARGE)); // glowcoral

        plantReg.registerPlantInfo(MOD_ID, "coral2", 8, new SimplePlantInfo(PlantType.AQUATIC, PlantSize.LARGE)); // algae

        plantReg.registerPlantRenderer(MOD_ID, "coral1", PlantRegistry.CROSSED_SQUARES_RENDERER);
        plantReg.registerPlantRenderer(MOD_ID, "coral2", PlantRegistry.CROSSED_SQUARES_RENDERER);

        plantReg.registerPlantInfo(MOD_ID, "ivy", new SimplePlantInfo(PlantType.HANGING_SIDE, PlantSize.FULL));
        plantReg.registerPlantInfo(MOD_ID, "flowervine", new SimplePlantInfo(PlantType.HANGING_SIDE, PlantSize.FULL));
        plantReg.registerPlantInfo(MOD_ID, "moss", new SimplePlantInfo(PlantType.SIDE_COVER, PlantSize.FULL));

        plantReg.registerPlantInfo(MOD_ID, "stoneFormations", 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE)); // atalagmite
        plantReg.registerPlantInfo(MOD_ID, "stoneFormations", 1, new SimplePlantInfo(PlantType.HANGING, PlantSize.LARGE)); // stalacmite

        plantReg.registerPlantRenderer(MOD_ID, "stoneFormations", PlantRegistry.CROSSED_SQUARES_RENDERER);

        // Default: glowshroom, flatmushroom, shadowshroom
        // toadstool, portabello, bluemilk
        for (int i : new int[] { 0, 1, 2 })
            plantReg.registerPlantInfo(MOD_ID, "mushrooms", i, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));

        plantReg.registerPlantRenderer(MOD_ID, "mushrooms", PlantRegistry.CROSSED_SQUARES_RENDERER);

        plantReg.registerPlantInfo(MOD_ID, "turnip", new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));

        plantReg.registerPlantRenderer(MOD_ID, "turnip", PlantRegistry.CROPS_RENDERER);
    }

    @Override
    public void postInit () throws Throwable { }

    private void initWood () {
        Block log1 = GameRegistry.findBlock(MOD_ID, "logs1");
        Block log2 = GameRegistry.findBlock(MOD_ID, "logs2");
        Block log3 = GameRegistry.findBlock(MOD_ID, "logs3");
        Block log4 = GameRegistry.findBlock(MOD_ID, "logs4");
        Block bamboo = GameRegistry.findBlock(MOD_ID, "bamboo");

        Block leaf1 = GameRegistry.findBlock(MOD_ID, "leaves1");
        Block leaf2 = GameRegistry.findBlock(MOD_ID, "leaves2");
        Block leaf3 = GameRegistry.findBlock(MOD_ID, "leaves3");
        Block leaf4 = GameRegistry.findBlock(MOD_ID, "leaves4");
        Block leafc1 = GameRegistry.findBlock(MOD_ID, "colorizedLeaves1");
        Block leafc2 = GameRegistry.findBlock(MOD_ID, "colorizedLeaves2");
        Block leafApple = GameRegistry.findBlock(MOD_ID, "appleLeaves");
        Block leafPersimmon = GameRegistry.findBlock(MOD_ID, "persimmonLeaves");

        Item sapling = GameRegistry.findItem(MOD_ID, "saplings");
        Item sapling2 = GameRegistry.findItem(MOD_ID, "colorizedSaplings");

        WoodRegistry woodReg = WoodRegistry.instance();

        woodReg.registerWoodType(log1, 0);
        woodReg.registerWoodType(log1, 1);
        woodReg.registerWoodType(log1, 2);
        woodReg.registerWoodType(log1, 3);

        woodReg.registerWoodType(log2, 0);
        woodReg.registerWoodType(log2, 1);
        woodReg.registerWoodType(log2, 2);
        woodReg.registerWoodType(log2, 3);

        woodReg.registerWoodType(log3, 0);
        woodReg.registerWoodType(log3, 1);
        woodReg.registerWoodType(log3, 2);
        woodReg.registerWoodType(log3, 3);

        woodReg.registerWoodType(log4, 0);
        woodReg.registerWoodType(log4, 1);
        woodReg.registerWoodType(log4, 2);
        woodReg.registerWoodType(log4, 3);

        SaplingRegistry saplingReg = SaplingRegistry.instance();

        saplingReg.registerSapling(sapling, 0, Blocks.log, 0, leafApple, 0);
        saplingReg.registerSapling(sapling, 1, Blocks.log, 2, leaf1, 0); // Autumn Tree
        saplingReg.registerSapling(sapling, 2, bamboo, 0, leaf1, 1); // Bamboo
        saplingReg.registerSapling(sapling, 3, log2, 1, leaf1, 2); // Magic Tree
        saplingReg.registerSapling(sapling, 4, log1, 2, leaf1, 3); // Dark Tree
        saplingReg.registerSapling(sapling, 5, log3, 2, leaf2, 0); // Dead Tree
        saplingReg.registerSapling(sapling, 6, log1, 3, leaf2, 1); // Fir Tree
        saplingReg.registerSapling(sapling, 7, log2, 0, leaf2, 2); // Loftwood Tree
        saplingReg.registerSapling(sapling, 8, Blocks.log2, 1, leaf2, 3); // Autumn Tree
        saplingReg.registerSapling(sapling, 9, Blocks.log, 0, leaf3, 0); // Origin Tree
        saplingReg.registerSapling(sapling, 10, log1, 1, leaf3, 1); // Pink Cherry Tree
        saplingReg.registerSapling(sapling, 11, Blocks.log, 0, leaf3, 2); // Maple Tree
        saplingReg.registerSapling(sapling, 12, log1, 1, leaf3, 3); // White Cherry Tree
        saplingReg.registerSapling(sapling, 13, log4, 1, leaf4, 0); // Hellbark
        saplingReg.registerSapling(sapling, 14, log4, 2, leaf4, 1); // Jacaranda
        saplingReg.registerSapling(sapling, 15, Blocks.log, 0, leafPersimmon, 0); // Persimmon Tree

        saplingReg.registerSapling(sapling2, 0, log1, 0, leafc1, 0); // Sacred Oak Tree
        saplingReg.registerSapling(sapling2, 1, log2, 2, leafc1, 1); // Mangrove Tree
        saplingReg.registerSapling(sapling2, 2, log2, 3, leafc1, 2 | 4); // Palm Tree
        saplingReg.registerSapling(sapling2, 3, log3, 0, leafc1, 3); // Redwood Tree
        saplingReg.registerSapling(sapling2, 4, log3, 1, leafc2, 0); // Willow Tree
        saplingReg.registerSapling(sapling2, 5, log4, 0, leafc2, 1); // Pine Tree
        saplingReg.registerSapling(sapling2, 6, log4, 3, leafc2, 2); // Mahogany Tree

        Map<String, int[]> saplingBank1 = new HashMap<String, int[]>();
        saplingBank1.put("small_oak", new int[] { 0, 1, 3, 5, 8, 9, 10, 11, 12, 14, 15 });
        saplingBank1.put("small_pine", new int[] { 2 });
        saplingBank1.put("small_spruce", new int[] { 4, 6, 7 });

        Map<String, int[]> saplingBank2 = new HashMap<String, int[]>();
        saplingBank2.put("small_oak", new int[] { 1 });
        saplingBank2.put("small_pine", new int[] { 3, 5 });
        saplingBank2.put("small_palm", new int[] { 2 });
        saplingBank2.put("small_willow", new int[] { 4 });
        saplingBank2.put("small_mahogany", new int[] { 6 });
        saplingBank2.put("large_oak", new int[] { 0 });

        Map<Item, Map<String, int[]>> banks = new HashMap<Item, Map<String, int[]>>();
        banks.put(GameRegistry.findItem(MOD_ID, "saplings"), saplingBank1);
        banks.put(GameRegistry.findItem(MOD_ID, "colorizedSaplings"), saplingBank2);

        SmallTreeRegistryHelper.registerSaplings(banks);
    }

    private static class BOPMetaResolver implements IPlantMetaResolver
    {
        @Override
        public int getPlantHeight (Block block, int meta) {
            GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(block);
            if (uid.name.equals("foliage")) {
                if (meta == 3)
                    return 2;
            }

            return 1;
        }

        @Override
        public int getPlantSectionMeta (Block block, int meta, int section) {
            GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(block);
            if (uid.name.equals("foliage")) {
                if (meta == 3)
                    return (section == 1) ? 3 : 6;
            }

            return meta;
        }
    }

    private static class BOPShrubRenderer implements IPlantRenderer
    {
        private CrossedSquaresPlantRenderer crossRender = new CrossedSquaresPlantRenderer();

        @Override
        public void render (IBlockAccess world, int x, int y, int z, RenderBlocks renderer, Block block, int meta, int height, AxisAlignedBB[] bounds) {
            crossRender.render(world, x, y, z, renderer, block, meta, height, bounds);

            if (meta != 3 || height > 1)
                return;

            IIcon hedgeTrunk = renderer.minecraftRB.getTextureMapBlocks().getTextureExtry("biomesoplenty:hedge_trunk");
            if (hedgeTrunk == null)
                return;

            Tessellator tesselator = Tessellator.instance;
            tesselator.setColorOpaque_F(1, 1, 1);

            renderer.drawCrossedSquares(hedgeTrunk, x, y, z, 1.0F);
        }
    }
}
