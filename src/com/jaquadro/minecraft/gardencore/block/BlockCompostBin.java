package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityCompostBin;
import com.jaquadro.minecraft.gardencore.client.particle.EntitySteamFX;
import com.jaquadro.minecraft.gardencore.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardencore.core.handlers.GuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCompostBin extends BlockContainer
{
    private static final int ICON_SIDE = 0;
    private static final int ICON_TOP = 1;
    private static final int ICON_BOTTOM = 2;
    private static final int ICON_INNER = 3;

    @SideOnly(Side.CLIENT)
    IIcon[] icons;

    public BlockCompostBin (String name) {
        super(Material.wood);

        setBlockName(name);
        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setTickRandomly(true);
        setHardness(1.5f);
        setResistance(5f);
        setStepSound(Block.soundTypeWood);
    }

    @Override
    public boolean renderAsNormalBlock () {
        return false;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.compostBinRenderID;
    }

    @Override
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
        player.openGui(GardenCore.instance, GuiHandler.compostBinGuiID, world, x, y, z);
        return true;
    }

    public static void updateBlockState (World world, int x, int y, int z) {
        TileEntityCompostBin te = (TileEntityCompostBin) world.getTileEntity(x, y, z);
        if (te == null)
            return;

        world.markBlockForUpdate(x, y, z);
        //world.func_147479_m(x, y, z);
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        TileEntityCompostBin te = getTileEntity(world, x, y, z);

        if (te != null) {
            for (int i = 0; i < te.getSizeInventory(); i++) {
                ItemStack item = te.getStackInSlot(i);
                if (item != null)
                    dropBlockAsItem(world, x, y, z, item);
            }
        }

        super.breakBlock(world, x, y, z, block, data);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick (World world, int x, int y, int z, Random random) {
        TileEntityCompostBin te = (TileEntityCompostBin) world.getTileEntity(x, y, z);
        if (te == null)
            return;

        if (te.isDecomposing()) {
            float px = x + .5f + random.nextFloat() * .6f - .3f;
            float py = y + .5f + random.nextFloat() * 6f / 16f;
            float pz = z + .5f + random.nextFloat() * .6f - .3f;

            //world.spawnParticle("smoke", px, py, pz, 0, 0, 0);
            EntitySteamFX.spawnParticle(world, px, py, pz);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta) {
        switch (side) {
            case 0:
                return icons[ICON_BOTTOM];
            case 1:
                return icons[ICON_TOP];
            default:
                return icons[ICON_SIDE];
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getInnerIcon () {
        return icons[ICON_INNER];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        icons = new IIcon[4];

        icons[ICON_SIDE] = register.registerIcon(GardenCore.MOD_ID + ":compost_bin_side");
        icons[ICON_TOP] = register.registerIcon(GardenCore.MOD_ID + ":compost_bin_top");
        icons[ICON_BOTTOM] = register.registerIcon(GardenCore.MOD_ID + ":compost_bin_bottom");
        icons[ICON_INNER] = register.registerIcon(GardenCore.MOD_ID + ":compost_bin_inner");
    }

    @Override
    public TileEntity createNewTileEntity (World world, int p_149915_2_) {
        return new TileEntityCompostBin();
    }

    public TileEntityCompostBin getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityCompostBin) ? (TileEntityCompostBin) te : null;
    }
}
