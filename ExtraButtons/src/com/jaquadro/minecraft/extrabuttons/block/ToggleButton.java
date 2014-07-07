package com.jaquadro.minecraft.extrabuttons.block;

import com.jaquadro.minecraft.extrabuttons.ExtraButtons;
import com.jaquadro.minecraft.extrabuttons.tileentity.TileEntityButton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class ToggleButton extends BlockContainer
{
    private boolean isLit;
    private static boolean blockIsChanging;

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArrayOn;

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArrayOff;

    public ToggleButton (boolean isLit)
    {
        super(Material.circuits);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.isLit = isLit;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    public int tickRate (World world)
    {
        return 5;
    }

    @Override
    public boolean isOpaqueCube ()
    {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock ()
    {
        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide (World world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        return (dir == NORTH && world.isSideSolid(x, y, z + 1, NORTH)) ||
                (dir == SOUTH && world.isSideSolid(x, y, z - 1, SOUTH)) ||
                (dir == WEST && world.isSideSolid(x + 1, y, z, WEST)) ||
                (dir == EAST && world.isSideSolid(x - 1, y, z, EAST));
    }

    @Override
    public boolean canPlaceBlockAt (World world, int x, int y, int z)
    {
        return (world.isSideSolid(x - 1, y, z, EAST)) ||
                (world.isSideSolid(x + 1, y, z, WEST)) ||
                (world.isSideSolid(x, y, z - 1, SOUTH)) ||
                (world.isSideSolid(x, y, z + 1, NORTH));
    }

    @Override
    public boolean hasTileEntity (int data)
    {
        return true;
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block block)
    {
        TileEntityButton te = (TileEntityButton) world.getTileEntity(x, y, z);
        int dir = (te != null)
                ? te.getDirection() : 0;

        boolean invalid = false;

        if (!world.isSideSolid(x - 1, y, z, EAST) && dir == 1) {
            invalid = true;
        }

        if (!world.isSideSolid(x + 1, y, z, WEST) && dir == 2) {
            invalid = true;
        }

        if (!world.isSideSolid(x, y, z - 1, SOUTH) && dir == 3) {
            invalid = true;
        }

        if (!world.isSideSolid(x, y, z + 1, NORTH) && dir == 4) {
            invalid = true;
        }

        if (invalid) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess blockAccess, int x, int y, int z)
    {
        TileEntityButton te = (TileEntityButton) blockAccess.getTileEntity(x, y, z);
        if (te != null)
            this.setBlockBoundsByTileEntity(te);
    }

    private void setBlockBoundsByTileEntity (TileEntityButton te)
    {
        int dir = te.getDirection();
        boolean isLatched = te.isDepressed();

        float var4 = 0.375F;
        float var5 = 0.625F;
        float var6 = 0.1875F;
        float depth = 0.125F;

        if (isLatched) {
            depth = 0.0625F;
        }

        if (dir == 1) {
            this.setBlockBounds(0.0F, var4, 0.5F - var6, depth, var5, 0.5F + var6);
        }
        else if (dir == 2) {
            this.setBlockBounds(1.0F - depth, var4, 0.5F - var6, 1.0F, var5, 0.5F + var6);
        }
        else if (dir == 3) {
            this.setBlockBounds(0.5F - var6, var4, 0.0F, 0.5F + var6, var5, depth);
        }
        else if (dir == 4) {
            this.setBlockBounds(0.5F - var6, var4, 1.0F - depth, 0.5F + var6, var5, 1.0F);
        }
    }

    @Override
    public void onBlockAdded (World world, int x, int y, int z)
    {
        super.onBlockAdded(world, x, y, z);

        if (isLit) {
            world.notifyBlockOfNeighborChange(x, y - 1, z, this);
            world.notifyBlockOfNeighborChange(x, y + 1, z, this);
            world.notifyBlockOfNeighborChange(x - 1, y, z, this);
            world.notifyBlockOfNeighborChange(x + 1, y, z, this);
            world.notifyBlockOfNeighborChange(x, y, z - 1, this);
            world.notifyBlockOfNeighborChange(x, y, z + 1, this);
        }
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntityButton te = (TileEntityButton) world.getTileEntity(x, y, z);
        if (te == null)
            return false;

        if (te.isDepressed())
            return true;

        int dir = te.getDirection();

        te.setIsDepressed(true);
        te.setIsLatched(!te.isLatched());

        updateBlockState(te.isLatched(), world, x, y, z);

        world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.6F);
        this.updateNeighbors(world, x, y, z, dir);
        world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));

        return true;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data)
    {
        if (!blockIsChanging) {
            TileEntityButton te = (TileEntityButton) world.getTileEntity(x, y, z);
            if (te != null && te.isLatched())
                this.updateNeighbors(world, x, y, z, te.getDirection());

            if (isLit) {
                world.notifyBlockOfNeighborChange(x, y - 1, z, this);
                world.notifyBlockOfNeighborChange(x, y + 1, z, this);
                world.notifyBlockOfNeighborChange(x - 1, y, z, this);
                world.notifyBlockOfNeighborChange(x + 1, y, z, this);
                world.notifyBlockOfNeighborChange(x, y, z - 1, this);
                world.notifyBlockOfNeighborChange(x, y, z + 1, this);
            }
        }

        super.breakBlock(world, x, y, z, block, data);
    }

    private static void updateBlockState (boolean isLit, World world, int x, int y, int z)
    {
        int data = world.getBlockMetadata(x, y, z);
        TileEntity te = world.getTileEntity(x, y, z);
        blockIsChanging = true;

        if (isLit)
            world.setBlock(x, y, z, ExtraButtons.illuminatedButtonOn);
        else
            world.setBlock(x, y, z, ExtraButtons.illuminatedButtonOff);

        blockIsChanging = false;
        world.setBlockMetadataWithNotify(x, y, z, data, 2);

        if (te != null) {
            te.validate();
            world.setTileEntity(x, y, z, te);
        }
    }

    @Override
    public int isProvidingWeakPower (IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntityButton te = (TileEntityButton) blockAccess.getTileEntity(x, y, z);

        return (te != null && te.isLatched()) ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower (IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntityButton te = (TileEntityButton) blockAccess.getTileEntity(x, y, z);
        if (te == null || !te.isLatched())
            return 0;

        int dir = te.getDirection();
        return (dir == 5 && side == 1 ? 15 : (dir == 4 && side == 2 ? 15 : (dir == 3 && side == 3 ? 15 : (dir == 2 && side == 4 ? 15 : (dir == 1 && side == 5 ? 15 : 0)))));
    }

    @Override
    public boolean canProvidePower ()
    {
        return true;
    }

    @Override
    public void updateTick (World world, int x, int y, int z, Random rand)
    {
        TileEntityButton te = (TileEntityButton) world.getTileEntity(x, y, z);

        if (te != null && te.isDepressed()) {
            if (!world.isRemote) {
                te.setIsDepressed(false);

                world.markBlockForUpdate(x, y, z);
                world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.5F);
            }
        }
    }

    @Override
    public void setBlockBoundsForItemRender ()
    {
        float hx = 0.1875F;
        float hy = 0.125F;
        float hz = 0.125F;
        this.setBlockBounds(0.5F - hx, 0.5F - hy, 0.5F - hz, 0.5F + hx, 0.5F + hy, 0.5F + hz);
    }

    private void updateNeighbors (World world, int x, int y, int z, int dir)
    {
        world.notifyBlocksOfNeighborChange(x, y, z, this);

        if (dir == 1) {
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
        }
        else if (dir == 2) {
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
        }
        else if (dir == 3) {
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
        }
        else if (dir == 4) {
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
        }
        else {
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
        }
    }

    @Override
    public TileEntity createNewTileEntity (World world, int data)
    {
        return new TileEntityButton();
    }

    @Override
    public boolean isAssociatedBlock (Block block) {
        return block == ExtraButtons.illuminatedButtonOn || block == ExtraButtons.illuminatedButtonOff;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side)
    {
        int data = world.getBlockMetadata(x, y, z);
        TileEntity baseTE = world.getTileEntity(x, y, z);
        if (baseTE != null && baseTE.getClass().isAssignableFrom(TileEntityButton.class)) {
            TileEntityButton te = (TileEntityButton) baseTE;
            if (te.isLatched())
                return iconArrayOn[data];
        }

        return iconArrayOff[data];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int data)
    {
        return iconArrayOff[data];
    }

    @Override
    public Item getItemDropped (int metadata, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ExtraButtons.illuminatedButtonOff);
    }

    @Override
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for (int i = 0; i < count; i++) {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
                ret.add(new ItemStack(item, 1, metadata));
        }
        return ret;
    }

    @Override
    public int damageDropped (int data)
    {
        return data;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List blockList)
    {
        if (!isLit) {
            for (int i = 0; i < 16; ++i)
                blockList.add(new ItemStack(item, 1, i));
        }
    }

    public static int getBlockFromDye (int data)
    {
        return ~data & 15;
    }

    public static int getDyeFromBlock (int data)
    {
        return ~data & 15;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        iconArrayOff = new IIcon[16];
        iconArrayOn = new IIcon[16];

        for (int i = 0; i < 16; i++) {
            iconArrayOff[i] = iconRegister.registerIcon(ExtraButtons.MOD_ID + ":illum_button_off_" + i);
            iconArrayOn[i] = iconRegister.registerIcon(ExtraButtons.MOD_ID + ":illum_button_on_" + i);
        }
    }
}
