package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class LargePotPlantProxy extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon transpIcon;

    public LargePotPlantProxy () {
        super(Material.plants);
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
    public int getRenderType () {
        return ClientProxy.transformPlantRendererID;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x, int y, int z) {
        return null;
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block block) {
        if (!hasValidUnderBlock(world, x, y, z)) {
            //dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        if (isUnderBlockPot(world, x, y, z)) {
            TileEntityLargePot tileEntity = getAttachedPotEntity(world, x, y, z);
            ItemStack item = new ItemStack(tileEntity.getFlowerPotItem(), 1, tileEntity.getFlowerPotData());
            dropBlockAsItem(world, x, y, z, item);
        }

        world.notifyBlockOfNeighborChange(x, y + 1, z, block);
        world.notifyBlockOfNeighborChange(x, y - 1, z, block);

        super.breakBlock(world, x, y, z, block, data);
    }

    @Override
    public IIcon getIcon (int side, int data) {
        return transpIcon;
    }

    private boolean hasValidUnderBlock (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return false;

        Block underBlock = world.getBlock(x, y - 1, z);
        return (underBlock instanceof LargePotPlantProxy || underBlock instanceof LargePot);
    }

    private boolean isUnderBlockPot (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return false;

        Block underBlock = world.getBlock(x, y - 1, z);
        return underBlock instanceof LargePot;
    }

    private LargePot getAttachedPot (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return null;

        Block underBlock = world.getBlock(x, --y, z);
        while (y > 0 && underBlock instanceof LargePotPlantProxy)
            underBlock = world.getBlock(x, --y, z);

        if (!(underBlock instanceof LargePot))
            return null;

        return (LargePot) underBlock;
    }

    private TileEntityLargePot getAttachedPotEntity (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return null;

        Block underBlock = world.getBlock(x, --y, z);
        while (y > 0 && underBlock instanceof LargePotPlantProxy)
            underBlock = world.getBlock(x, --y, z);

        if (!(underBlock instanceof LargePot))
            return null;

        LargePot largePot = (LargePot) underBlock;
        return largePot.getTileEntity(world, x, y, z);
    }

    public Block getItemBlock (IBlockAccess world, int x, int y, int z) {
        TileEntityLargePot tile = getAttachedPotEntity(world, x, y, z);
        if (tile == null)
            return null;

        Item item = tile.getFlowerPotItem();
        if (item == null)
            return null;
        if (item instanceof IPlantable)
            return ((IPlantable)item).getPlant(world, x, y, z);
        if (item instanceof ItemBlock)
            return Block.getBlockFromItem(item);

        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        transpIcon = iconRegister.registerIcon(ModularPots.MOD_ID + ":proxy_transp");
    }
}
