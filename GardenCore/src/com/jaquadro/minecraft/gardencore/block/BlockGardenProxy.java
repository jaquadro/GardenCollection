package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.api.GardenCoreAPI;
import com.jaquadro.minecraft.gardencore.api.IBonemealHandler;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.core.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;

public class BlockGardenProxy extends Block
{
    private static float[] plantOffsetX = new float[] {
        0, 0, -.252f, .25f, -.25f, .252f, -.5f, -.001f, .5f, .5f, .5f, -.001f, -.5f, -.5f
    };
    private static float[] plantOffsetZ = new float[] {
        0, 0, -.25f, -.252f, .252f, .25f, -.501f, -.5f, -.501f, 0, .449f, .5f, .449f, 0
    };

    @SideOnly(Side.CLIENT)
    private IIcon transpIcon;

    // Scratch State
    private boolean applyingBonemeal;
    private int boundSlot = -1;

    // Reentrant Flags
    private int reeLightValue;

    public BlockGardenProxy (String blockName) {
        super(Material.plants);

        setHardness(0);
        setLightOpacity(0);
        setBlockName(blockName);
    }

    public float getPlantOffsetX (int slot) {
        return plantOffsetX[slot];
    }

    public float getPlantOffsetY (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        BlockGarden gardenBlock = getGardenBlock(blockAccess, x, y, z);
        if (gardenBlock == null)
            return 0;

        return gardenBlock.getPlantOffsetY(blockAccess, x, getBaseBlockYCoord(blockAccess, x, y, z), z, slot);
    }

    public float getPlantOffsetZ (int slot) {
        return plantOffsetZ[slot];
    }

    public void bindSlot (World world, int x, int y, int z, TileEntityGarden te, int slot) {
        boundSlot = slot;

        Block block = getPlantBlock(te, slot);
        if (block != null) {
            int data = getPlantData(te, slot);
           world.setBlockMetadataWithNotify(x, y, z, data, 0);
        }
    }

    public void unbindSlot (World world, int x, int y, int z, TileEntityGarden te) {
        boundSlot = -1;

        world.setBlockMetadataWithNotify(x, y, z, 0, 0);
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
        return ClientProxy.gardenProxyRenderID;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x, int y, int z) {
        TileEntityGarden te = getGardenEntity(world, x, y, z);
        if (te == null)
            return null;

        BlockGarden garden = getGardenBlock(world, x, y, z);
        if (garden == null)
            return null;

        AxisAlignedBB aabb = null;
        for (int slot : garden.getSlotProfile().getPlantSlots()) {
            Block block = getPlantBlock(te, slot);
            if (block == null)
                continue;

            try {
                AxisAlignedBB sub = block.getCollisionBoundingBoxFromPool(world, x, y, z);
                if (sub == null)
                    continue;

                sub.offset(plantOffsetX[slot], 0, plantOffsetZ[slot]);

                if (aabb == null)
                    aabb = sub;
                else
                    aabb = aabb.func_111270_a(sub); // Union
            }
            catch (Exception e) {
                continue;
            }
        }

        return aabb;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool (World world, int x, int y, int z) {
        TileEntityGarden te = getGardenEntity(world, x, y, z);
        BlockGarden garden = getGardenBlock(world, x, y, z);
        if (te == null || garden == null)
            return super.getSelectedBoundingBoxFromPool(world, x, y, z);

        AxisAlignedBB aabb = null;
        for (int slot : garden.getSlotProfile().getPlantSlots()) {
            Block block = getPlantBlock(te, slot);
            if (block == null)
                continue;

            try {
                AxisAlignedBB sub = block.getSelectedBoundingBoxFromPool(world, x, y, z);
                if (sub == null)
                    continue;

                sub.offset(plantOffsetX[slot], 0, plantOffsetZ[slot]);

                if (aabb == null)
                    aabb = sub;
                else
                    aabb = aabb.func_111270_a(sub); // Union
            }
            catch (Exception e) {
                continue;
            }
        }

        if (aabb == null)
            aabb = super.getSelectedBoundingBoxFromPool(world, x, y, z);

        return aabb;
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        TileEntityGarden te = getGardenEntity(world, x, y, z);
        BlockGarden garden = getGardenBlock(world, x, y, z);
        if (te == null || garden == null) {
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
            return;
        }

        for (int slot : garden.getSlotProfile().getPlantSlots()) {
            Block block = getPlantBlock(te, slot);
            if (block == null)
                continue;

            try {
                AxisAlignedBB sub = block.getCollisionBoundingBoxFromPool(world, x, y, z);
                if (sub == null)
                    continue;

                sub.offset(plantOffsetX[slot], 0, plantOffsetZ[slot]);
                if (mask.intersectsWith(sub))
                    list.add(sub);
            }
            catch (Exception e) {
                continue;
            }
        }

        if (list.isEmpty())
            super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);
    }

