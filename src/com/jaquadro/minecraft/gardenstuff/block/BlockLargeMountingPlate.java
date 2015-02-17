package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
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
        setBlockTextureName(GardenStuff.MOD_ID + ":iron_baseplate_4");
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
