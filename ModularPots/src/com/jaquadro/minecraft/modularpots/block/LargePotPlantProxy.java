package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.item.ItemUsedSoilKit;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import com.jaquadro.minecraft.modularpots.world.gen.feature.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IPlantable;

import java.util.List;

public class LargePotPlantProxy extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon transpIcon;

    // Scratch State
    private int scratchX;
    private int scratchY;
    private int scratchZ;
    private boolean applyingBonemeal;

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
        return ClientProxy.transformPlantRenderID;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x, int y, int z) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null)
            return null;

        return block.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool (World world, int x, int y, int z) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null)
            return super.getSelectedBoundingBoxFromPool(world, x, y, z);

        return block.getSelectedBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null)
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        else
            block.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
    }

    @Override
    public MovingObjectPosition collisionRayTrace (World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null)
            return super.collisionRayTrace(world, x, y, z, startVec, endVec);

        return block.collisionRayTrace(world, x, y, z, startVec, endVec);
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block block) {
        if (!hasValidUnderBlock(world, x, y, z)) {
            //dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
        ItemStack itemStack = player.inventory.getCurrentItem();
        if (itemStack == null)
            return false;

        if (itemStack.getItem() == ModularPots.soilTestKitUsed)
            return applyTestKit(world, x, y, z, itemStack);

        return false;
    }

    public boolean applyTestKit (World world, int x, int y, int z, ItemStack testKit) {
        LargePot block = getAttachedPot(world, x, y, z);
        if (block == null)
            return false;

        y = getAttachedPotYIndex(world, x, y, z);

        return block.applyTestKit(world, x, y, z, testKit);
    }

    public boolean applyBonemeal (World world, int x, int y, int z) {
        Block block = getItemBlock(world, x, y, z);
        if (block != Blocks.sapling)
            return false;

        TileEntityLargePot te = getAttachedPotEntity(world, x, y, z);
        int blockMeta = te.getFlowerPotData();

        applyingBonemeal = true;
        scratchX = x;
        scratchY = y;
        scratchZ = z;

        world.setBlock(x, y, z, Blocks.air, 0, 4);

        WorldGenerator generator = null;
        switch (blockMeta) {
            case 0:
            case 2:
                generator = new WorldGenOakOrnTree(false, ModularPots.thinLog, blockMeta, Blocks.leaves, blockMeta);
                break;
            case 1:
                generator = new WorldGenPineOrnTree(false, ModularPots.thinLog, blockMeta, Blocks.leaves, blockMeta);
                break;
            case 3:
                generator = new WorldGenJungleOrnTree(false, ModularPots.thinLog, blockMeta, Blocks.leaves, blockMeta);
                break;
            case 4:
                generator = new WorldGenAcaciaOrnTree(false, ModularPots.thinLog, blockMeta, Blocks.leaves2, blockMeta & 3);
                break;
            case 5:
                generator = new WorldGenOakOrnTree(false, ModularPots.thinLog, blockMeta, Blocks.leaves2, blockMeta & 3);
                break;
        }

        if (generator == null || !generator.generate(world, world.rand, x, y, z))
            world.setBlock(x, y, z, this, blockMeta, 4);

        applyingBonemeal = false;

        return true;
    }

    @Override
    public void onBlockHarvested (World world, int x, int y, int z, int p_149681_5_, EntityPlayer player) {
        super.onBlockHarvested(world, x, y, z, p_149681_5_, player);

        if (player.capabilities.isCreativeMode) {
            if (isUnderBlockPot(world, x, y, z)) {
                TileEntityLargePot tileEntity = getAttachedPotEntity(world, x, y, z);
                if (tileEntity != null) {
                    tileEntity.setItem(null, 0);
                    tileEntity.markDirty();
                }
            }
        }
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        if (isUnderBlockPot(world, x, y, z) && !isApplyingBonemealTo(x, y, z)) {
            TileEntityLargePot tileEntity = getAttachedPotEntity(world, x, y, z);
            if (tileEntity != null && tileEntity.getFlowerPotItem() != null) {
                ItemStack item = new ItemStack(tileEntity.getFlowerPotItem(), 1, tileEntity.getFlowerPotData());
                dropBlockAsItem(world, x, y, z, item);
            }
        }

        world.notifyBlockOfNeighborChange(x, y + 1, z, block);
        world.notifyBlockOfNeighborChange(x, y - 1, z, block);

        super.breakBlock(world, x, y, z, block, data);
    }

    private boolean isApplyingBonemealTo (int x, int y, int z) {
        return applyingBonemeal
            && scratchX == x
            && scratchY == y
            && scratchZ == z;
    }

    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null)
            return super.getIcon(world, x, y, z, side);

        return block.getIcon(world, x, y, z, side);
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

    private int getAttachedPotYIndex (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return 0;

        Block underBlock = world.getBlock(x, --y, z);
        while (y > 0 && underBlock instanceof LargePotPlantProxy)
            underBlock = world.getBlock(x, --y, z);

        return y;
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

    public TileEntityLargePot getAttachedPotEntity (IBlockAccess world, int x, int y, int z) {
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
