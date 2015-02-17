package com.jaquadro.minecraft.gardentrees.world.gen;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class WorldGenStandardOrnTree extends WorldGenOrnamentalTree
{
    protected List<String> layers = new ArrayList<String>();

    public WorldGenStandardOrnTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
        super(blockNotify, wood, metaWood, leaves, metaLeaves);
    }

    @Override
    protected boolean canGenerateCanopy (World world, int x, int y, int z, int trunkHeight) {
        for (int i = 0; i < layers.size(); i++) {
            if (!canGeneratePattern(world, x, y + trunkHeight + i, z, layers.get(i)))
                return false;
        }
        return true;
    }

    @Override
    protected void generateCanopy (World world, int x, int y, int z, int trunkHeight) {
        for (int i = 0; i < layers.size(); i++)
            generatePattern(world, x, y + trunkHeight + i, z, layers.get(i));
    }

    public static class SmallOakTree extends WorldGenStandardOrnTree
    {
        public SmallOakTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_3X3PLUS, LayerType.CORE),
                transform(PAT_3X3, LayerType.CORE),
                transform(PAT_3X3PLUS, LayerType.LEAF),
                transform(PAT_1X1, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallOakTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallSpruceTree extends WorldGenStandardOrnTree
    {
        public SmallSpruceTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_3X3, LayerType.CORE),
                transform(PAT_3X3PLUS, LayerType.CORE),
                transform(PAT_3X3PLUS, LayerType.LEAF),
                transform(PAT_1X1, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallSpruceTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallJungleTree extends WorldGenStandardOrnTree
    {
        public SmallJungleTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
        }

        @Override
        protected void prepare (World world, Random rand, int x, int y, int z, int trunkHeight) {
            layers = new ArrayList<String>();
            layers.add(transform(PAT_5X5UNBAL, LayerType.CORE, rand.nextInt()));
            layers.add(transform(PAT_3X3PLUS, LayerType.CORE));
            layers.add(transform(PAT_5X5UNBAL, LayerType.CORE));
            layers.add(transform(PAT_1X1, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallJungleTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallAcaciaTree extends WorldGenStandardOrnTree
    {
        public SmallAcaciaTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_1X1, LayerType.CORE),
                transform(PAT_3X3, LayerType.CORE),
                transform(PAT_3X3PLUS, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallAcaciaTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class LargeOakTree extends WorldGenStandardOrnTree
    {
        public LargeOakTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_3X3PLUS, LayerType.CORE),
                transform(PAT_5X5PLUS, LayerType.CORE),
                transform(PAT_5X5PLUS2T, LayerType.CORE),
                transform(PAT_5X5PLUS, LayerType.CORE),
                transform(PAT_5X5PLUS2N, LayerType.CORE),
                transform(PAT_3X3, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new LargeOakTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallPalmTree extends WorldGenStandardOrnTree
    {
        public SmallPalmTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_1X1, LayerType.CORE),
                transform(PAT_3X3IPLUS, LayerType.CORE),
                transform(PAT_3X3PLUS, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallPalmTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallWillowTree extends WorldGenStandardOrnTree
    {
        public SmallWillowTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_3X3IPLUS, LayerType.CORE),
                transform(PAT_3X3, LayerType.CORE),
                transform(PAT_3X3PLUS, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallWillowTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallPineTree extends WorldGenStandardOrnTree
    {
        public SmallPineTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
        }

        @Override
        protected void prepare (World world, Random rand, int x, int y, int z, int trunkHeight) {
            layers = new ArrayList<String>();
            layers.add(transform(PAT_3X3UNBAL, LayerType.CORE, rand.nextInt()));
            layers.add(transform(PAT_1X1, LayerType.CORE));
            layers.add(transform(PAT_3X3PLUS, LayerType.CORE));
            layers.add(transform(PAT_1X1, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallPineTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallMahoganyTree extends WorldGenStandardOrnTree
    {
        public SmallMahoganyTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_3X3PLUS, LayerType.TRUNK),
                transform(PAT_3X3, LayerType.LEAF),
                transform(PAT_3X3PLUS, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallMahoganyTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallShrubTree extends WorldGenStandardOrnTree
    {
        public SmallShrubTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_3X3PLUS, LayerType.LEAF),
                transform(PAT_1X1, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallShrubTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class TallSmallOakTree extends WorldGenStandardOrnTree
    {
        public TallSmallOakTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_1X1, LayerType.TRUNK),
                transform(PAT_3X3PLUS, LayerType.CORE),
                transform(PAT_3X3, LayerType.CORE),
                transform(PAT_1X1, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new TallSmallOakTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class LargeSpruceTree extends WorldGenStandardOrnTree
    {
        public LargeSpruceTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
        }

        @Override
        protected void prepare (World world, Random rand, int x, int y, int z, int trunkHeight) {
            layers = new ArrayList<String>();
            layers.add(transform(PAT_1X1, LayerType.CORE));
            layers.add(transform(PAT_3X3PLUS, LayerType.CORE));
            layers.add(transform(PAT_3X3, LayerType.CORE));
            layers.add(transform(PAT_5X5PLUS, LayerType.CORE));
            layers.add(transform(PAT_3X3, LayerType.CORE));
            layers.add(transform(PAT_3X3PLUS, LayerType.CORE));
            layers.add(transform(PAT_3X3PLUS, LayerType.LEAF));
            layers.add(transform(PAT_1X1, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new LargeSpruceTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallCyprusTree extends WorldGenStandardOrnTree
    {
        public SmallCyprusTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
            layers = Arrays.asList(transform(PAT_3X3PLUS, LayerType.CORE),
                transform(PAT_3X3, LayerType.CORE),
                transform(PAT_3X3PLUS, LayerType.LEAF),
                transform(PAT_3X3PLUS, LayerType.LEAF),
                transform(PAT_1X1, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallCyprusTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }

    public static class SmallCanopyTree extends WorldGenStandardOrnTree
    {
        public SmallCanopyTree (boolean blockNotify, Block wood, int metaWood, Block leaves, int metaLeaves) {
            super(blockNotify, wood, metaWood, leaves, metaLeaves);
        }

        @Override
        protected void prepare (World world, Random rand, int x, int y, int z, int trunkHeight) {
            layers = new ArrayList<String>();
            layers.add(transform(PAT_3X3OPT, LayerType.TRUNK, rand.nextInt()));
            layers.add(transform(PAT_5X5PLUS2, LayerType.CORE));
            layers.add(transform(PAT_3X3, LayerType.LEAF));
        }

        public static final OrnamentalTreeFactory FACTORY = new OrnamentalTreeFactory() {
            @Override
            public WorldGenOrnamentalTree create (Block woodBlock, int woodMeta, Block leafBlock, int leafMeta) {
                return new SmallCanopyTree(false, woodBlock, woodMeta, leafBlock, leafMeta);
            }
        };
    }
}