    @Override
    public MovingObjectPosition collisionRayTrace (World world, int x, int y, int z, Vec3 startVec, Vec3 endVec) {
        TileEntityGarden te = getGardenEntity(world, x, y, z);
        BlockGarden garden = getGardenBlock(world, x, y, z);
        if (te == null || garden == null)
            return super.collisionRayTrace(world, x, y, z, startVec, endVec);

        MovingObjectPosition mop = null;
        for (int slot : garden.getSlotProfile().getPlantSlots()) {
            Block block = getPlantBlock(te, slot);
            if (block == null)
                continue;

            try {
                MovingObjectPosition sub = block.collisionRayTrace(world, x, y, z, startVec, endVec);
                if (mop == null || startVec.squareDistanceTo(mop.hitVec) > startVec.squareDistanceTo(sub.hitVec))
                    mop = sub;
            }
            catch (Exception e) {
                continue;
            }
        }

        return mop;
    }

    @Override
    public void onNeighborBlockChange (World world, int x, int y, int z, Block block) {
        TileEntityGarden te = getGardenEntity(world, x, y, z);
        if (te != null)
            BlockGarden.validateBlockState(te);
        /*if (!hasValidUnderBlock(world, x, y, z)) {
            world.setBlockToAir(x, y, z);
        }*/
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float vx, float vy, float vz) {
        TileEntityGarden te = getGardenEntity(world, x, y, z);
        BlockGarden garden = getGardenBlock(world, x, y, z);
        if (te == null || garden == null)
            return false;

        boolean flag = false;
        for (int slot : garden.getSlotProfile().getPlantSlots()) {
            Block block = getPlantBlock(te, slot);
            if (block == null)
                continue;

            try {
                flag |= block.onBlockActivated(world, x, y, z, player, side, vx, vy, vz);
            }
            catch (Exception e) {
                continue;
            }
        }

        if (flag)
            return true;

        BlockGarden block = getGardenBlock(world, x, y, z);
        if (block != null) {
            y = getBaseBlockYCoord(world, x, y, z);
            return block.applyItemToGarden(world, x, y, z, player, null);
        }

        return false;
    }

    /*public boolean applyTestKit (World world, int x, int y, int z, ItemStack testKit) {
        BlockGarden block = getGardenBlock(world, x, y, z);
        if (block == null)
            return false;

        y = getBaseBlockYCoord(world, x, y, z);

        return block.applyTestKit(world, x, y, z, testKit);
    }*/

    public boolean applyBonemeal (World world, int x, int y, int z) {
        BlockGarden block = getGardenBlock(world, x, y, z);
        if (block == null)
            return false;

        y = getBaseBlockYCoord(world, x, y, z);

        TileEntityGarden te = block.getTileEntity(world, x, y, z);

        boolean handled = false;
        for (int slot : block.getSlotProfile().getPlantSlots()) {
            for (IBonemealHandler handler : GardenCoreAPI.instance().getBonemealHandlers()) {
                if (handler.applyBonemeal(world, x, y, z, block, slot)) {
                    handled = true;
                    break;
                }
            }
        }

        return handled;
    }

    @Override
    public void onBlockHarvested (World world, int x, int y, int z, int p_149681_5_, EntityPlayer player) {
        super.onBlockHarvested(world, x, y, z, p_149681_5_, player);

        if (player.capabilities.isCreativeMode) {
            TileEntityGarden te = getGardenEntity(world, x, y, z);
            if (te != null)
                te.clearPlantedContents();
        }
    }

