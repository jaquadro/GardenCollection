package com.jaquadro.minecraft.modularpots.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class WorldGenOakOrnTree extends WorldGenOrnamentalTree
{
    public WorldGenOakOrnTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
        super(blockNotify, wood, metaWood, leaves, metaLeaves);
    }

    @Override
    protected boolean canGenerateCanopy (World world, int x, int y, int z, int trunkHeight) {
        return canGeneratePattern(world, x, y + trunkHeight, z, PATTERN_3X3PLUS)
            && canGeneratePattern(world, x, y + trunkHeight + 1, z, PATTERN_3X3)
            && canGeneratePattern(world, x, y + trunkHeight + 2, z, PATTERN_3X3PLUS)
            && canGeneratePattern(world, x, y + trunkHeight + 3, z, PATTERN_1X1);
    }

    @Override
    protected void generateCanopy (World world, int x, int y, int z, int trunkHeight) {
        generatePattern(world, x, y + trunkHeight, z, PATTERN_3X3PLUS);
        generatePattern(world, x, y + trunkHeight + 1, z, PATTERN_3X3);
        generatePattern(world, x, y + trunkHeight + 2, z, PATTERN_3X3PLUS);
        generatePattern(world, x, y + trunkHeight + 3, z, PATTERN_1X1);
    }
}
