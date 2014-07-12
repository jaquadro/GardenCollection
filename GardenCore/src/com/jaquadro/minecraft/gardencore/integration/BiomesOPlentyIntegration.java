package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.*;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.api.plant.SimplePlantInfo;
import com.jaquadro.minecraft.gardencore.client.renderer.plant.CrossedSquaresPlantRenderer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BiomesOPlentyIntegration
{
    public static final String MOD_ID = "BiomesOPlenty";

    private static BOPMetaResolver metaResolver = new BOPMetaResolver();

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        initWood();

        PlantRegistry plantReg = PlantRegistry.instance();
        plantReg.registerPlantMetaResolver(MOD_ID, "foliage", metaResolver);

        plantReg.registerPlantRenderer(MOD_ID, "foliage", new BOPShrubRenderer());

        plantReg.registerPlantInfo(MOD_ID, "plants", 0, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));  // Dead Grass
        plantReg.registerPlantInfo(MOD_ID, "plants", 1, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));  // Desert Grass
        plantReg.registerPlantInfo(MOD_ID, "plants", 2, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));  // Desert Sprouts
        plantReg.registerPlantInfo(MOD_ID, "plants", 3, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));  // Dune Grass
        plantReg.registerPlantInfo(MOD_ID, "plants", 4, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));  // Spectral Fern
        plantReg.registerPlantInfo(MOD_ID, "plants", 5, new SimplePlantInfo(PlantType.GROUND, PlantSize.LARGE));  // Thorn
        plantReg.registerPlantInfo(MOD_ID, "plants", 6, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));  // Barley
        plantReg.registerPlantInfo(MOD_ID, "plants", 7, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));  // Cattail -- Switch to 2-height, 9/10
        plantReg.registerPlantInfo(MOD_ID, "plants", 8, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));  // Rivercane
        plantReg.registerPlantInfo(MOD_ID, "plants", 11, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));  // Wild carrot
        plantReg.registerPlantInfo(MOD_ID, "plants", 12, new SimplePlantInfo(PlantType.GROUND, PlantSize.SMALL));  // Cactus
        plantReg.registerPlantInfo(MOD_ID, "plants", 13, new SimplePlantInfo(PlantType.GROUND, PlantSize.FULL));  // Witherwart
        plantReg.registerPlantInfo(MOD_ID, "plants", 14, new SimplePlantInfo(PlantType.AQUATIC_EMERGENT, PlantSize.FULL, 2, 2));  // Reed
        plantReg.registerPlantInfo(MOD_ID, "plants", 15, new SimplePlantInfo(PlantType.HANGING, PlantSize.LARGE));  // Root

        plantReg.registerPlantInfo(MOD_ID, "foliage", 0, new SimplePlantInfo(PlantType.AQUATIC, PlantSize.FULL));

        // Foliage (Default: shortgrass, mediumgrass, bush, sprout, poisonivy, berrybush, shrub, wheatgrass, dampgrass, koru)
        // duckweed: Full aquatic cover
        // hedgebottom: Large Normal 2x special renderer
        // cloverpatch: Full cover
        // leafpile: Full cover
        // deadleafpile: Full cover

        // Flower 1 (Default: swampflower, hydrangia, wildflower, violet, anemone, enderlotus, bromeliad)
        // clover: Full cover
        // deadbloom: Small normal
        // Glowflower: Small normal
        // cosmos: Small normal
        // daffodil: Small normal
        // lilyflower: Small aquatic_surface (req. lilypad cover)
        // eyebulbtop: Large normal 2x
        // dandelion: Small normal

        // Flower 2 (Default: hibiscus, lilyofthevalley, burningblossom, lavender, bluebells, minersdelight, icyiris)
        // goldenrod: Small normal
        // rose: Small normal

        // Coral (Default: )
        // kelpsingle: Large aquatic 1-?
        // pinkcoral: Large aquatic
        // orangecoral: Large aquatic
        // bluecoral: Large aquatic
        // glowcoral: Large aquatic
        // algae: Large aquatic

        // BlockIvy: Full Side Hang
        // BlockFlowerFine: Full Side Hang
        // BlockMoss: Full Side Cover

        // BlockStoneFormations:
        // stalagmite: Large ground
        // stalactite: Large hanging

        // BlockBOPMushroom (Default: glowshroom, flatmushroom, shadowshroom)
        // toadstool: Small normal
        // portobello: Small normal
        // bluemilk: Small normal

        // BlockTurnip: Staged crop
    }

    private static void initWood () {
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
        public void render (IBlockAccess world, int x, int y, int z, RenderBlocks renderer, Block block, int meta, int height) {
            crossRender.render(world, x, y, z, renderer, block, meta, height);

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
