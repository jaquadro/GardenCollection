package com.jaquadro.minecraft.modularpots.world.gen.feature;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class WorldGenJungleOrnTree extends WorldGenOrnamentalTree
{
    private static String[] options = new String[] {
        PATTERN_3X3, PATTERN_5X5UNBAL1, PATTERN_5X5UNBAL2, PATTERN_5X5UNBAL3, PATTERN_5X5UNBAL4
    };

    private int cacheRand1;
    private int cacheRand2;

    public WorldGenJungleOrnTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
        super(blockNotify, wood, metaWood, leaves, metaLeaves);
    }

    @Override
    protected void prepare (World world, Random rand, int x, int y, int z, int trunkHeight) {
        cacheRand1 = rand.nextInt(5);
        cacheRand2 = rand.nextInt(5);
    }

    @Override
    protected boolean canGenerateCanopy (World world, int x, int y, int z, int trunkHeight) {
        return canGeneratePattern(world, x, y + trunkHeight, z, options[cacheRand1])
            && canGeneratePattern(world, x, y + trunkHeight + 1, z, PATTERN_3X3PLUS)
            && canGeneratePattern(world, x, y + trunkHeight + 2, z, options[cacheRand2])
            && canGeneratePattern(world, x, y + trunkHeight + 3, z, PATTERN_1X1);
    }

    @Override
    protected void generateCanopy (World world, int x, int y, int z, int trunkHeight) {
        generatePattern(world, x, y + trunkHeight, z, options[cacheRand1]);
        generatePattern(world, x, y + trunkHeight + 1, z, PATTERN_3X3PLUS);
        generatePattern(world, x, y + trunkHeight + 2, z, options[cacheRand2]);
        generatePattern(world, x, y + trunkHeight + 3, z, PATTERN_1X1);
    }
}
