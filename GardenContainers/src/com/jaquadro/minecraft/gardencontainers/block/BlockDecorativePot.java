package com.jaquadro.minecraft.gardencontainers.block;

import com.jaquadro.minecraft.gardencontainers.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenSingle;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.List;

public class BlockDecorativePot extends BlockGarden
{
    public BlockDecorativePot (String blockName) {
        super(blockName, Material.rock);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(.5f);
        setStepSound(Block.soundTypeStone);
    }

    @Override
    public ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z) {
        TileEntityGarden te = getTileEntity(world, x, y, z);
        return (te != null) ? te.getSubstrate() : null;
    }

    @Override
    protected boolean isValidSubstrate (ItemStack itemStack) {
        if (Block.getBlockFromItem(itemStack.getItem()) == Blocks.netherrack)
            return true;

        return super.isValidSubstrate(itemStack);
    }

    @Override
    protected int getSlot (World world, int x, int y, int z, int side, EntityPlayer player, float hitX, float hitY, float hitZ, IPlantable plant) {
        return TileEntityGardenSingle.SLOT_CENTER;
    }

    @Override
    public float getPlantOffsetY (IBlockAccess world, int x, int y, int z, int slot) {
        return -.0625f;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.decorativePotRenderID;
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
    public boolean shouldSideBeRendered (IBlockAccess blockAccess, int x, int y, int z, int side) {
        return true;
    }

    @Override
    public TileEntityGardenSingle createNewTileEntity (World var1, int var2) {
        return new TileEntityGardenSingle();
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        setBlockBounds(0, .5f, 0, 1, 1, 1);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);

        setBlockBounds(.1875f, 0, .1875f, 1f - .1875f, .5f, 1f - .1875f);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);

        setBlockBounds(0, 0, 0, 1, 1, 1);
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.inventory.getCurrentItem();
        if (side == 1 && itemStack != null && itemStack.getItem() == Items.flint_and_steel) {
            ItemStack substrate = getGardenSubstrate(world, x, y, z);
            if (substrate != null && substrate.getItem() == Item.getItemFromBlock(Blocks.netherrack)) {
                if (world.isAirBlock(x, y + 1, z)) {
                    world.playSoundEffect(x + .5, y + .5, z + .5, "fire.ignite", 1, world.rand.nextFloat() * .4f + .8f);
                    world.setBlock(x, y + 1, z, ModBlocks.smallFire);

                    world.notifyBlocksOfNeighborChange(x, y, z, this);
                    world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
                }

                itemStack.damageItem(1, player);
                return true;
            }
        }

        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        if (isSconceLit(world, x, y, z)) {
            world.notifyBlocksOfNeighborChange(x, y, z, this);
            world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
        }

        super.breakBlock(world, x, y, z, block, data);
    }

    @Override
    public int isProvidingStrongPower (IBlockAccess world, int x, int y, int z, int side) {
        return (side == 1 && isSconceLit(world, x, y, z)) ? 15 : 0;
    }

    @Override
    public int isProvidingWeakPower (IBlockAccess world, int x, int y, int z, int side) {
        return isSconceLit(world, x, y, z) ? 15 : 0;
    }

    @Override
    public boolean canProvidePower () {
        return true;
    }

    private boolean isSconceLit (IBlockAccess world, int x, int y, int z) {
        ItemStack substrate = getGardenSubstrate(world, x, y, z);
        if (substrate != null && substrate.getItem() == Item.getItemFromBlock(Blocks.netherrack))
            return world.getBlock(x, y + 1, z) == ModBlocks.smallFire;

        return false;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return Blocks.quartz_block.getIcon(side, 2);
    }

    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side) {
        return Blocks.quartz_block.getIcon(world, x, y, z, side);
    }
}
