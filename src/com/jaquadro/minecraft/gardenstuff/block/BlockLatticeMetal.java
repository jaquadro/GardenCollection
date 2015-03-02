package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.block.tile.TileEntityBlockMateralProxy;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityLatticeMetal;
import com.jaquadro.minecraft.gardentrees.block.tile.TileEntityWoodProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Justin on 3/1/2015.
 */
public class BlockLatticeMetal extends BlockLattice
{
    public static final String[] subNames = new String[] { "iron", "rust", "blackoxide", "moss" };

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockLatticeMetal (String blockName) {
        super(blockName, Material.iron);

        setStepSound(Block.soundTypeMetal);
        setBlockTextureName(GardenStuff.MOD_ID + ":lattice");
    }

    @Override
    public TileEntity createNewTileEntity (World world, int meta) {
        return new TileEntityLatticeMetal();
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < subNames.length; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int meta) {
        Block protoBlock = TileEntityLatticeMetal.instance.getBlockFromComposedMetadata(meta);
        if (protoBlock != null && protoBlock != this)
            return protoBlock.getIcon(side, TileEntityLatticeMetal.instance.getMetaFromComposedMetadata(meta));

        return icons[meta % icons.length];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntityBlockMateralProxy te = getTileEntity(blockAccess, x, y, z);
        if (te == null || te.getProtoBlock() == null)
            return super.getIcon(blockAccess, x, y, z, side);

        Block protoBlock = te.getProtoBlock();
        if (protoBlock == null)
            protoBlock = this;

        return protoBlock.getIcon(side, te.getProtoMeta());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[subNames.length];
        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(getTextureName() + "_" + subNames[i]);
    }
}
