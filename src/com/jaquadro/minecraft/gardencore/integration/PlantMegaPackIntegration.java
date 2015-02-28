package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.*;
import com.jaquadro.minecraft.gardencore.api.plant.IPlantInfo;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import plantmegapack.bin.PMPRenderers;
import plantmegapack.block.PMPBlockPlant;
import plantmegapack.common.PMPPlantGrowthType;

public class PlantMegaPackIntegration
{
    public static final String MOD_ID = "plantmegapack";

    public static void init () {
        if (!Loader.isModLoaded(MOD_ID))
            return;

        GardenCoreAPI.instance().registerBonemealHandler(new BonemealHandler());

        PlantRegistry plantRegistry = PlantRegistry.instance();
        PlantInfo resolver = new PlantInfo();

        plantRegistry.registerPlantInfo(MOD_ID, resolver);

        plantRegistry.registerPlantRenderer(PMPRenderers.renderPlantID, PlantRegistry.CROSSED_SQUARES_RENDERER);
    }

    public static class BonemealHandler implements IBonemealHandler
    {
        @Override
        public boolean applyBonemeal (World world, int x, int y, int z, BlockGarden hostBlock, int slot) {
            TileEntityGarden te = hostBlock.getTileEntity(world, x, y, z);

            Block block = hostBlock.getPlantBlockFromSlot(world, x, y, z, slot);
            int meta = hostBlock.getPlantMetaFromSlot(world, x, y, z, slot);

            if (block == null || !(block instanceof PMPBlockPlant))
                return false;

            PMPBlockPlant plantBlock = (PMPBlockPlant)block;
            if (plantBlock.isFullyGrown(meta))
                return false;

            int growMeta = growMeta(plantBlock, meta);
            if (meta != growMeta) {
                ItemStack upgrade = new ItemStack(block, 1, growMeta);
                if (hostBlock.isPlantValidForSlot(world, x, y, z, slot, PlantItem.getForItem(upgrade))) {
                    te.setInventorySlotContents(slot, upgrade);
                    return true;
                }
                return false;
            }

            return false;
        }

        private int growMeta (PMPBlockPlant block, int meta) {
            if (meta == 0)
                return 1;
            if (meta == 1 && block.plantData.attributes.growthStages > 2)
                return 2;
            if (meta == 2 && block.plantData.attributes.growthStages > 3)
                return 4;
            if (meta == 4 && block.plantData.attributes.growthStages > 4)
                return 6;

            return meta;
        }
    }

    public static class PlantInfo implements IPlantInfo
    {
        @Override
        public int getPlantHeight (Block block, int meta) {
            if (block == null || !(block instanceof PMPBlockPlant))
                return 1;

            PMPBlockPlant plantBlock = (PMPBlockPlant)block;
            if (plantBlock.plantData.attributes.growthType == PMPPlantGrowthType.DOUBLE && meta >= 2)
                return 2;

            return 1;
        }

        @Override
        public int getPlantSectionMeta (Block block, int meta, int section) {
            if (block == null || !(block instanceof PMPBlockPlant))
                return meta;

            PMPBlockPlant plantBlock = (PMPBlockPlant)block;
            if (plantBlock.plantData.attributes.growthType == PMPPlantGrowthType.DOUBLE) {
                switch (section) {
                    case 1: return meta;
                    case 2: return (meta >= 2) ? meta + 1 : meta;
                }
            }

            return meta;
        }

        @Override
        public PlantType getPlantTypeClass (Block block, int meta) {
            if (block == null || !(block instanceof PMPBlockPlant))
                return PlantType.INVALID;

            PMPBlockPlant plantBlock = (PMPBlockPlant)block;
            switch (plantBlock.plantData.attributes.renderType) {
                case CROP:
                case NORMAL:
                case STALK:
                case STAR:
                    return PlantType.GROUND;
                case EPIPHYTE_HORIZONTAL:
                case EPIPHYTE_VERTICAL:
                    return PlantType.SIDE;
                case FLAT:
                case GROUNDCOVER:
                    return PlantType.GROUND_COVER;
                case FLOATING_FLAT:
                    return PlantType.AQUATIC_COVER;
                case FLOATING_FLOWER:
                case FLOATING_PLANT:
                    return PlantType.AQUATIC_SURFACE;
                case IMMERSED:
                    return PlantType.AQUATIC_EMERGENT;
                case VINE_FLOWER:
                case VINE_NORMAL:
                case VINE_RANDOM:
                case VINE_VANILLA:
                    return PlantType.HANGING_SIDE;
                case WATER:
                case WATER_FLAT:
                    return PlantType.AQUATIC;
                default:
                    return PlantType.INVALID;
            }
        }

        @Override
        public PlantSize getPlantSizeClass (Block block, int meta) {
            return PlantSize.SMALL;
        }

        @Override
        public int getPlantMaxHeight (Block block, int meta) {
            if (block == null || !(block instanceof PMPBlockPlant))
                return 1;

            PMPBlockPlant plantBlock = (PMPBlockPlant)block;
            if (plantBlock.plantData.attributes.growthType == PMPPlantGrowthType.DOUBLE)
                return 2;

            return 1;
        }
    }

    public static class PlantRenderer implements IPlantRenderer {

        @Override
        public void render (IBlockAccess world, int x, int y, int z, RenderBlocks renderer, Block block, int meta, int height) {

        }
    }
}
