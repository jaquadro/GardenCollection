package com.jaquadro.minecraft.extrabuttons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockButton;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class IlluminatedButton extends BlockButton
{
    public IlluminatedButton (int id, int texture)
    {
        super(id, texture, false);
        isBlockContainer = true;
    }

    @Override
    public String getTextureFile ()
    {
        return CommonProxy.BLOCK_PNG;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockColor()
    {
        return 16711680;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderColor(int par1)
    {
        return 16711680;
    }

    /*@SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return 0xFF0000;
    }*/

    @SideOnly(Side.CLIENT)
    @Override
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
        int data = world.getBlockMetadata(x, y, z);
        TileEntityButton te = (TileEntityButton)world.getBlockTileEntity(x, y, z);
        int colorIndex = (te != null) ? te.colorIndex : 0;

        /*if (te == null)
            System.out.println("Block Placed: ci=" + colorIndex + " NO TE FOUND");
        else
            System.out.println("Block Placed: ci=" + colorIndex + " colorIndex=" + te.colorIndex);*/

        if ((data & 8) != 0)
            return this.blockIndexInTexture + colorIndex;
        else
            return this.blockIndexInTexture + 16 + colorIndex;
    }

    @SideOnly(Side.CLIENT)
    public int getBlockTextureFromIndex(int colorIndex)
    {
        return this.blockIndexInTexture + colorIndex;
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);

        if (world.getBlockTileEntity(x, y, z) == null)
            world.setBlockTileEntity(x, y, z, this.createTileEntity(world, world.getBlockMetadata(x, y, z)));
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
        super.breakBlock(world, x, y, z, par5, par6);
        world.removeBlockTileEntity(x, y, z);
    }

    @Override
    public void onBlockEventReceived(World world, int x, int y, int z, int par5, int par6)
    {
        super.onBlockEventReceived(world, x, y, z, par5, par6);
        TileEntity te = world.getBlockTileEntity(x, y, z);

        if (te != null)
        {
            te.receiveClientEvent(par5, par6);
        }
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileEntityButton();
    }

    @Override
    public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for(int i = 0; i < count; i++)
        {
            int id = idDropped(metadata, world.rand, fortune);
            if (id > 0)
            {
                TileEntityButton te = (TileEntityButton)world.getBlockTileEntity(x, y, z);
                int damage = (te != null) ? te.colorIndex : 0;
                ret.add(new ItemStack(id, 1, damage));
            }
        }
        return ret;
    }

    @Override
    public int damageDropped(int data)
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int blockId, CreativeTabs creativeTabs, List blockList)
    {
        for (int i = 0; i < 16; ++i)
        {
            blockList.add(new ItemStack(blockId, 1, i));
        }
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int data)
    {
        TileEntityButton te = (TileEntityButton)world.getBlockTileEntity(x, y, z);
        if (te == null) {
            te = (TileEntityButton)this.createTileEntity(world, data);
            world.setBlockTileEntity(x, y, z, te);
        }

        te.colorIndex = (byte)data;

        TileEntityButton te2 = (TileEntityButton)world.getBlockTileEntity(x, y, z);


        if (te2 == null)
            System.out.println("Block Placed: data=" + data + " NO TE FOUND");
        else
            System.out.println("Block Placed: data=" + data + " colorIndex=" + te2.colorIndex);

        return super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, data);
    }

    public static int getBlockFromDye(int data)
    {
        return ~data & 15;
    }

    public static int getDyeFromBlock(int data)
    {
        return ~data & 15;
    }

    /*@Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
        int data = world.getBlockMetadata(x, y, z);
        this.updateBlockBoundsWithState(data);
    }

    private void updateBlockBoundsWithState(int data)
    {
        int direction = data & 7;
        boolean isPressed = (data & 8) > 0;
        float var4 = 0.075F;
        float var5 = 0.925F;
        float var6 = 0.4375F;
        float depth = 0.125F;

        if (isPressed)
        {
            depth = 0.0625F;
        }

        if (direction == 1)
        {
            this.setBlockBounds(0.0F, var4, 0.5F - var6, depth, var5, 0.5F + var6);
        }
        else if (direction == 2)
        {
            this.setBlockBounds(1.0F - depth, var4, 0.5F - var6, 1.0F, var5, 0.5F + var6);
        }
        else if (direction == 3)
        {
            this.setBlockBounds(0.5F - var6, var4, 0.0F, 0.5F + var6, var5, depth);
        }
        else if (direction == 4)
        {
            this.setBlockBounds(0.5F - var6, var4, 1.0F - depth, 0.5F + var6, var5, 1.0F);
        }
    }*/

    /*@Override
    public void setBlockBoundsForItemRender ()
    {
        float x = 0.4375F;
        float y = 0.4375F;
        float z = 0.125F;
        this.setBlockBounds(0.5F - x, 0.5F - y, 0.5F - z, 0.5F + x, 0.5F + y, 0.5F + z);
    }*/
}
