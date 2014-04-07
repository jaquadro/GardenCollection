package com.jaquadro.minecraft.modularpots.world.gen.feature;

import com.jaquadro.minecraft.modularpots.core.ModBlocks;
import com.jaquadro.minecraft.modularpots.block.BlockLargePot;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityWoodProxy;
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

    protected enum LayerType {
        LEAF,
        TRUNK,
        CORE;
    }

    protected final static String PAT_1X1 = "T";
    protected final static String PAT_3X3 = "XXX" + "XTX" + "XXX";
    protected final static String PAT_3X3PLUS = " X " + "XTX" + " X ";
    protected final static String PAT_3X3IPLUS = "X X" + " T " + "X X";
    protected final static String PAT_3X3UNBAL = "0X " + "XTX" + " X ";
    protected final static String PAT_5X5 = "XXXXX" + "XXXXX" + "XXTXX" + "XXXXX" + "XXXXX";
    protected final static String PAT_5X5PLUS = "  X  " + " XXX " + "XXTXX" + " XXX " + "  X  ";
    protected final static String PAT_5X5PLUS2 = " XXX " + "XXXXX" + "XXTXX" + "XXXXX" + " XXX ";
    protected final static String PAT_5X5PLUS2N = " X X " + "XXXXX" + " XTX " + "XXXXX" + " X X ";
    protected final static String PAT_5X5PLUS2T = " XXX " + "XXTXX" + "XTTTX" + "XXTXX" + " XXX ";
    protected final static String PAT_5X5UNBAL = " 00  " + "0XXX " + "0XTX " + " XXX " + "     ";

    protected static String transform (String pattern, LayerType type) {
        return transform(pattern, type, 0);
    }

    protected static String transform (String pattern, LayerType type, int option) {
        int groups = countOptionGroups(pattern);
        for (int i = 0; i < groups; i++) {
            if (((option >> i) & 1) == 0)
                pattern = pattern.replace((char)('0' + i), ' ');
            else
                pattern = pattern.replace((char)('0' + i), 'X');
        }

        if (type == LayerType.LEAF)
            pattern = pattern.replace('T', 'X');
        else if (type == LayerType.TRUNK)
            pattern = pattern.replace('X', 'T');

        boolean flipH = ((option >>> 30) & 1) == 1;
        boolean flipV = ((option >>> 31) & 1) == 1;
        if (flipH && flipV)
            pattern = new StringBuilder(pattern).reverse().toString();
        else if (flipH) {
            int dim = getPatternDim(pattern);
            StringBuilder sb = new StringBuilder(pattern);
            for (int y = 0; y < dim; y++) {
                int base = y * dim;
                for (int x = 0; x < dim; x++)
                    sb.setCharAt(base + x, pattern.charAt(base + dim - x - 1));
            }
            pattern = sb.toString();
        }
        else if (flipV) {
            int dim = getPatternDim(pattern);
            StringBuilder sb = new StringBuilder(pattern);
            for (int y = 0; y < dim; y++) {
                int base = y * dim;
                int base2 = (dim - y - 1) * dim;
                for (int x = 0; x < dim; x++)
                    sb.setCharAt(base + x, pattern.charAt(base2 + x));
            }
            pattern = sb.toString();
        }

        return pattern;
    }

    private static int countOptionGroups (String pattern) {
        char high = '0' - 1;
        for (int i = 0, n = pattern.length(); i < n; i++) {
            char c = pattern.charAt(i);
            if (c >= '0' && c <= '9' && c > high)
                high = c;
        }

        return high - '0' + 1;
    }


    public WorldGenOrnamentalTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
        super(blockNotify);

        /*if (wood != ModBlocks.thinLog) {
            metaWood = TileEntityWoodProxy.composeMetadata(wood, metaWood);
            wood = ModBlocks.thinLog;
        }*/

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

    private static int getPatternDim (String pattern) {
        return (int)Math.floor(Math.sqrt(pattern.length()));
    }

    protected boolean canGeneratePattern (World world, int x, int y, int z, String pattern) {
        int dim = getPatternDim(pattern) / 2;
        for (int ix = x - dim; ix <= x + dim; ix++) {
            for (int iz = z - dim; iz <= z + dim; iz++) {
                int index = (iz - z + dim) * (dim * 2 + 1) + (ix - x + dim);
                if ((pattern.charAt(index) == 'X' || pattern.charAt(index) == 'T') && !isReplaceable(world, ix, y, iz))
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
                else if (pattern.charAt(index) == 'T')
                    generateTrunk(world, ix, y, iz);
            }
        }
    }

    private void generateBlock (World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block.isAir(world, x, y, z) || block.isLeaves(world, x, y, z))
            setBlockAndNotifyAdequately(world, x, y, z, leaves, metaLeaves);
    }

    private void generateTrunk (World world, int x, int y, int z) {
        generateBlock(world, x, y, z, wood, metaWood);
    }

    private void generateBlock (World world, int x, int y, int z, Block block, int meta) {
        Block existingBlock = world.getBlock(x, y, z);
        if (existingBlock.isAir(world, x, y, z) || existingBlock.isLeaves(world, x, y, z)) {
            if (block != ModBlocks.thinLog) {
                setBlockAndNotifyAdequately(world, x, y, z, block, 0);
                TileEntityWoodProxy te = new TileEntityWoodProxy();
                te.setProtoBlock(block, meta);
                
                setBlockAndNotifyAdequately(world, x, y, z, ModBlocks.thinLog, 0);
                world.setTileEntity(x, y, z, te);
            }
            else
                setBlockAndNotifyAdequately(world, x, y, z, block, meta);
        }
    }

    @Override
    protected boolean isReplaceable (World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        return super.isReplaceable(world, x, y, z);
    }
}
