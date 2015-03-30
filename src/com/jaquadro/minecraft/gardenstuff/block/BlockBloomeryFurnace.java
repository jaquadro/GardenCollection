package com.jaquadro.minecraft.gardenstuff.block;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.block.tile.TileEntityBloomeryFurnace;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import com.jaquadro.minecraft.gardenstuff.core.handlers.GuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBloomeryFurnace extends BlockContainer
{
    private Random random = new Random();

    @SideOnly(Side.CLIENT)
    private IIcon iconSide;
    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;
    @SideOnly(Side.CLIENT)
    private IIcon iconTop;
    @SideOnly(Side.CLIENT)
    private IIcon iconFrontLit;

    public BlockBloomeryFurnace (String name) {
        super(Material.rock);

        setHardness(3.5f);
        setStepSound(Block.soundTypePiston);
        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setBlockName(name);
    }

    @Override
    public Item getItemDropped (int meta, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.bloomeryFurnace);
    }

    @Override
    public void onBlockAdded (World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        if (!world.isRemote) {
            Block neighborZN = world.getBlock(x, y, z - 1);
            Block neighborZP = world.getBlock(x, y, z + 1);
            Block neighborXN = world.getBlock(x - 1, y, z);
            Block neighborXP = world.getBlock(x + 1, y, z);
            byte direction = 3;

            if (neighborZP.func_149730_j() && !neighborZN.func_149730_j())
                direction = 3;
            if (neighborZN.func_149730_j() && !neighborZP.func_149730_j())
                direction = 2;
            if (neighborXP.func_149730_j() && !neighborXN.func_149730_j())
                direction = 5;
            if (neighborXN.func_149730_j() && !neighborXP.func_149730_j())
                direction = 4;

            world.setBlockMetadataWithNotify(x, y, z, direction, 2);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta) {
        if (side == 0)
            return iconBottom;
        if (side == 1)
            return iconTop;

        boolean lit = (meta & 0x8) > 0;
        int metaDir = (meta & 0x7);

        if (metaDir == 0)
            metaDir = 3;

        if (metaDir == side)
            return (lit) ? iconFrontLit : blockIcon;

        return iconSide;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons (IIconRegister register) {
        blockIcon = register.registerIcon(GardenStuff.MOD_ID + ":bloomery_furnace_front_off");
        iconFrontLit = register.registerIcon(GardenStuff.MOD_ID + ":bloomery_furnace_front_on");
        iconSide = register.registerIcon(GardenStuff.MOD_ID + ":bloomery_furnace_side");
        iconBottom = register.registerIcon(GardenStuff.MOD_ID + ":bloomery_furnace_bottom");
        iconTop = register.registerIcon(GardenStuff.MOD_ID + ":bloomery_furnace_top");
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        TileEntityBloomeryFurnace tile = (TileEntityBloomeryFurnace)world.getTileEntity(x, y, z);
        if (tile != null)
            player.openGui(GardenStuff.instance, GuiHandler.bloomeryFurnaceGuiID, world, x, y, z);

        return true;
    }

    public static void updateFurnaceBlockState (World world, int x, int y, int z, boolean lit) {
        int meta = world.getBlockMetadata(x, y, z);
        int litFlag = (lit) ? 8 : 0;

        world.setBlockMetadataWithNotify(x, y, z, (meta & 0x7) | litFlag, 3);
    }

    @Override
    public TileEntity createNewTileEntity (World world, int meta) {
        return new TileEntityBloomeryFurnace();
    }

    @Override
    public int getLightValue (IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if ((meta & 0x8) == 0)
            return 0;
        else
            return 14;
    }

    @Override
    public void onBlockPlacedBy (World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        int dir = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        switch (dir) {
            case 0:
                world.setBlockMetadataWithNotify(x, y, z, 2, 2);
                break;
            case 1:
                world.setBlockMetadataWithNotify(x, y, z, 5, 2);
                break;
            case 2:
                world.setBlockMetadataWithNotify(x, y, z, 3, 2);
                break;
            case 3:
                world.setBlockMetadataWithNotify(x, y, z, 4, 2);
                break;
        }

        if (stack.hasDisplayName()) {
            TileEntityBloomeryFurnace tile = (TileEntityBloomeryFurnace)world.getTileEntity(x, y, z);
            if (tile != null)
                tile.setCustomName(stack.getDisplayName());
        }
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int side) {
        TileEntityBloomeryFurnace tile = (TileEntityBloomeryFurnace)world.getTileEntity(x, y, z);
        if (tile != null) {
            for (int i = 0, n = tile.getSizeInventory(); i < n; i++) {
                ItemStack stack = tile.getStackInSlot(i);
                if (stack == null)
                    continue;

                float fx = random.nextFloat() * .8f + .1f;
                float fy = random.nextFloat() * .8f + .1f;
                float fz = random.nextFloat() * .8f + .1f;

                while (stack.stackSize > 0) {
                    int amount = random.nextInt(21) + 10;
                    if (amount > stack.stackSize)
                        amount = stack.stackSize;

                    stack.stackSize -= amount;
                    EntityItem entity = new EntityItem(world, x + fx, y + fy, z + fz, new ItemStack(stack.getItem(), amount, stack.getItemDamage()));

                    if (stack.hasTagCompound())
                        entity.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());

                    entity.motionX = random.nextGaussian() * .05f;
                    entity.motionY = random.nextGaussian() * .05f + .2f;
                    entity.motionZ = random.nextGaussian() * .05f;

                    world.spawnEntityInWorld(entity);
                }
            }

            world.func_147453_f(x, y, z, block);
        }

        super.breakBlock(world, x, y, z, block, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick (World world, int x, int y, int z, Random rand) {
        int meta = world.getBlockMetadata(x, y, z);
        if ((meta & 0x8) == 0)
            return;

        int dir = meta & 0x7;
        float fx = x + .5f;
        float fy = y + rand.nextFloat() * 6f / 16f;
        float fz = z + .5f;
        float depth = .52f;
        float adjust = rand.nextFloat() * .6f - .3f;

        switch (dir) {
            case 4:
                world.spawnParticle("smoke", fx - depth, fy, fz + adjust, 0, 0, 0);
                world.spawnParticle("flame", fx - depth, fy, fz + adjust, 0, 0, 0);
                break;
            case 5:
                world.spawnParticle("smoke", fx + depth, fy, fz + adjust, 0, 0, 0);
                world.spawnParticle("flame", fx + depth, fy, fz + adjust, 0, 0, 0);
                break;
            case 2:
                world.spawnParticle("smoke", fx + adjust, fy, fz - depth, 0, 0, 0);
                world.spawnParticle("flame", fx + adjust, fy, fz - depth, 0, 0, 0);
                break;
            case 3:
                world.spawnParticle("smoke", fx + adjust, fy, fz + depth, 0, 0, 0);
                world.spawnParticle("flame", fx + adjust, fy, fz + depth, 0, 0, 0);
                break;
        }

        if (!world.getBlock(x, y + 1, z).isOpaqueCube()) {
            world.spawnParticle("smoke", fx, fy + .5f, fz, 0, 0, 0);
            world.spawnParticle("smoke", fx, fy + .5f, fz, 0, 0, 0);
            world.spawnParticle("smoke", fx, fy + .5f, fz, 0, 0, 0);
        }
    }

    @Override
    public boolean hasComparatorInputOverride () {
        return true;
    }

    @Override
    public int getComparatorInputOverride (World world, int x, int y, int z, int side) {
        return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(x, y, z));
    }

    @Override
    public Item getItem (World world, int x, int y, int z) {
        return Item.getItemFromBlock(ModBlocks.bloomeryFurnace);
    }
}
