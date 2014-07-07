package com.jaquadro.minecraft.gardencore.integration;

import com.jaquadro.minecraft.gardencore.api.IPlantMetaResolver;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;

public class VanillaMetaResolver implements IPlantMetaResolver
{
    @Override
    public int getPlantHeight (Block block, int meta) {
        if (block instanceof BlockDoublePlant)
            return 2;

        // Default: Use for BlockFlower, BlockTallGrass, BlockMushroom
        return 1;
    }

    @Override
    public int getPlantSectionMeta (Block block, int meta, int section) {
        if (block instanceof BlockDoublePlant) {
            switch (section) {
                case 1: return meta & 0x7;
                case 2: return meta | 0x8;
            }
        }

        // Default: Use for BlockFlower, BlockTallGrass, BlockMushroom
        return meta;
    }
}
