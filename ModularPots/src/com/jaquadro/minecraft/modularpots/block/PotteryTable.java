package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityPotteryTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class PotteryTable extends BlockContainer
{
    private final Random rand = new Random();

    @SideOnly(Side.CLIENT)
    private IIcon iconSide;

    @SideOnly(Side.CLIENT)
    private IIcon iconTop;

    public PotteryTable () {
        super(Material.wood);
        setCreativeTab(ModularPots.tabModularPots);
    }

    @Override
    public void onBlockAdded (World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        setBlockDirection(world, x, y, z);
    }

    private void setBlockDirection (World world, int x, int y, int z) {
        if (!world.isRemote) {
            Block blockZNeg = world.getBlock(x, y, z - 1);
            Block blockZPos = world.getBlock(x, y, z + 1);
            Block blockXNeg = world.getBlock(x - 1, y, z);
            Block blockXPos = world.getBlock(x + 1, y, z);

            byte dir = 3;
            if (blockZNeg.func_149730_j() && !blockZPos.func_149730_j())
                dir = 3;
            if (blockZPos.func_149730_j() && !blockZNeg.func_149730_j())
                dir = 2;
            if (blockXNeg.func_149730_j() && !blockXPos.func_149730_j())
                dir = 5;
            if (blockXPos.func_149730_j() && !blockXNeg.func_149730_j())
                dir = 4;

            world.setBlockMetadataWithNotify(x, y, z, dir, 2);
        }
    }

    @Override
    public TileEntity createNewTileEntity (World world, int data) {
        return new TileEntityPotteryTable();
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
        player.openGui(ModularPots.instance, ModularPots.potteryTableGuiID, world, x, y, z);
        return true;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int metadata) {
        TileEntityPotteryTable te = (TileEntityPotteryTable) world.getTileEntity(x, y, z);
        if (te != null) {
            for (int i = 0; i < te.getSizeInventory(); i++) {
                ItemStack stack = te.getStackInSlot(i);
                if (stack != null) {
                    float ex = rand.nextFloat() * .8f + .1f;
                    float ey = rand.nextFloat() * .8f + .1f;
                    float ez = rand.nextFloat() * .8f + .1f;

                    EntityItem entity;
                    for (; stack.stackSize > 0; world.spawnEntityInWorld(entity)) {
                        int stackPartSize = rand.nextInt(21) + 10;
                        if (stackPartSize > stack.stackSize)
                            stackPartSize = stack.stackSize;

                        stack.stackSize -= stackPartSize;
                        entity = new EntityItem(world, x + ex, y + ey, z + ez, new ItemStack(stack.getItem(), stackPartSize, stack.getItemDamage()));

                        float motionUnit = .05f;
                        entity.motionX = rand.nextGaussian() * motionUnit;
                        entity.motionY = rand.nextGaussian() * motionUnit + .2f;
                        entity.motionZ = rand.nextGaussian() * motionUnit;

                        if (stack.hasTagCompound())
                            entity.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
                    }
                }
            }

            world.func_147453_f(x, y, z, block);
        }

        super.breakBlock(world, x, y, z, block, metadata);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int data) {
        if (side == 0)
            return Blocks.planks.getBlockTextureFromSide(side);
        if (side == 1)
            return iconTop;
        if (side == data)
            return blockIcon;
        return iconSide;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(ModularPots.MOD_ID + ":pottery_table_front");
        iconSide = iconRegister.registerIcon(ModularPots.MOD_ID + ":pottery_table_side");
        iconTop = iconRegister.registerIcon(ModularPots.MOD_ID + ":pottery_table_top");
    }
}
