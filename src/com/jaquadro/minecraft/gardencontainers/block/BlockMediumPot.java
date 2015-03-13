package com.jaquadro.minecraft.gardencontainers.block;

import com.jaquadro.minecraft.gardencontainers.block.tile.TileEntityMediumPot;
import com.jaquadro.minecraft.gardencontainers.core.ClientProxy;
import com.jaquadro.minecraft.gardencore.api.block.IChainAttachable;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.BlockGardenContainer;
import com.jaquadro.minecraft.gardencore.block.support.BasicConnectionProfile;
import com.jaquadro.minecraft.gardencore.block.support.BasicSlotProfile;
import com.jaquadro.minecraft.gardencore.block.support.Slot2Profile;
import com.jaquadro.minecraft.gardencore.block.support.SlotShare0Profile;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockMediumPot extends BlockGardenContainer implements IChainAttachable
{
    private class LocalSlotProfile extends Slot2Profile
    {
        public LocalSlotProfile (Slot[] slots) {
            super(slots);
        }

        @Override
        public float getPlantOffsetY (IBlockAccess blockAccess, int x, int y, int z, int slot) {
            return -.0625f * 5;
        }
    }

    public BlockMediumPot (String blockName) {
        super(blockName, Material.rock);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(.5f);
        setResistance(5f);
        setStepSound(Block.soundTypeStone);

        connectionProfile = new BasicConnectionProfile();
        slotShareProfile = new SlotShare0Profile();

        PlantType[] commonType = new PlantType[] { PlantType.GROUND };
        PlantSize[] allSize = new PlantSize[] { PlantSize.MEDIUM, PlantSize.SMALL };

        slotProfile = new LocalSlotProfile(new BasicSlotProfile.Slot[] {
            new BasicSlotProfile.Slot(Slot2Profile.SLOT_CENTER, commonType, allSize),
            new BasicSlotProfile.Slot(Slot2Profile.SLOT_COVER, new PlantType[]{PlantType.GROUND_COVER}, allSize),
        });
    }

    public abstract String[] getSubTypes ();

    @Override
    public int getDefaultSlot () {
        return Slot2Profile.SLOT_CENTER;
    }

    @Override
    protected int getSlot (World world, int x, int y, int z, EntityPlayer player, float hitX, float hitY, float hitZ) {
        return Slot2Profile.SLOT_CENTER;
    }

    @Override
    protected int getEmptySlotForPlant (World world, int x, int y, int z, EntityPlayer player, PlantItem plant) {
        if (plant.getPlantTypeClass() == PlantType.GROUND_COVER)
            return Slot2Profile.SLOT_COVER;

        return Slot2Profile.SLOT_CENTER;
    }

    @Override
    public int getRenderType () {
        return ClientProxy.mediumPotRenderID;
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
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

        int count = quantityDropped(metadata, fortune, world.rand);
        for (int i = 0; i < count; i++) {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
                items.add(new ItemStack(item, 1, metadata));
        }

        return items;
    }

    @Override
    public TileEntityMediumPot createNewTileEntity (World world, int meta) {
        return new TileEntityMediumPot();
    }

    @Override
    public void addCollisionBoxesToList (World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity colliding) {
        setBlockBounds(.125f, 0, .125f, .875f, .75f, .875f);
        super.addCollisionBoxesToList(world, x, y, z, mask, list, colliding);

        setBlockBounds(.125f, 0, .125f, .875f, .75f, .875f);
    }

    @Override
    public void setBlockBoundsForItemRender () {
        setBlockBounds(.125f, 0, .125f, .875f, .75f, .875f);
    }

    @Override
    public boolean canHarvestBlock (EntityPlayer player, int meta) {
        return true;
    }

    @Override
    public TileEntityMediumPot getTileEntity (IBlockAccess world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(x, y, z);
        return (te != null && te instanceof TileEntityMediumPot) ? (TileEntityMediumPot) te : null;
    }

    private static final Vec3[] chainAttachPoints = new Vec3[] {
        Vec3.createVectorHelper(.125f + .03125 + .005, .75f, .125f + .03125), Vec3.createVectorHelper(.125f + .03125, .75f, 1 - .125f - .03125 - .005),
        Vec3.createVectorHelper(1 - .125f - .03125, .75f, .125f + .03125 + .005), Vec3.createVectorHelper(1 - .125f - .03125 - .005, .75f, 1 - .125f - .03125),
    };

    @Override
    public Vec3[] getChainAttachPoints (int side) {
        if (side == 1)
            return chainAttachPoints;

        return null;
    }

    @Override
    public void registerBlockIcons (IIconRegister register) { }
}
