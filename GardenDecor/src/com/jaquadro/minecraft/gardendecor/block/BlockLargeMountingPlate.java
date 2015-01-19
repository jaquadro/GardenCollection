package com.jaquadro.minecraft.gardendecor.block;

import com.jaquadro.minecraft.gardencontainers.GardenContainers;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockLargeMountingPlate extends Block
{
    public BlockLargeMountingPlate (String blockName) {
        super(Material.iron);

        setBlockName(blockName);
        setHardness(2.5f);
        setResistance(5f);
        setStepSound(Block.soundTypeMetal);
        setBlockBounds(0, 1 - .125f, 0, 1, 1, 1);
        setBlockTextureName(GardenContainers.MOD_ID + ":iron_baseplate_4");
        setCreativeTab(ModCreativeTabs.tabGardenCore);
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }
}
