package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.support.BasicConnectionProfile;
import com.jaquadro.minecraft.gardencore.block.support.BasicSlotProfile;
import com.jaquadro.minecraft.gardencore.block.support.SlotShare8Profile;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenSoil;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGardenSoil extends BlockGarden
{
    public static final int SLOT_CENTER = 0;
    public static final int SLOT_COVER = 1;
    public static final int SLOT_NW = 2;
    public static final int SLOT_NE = 3;
    public static final int SLOT_SW = 4;
    public static final int SLOT_SE = 5;
    public static final int SLOT_TOP_LEFT = 6;
    public static final int SLOT_TOP = 7;
    public static final int SLOT_TOP_RIGHT = 8;
    public static final int SLOT_RIGHT = 9;
    public static final int SLOT_BOTTOM_RIGHT = 10;
    public static final int SLOT_BOTTOM = 11;
    public static final int SLOT_BOTTOM_LEFT = 12;
    public static final int SLOT_LEFT = 13;

    private static ItemStack substrate = new ItemStack(Blocks.dirt, 1);

    public BlockGardenSoil (String blockName) {
        super(blockName, Material.ground);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(0.5f);
        setStepSound(Block.soundTypeGravel);

        connectionProfile = new BasicConnectionProfile();
        slotShareProfile = new SlotShare8Profile(SLOT_TOP_LEFT, SLOT_TOP, SLOT_TOP_RIGHT, SLOT_RIGHT, SLOT_BOTTOM_RIGHT, SLOT_BOTTOM, SLOT_BOTTOM_LEFT, SLOT_LEFT);

        PlantType[] commonType = new PlantType[] { PlantType.GROUND, PlantType.AQUATIC, PlantType.AQUATIC_EMERGENT};

        PlantSize[] smallSize = new PlantSize[] { PlantSize.SMALL };
        PlantSize[] commonSize = new PlantSize[] { PlantSize.LARGE, PlantSize.SMALL };
        PlantSize[] allSize = new PlantSize[] { PlantSize.FULL, PlantSize.LARGE, PlantSize.SMALL };

        slotProfile = new BasicSlotProfile(new BasicSlotProfile.Slot[] {
            new BasicSlotProfile.Slot(SLOT_CENTER, commonType, allSize),
            new BasicSlotProfile.Slot(SLOT_COVER, new PlantType[] { PlantType.GROUND_COVER}, allSize),
            new BasicSlotProfile.Slot(SLOT_NW, commonType, smallSize),
            new BasicSlotProfile.Slot(SLOT_NE, commonType, smallSize),
            new BasicSlotProfile.Slot(SLOT_SW, commonType, smallSize),
            new BasicSlotProfile.Slot(SLOT_SE, commonType, smallSize),
            new BasicSlotProfile.Slot(SLOT_TOP_LEFT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_TOP, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_TOP_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_BOTTOM_RIGHT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_BOTTOM, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_BOTTOM_LEFT, commonType, commonSize),
            new BasicSlotProfile.Slot(SLOT_LEFT, commonType, commonSize),
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
        return SLOT_CENTER;
    }

    @Override
    protected int getEmptySlotForPlant (World world, int x, int y, int z, EntityPlayer player, PlantItem plant) {
        if (plant.getPlantTypeClass() == PlantType.GROUND_COVER)
            return SLOT_COVER;

        return SLOT_CENTER;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        return blockIcon;
    }

    @Override
    public IIcon getIcon (IBlockAccess world, int x, int y, int z, int side) {
        return blockIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(GardenCore.MOD_ID + ":garden_dirt");
    }
}