    @Override
    public void breakBlock (World world, int x, int y, int z, Block block, int data) {
        if (hasValidUnderBlock(world, x, y, z) && !isApplyingBonemealTo(x, y, z)) {
            TileEntityGarden te = getGardenEntity(world, x, y, z);
            BlockGarden garden = getGardenBlock(world, x, y, z);

            if (te != null && block != null) {
                for (int slot : garden.getSlotProfile().getPlantSlots()) {
                    ItemStack item = te.getPlantInSlot(slot);
                    if (item != null)
                        dropBlockAsItem(world, x, y, z, item);
                }

                te.clearPlantedContents();
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

    @Override
    public int getLightValue (IBlockAccess world, int x, int y, int z) {
        if (reeLightValue > 0)
            return getLightValue();

        reeLightValue++;
        int value = 0;

        TileEntityGarden te = getGardenEntity(world, x, y, z);
        BlockGarden garden = getGardenBlock(world, x, y, z);
        if (te == null || garden == null)
            value = super.getLightValue(world, x, y, z);
        else {
            for (int slot : garden.getSlotProfile().getPlantSlots()) {
                Block block = getPlantBlock(te, slot);
                if (block == null)
                    continue;

                try {
                    int sub = block.getLightValue(world, x, y, z);
                    if (sub > value)
                        value = sub;
                }
                catch (Exception e) {
                    continue;
                }
            }
        }

        reeLightValue--;
        return value;
    }

    @Override
    public void onEntityCollidedWithBlock (World world, int x, int y, int z, Entity entity) {
        TileEntityGarden te = getGardenEntity(world, x, y, z);
        BlockGarden garden = getGardenBlock(world, x, y, z);
        if (te == null || garden == null)
            return;

        for (int slot : garden.getSlotProfile().getPlantSlots()) {
            Block block = getPlantBlock(te, slot);
            if (block == null)
                continue;

            try {
                block.onEntityCollidedWithBlock(world, x, y, z, entity);
            }
            catch (Exception e) {
                continue;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier (IBlockAccess blockAccess, int x, int y, int z) {
        TileEntityGarden te = getGardenEntity(blockAccess, x, y, z);
        if (te == null || boundSlot == -1)
            return super.colorMultiplier(blockAccess, x, y, z);

        Block block = getPlantBlockRestricted(te, boundSlot);
        if (block == null)
            return super.colorMultiplier(blockAccess, x, y, z);

        try {
            return block.colorMultiplier(blockAccess, x, y, z);
        }
        catch (Exception e) {
            return super.colorMultiplier(blockAccess, x, y, z);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (IBlockAccess blockAccess, int x, int y, int z, int side) {
        TileEntityGarden te = getGardenEntity(blockAccess, x, y, z);
        if (te == null || boundSlot == -1)
            return super.getIcon(blockAccess, x, y, z, side);

        Block block = getPlantBlockRestricted(te, boundSlot);
        if (block == null)
            return super.getIcon(blockAccess, x, y, z, side);

        try {
            return block.getIcon(blockAccess, x, y, z, side);
        }
        catch (Exception e) {
            return super.getIcon(blockAccess, x, y, z, side);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon (int side, int data) {
        return transpIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects (World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        TileEntityGarden te = getGardenEntity(world, x, y, z);
        BlockGarden garden = getGardenBlock(world, x, y, z);
        if (te == null || garden == null)
            return true;

        for (int slot : garden.getSlotProfile().getPlantSlots()) {
            Block block = getPlantBlock(te, slot);
            int blockData = getPlantData(te, slot);
            if (block == null)
                continue;

            try {
                byte count = 4;
                for (int ix = 0; ix < count; ++ix) {
                    for (int iy = 0; iy < count; ++iy) {
                        for (int iz = 0; iz < count; ++iz) {
                            double xOff = (double)x + ((double)ix + 0.5D) / (double)count;
                            double yOff = (double)y + ((double)iy + 0.5D) / (double)count;
                            double zOff = (double)z + ((double)iz + 0.5D) / (double)count;

                            EntityDiggingFX fx = new EntityDiggingFX(world, xOff, yOff, zOff, xOff - (double) x - 0.5D, yOff - (double) y - 0.5D, zOff - (double) z - 0.5D, this, meta);
                            fx.setParticleIcon(block.getIcon(world.rand.nextInt(6), blockData));

                            effectRenderer.addEffect(fx.applyColourMultiplier(x, y, z));
                        }
                    }
                }
            }
            catch (Exception e) {
                continue;
            }
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        transpIcon = iconRegister.registerIcon(GardenCore.MOD_ID + ":proxy_transp");
    }

    private boolean hasValidUnderBlock (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return false;

        Block underBlock = world.getBlock(x, y - 1, z);
        return underBlock instanceof BlockGarden || underBlock instanceof BlockGardenProxy;
    }

    private int getBaseBlockYCoord (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return 0;

        Block underBlock = world.getBlock(x, --y, z);
        while (y > 0 && underBlock instanceof BlockGardenProxy)
            underBlock = world.getBlock(x, --y, z);

        return y;
    }

    public BlockGarden getGardenBlock (IBlockAccess world, int x, int y, int z) {
        if (y == 0)
            return null;

        y = getBaseBlockYCoord(world, x, y, z);
        Block underBlock = world.getBlock(x, y, z);

        if (!(underBlock instanceof BlockGarden))
            return null;

        return (BlockGarden) underBlock;
    }

    public TileEntityGarden getGardenEntity (IBlockAccess world, int x, int y, int z) {
        y = getBaseBlockYCoord(world, x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof TileEntityGarden))
            return null;

        return (TileEntityGarden) tileEntity;
    }

    public Block getPlantBlock (TileEntityGarden tileEntity, int slot) {
        ItemStack itemStack = tileEntity.getPlantInSlot(slot);
        if (itemStack == null)
            return null;

        return getPlantBlock(tileEntity, itemStack.getItem());
    }

    public Block getPlantBlockRestricted (TileEntityGarden tileEntity, int slot) {
        ItemStack itemStack = tileEntity.getStackInSlot(slot);
        if (itemStack == null)
            return null;

        return getPlantBlock(tileEntity, itemStack.getItem());
    }

    private Block getPlantBlock (TileEntityGarden tileEntity, Item item) {
        if (item == null)
            return null;
        if (item instanceof IPlantable)
            return ((IPlantable)item).getPlant(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        if (item instanceof ItemBlock)
            return Block.getBlockFromItem(item);

        return null;
    }

    public int getPlantData (TileEntityGarden tileEntity, int slot) {
        ItemStack itemStack = tileEntity.getPlantInSlot(slot);
        if (itemStack == null)
            return 0;

        return itemStack.getItemDamage();
    }

    private boolean isApplyingBonemealTo (int x, int y, int z) {
        return applyingBonemeal;
    }
}
