package com.jaquadro.minecraft.gardencore.item;

import com.jaquadro.minecraft.gardencore.block.tile.TileEntityWindowBox;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWindowBox extends ItemBlock
{
    public ItemWindowBox (Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata & 15))
            return false;

        boolean isLower = side != 0 && (side == 1 || hitY <= 0.5);

        TileEntityWindowBox te = (TileEntityWindowBox) world.getTileEntity(x, y, z);
        if (te != null)
            te.setUpper(!isLower);



        return true;
    }
}
