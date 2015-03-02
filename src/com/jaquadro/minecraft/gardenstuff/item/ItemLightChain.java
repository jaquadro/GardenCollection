package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardenstuff.block.BlockLightChain;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLightChain extends ItemMultiTexture
{
    public ItemLightChain (Block block) {
        super(block, block, BlockLightChain.types);
    }

    @Override
    public boolean onItemUse (ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        if (block instanceof BlockCauldron && side == 1) {
            int waterLevel = BlockCauldron.func_150027_b(meta);
            if (waterLevel == 0)
                return false;

            ItemStack newItem = new ItemStack(ModBlocks.lightChain, 1, 3);
            itemStack.stackSize--;

            world.spawnEntityInWorld(new EntityItem(world, x + .5, y + 1.5, z + .5, newItem));
            return true;
        }

        return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
}
