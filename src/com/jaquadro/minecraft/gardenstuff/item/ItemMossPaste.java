package com.jaquadro.minecraft.gardenstuff.item;

import com.jaquadro.minecraft.gardencore.core.ModCreativeTabs;
import com.jaquadro.minecraft.gardenstuff.GardenStuff;
import com.jaquadro.minecraft.gardenstuff.core.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMossPaste extends Item
{
    public ItemMossPaste (String name) {

        setUnlocalizedName(name);
        setMaxStackSize(1);
        setMaxDamage(64);
        setCreativeTab(ModCreativeTabs.tabGardenCore);
        setTextureName(GardenStuff.MOD_ID + ":moss_paste");
    }

    @Override
    public boolean onItemUse (ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);

        Block newBlock = null;
        int newMeta = -1;

        if (block == Blocks.stonebrick) {
            if (meta == 0)
                newMeta = 1;
            else if (meta == 1) {
                newBlock = ModBlocks.mossBrick;
                newMeta = 0;
            }
            else if (meta == 2) {
                newBlock = ModBlocks.mossBrick;
                newMeta = 3;
            }
        }
        else if (block == ModBlocks.mossBrick) {
            if (meta == 0)
                newMeta = 1;
            if (meta == 1)
                newMeta = 2;
            if (meta == 3)
                newMeta = 4;
            if (meta == 4)
                newMeta = 5;
            if (meta == 5)
                newMeta = 6;
        }
        else if (block == Blocks.cobblestone) {
            if (meta == 0) {
                newBlock = Blocks.mossy_cobblestone;
                newMeta = 0;
            }
        }

        if (newBlock != null) {
            world.setBlock(x, y, z, newBlock);
        }
        if (newMeta != -1) {
            world.setBlockMetadataWithNotify(x, y, z, newMeta, 3);
            itemStack.damageItem(1, player);

            return true;
        }

        return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
}
