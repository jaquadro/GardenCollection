package com.jaquadro.minecraft.gardentrees.block;

import com.jaquadro.minecraft.gardentrees.GardenTrees;
import com.jaquadro.minecraft.gardentrees.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockStrangePlant extends BlockFlower implements IShearable
{
    @SideOnly(Side.CLIENT)
    private IIcon icon;

    public BlockStrangePlant (String blockName) {
        super(0);

        setBlockTextureName(GardenTrees.MOD_ID + ":strange_plant");
        setBlockName(blockName);
        setHardness(0);
        setStepSound(Block.soundTypeGrass);
        setCreativeTab(ModCreativeTabs.tabGardenTrees);
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared (ItemStack item, IBlockAccess world, int x, int y, int z, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z)));
        return ret;
    }

    @Override
    public Item getItemDropped (int meta, Random rand, int fortune) {
        return null;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        ItemStack[] strangePlantDrops = GardenTrees.config.getStrangePlantDrops();
        if (strangePlantDrops.length == 0)
            return ret;
        if (world.rand.nextDouble() > GardenTrees.config.strangePlantDropChance)
            return ret;

        ItemStack item = strangePlantDrops[world.rand.nextInt(strangePlantDrops.length)].copy();
        int range = GardenTrees.config.strangePlantDropMax - GardenTrees.config.strangePlantDropMin;
        int count = GardenTrees.config.strangePlantDropMin + ((range > 0) ? world.rand.nextInt(range) : 0);

        for (int i = 0; i < count; i++)
            ret.add(item);

        return ret;
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTab, List list) {
        list.add(new ItemStack(item));
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return icon;
    }

    @Override
    public void registerBlockIcons (IIconRegister register) {
        icon = register.registerIcon(getTextureName());
    }
}
