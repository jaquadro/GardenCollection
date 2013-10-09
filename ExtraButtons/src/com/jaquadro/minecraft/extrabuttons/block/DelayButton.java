package com.jaquadro.minecraft.extrabuttons.block;

import com.jaquadro.minecraft.extrabuttons.ExtraButtons;
import com.jaquadro.minecraft.extrabuttons.client.ClientProxy;
import com.jaquadro.minecraft.extrabuttons.tileentity.TileEntityDelayButton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.Random;

import static net.minecraftforge.common.ForgeDirection.*;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;

public class DelayButton extends BlockContainer
{
    private static final float PIXEL_UNIT = 0.0625f;

    @SideOnly(Side.CLIENT)
    private Icon iconButton;

    @SideOnly(Side.CLIENT)
    private Icon iconPanelBack;

    @SideOnly(Side.CLIENT)
    private Icon[] iconPanelFront;

    @SideOnly(Side.CLIENT)
    private boolean renderPanel;

    public static TileEntityDelayButton defaultTileEntity;

    public DelayButton (int id)
    {
        super(id, Material.circuits);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);

        defaultTileEntity = new TileEntityDelayButton();
        defaultTileEntity.setDirection(3);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World par1World, int par2, int par3, int par4)
    {
        return null;
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
    public boolean renderAsNormalBlock ()
    {
        return false;
    }

    @Override
    public int getRenderType ()
    {
        return ClientProxy.delayButtonRenderID;
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        return true;
    }

    @Override
    public boolean canPlaceBlockOnSide (World world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);
        return (dir == NORTH && world.isBlockSolidOnSide(x, y, z + 1, NORTH)) ||
                (dir == SOUTH && world.isBlockSolidOnSide(x, y, z - 1, SOUTH)) ||
                (dir == WEST && world.isBlockSolidOnSide(x + 1, y, z, WEST)) ||
                (dir == EAST && world.isBlockSolidOnSide(x - 1, y, z, EAST));
    }

    @Override
    public boolean canPlaceBlockAt (World world, int x, int y, int z)
    {
        return (world.isBlockSolidOnSide(x - 1, y, z, EAST)) ||
                (world.isBlockSolidOnSide(x + 1, y, z, WEST)) ||
                (world.isBlockSolidOnSide(x, y, z - 1, SOUTH)) ||
                (world.isBlockSolidOnSide(x, y, z + 1, NORTH));
    }

    @Override
    public boolean hasTileEntity (int data)
    {
        return true;
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, int neighborId)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) world.getBlockTileEntity(x, y, z);
        int dir = (te != null)
                ? te.getDirection() : 0;

        boolean invalid = false;

        if (!world.isBlockSolidOnSide(x - 1, y, z, EAST) && dir == 1) {
            invalid = true;
        }

        if (!world.isBlockSolidOnSide(x + 1, y, z, WEST) && dir == 2) {
            invalid = true;
        }

        if (!world.isBlockSolidOnSide(x, y, z - 1, SOUTH) && dir == 3) {
            invalid = true;
        }

        if (!world.isBlockSolidOnSide(x, y, z + 1, NORTH) && dir == 4) {
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
        TileEntityDelayButton te = (TileEntityDelayButton) blockAccess.getBlockTileEntity(x, y, z);
        if (te != null) {
            this.setBlockBoundsByTileEntity(te, 8, 8, te.isDepressed() ? 1 : 2, 0, 1, 0);
        }
    }

    private void setBlockBoundsByTileEntity (TileEntityDelayButton te, float width, float height, float depth, int offsetX, int offsetY, int offsetZ)
    {
        int dir = te.getDirection();

        float heightHalf = height / 2f * PIXEL_UNIT;
        float widthHalf = width / 2f * PIXEL_UNIT;
        float depthMin = offsetZ * PIXEL_UNIT;
        float depthMax = depthMin + depth * PIXEL_UNIT;

        float offsetYF = offsetY * PIXEL_UNIT;

        if (dir == 1) {
            this.setBlockBounds(offsetX + depthMin, offsetYF + 0.5F - heightHalf, 0.5F - widthHalf, offsetX + depthMax, offsetYF + 0.5F + heightHalf, 0.5F + widthHalf);
        }
        else if (dir == 2) {
            this.setBlockBounds(offsetX + 1.0F - depthMax, offsetYF + 0.5F - heightHalf, 0.5F - widthHalf, offsetX + 1.0F - depthMin, offsetYF + 0.5F + heightHalf, 0.5F + widthHalf);
        }
        else if (dir == 3) {
            this.setBlockBounds(offsetX + 0.5F - widthHalf, offsetYF + 0.5F - heightHalf, depthMin, offsetX + 0.5F + widthHalf, offsetYF + 0.5F + heightHalf, depthMax);
        }
        else if (dir == 4) {
            this.setBlockBounds(offsetX + 0.5F - widthHalf, offsetYF + 0.5F - heightHalf, offsetX + 1.0F - depthMax, 0.5F + widthHalf, offsetYF + 0.5F + heightHalf, 1.0F - depthMin);
        }
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) world.getBlockTileEntity(x, y, z);
        if (te == null)
            return false;

        if (te.isDepressed())
            return true;

        int dir = te.getDirection();

        te.setIsDepressed(true);
        te.setState(te.getDelay());
        te.setIsLatched(true);

        world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.6F);
        this.updateNeighbors(world, x, y, z, dir);
        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));

        return true;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, int side, int data)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) world.getBlockTileEntity(x, y, z);
        if (te != null && te.isLatched())
            this.updateNeighbors(world, x, y, z, te.getDirection());

        super.breakBlock(world, x, y, z, side, data);
    }

    @Override
    public int isProvidingWeakPower (IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) blockAccess.getBlockTileEntity(x, y, z);

        return (te != null && te.isLatched()) ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower (IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) blockAccess.getBlockTileEntity(x, y, z);
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
        TileEntityDelayButton te = (TileEntityDelayButton) world.getBlockTileEntity(x, y, z);

        if (te != null && (te.isDepressed() || te.isLatched())) {
            if (!world.isRemote) {
                if (te.isLatched()) {
                    if (te.getState() > 0) {
                        te.setState(te.getState() - 1);
                        world.scheduleBlockUpdate(x, y, z, this.blockID, this.tickRate(world));
                    }
                    else {
                        te.setIsLatched(false);
                        te.setIsDepressed(false);
                        world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.5F);
                        this.updateNeighbors(world, x, y, z, te.getDirection());
                    }
                }

                world.markBlockForUpdate(x, y, z);
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
        world.notifyBlocksOfNeighborChange(x, y, z, this.blockID);

        if (dir == 1) {
            world.notifyBlocksOfNeighborChange(x - 1, y, z, this.blockID);
        }
        else if (dir == 2) {
            world.notifyBlocksOfNeighborChange(x + 1, y, z, this.blockID);
        }
        else if (dir == 3) {
            world.notifyBlocksOfNeighborChange(x, y, z - 1, this.blockID);
        }
        else if (dir == 4) {
            world.notifyBlocksOfNeighborChange(x, y, z + 1, this.blockID);
        }
        else {
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this.blockID);
        }
    }

    @Override
    public TileEntity createNewTileEntity (World world)
    {
        return new TileEntityDelayButton();
    }

    @SideOnly(Side.CLIENT)
    public void setBlockForPanelRender (TileEntity tileEntity)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) tileEntity;
        if (te != null)
            this.setBlockBoundsByTileEntity(te, 8, 8, 1, 0, 1, 0);

        renderPanel = true;
    }

    @SideOnly(Side.CLIENT)
    public void setBlockForButtonRender (TileEntity tileEntity)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) tileEntity;
        if (te != null)
            this.setBlockBoundsByTileEntity(te, 6, 4, te.isDepressed() ? 0.1f : 1, 0, 0, 1);

        renderPanel = false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getBlockTexture (IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity baseTE = world.getBlockTileEntity(x, y, z);
        if (baseTE != null && baseTE.getClass().isAssignableFrom(TileEntityDelayButton.class))
            return getIcon((TileEntityDelayButton)baseTE, side);
        else
            return getIcon(null, side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIcon(int side, int metadata)
    {
        return getIcon(defaultTileEntity, side);
    }

    @SideOnly(Side.CLIENT)
    private Icon getIcon (TileEntityDelayButton te, int side)
    {
        if (renderPanel) {
            if (te == null || side == 0 || side == 1)
                return iconPanelBack;

            boolean renderBack = false;
            switch (te.getDirection()) {
                case 1:
                    renderBack = (side == 4);
                    break;
                case 2:
                    renderBack = (side == 5);
                    break;
                case 3:
                    renderBack = (side == 2);
                    break;
                case 4:
                    renderBack = (side == 3);
                    break;
            }

            if (renderBack)
                return iconPanelBack;
            else
                return iconPanelFront[Math.max(0, Math.min(3, te.getState()))];
        }
        else
            return iconButton;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IconRegister iconRegister)
    {
        iconButton = iconRegister.registerIcon(ExtraButtons.MOD_ID + ":delay_button_button");
        iconPanelBack = iconRegister.registerIcon(ExtraButtons.MOD_ID + ":delay_button_back");
        iconPanelFront = new Icon[4];

        for (int i = 0; i < 4; i++) {
            iconPanelFront[i] = iconRegister.registerIcon(ExtraButtons.MOD_ID + ":delay_button_" + i);
        }
    }
}
