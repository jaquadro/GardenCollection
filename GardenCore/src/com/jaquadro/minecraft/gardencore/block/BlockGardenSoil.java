package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.support.*;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenSoil;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockGardenSoil extends BlockGarden
{
    private static ItemStack substrate = new ItemStack(Blocks.dirt, 1);

    public BlockGardenSoil (String blockName) {
        super(blockName, Material.ground);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(0.5f);
        setStepSound(Block.soundTypeGrass);

        PlantType[] commonType = new PlantType[] { PlantType.GROUND, PlantType.AQUATIC, PlantType.AQUATIC_EMERGENT};
        PlantSize[] smallSize = new PlantSize[] { PlantSize.SMALL };
        PlantSize[] commonSize = new PlantSize[] { PlantSize.LARGE, PlantSize.MEDIUM, PlantSize.SMALL };
        PlantSize[] allSize = new PlantSize[] { PlantSize.FULL, PlantSize.LARGE, PlantSize.MEDIUM, PlantSize.SMALL };

        connectionProfile = new BasicConnectionProfile();
        slotShareProfile = new SlotShare8Profile(Slot14Profile.SLOT_TOP_LEFT, Slot14Profile.SLOT_TOP,
            Slot14Profile.SLOT_TOP_RIGHT, Slot14Profile.SLOT_RIGHT, Slot14Profile.SLOT_BOTTOM_RIGHT,
            Slot14Profile.SLOT_BOTTOM, Slot14Profile.SLOT_BOTTOM_LEFT, Slot14Profile.SLOT_LEFT);

        slotProfile = new Slot14Profile(new BasicSlotProfile.Slot[]{
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_CENTER, commonType, allSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_COVER, new PlantType[]{PlantType.GROUND_COVER}, allSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_NW, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_NE, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_SW, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_SE, commonType, smallSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_TOP_LEFT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_TOP, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_TOP_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_BOTTOM_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_BOTTOM, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_BOTTOM_LEFT, commonType, commonSize),
            new BasicSlotProfile.Slot(Slot14Profile.SLOT_LEFT, commonType, commonSize),
        });
    }

    @Override
    public TileEntityGardenSoil createNewTileEntity (World var1, int var2) {
        return new TileEntityGardenSoil();
    }

    @Override
    public ItemStack getGardenSubstrate (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        return substrate;
    }

    @Override
    protected int getSlot (World world, int x, int y, int z, EntityPlayer player, float hitX, float hitY, float hitZ) {
        return Slot14Profile.SLOT_CENTER;
    }

    @Override
    protected int getEmptySlotForPlant (World world, int x, int y, int z, EntityPlayer player, PlantItem plant) {
        TileEntityGarden garden = getTileEntity(world, x, y, z);

        if (plant.getPlantTypeClass() == PlantType.GROUND_COVER)
            return garden.getStackInSlot(Slot14Profile.SLOT_COVER) == null ? Slot14Profile.SLOT_COVER : SLOT_INVALID;

        if (plant.getPlantSizeClass() == PlantSize.FULL)
            return garden.getStackInSlot(Slot14Profile.SLOT_CENTER) == null ? Slot14Profile.SLOT_CENTER : SLOT_INVALID;

        if (garden.getStackInSlot(Slot14Profile.SLOT_CENTER) == null)
            return Slot14Profile.SLOT_CENTER;

        if (plant.getPlantSizeClass() == PlantSize.SMALL) {
            for (int slot : new int[] { Slot14Profile.SLOT_NE, Slot14Profile.SLOT_SW, Slot14Profile.SLOT_NW, Slot14Profile.SLOT_SE }) {
                if (garden.getStackInSlot(slot) == null)
                    return slot;
            }
        }

        for (int slot : new int[] { Slot14Profile.SLOT_LEFT, Slot14Profile.SLOT_RIGHT, Slot14Profile.SLOT_TOP,
            Slot14Profile.SLOT_BOTTOM, Slot14Profile.SLOT_TOP_LEFT, Slot14Profile.SLOT_BOTTOM_RIGHT,
            Slot14Profile.SLOT_TOP_RIGHT, Slot14Profile.SLOT_BOTTOM_LEFT }) {
            if (!garden.isSlotValid(slot))
                continue;
            if (garden.getStackInSlot(slot) == null)
                return slot;
        }

        return SLOT_INVALID;
    }

    @Override
    protected boolean applyItemToGarden (World world, int x, int y, int z, EntityPlayer player, ItemStack itemStack, float hitX, float hitY, float hitZ, boolean hitValid) {
        ItemStack item = (itemStack == null) ? player.inventory.getCurrentItem() : itemStack;

        if (item != null && item.getItem() instanceof ItemHoe) {
            world.playSoundEffect(x + .5, y + .5, z + .5, stepSound.getStepResourcePath(), (stepSound.getVolume() + 1) / 2, stepSound.getPitch() * .8f);
            if (!world.isRemote) {
                world.setBlock(x, y, z, ModBlocks.gardenFarmland);
                item.damageItem(1, player);
            }

            return true;
        }

        return super.applyItemToGarden(world, x, y, z, player, itemStack, hitX, hitY, hitZ, hitValid);
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return blockIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(GardenCore.MOD_ID + ":garden_dirt");
    }
}
