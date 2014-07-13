package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenConnected;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenSingle;
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
import net.minecraftforge.common.IPlantable;

public class BlockGardenSoil extends BlockGarden
{
    private static ItemStack substrate = new ItemStack(Blocks.dirt, 1);

    public BlockGardenSoil (String blockName) {
        super(blockName, Material.ground);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(0.5f);
        setStepSound(Block.soundTypeGravel);
    }

    @Override
    public TileEntityGardenConnected createNewTileEntity (World var1, int var2) {
        return new TileEntityGardenConnected();
    }

    @Override
    public ItemStack getGardenSubstrate (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        return substrate;
    }

    @Override
    protected int getSlot (World world, int x, int y, int z, EntityPlayer player, float hitX, float hitY, float hitZ) {
        return TileEntityGardenSingle.SLOT_CENTER;
    }

    @Override
    protected int getEmptySlotForPlant (World world, int x, int y, int z, EntityPlayer player, PlantItem plant) {
        if (plant.getPlantTypeClass() == PlantType.GROUND_COVER)
            return TileEntityGardenSingle.SLOT_COVER;

        return TileEntityGardenSingle.SLOT_CENTER;
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
