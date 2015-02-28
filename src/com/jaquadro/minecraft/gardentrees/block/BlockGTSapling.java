package com.jaquadro.minecraft.gardentrees.block;

import com.jaquadro.minecraft.gardentrees.GardenTrees;
import com.jaquadro.minecraft.gardentrees.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.List;
import java.util.Random;

public class BlockGTSapling extends BlockSapling
{
    public static final String[] types = new String[] { "pine", "swamp", "tallbirch" };

    @SideOnly(Side.CLIENT)
    private static IIcon[] icons;

    public BlockGTSapling (String name) {
        setBlockName(name);
        setBlockTextureName("sapling");
        setStepSound(Block.soundTypeGrass);
        setCreativeTab(ModCreativeTabs.tabGardenTrees);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta) {
        meta &= 7;
        return icons[MathHelper.clamp_int(meta, 0, types.length - 1)];
    }

    // Generate Tree
    @Override
    public void func_149878_d (World world, int x, int y, int z, Random random) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, random, x, y, z))
            return;

        int id = world.getBlockMetadata(x, y, z) & 7;
        WorldGenerator generator = null;

        switch (id) {
            case 0:
                generator = new WorldGenTaiga1() {
                    @Override
                    protected void setBlockAndNotifyAdequately (World world, int x, int y, int z, Block block, int meta) {
                        world.setBlock(x, y, z, block, meta, 3);
                    }
                };
                break;
            case 1:
                generator = new WorldGenSwamp() {
                    @Override
                    protected void setBlockAndNotifyAdequately (World world, int x, int y, int z, Block block, int meta) {
                        world.setBlock(x, y, z, block, meta, 3);
                    }
                };
                break;
            case 2:
                generator = new WorldGenForest(true, true);
                break;
            default:
                return;
        }

        world.setBlock(x, y, z, Blocks.air, 0, 4);

        if (!(generator.generate(world, random, x, y, z)))
            world.setBlock(x, y, z, this, id, 4);
    }

    @Override
    public int damageDropped (int meta) {
        return MathHelper.clamp_int(meta & 7, 0, types.length);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    @Override
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[types.length];

        for (int i = 0; i < types.length; i++)
            icons[i] = register.registerIcon(GardenTrees.MOD_ID + ":" + getTextureName() + "_" + types[i]);
    }
}
