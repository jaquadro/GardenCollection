package com.jaquadro.minecraft.gardencore.core.handlers;

import com.jaquadro.minecraft.gardencore.api.IBonemealHandler;
import com.jaquadro.minecraft.gardencore.api.plant.PlantItem;
import com.jaquadro.minecraft.gardencore.block.BlockGarden;
import com.jaquadro.minecraft.gardencore.block.tile.TileEntityGarden;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class VanillaBonemealHandler implements IBonemealHandler
{
    @Override
    public boolean applyBonemeal (World world, int x, int y, int z, BlockGarden hostBlock, int slot) {
        TileEntityGarden te = hostBlock.getTileEntity(world, x, y, z);

        Block block = hostBlock.getPlantBlockFromSlot(world, x, y, z, slot);
        int meta = hostBlock.getPlantMetaFromSlot(world, x, y, z, slot);

        if (block == Blocks.tallgrass && meta == 1) {
            ItemStack upgrade = new ItemStack(Blocks.double_plant, 1, 2);
            if (hostBlock.isPlantValidForSlot(world, x, y, z, slot, PlantItem.getForItem(upgrade))) {
                te.setInventorySlotContents(slot, upgrade);
                return true;
            }
        }
        else if (block == Blocks.tallgrass && meta == 2) {
            ItemStack upgrade = new ItemStack(Blocks.double_plant, 1, 3);
            if (hostBlock.isPlantValidForSlot(world, x, y, z, slot, PlantItem.getForItem(upgrade))) {
                te.setInventorySlotContents(slot, upgrade);
                return true;
            }
        }

        return false;
    }
}
