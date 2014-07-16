package com.jaquadro.minecraft.gardencore.block;

import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.api.plant.PlantType;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockGardenContainer extends BlockGarden
{
    protected BlockGardenContainer (String blockName, Material material) {
        super(blockName, material);
    }

    @Override
    public ItemStack getGardenSubstrate (IBlockAccess world, int x, int y, int z, int slot) {
        TileEntityGarden te = getTileEntity(world, x, y, z);
        return (te != null) ? te.getSubstrate() : null;
    }

    @Override
    protected boolean applyItemToGarden (World world, int x, int y, int z, EntityPlayer player, ItemStack itemStack, float hitX, float hitY, float hitZ, boolean hitValid) {
        ItemStack item = (itemStack == null) ? player.inventory.getCurrentItem() : itemStack;
        int slot = getSlot(world, x, y, z, player, hitX, hitY, hitZ);

        if (applySubstrateToGarden(world, x, y, z, (itemStack == null) ? player : null, slot, item))
            return true;

        return super.applyItemToGarden(world, x, y, z, player, itemStack, hitX, hitY, hitZ, hitValid);
    }

    protected boolean applySubstrateToGarden (World world, int x, int y, int z, EntityPlayer player, int slot, ItemStack itemStack) {
        if (getGardenSubstrate(world, x, y, z, slot) != null)
            return false;

        if (!isValidSubstrate(world, x, y, z, slot, itemStack))
            return false;

        TileEntityGarden garden = getTileEntity(world, x, y, z);

        ItemStack translation = translateSubstrate(world, x, y, z, slot, itemStack);
        if (translation == null || translation == itemStack)
            garden.setSubstrate(itemStack);
        else
            garden.setSubstrate(itemStack, translation);

        garden.markDirty();
        world.markBlockForUpdate(x, y, z);

        if (player != null && !player.capabilities.isCreativeMode) {
            ItemStack currentItem = player.inventory.getCurrentItem();
            if (--currentItem.stackSize <= 0)
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
        }

        return true;
    }

    @Override
    protected boolean isPlantValidForSubstrate (ItemStack substrate, PlantItem plant) {
        if (substrate == null || substrate.getItem() == null)
            return false;

        switch (plant.getPlantTypeClass()) {
            case AQUATIC:
            case AQUATIC_COVER:
            case AQUATIC_EMERGENT:
            case AQUATIC_SURFACE:
                if (Block.getBlockFromItem(substrate.getItem()) != Blocks.water)
                    return false;
                break;
            case GROUND:
            case GROUND_COVER:
                if (Block.getBlockFromItem(substrate.getItem()) == Blocks.water)
                    return false;
        }

        return super.isPlantValidForSubstrate(substrate, plant);
    }

    protected boolean isValidSubstrate (World world, int x, int y, int z, int slot, ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null)
            return false;

        Block block = Block.getBlockFromItem(itemStack.getItem());
        if (block == null)
            return false;

        return block == Blocks.dirt
            || block == Blocks.sand
            || block == Blocks.gravel
            || block == Blocks.soul_sand
            || block == Blocks.grass
            || block == Blocks.water
            || block == Blocks.farmland;
    }

    protected ItemStack translateSubstrate (World world, int x, int y, int z, int slot, ItemStack itemStack) {
        if (Block.getBlockFromItem(itemStack.getItem()) == Blocks.farmland)
            return new ItemStack(Blocks.farmland, 1, 1);

        return itemStack;
    }

}
