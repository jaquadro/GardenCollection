package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.api.block.IChainAttachable;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;

public class BlockHoop extends Block implements IChainAttachable
{
    public BlockHoop (String name) {
        super(Material.iron);

        setBlockName(name);
        setHardness(2.5f);
        setResistance(5f);
        setStepSound(soundTypeMetal);
        setBlockBounds(0, .0625f, 0, 1, .375f, 1);
        setBlockTextureName(GardenStuff.MOD_ID + "hoop");
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

    @Override
    public int getRenderType () {
        return ClientProxy.hoopRenderID;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return ModBlocks.metalBlock.getIcon(side, meta);
    }

    private Vec3[] attachPoints = new Vec3[] {
        Vec3.createVectorHelper(.03125, .375f, .03125), Vec3.createVectorHelper(.03125, .375f, 1 - .03125),
        Vec3.createVectorHelper(1 - .03125, .375f, .03125), Vec3.createVectorHelper(1 - .03125, .375f, 1 - .03125),
    };

    @Override
    public Vec3[] getChainAttachPoints (int side) {
        if (side == 1)
            return attachPoints;

        return null;
    }
}
