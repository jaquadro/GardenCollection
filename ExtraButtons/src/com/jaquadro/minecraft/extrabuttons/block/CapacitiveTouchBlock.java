package com.jaquadro.minecraft.extrabuttons.block;

import com.jaquadro.minecraft.extrabuttons.ExtraButtons;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class CapacitiveTouchBlock extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    public CapacitiveTouchBlock ()
    {
        super(Material.circuits);

        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    public int tickRate (World world)
    {
        return 20;
    }

    @Override
    public boolean isOpaqueCube ()
    {
        return false;
    }

    @Override
    public int onBlockPlaced (World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int data)
    {
        return 0;
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        int data = world.getBlockMetadata(x, y, z);
        int isPressed = 8 - (data & 8);

        if (isPressed == 0) {
            return true;
        }
        else {
            world.setBlockMetadataWithNotify(x, y, z, isPressed, 3);
            notifyNeighbors(world, x, y, z);
            world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
            return true;
        }
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data)
    {
        if ((data & 8) > 0) {
            this.notifyNeighbors(world, x, y, z);
        }

        super.breakBlock(world, x, y, z, block, data);
    }

    @Override
    public int isProvidingWeakPower (IBlockAccess world, int x, int y, int z, int side)
    {
        return (world.getBlockMetadata(x, y, z) & 8) > 0 ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower (IBlockAccess world, int x, int y, int z, int side)
    {
        return (world.getBlockMetadata(x, y, z) & 8) > 0 ? 15 : 0;
    }

    @Override
    public boolean canProvidePower ()
    {
        return true;
    }

    @Override
    public void updateTick (World world, int x, int y, int z, Random rand)
    {
        if (!world.isRemote) {
            int data = world.getBlockMetadata(x, y, z);

            if ((data & 8) != 0) {
                world.setBlockMetadataWithNotify(x, y, z, data & 7, 3);
                notifyNeighbors(world, x, y, z);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int data)
    {
        if ((data & 8) > 0)
            return iconArray[1];
        else
            return iconArray[0];
    }

    private void notifyNeighbors (World world, int x, int y, int z)
    {
        world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
        world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
        world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
        world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
        world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
        world.notifyBlocksOfNeighborChange(x, y + 1, z, this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        iconArray = new IIcon[] {
                iconRegister.registerIcon(ExtraButtons.MOD_ID + ":captouch_off"),
                iconRegister.registerIcon(ExtraButtons.MOD_ID + ":captouch_on"),
        };
    }
}
