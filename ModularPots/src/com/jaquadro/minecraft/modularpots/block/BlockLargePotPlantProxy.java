package com.jaquadro.minecraft.modularpots.block;

import com.jaquadro.minecraft.modularpots.core.ModItems;
import com.jaquadro.minecraft.modularpots.ModularPots;
import com.jaquadro.minecraft.modularpots.addon.PlantHandlerRegistry;
import com.jaquadro.minecraft.modularpots.block.support.SaplingRegistry;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
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

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

public class BlockLargePotPlantProxy extends Block
{
    @SideOnly(Side.CLIENT)
    private IIcon transpIcon;

    // Scratch State
    private int scratchX;
    private int scratchY;
    private int scratchZ;
    private boolean applyingBonemeal;

    // Reentrant Flags
    private int reeLightValue;

    public BlockLargePotPlantProxy (String blockName) {
        super(Material.plants);

        setHardness(0);
        setLightOpacity(0);
        setBlockName(blockName);
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

        try {
            return block.getCollisionBoundingBoxFromPool(world, x, y, z);
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through getCollisionBoundingBoxFromPool(): " + e.getMessage());
            return null;
        }
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool (World world, int x, int y, int z) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null)
            return super.getSelectedBoundingBoxFromPool(world, x, y, z);

        try {
            return block.getSelectedBoundingBoxFromPool(world, x, y, z);
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through getSelectedBoundingBoxFromPool(): " + e.getMessage());
            return super.getSelectedBoundingBoxFromPool(world, x, y, z);
        }
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null) {
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
            return;
        }

        try {
            block.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through addCollisionBoxesToList(): " + e.getMessage());
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace (World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null)
            return super.collisionRayTrace(world, x, y, z, startVec, endVec);

        try {
            return block.collisionRayTrace(world, x, y, z, startVec, endVec);
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through collisionRayTrace(): " + e.getMessage());
            return super.collisionRayTrace(world, x, y, z, startVec, endVec);
        }
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block block) {
        if (!hasValidUnderBlock(world, x, y, z)) {
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
        ItemStack itemStack = player.inventory.getCurrentItem();
        if (itemStack != null) {
            if (itemStack.getItem() == ModItems.usedSoilTestKit)
                return applyTestKit(world, x, y, z, itemStack);
        }

        Block block = getItemBlock(world, x, y, z);
        if (block == null)
            return false;

        try {
            return block.onBlockActivated(world, x, y, z, player, side, vx, vy, vz);
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through onBlockActivated(): " + e.getMessage());
        }

        return false;
    }

    public boolean applyTestKit (World world, int x, int y, int z, ItemStack testKit) {
        BlockLargePot block = getAttachedPot(world, x, y, z);
        if (block == null)
            return false;

        y = getAttachedPotYIndex(world, x, y, z);

        return block.applyTestKit(world, x, y, z, testKit);
    }

    public boolean applyBonemeal (World world, int x, int y, int z) {
        applyingBonemeal = true;

        if (PlantHandlerRegistry.applyBonemeal(world, x, y, z)) {
            applyingBonemeal = false;
            return true;
        }

        Block block = getItemBlock(world, x, y, z);
        TileEntityLargePot te = getAttachedPotEntity(world, x, y, z);
        int blockMeta = te.getFlowerPotData();

        WorldGenerator generator = SaplingRegistry.getGenerator(block, blockMeta);
        if (generator == null) {
            applyingBonemeal = false;
            return false;
        }

        world.setBlock(x, y, z, Blocks.air, 0, 4);

        if (generator == null || !generator.generate(world, world.rand, x, y, z))
            world.setBlock(x, y, z, this, blockMeta, 4);

        applyingBonemeal = false;

        return true;
    }

    @Override
    public void onBlockHarvested (World world, int x, int y, int z, int p_149681_5_, EntityPlayer player) {
        super.onBlockHarvested(world, x, y, z, p_149681_5_, player);

        if (player.capabilities.isCreativeMode) {
            TileEntityLargePot tileEntity = getAttachedPotEntity(world, x, y, z);
            if (tileEntity != null) {
                tileEntity.setItem(null, 0);
                tileEntity.markDirty();
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

        if (!isApplyingBonemealTo(x, y, z)) {
            if (world.getBlock(x, y - 1, z) == this)
                world.setBlockToAir(x, y - 1, z);
        }

        super.breakBlock(world, x, y, z, block, data);
    }

    @Override
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        return drops;
    }

    private boolean isApplyingBonemealTo (int x, int y, int z) {
        return applyingBonemeal;
    }

    @Override
    public int getLightValue (IBlockAccess world, int x, int y, int z) {
        if (reeLightValue > 0)
            return getLightValue();

        reeLightValue++;

        int value = 0;
        Block block = getItemBlock(world, x, y, z);

        if (block == null)
            value = super.getLightValue(world, x, y, z);
        else {
            try {
                value = block.getLightValue(world, x, y, z);
            }
            catch (Exception e) {
                FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through getLightValue(): " + e.getMessage());
                value = block.getLightValue(world, x, y, z);
            }
        }

        reeLightValue--;
        return value;
    }

    @Override
    public void onEntityCollidedWithBlock (World world, int x, int y, int z, Entity entity) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null) {
            super.onEntityCollidedWithBlock(world, x, y, z, entity);
            return;
        }

        try {
            block.onEntityCollidedWithBlock(world, x, y, z, entity);
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through onEntityCollidedWithBlock(): " + e.getMessage());
            super.onEntityCollidedWithBlock(world, x, y, z, entity);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier (IBlockAccess blockAccess, int x, int y, int z) {
        Block block = getItemBlock(blockAccess, x, y, z);
        if (block == null)
            return super.colorMultiplier(blockAccess, x, y, z);

        try {
            return block.colorMultiplier(blockAccess, x, y, z);
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through colorMultiplier(): " + e.getMessage());
            return super.colorMultiplier(blockAccess, x, y, z);
        }
    }

    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side) {
        Block block = getItemBlock(world, x, y, z);
        if (block == null)
            return super.getIcon(world, x, y, z, side);

        try {
            return block.getIcon(world, x, y, z, side);
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through getIcon(): " + e.getMessage());
            return super.getIcon(world, x, y, z, side);
        }
    }

    @Override
    public IIcon getIcon (int side, int data) {
        return transpIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects (World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        TileEntityLargePot te = getAttachedPotEntity(world, x, y, z);
        if (te == null || te.getFlowerPotItem() == null)
            return true;

        Block proxy = Block.getBlockFromItem(te.getFlowerPotItem());
        if (proxy == null || proxy == Blocks.air)
            return true;

        try {
            byte count = 4;
            for (int ix = 0; ix < count; ++ix)
            {
                for (int iy = 0; iy < count; ++iy)
                {
                    for (int iz = 0; iz < count; ++iz)
                    {
                        double xOff = (double)x + ((double)ix + 0.5D) / (double)count;
                        double yOff = (double)y + ((double)iy + 0.5D) / (double)count;
                        double zOff = (double)z + ((double)iz + 0.5D) / (double)count;

                        EntityDiggingFX fx = new EntityDiggingFX(world, xOff, yOff, zOff, xOff - (double) x - 0.5D, yOff - (double) y - 0.5D, zOff - (double) z - 0.5D, this, meta);
                        fx.setParticleIcon(proxy.getIcon(world.rand.nextInt(6), te.getFlowerPotData()));

                        effectRenderer.addEffect(fx.applyColourMultiplier(x, y, z));
                    }
                }
            }
        }
        catch (Exception e) {
            FMLLog.log(ModularPots.MOD_ID, Level.WARN, "Exception passing through addDestroyEffects(): " + e.getMessage());
        }

        return true;
    }

    private boolean hasValidUnderBlock (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return false;

        Block underBlock = world.getBlock(x, y - 1, z);
        return (underBlock instanceof BlockLargePotPlantProxy || underBlock instanceof BlockLargePot);
    }

    private boolean isUnderBlockPot (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return false;

        Block underBlock = world.getBlock(x, y - 1, z);
        return underBlock instanceof BlockLargePot;
    }

    private int getAttachedPotYIndex (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return 0;

        Block underBlock = world.getBlock(x, --y, z);
        while (y > 0 && underBlock instanceof BlockLargePotPlantProxy)
            underBlock = world.getBlock(x, --y, z);

        return y;
    }

    private BlockLargePot getAttachedPot (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return null;

        Block underBlock = world.getBlock(x, --y, z);
        while (y > 0 && underBlock instanceof BlockLargePotPlantProxy)
            underBlock = world.getBlock(x, --y, z);

        if (!(underBlock instanceof BlockLargePot))
            return null;

        return (BlockLargePot) underBlock;
    }

    public TileEntityLargePot getAttachedPotEntity (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return null;

        Block underBlock = world.getBlock(x, --y, z);
        while (y > 0 && underBlock instanceof BlockLargePotPlantProxy)
            underBlock = world.getBlock(x, --y, z);

        if (!(underBlock instanceof BlockLargePot))
            return null;

        BlockLargePot largePot = (BlockLargePot) underBlock;
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
