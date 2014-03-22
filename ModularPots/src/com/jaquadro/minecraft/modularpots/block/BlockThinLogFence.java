package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockThinLogFence extends Block
{
    public static final String[] subNames = new String[] { "oak", "spruce", "birch", "jungle", "acacia", "big_oak" };

    @SideOnly(Side.CLIENT)
    IIcon sideIcon;

    public BlockThinLogFence (String blockName) {
        super(Material.wood);

        setCreativeTab(ModularPots.tabModularPots);
        setHardness(1.5f);
        setResistance(5f);
        setLightOpacity(0);
        setStepSound(Block.soundTypeWood);
        setBlockName(blockName);

        setBlockBoundsForItemRender();
    }

    public float getMargin () {
        return 0.25f;
    }

    @Override
    public void setBlockBoundsForItemRender () {
        float margin = getMargin();
        setBlockBounds(margin, 0, margin, 1 - margin, 1, 1 - margin);
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        boolean connectedZNeg = canConnectFenceTo(world, x, y, z - 1);
        boolean connectedZPos = canConnectFenceTo(world, x, y, z + 1);
        boolean connectedXNeg = canConnectFenceTo(world, x - 1, y, z);
        boolean connectedXPos = canConnectFenceTo(world, x + 1, y, z);

        float margin = getMargin();
        float xs = margin;
        float xe = 1 - margin;
        float zs = margin;
        float ze = 1 - margin;

        if (connectedZNeg)
            zs = 0;
        if (connectedZPos)
            ze = 1;
        if (connectedZNeg || connectedZPos) {
            setBlockBounds(xs, 0, zs, xe, 1.5f, ze);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }

        zs = margin;
        ze = 1 - margin;

        if (connectedXNeg)
            xs = 0;
        if (connectedXPos)
            xe = 1;
        if (connectedXNeg || connectedXPos || (!connectedZNeg && !connectedZPos)) {
            setBlockBounds(xs, 0, zs, xe, 1.5f, ze);
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }

        if (connectedZNeg)
            zs = 0;
        if (connectedZPos)
            ze = 1;

        setBlockBounds(xs, 0, zs, xe, 1, ze);
    }

    @Override
    public void setBlockBoundsBasedOnState (IBlockAccess world, int x, int y, int z) {
        boolean connectedZNeg = canConnectFenceTo(world, x, y, z - 1);
        boolean connectedZPos = canConnectFenceTo(world, x, y, z + 1);
        boolean connectedXNeg = canConnectFenceTo(world, x - 1, y, z);
        boolean connectedXPos = canConnectFenceTo(world, x + 1, y, z);

        float margin = getMargin();
        float xs = margin;
        float xe = 1 - margin;
        float zs = margin;
        float ze = 1 - margin;

        if (connectedZNeg)
            zs = 0;
        if (connectedZPos)
            ze = 1;
        if (connectedXNeg)
            xs = 0;
        if (connectedXPos)
            xe = 1;

        setBlockBounds(xs, 0, zs, xe, 1, ze);
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
    public boolean getBlocksMovement (IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.thinLogFenceRenderID;
    }

    @Override
    public boolean canPlaceTorchOnTop (World world, int x, int y, int z) {
        return true;
    }

    public boolean canConnectFenceTo (IBlockAccess world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        if (block != this)
            return (block.getMaterial().isOpaque() && block.renderAsNormalBlock()) ? block.getMaterial() != Material.gourd : false;

        return true;
    }

    @Override
    public boolean shouldSideBeRendered (IBlockAccess world, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return getIconSource(meta).getIcon(side, meta % 4);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getSideIcon () {
        return sideIcon;
    }

    private Block getIconSource (int meta) {
        switch (meta / 4) {
            case 0:
                return Blocks.log;
            case 1:
                return Blocks.log2;
            default:
                return Blocks.log;
        }
    }

    @Override
    public void getSubBlocks (Item item, CreativeTabs creativeTabs, List blockList) {
        for (int i = 0; i < 6; i++)
            blockList.add(new ItemStack(item, 1, i));
    }

    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        sideIcon = iconRegister.registerIcon(ModularPots.MOD_ID + ":thinlog_fence_side");
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
        return world.isRemote ? true : ItemLead.func_150909_a(player, world, x, y, z);
    }
}
