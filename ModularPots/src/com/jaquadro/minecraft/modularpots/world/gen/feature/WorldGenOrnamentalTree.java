package com.jaquadro.minecraft.modularpots.world.gen.feature;

import com.jaquadro.minecraft.modularpots.block.BlockLargePot;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import java.util.Random;

public abstract class WorldGenOrnamentalTree extends WorldGenAbstractTree
{
    private final Block wood;
    private final Block leaves;
    private final int metaWood;
    private final int metaLeaves;

    protected final static String PATTERN_1X1 = "X";
    protected final static String PATTERN_3X3PLUS = " X " + "XXX" + " X ";
    protected final static String PATTERN_3X3 = "XXX" + "XXX" + "XXX";
    protected final static String PATTERN_5X5PLUS = "  X  " + " XXX " + "XXXXX" + " XXX " + "  X  ";
    protected final static String PATTERN_5X5PLUSW = " XXX " + "XXXXX" + "XXXXX" + "XXXXX" + " XXX ";
    protected final static String PATTERN_5X5 = "XXXXX" + "XXXXX" + "XXXXX" + "XXXXX" + "XXXXX";
    protected final static String PATTERN_5X5UNBAL1 = " XX  " + "XXXX " + "XXXX " + " XXX " + "     ";
    protected final static String PATTERN_5X5UNBAL2 = "  XX " + " XXXX" + " XXXX" + " XXX " + "     ";
    protected final static String PATTERN_5X5UNBAL3 = "     " + " XXX " + " XXXX" + " XXXX" + "  XX ";
    protected final static String PATTERN_5X5UNBAL4 = "     " + " XXX " + "XXXX " + "XXXX " + " XX  ";

    public WorldGenOrnamentalTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
        super(blockNotify);
        this.wood = wood;
        this.leaves = leaves;
        this.metaWood = metaWood;
        this.metaLeaves = metaLeaves;
    }

    @Override
    public boolean generate (World world, Random rand, int x, int y, int z) {
        boolean potted = world.getBlock(x, y - 1, z) instanceof BlockLargePot;
        int height = potted ? 5 : 6;
        int trunkHeight = height - 4;

        prepare(world, rand, x, y, z, trunkHeight);

        if (!canGenerate(world, x, y, z, height))
            return false;

        generateCanopy(world, x, y, z, trunkHeight);
        generateTrunk(world, x, y, z, trunkHeight);

        return true;
    }

    private boolean canGenerate (World world, int x, int y, int z, int height) {
        int trunkHeight = height - 4;

        if (y + height > 256)
            return false;

        return canGenerateTrunk(world, x, y, z, trunkHeight)
            && canGenerateCanopy(world, x, y, z, trunkHeight);
    }

    protected void prepare (World world, Random rand, int x, int y, int z, int trunkHeight) { }

    protected boolean canGenerateTrunk (World world, int x, int y, int z, int trunkHeight) {
        for (int iy = y; iy < y + trunkHeight; iy++) {
            if (!isReplaceable(world, x, iy, z))
                return false;
        }
        return true;
    }

    protected abstract boolean canGenerateCanopy (World world, int x, int y, int z, int trunkHeight);

    protected void generateTrunk (World world, int x, int y, int z, int trunkHeight) {
        for (int iy = y; iy < y + trunkHeight + 1; iy++)
            generateBlock(world, x, iy, z, wood, metaWood);
    }

    protected abstract void generateCanopy (World world, int x, int y, int z, int trunkHeight);

    private int getPatternDim (String pattern) {
        return (int)Math.floor(Math.sqrt(pattern.length()));
    }

    protected boolean canGeneratePattern (World world, int x, int y, int z, String pattern) {
        int dim = getPatternDim(pattern) / 2;
        for (int ix = x - dim; ix <= x + dim; ix++) {
            for (int iz = z - dim; iz <= z + dim; iz++) {
                int index = (iz - z + dim) * (dim * 2 + 1) + (ix - x + dim);
                if (pattern.charAt(index) == 'X' && !isReplaceable(world, ix, y, iz))
                    return false;
            }
        }

        return true;
    }

    protected void generatePattern (World world, int x, int y, int z, String pattern) {
        int dim = getPatternDim(pattern) / 2;
        for (int ix = x - dim; ix <= x + dim; ix++) {
            for (int iz = z - dim; iz <= z + dim; iz++) {
                int index = (iz - z + dim) * (dim * 2 + 1) + (ix - x + dim);
                if (pattern.charAt(index) == 'X')
                    generateBlock(world, ix, y, iz);
            }
        }
    }

    /*private boolean canGenerate3x3Plus (World world, int x, int y, int z) {
        return isReplaceable(world, x, y, z)
            && isReplaceable(world, x - 1, y, z)
            && isReplaceable(world, x + 1, y, z)
            && isReplaceable(world, x, y, z - 1)
            && isReplaceable(world, x, y, z + 1);
    }

    private boolean canGenerate3x3 (World world, int x, int y, int z) {
        return canGenerate3x3Plus(world, x, y, z)
            && isReplaceable(world, x - 1, y, z - 1)
            && isReplaceable(world, x - 1, y, z + 1)
            && isReplaceable(world, x + 1, y, z - 1)
            && isReplaceable(world, x + 1, y, z + 1);
    }

    private void generate3x3Plus (World world, int x, int y, int z) {
        generateBlock(world, x, y, z);
        generateBlock(world, x - 1, y, z);
        generateBlock(world, x + 1, y, z);
        generateBlock(world, x, y, z - 1);
        generateBlock(world, x, y, z + 1);
    }

    private void generate3x3 (World world, int x, int y, int z) {
        generate3x3Plus(world, x, y, z);
        generateBlock(world, x - 1, y, z - 1);
        generateBlock(world, x - 1, y, z + 1);
        generateBlock(world, x + 1, y, z - 1);
        generateBlock(world, x + 1, y, z + 1);
    }*/

    private void generateBlock (World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block.isAir(world, x, y, z) || block.isLeaves(world, x, y, z))
            setBlockAndNotifyAdequately(world, x, y, z, leaves, metaLeaves);
    }

    private void generateBlock (World world, int x, int y, int z, Block block, int meta) {
        Block existingBlock = world.getBlock(x, y, z);
        if (existingBlock.isAir(world, x, y, z) || existingBlock.isLeaves(world, x, y, z))
            setBlockAndNotifyAdequately(world, x, y, z, block, meta);
    }

    @Override
    protected boolean isReplaceable (World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return super.isReplaceable(world, x, y, z);
    }
}
