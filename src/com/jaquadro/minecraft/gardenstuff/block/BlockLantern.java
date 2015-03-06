package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * Created by Justin on 3/6/2015.
 */
public class BlockLantern extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;
    @SideOnly(Side.CLIENT)
    private IIcon iconSide;
    @SideOnly(Side.CLIENT)
    private IIcon iconTop;
    @SideOnly(Side.CLIENT)
    private IIcon iconTopCross;
    @SideOnly(Side.CLIENT)
    private IIcon iconGlass;
    @SideOnly(Side.CLIENT)
    private IIcon iconCandle;

    public BlockLantern (String blockName) {
        super(Material.iron);

        setBlockName(blockName);
        setTickRandomly(true);
        setHardness(2.5f);
        setResistance(5);
        setLightLevel(1);
        setBlockTextureName(GardenStuff.MOD_ID + ":lantern");
        setCreativeTab(ModCreativeTabs.tabGardenCore);

        setBlockBoundsForItemRender();
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
        return ClientProxy.lanternRenderID;
    }

    @Override
    public int getRenderBlockPass () {
        return 1;
    }

    @Override
    public boolean canRenderInPass (int pass) {
        ClientProxy.lanternRenderer.renderPass = pass;
        return true;
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z) {
        setBlockBounds(.125f, 0, .125f, .875f, .875f, .875f);
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        setBlockBoundsBasedOnState(world, x, y, z);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        double px = (double) ((float) x + 0.5F);
        double py = (double) ((float) y + 0.6F);
        double pz = (double) ((float) z + 0.5F);

        world.spawnParticle("smoke", px, py, pz, 0.0D, 0.0D, 0.0D);
        world.spawnParticle("flame", px, py, pz, 0.0D, 0.0D, 0.0D);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int meta) {
        if (side == 0)
            return iconBottom;
        if (side == 1)
            return iconTop;

        return iconSide;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconCandle () {
        return iconCandle;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconTopCross () {
        return iconTopCross;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconGlass () {
        return iconGlass;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister register) {
        iconBottom = register.registerIcon(getTextureName() + "_bottom");
        iconSide = register.registerIcon(getTextureName());
        iconGlass = register.registerIcon(getTextureName() + "_glass");
        iconTop = register.registerIcon(getTextureName() + "_top");
        iconTopCross = register.registerIcon(getTextureName() + "_top_cross");
        iconCandle = register.registerIcon(GardenStuff.MOD_ID + ":candle");
    }
}
