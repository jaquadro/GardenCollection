package com.jaquadro.minecraft.extrabuttons.block;

import com.jaquadro.minecraft.extrabuttons.ExtraButtons;
import com.jaquadro.minecraft.extrabuttons.client.ClientProxy;
import com.jaquadro.minecraft.extrabuttons.tileentity.TileEntityDelayButton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class DelayButton extends BlockContainer
{
    private static final float PIXEL_UNIT = 0.0625f;

    @SideOnly(Side.CLIENT)
    private IIcon iconButton;

    @SideOnly(Side.CLIENT)
    private IIcon iconPanelBack;

    @SideOnly(Side.CLIENT)
    private IIcon[] iconPanelFront;

    @SideOnly(Side.CLIENT)
    private boolean renderPanel;

    public static TileEntityDelayButton defaultTileEntity;

    public DelayButton ()
    {
        super(Material.circuits);
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
        TileEntityDelayButton te = (TileEntityDelayButton) world.getTileEntity(x, y, z);
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
        TileEntityDelayButton te = (TileEntityDelayButton) blockAccess.getTileEntity(x, y, z);
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
        TileEntityDelayButton te = (TileEntityDelayButton) world.getTileEntity(x, y, z);
        if (te == null)
            return false;

        if (te.isDepressed())
            return true;

        if (player.isSneaking())
            cycleDelaySetting(te);
        else {
            activateButton(te);

            world.playSoundEffect((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, "random.click", 0.3F, 0.6F);
            this.updateNeighbors(world, x, y, z, te.getDirection());
        }

        world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));

        return true;
    }

    private void cycleDelaySetting (TileEntityDelayButton te)
    {
        if (te.isShowingDelay())
            te.setUpdateTime(te.getWorldObj().getTotalWorldTime() + tickRate(te.getWorldObj()));

        int cycle = te.getDelay() + 1;
        if (cycle > 3)
            cycle = 0;

        te.setDelay(cycle);
        te.setShowingDelay(true);
    }

    private void activateButton (TileEntityDelayButton te)
    {
        te.setIsDepressed(true);
        te.setState(te.getDelay());
        te.setIsLatched(true);
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) world.getTileEntity(x, y, z);
        if (te != null && te.isLatched())
            this.updateNeighbors(world, x, y, z, te.getDirection());

        super.breakBlock(world, x, y, z, block, data);
    }

    @Override
    public int isProvidingWeakPower (IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) blockAccess.getTileEntity(x, y, z);

        return (te != null && te.isLatched()) ? 15 : 0;
    }

    @Override
    public int isProvidingStrongPower (IBlockAccess blockAccess, int x, int y, int z, int side)
    {
        TileEntityDelayButton te = (TileEntityDelayButton) blockAccess.getTileEntity(x, y, z);
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
        TileEntityDelayButton te = (TileEntityDelayButton) world.getTileEntity(x, y, z);
        if (te == null)
            return;

        // If "update" is set, reschedule the tick
        if (te.getUpdateTime() > 0) {
            int tick = (int)(te.getUpdateTime() - world.getTotalWorldTime());
            te.setUpdateTime(0);

            if (tick > 0) {
                world.scheduleBlockUpdate(x, y, z, this, tick);
                world.markBlockForUpdate(x, y, z);
                return;
            }
        }

        if (te.isDepressed() || te.isLatched() || te.isShowingDelay()) {
            if (!world.isRemote) {
                if (te.isShowingDelay())
                    te.setShowingDelay(false);

                if (te.isLatched()) {
                    if (te.getState() > 0) {
                        te.setState(te.getState() - 1);
                        world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
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
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity baseTE = world.getTileEntity(x, y, z);
        if (baseTE != null && baseTE.getClass().isAssignableFrom(TileEntityDelayButton.class))
            return getIcon((TileEntityDelayButton)baseTE, side);
        else
            return getIcon(null, side);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int metadata)
    {
        return getIcon(defaultTileEntity, side);
    }

    @SideOnly(Side.CLIENT)
    private IIcon getIcon (TileEntityDelayButton te, int side)
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
            else {
                int level = te.isShowingDelay() ? te.getDelay() : te.getState();
                return iconPanelFront[Math.max(0, Math.min(3, level))];
            }
        }
        else
            return iconButton;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        iconButton = iconRegister.registerIcon(ExtraButtons.MOD_ID + ":delay_button_button");
        iconPanelBack = iconRegister.registerIcon(ExtraButtons.MOD_ID + ":delay_button_back");
        iconPanelFront = new IIcon[4];

        for (int i = 0; i < 4; i++) {
            iconPanelFront[i] = iconRegister.registerIcon(ExtraButtons.MOD_ID + ":delay_button_" + i);
        }
    }
}
