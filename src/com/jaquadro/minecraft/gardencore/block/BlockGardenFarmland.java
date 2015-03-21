package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.GardenCore;
import com.jaquadro.minecraft.gardencore.api.plant.PlantSize;
import com.jaquadro.minecraft.gardencore.block.support.BasicConnectionProfile;
import com.jaquadro.minecraft.gardencore.block.support.BasicSlotProfile;
import com.jaquadro.minecraft.gardencore.block.support.SlotShare0Profile;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGardenFarmland;
import com.jaquadro.minecraft.gardencore.core.ModBlocks;
import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockGardenFarmland extends BlockGarden
{
    @SideOnly(Side.CLIENT)
    IIcon iconTilledSoil;

    private static ItemStack substrate = new ItemStack(Blocks.farmland, 1);

    public BlockGardenFarmland (String blockName) {
        super(blockName, Material.ground);

        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setHardness(0.5f);
        setStepSound(Block.soundTypeGrass);
        setBlockBounds(0, 0, 0, 1, .9375f, 1);

        if (GardenCore.config.enableTilledSoilGrowthBonus)
            setTickRandomly(true);

        connectionProfile = new BasicConnectionProfile();
        slotShareProfile = new SlotShare0Profile();
        slotProfile = new BasicSlotProfile(new BasicSlotProfile.Slot[0]);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool (World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1);
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
    public boolean isFertile (World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean canSustainPlant (IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        EnumPlantType plantType = plantable.getPlantType(world, x, y, z);
        if (plantType == EnumPlantType.Crop)
            return true;

        return false;
    }

    @Override
    public void updateTick (World world, int x, int y, int z, Random random) {
        Block block = world.getBlock(x, y + 1, z);
        if (block instanceof IPlantable || block instanceof IGrowable)
            block.updateTick(world, x, y + 1, z, random);
    }

    @Override
    public Item getItemDropped (int meta, Random rand, int fortune) {
        return Item.getItemFromBlock(ModBlocks.gardenSoil);
    }

    @Override
    public TileEntityGarden createNewTileEntity (World world, int meta) {
        return new TileEntityGardenFarmland();
    }

    @Override
    public ItemStack getGardenSubstrate (IBlockAccess blockAccess, int x, int y, int z, int slot) {
        return substrate;
    }

    @Override
    public IIcon getIcon (int side, int meta) {
        if (side == 1)
            return iconTilledSoil;
        return blockIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(GardenCore.MOD_ID + ":garden_dirt");
        iconTilledSoil = iconRegister.registerIcon(GardenCore.MOD_ID + ":garden_farmland");
    }
}
