package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import com.jaquadro.minecraft.modularpots.world.gen.feature.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
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
    private Icon transpIcon;

    public LargePotPlantProxy (int id) {
        super(id, Material.plants);
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
    public void onNeighborBlockChange (World world, int x, int y, int z, int blockId) {
        if (!hasValidUnderBlock(world, x, y, z)) {
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
        if (block != Block.sapling)
            return false;

        TileEntityLargePot te = getAttachedPotEntity(world, x, y, z);
        int blockMeta = te.getFlowerPotData();

        world.setBlock(x, y, z, 0, 0, 4);

        WorldGenerator generator = null;
        switch (blockMeta) {
            case 0:
            case 2:
                generator = new WorldGenOakOrnTree(false, ModularPots.thinLog, blockMeta, Block.leaves, blockMeta);
                break;
            case 1:
                generator = new WorldGenPineOrnTree(false, ModularPots.thinLog, blockMeta, Block.leaves, blockMeta);
                break;
            case 3:
                generator = new WorldGenJungleOrnTree(false, ModularPots.thinLog, blockMeta, Block.leaves, blockMeta);
                break;
        }

        if (generator == null || !generator.generate(world, world.rand, x, y, z))
            world.setBlock(x, y, z, this.blockID, blockMeta, 4);

        return true;
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, int blockId, int data) {
        if (isUnderBlockPot(world, x, y, z)) {
            TileEntityLargePot tileEntity = getAttachedPotEntity(world, x, y, z);
            ItemStack item = new ItemStack(tileEntity.getFlowerPotItem(), 1, tileEntity.getFlowerPotData());
            world.spawnEntityInWorld(new EntityItem(world, x, y, z, item));
        }

        world.notifyBlockOfNeighborChange(x, y + 1, z, blockId);
        world.notifyBlockOfNeighborChange(x, y - 1, z, blockId);

        super.breakBlock(world, x, y, z, blockId, data);
    }

    @Override
    public Icon getIcon (int side, int data) {
        return transpIcon;
    }

    private boolean hasValidUnderBlock (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return false;

        Block underBlock = Block.blocksList[world.getBlockId(x, y - 1, z)];
        return (underBlock instanceof LargePotPlantProxy || underBlock instanceof LargePot);
    }

    private boolean isUnderBlockPot (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return false;

        Block underBlock = Block.blocksList[world.getBlockId(x, y - 1, z)];
        return underBlock instanceof LargePot;
    }

    private int getAttachedPotYIndex (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return 0;

        Block underBlock = Block.blocksList[world.getBlockId(x, --y, z)];
        while (y > 0 && underBlock instanceof LargePotPlantProxy)
            underBlock = Block.blocksList[world.getBlockId(x, --y, z)];

        return y;
    }

    private LargePot getAttachedPot (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return null;

        Block underBlock = Block.blocksList[world.getBlockId(x, --y, z)];
        while (y > 0 && underBlock instanceof LargePotPlantProxy)
            underBlock = Block.blocksList[world.getBlockId(x, --y, z)];

        if (!(underBlock instanceof LargePot))
            return null;

        return (LargePot) underBlock;
    }

    public TileEntityLargePot getAttachedPotEntity (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return null;

        Block underBlock = Block.blocksList[world.getBlockId(x, --y, z)];
        while (y > 0 && underBlock instanceof LargePotPlantProxy)
            underBlock = Block.blocksList[world.getBlockId(x, --y, z)];

        if (!(underBlock instanceof LargePot))
            return null;

        LargePot largePot = (LargePot) underBlock;
        return largePot.getTileEntity(world, x, y, z);
    }

    public Block getItemBlock (World world, int x, int y, int z) {
        TileEntityLargePot tile = getAttachedPotEntity(world, x, y, z);
        if (tile == null)
            return null;

        Item item = tile.getFlowerPotItem();
        if (item == null)
            return null;
        if (item instanceof IPlantable)
            return Block.blocksList[((IPlantable)item).getPlantID(world, x, y, z)];
        if (item instanceof ItemBlock)
            return Block.blocksList[((ItemBlock) item).getBlockID()];

        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IconRegister iconRegister) {
        transpIcon = iconRegister.registerIcon(ModularPots.MOD_ID + ":proxy_transp");
    }
}
