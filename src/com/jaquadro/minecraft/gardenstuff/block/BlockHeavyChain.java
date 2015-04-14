package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockHeavyChain extends Block
{
    public static final String[] types = new String[] { "iron", "gold", "rope", "rust", "wrought_iron", "moss" };

    @SideOnly(Side.CLIENT)
    private static IIcon[] icons;

    public BlockHeavyChain (String blockName) {
        super(Material.iron);

        setBlockName(blockName);
        setHardness(2.5f);
        setResistance(5f);
        setStepSound(Block.soundTypeMetal);
        setBlockBounds(.5f - .125f, 0, .5f - .125f, .5f + .125f, 1, .5f + .125f);
        setBlockTextureName(GardenStuff.MOD_ID + ":chain_heavy");
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
        return ClientProxy.heavyChainRenderID;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x, int y, int z) {
        return null;
    }

    @Override
    public int damageDropped (int meta) {
        return MathHelper.clamp_int(meta, 0, types.length - 1);
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        //list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 4));
        list.add(new ItemStack(item, 1, 5));
    }

    @Override
    public void onBlockAdded (World world, int x, int y, int z) {
        world.notifyBlockOfNeighborChange(x, y + 1, z, this);
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block block) {
        if (block == this || block == ModBlocks.lantern)
            world.notifyBlockOfNeighborChange(x, y + 1, z, this);

        if (world.getBlock(x, y + 1, z) != this)
            world.notifyBlockOfNeighborChange(x, y + 2, z, this);
    }

    @Override
    public boolean canProvidePower () {
        return true;
    }

    @Override
    public int isProvidingWeakPower (IBlockAccess world, int x, int y, int z, int side) {
        return isProvidingStrongPower(world, x, y, z, side);
    }

    @Override
    public int isProvidingStrongPower (IBlockAccess world, int x, int y, int z, int side) {
        if (side != 0)
            return 0;

        for (int i = 1; i <= 8 && y - i > 0; i++) {
            Block block = world.getBlock(x, y - i, z);
            if (block == this)
                continue;

            return block.isProvidingWeakPower(world, x, y - i, z, side);
        }

        return 0;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return icons[MathHelper.clamp_int(meta, 0, types.length - 1)];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[types.length];

        for (int i = 0; i < types.length; i++)
            icons[i] = register.registerIcon(getTextureName() + "_" + types[i]);
    }
}
